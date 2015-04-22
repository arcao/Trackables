package com.arcao.trackables.ui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arcao.trackables.R;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Builder;

public class ErrorActivity extends AppCompatActivity {
	private static final String PARAM_RESOURCE_TITLE = "RESOURCE_TITLE";
	private static final String PARAM_RESOURCE_TEXT = "RESOURCE_TEXT";
	private static final String PARAM_ADDITIONAL_MESSAGE = "ADDITIONAL_MESSAGE";
	private static final String PARAM_INTENT = "INTENT";


	@Override
	protected void onPostResume() {
		super.onPostResume();

		showErrorDialog();
	}

	private void showErrorDialog () {
		final int resTitleId = getIntent().getIntExtra(PARAM_RESOURCE_TITLE, 0);
		final int resTextId = getIntent().getIntExtra(PARAM_RESOURCE_TEXT, 0);
		final String additionalMessage = getIntent().getStringExtra(PARAM_ADDITIONAL_MESSAGE);
		final Intent intent = getIntent().getParcelableExtra(PARAM_INTENT);

		if (getFragmentManager().findFragmentByTag(ErrorDialogFragment.FRAGMENT_TAG) != null)
			return;

		ErrorDialogFragment.newInstance(resTitleId, resTextId, additionalMessage, intent)
			.show(getFragmentManager(), ErrorDialogFragment.FRAGMENT_TAG);
	}


	public static class ErrorDialogFragment extends DialogFragment {
		public static final String FRAGMENT_TAG = ErrorDialogFragment.class.getName();


		public static DialogFragment newInstance(int resTitleId, int resTextId, String additionalMessage, Intent intent) {
			ErrorDialogFragment fragment = new ErrorDialogFragment();
			fragment.setCancelable(false);

			Bundle args = new Bundle();
			args.putInt(PARAM_RESOURCE_TITLE, resTitleId == 0 ? R.string.error_title : resTitleId);
			args.putInt(PARAM_RESOURCE_TEXT, resTextId);
			args.putString(PARAM_ADDITIONAL_MESSAGE, additionalMessage);
			args.putParcelable(PARAM_INTENT, intent);

			fragment.setArguments(args);

			return fragment;
		}

		@NonNull
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final int resTitleId = getArguments().getInt(PARAM_RESOURCE_TITLE);
			final int resTextId = getArguments().getInt(PARAM_RESOURCE_TEXT);
			final String additionalMessage = getArguments().getString(PARAM_ADDITIONAL_MESSAGE);
			final Intent intent = getArguments().getParcelable(PARAM_INTENT);

			MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
							.title(resTitleId)
							.positiveText(R.string.button_ok);

			if (resTextId != 0) {
				builder.content(getString(resTextId, StringUtils.defaultString(additionalMessage)));
			} else {
				builder.content(StringUtils.defaultString(additionalMessage));
			}

			builder.callback(new MaterialDialog.ButtonCallback() {
				@Override
				public void onPositive(MaterialDialog dialog) {
					if (intent != null) {
						getActivity().startActivity(intent);
					}
					getActivity().finish();
				}
			});

			return builder.build();
		}
	}


	public static final class IntentBuilder implements Builder<Intent> {
		private final Context mContext;
		private int mTitle = 0;
		private int mText = 0;
		private String mAdditionalMessage;
		private Intent mIntent;

		public IntentBuilder(Context context) {
			mContext = context;
		}

		public IntentBuilder withTitle(int title) {
			mTitle = title;
			return this;
		}

		public IntentBuilder withText(int text) {
			mText = text;
			return this;
		}

		public IntentBuilder withAdditionalMessage(String additionalMessage) {
			mAdditionalMessage = additionalMessage;
			return this;
		}

		public IntentBuilder withIntent(Intent intent) {
			mIntent = intent;
			return this;
		}

		public Intent build() {
			return new Intent(mContext, ErrorActivity.class)
							.putExtra(ErrorActivity.PARAM_RESOURCE_TITLE, mTitle)
							.putExtra(ErrorActivity.PARAM_RESOURCE_TEXT, mText)
							.putExtra(ErrorActivity.PARAM_ADDITIONAL_MESSAGE, mAdditionalMessage)
							.putExtra(ErrorActivity.PARAM_INTENT, mIntent);
		}
	}
}
