package com.arcao.trackables.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import com.arcao.trackables.AppConstants;
import com.arcao.trackables.R;
import com.arcao.trackables.data.service.AccountService;
import com.arcao.trackables.exception.ExceptionHandler;
import com.arcao.trackables.internal.di.HasComponent;
import com.arcao.trackables.internal.di.component.WelcomeActivityComponent;
import com.arcao.trackables.internal.rx.AndroidSchedulerTransformer;
import com.arcao.trackables.ui.ErrorActivity;
import com.arcao.trackables.ui.WelcomeActivity;
import rx.functions.Actions;
import rx.internal.util.SubscriptionList;
import rx.observers.Subscribers;
import timber.log.Timber;

import javax.inject.Inject;
import java.util.Locale;

public class OAuthLoginFragment extends Fragment implements HasComponent<WelcomeActivityComponent> {
	public static final String FRAGMENT_TAG = OAuthLoginFragment.class.getName();

	private static final String STATE_PROGRESS_VISIBLE = "STATE_PROGRESS_VISIBLE";

	private static final String OAUTH_VERIFIER = "oauth_verifier";

	private WebView mWebView = null;
	private View mProgressHolder = null;
	private Bundle mLastInstanceState;
	private final SubscriptionList subscriptionList = new SubscriptionList();

	@Inject
	protected AccountService accountService;

	@Inject
	protected ExceptionHandler exceptionHandler;

	public static OAuthLoginFragment newInstance() {
		return new OAuthLoginFragment();
	}

	public WelcomeActivityComponent component() {
		return ((WelcomeActivity)getActivity()).component();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRetainInstance(true);

		// clear geocaching.com cookies
		//App.get(getActivity()).clearGeocachingCookies();

		component().inject(this);

		subscriptionList.add(
						accountService.startOAuthLogin()
										.compose(AndroidSchedulerTransformer.get())
										.subscribe(this::onLoginUrlAvailable)
		);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		subscriptionList.unsubscribe();
	}

	public void onLoginUrlAvailable(String url) {
		if (mWebView != null) {
			mWebView.loadUrl(url);
		}
	}

	public void onLoginFinished(Intent errorIntent) {
		WelcomeActivity activity = (WelcomeActivity) getActivity();
		if (activity == null)
			return;

		if (errorIntent == null) {
			activity.switchTo(WelcomeActivity.WelcomeState.AFTER_LOGIN);
		}
		else {
			activity.showError(errorIntent);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mWebView != null) {
			mWebView.saveState(outState);
		}

		if (mProgressHolder != null) {
			outState.putInt(STATE_PROGRESS_VISIBLE, mProgressHolder.getVisibility());
			Timber.d("setVisibility: " + mProgressHolder.getVisibility());
		}

		mLastInstanceState = outState;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// FIX savedInstanceState is null after rotation change
		if (savedInstanceState != null)
			mLastInstanceState = savedInstanceState;

		//getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

		View view = inflater.inflate(R.layout.fragment_login_oauth, container, false);
		mProgressHolder = view.findViewById(R.id.progressHolder);
		mProgressHolder.setVisibility(View.VISIBLE);

		if (mLastInstanceState != null) {
			//noinspection ResourceType
			mProgressHolder.setVisibility(mLastInstanceState.getInt(STATE_PROGRESS_VISIBLE, View.VISIBLE));
		}

		mWebView = createWebView(mLastInstanceState);

		FrameLayout webViewHolder = (FrameLayout) view.findViewById(R.id.webViewPlaceholder);
		webViewHolder.addView(mWebView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

		return view;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private WebView createWebView(Bundle savedInstanceState) {
		WebView webView = new FixedWebView(getActivity());

		webView.setHorizontalScrollBarEnabled(false);
		webView.setWebViewClient(new DialogWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);

		if (savedInstanceState != null)
			webView.restoreState(savedInstanceState);

		return webView;
	}

	private class DialogWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith(AppConstants.OAUTH_CALLBACK_URL)) {
				Uri uri = Uri.parse(url);

				if (mProgressHolder != null) {
					mProgressHolder.setVisibility(View.VISIBLE);
				}

				subscriptionList.add(
								accountService.finishOAuthLogin(uri.getQueryParameter(OAUTH_VERIFIER))
												.compose(AndroidSchedulerTransformer.get()).
												subscribe(Subscribers.create(
																Actions.empty(),
																throwable -> onLoginFinished(exceptionHandler.handle(throwable)),
																() -> onLoginFinished(null)))
				);

				return true;
			}

			if (!isOAuthUrl(url)) {
				Timber.d("External URL: " + url);

				// launch external URLs in a full browser
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				getActivity().startActivity(intent);
				return true;
			}

			return false;
		}

		private boolean isOAuthUrl(String url) {
			String urlLowerCase = url.toLowerCase(Locale.US);

			return urlLowerCase.contains("/oauth/") ||
							urlLowerCase.contains("/mobileoauth/") ||
							urlLowerCase.contains("/mobilesignin/") ||
							urlLowerCase.contains("/mobilecontent/") ||
							urlLowerCase.contains("//m.facebook");
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);

			if (getActivity() != null)
				onLoginFinished(new ErrorActivity.IntentBuilder(getActivity()).withAdditionalMessage(description).build());
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			if (mProgressHolder != null) {
				mProgressHolder.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);

			if (mProgressHolder != null && !url.startsWith(AppConstants.OAUTH_CALLBACK_URL)) {
				mProgressHolder.setVisibility(View.GONE);
			}
		}
	}

	private static class FixedWebView extends WebView {
		public FixedWebView(Context context) {
			super (context);
		}

		public FixedWebView(Context context, AttributeSet attrs, int defStyle) {
			super (context, attrs, defStyle);
		}

		public FixedWebView(Context context, AttributeSet attrs) {
			super (context, attrs);
		}

		@Override
		public void onWindowFocusChanged(boolean hasWindowFocus) {
			try {
				super.onWindowFocusChanged(hasWindowFocus);
			} catch (NullPointerException e) {
				// Catch null pointer exception
				// suggested workaround from Web
			}
		}
	}
}
