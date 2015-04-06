package com.arcao.trackables.geocaching.oauth;

import com.arcao.trackables.geocaching.oauth.services.CustomTokenExtractorImpl;
import com.arcao.trackables.geocaching.oauth.services.ServerTimestampServiceImpl;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.RequestTokenExtractor;
import org.scribe.model.Token;
import org.scribe.services.TimestampService;
import org.scribe.utils.OAuthEncoder;

import java.net.URI;

public class GeocachingOAuthProvider extends DefaultApi10a {
	private static final URI TIMESTAMP_URI = URI.create("http://www.geocaching.com/robots.txt");
	private static final String OAUTH_URL = "https://www.geocaching.com/oauth/mobileoauth.ashx";

	private TimestampService timestampService = null;

	@Override
	public RequestTokenExtractor getRequestTokenExtractor() {
		return new CustomTokenExtractorImpl();
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new CustomTokenExtractorImpl();
	}

	@Override
	public TimestampService getTimestampService() {
		if (timestampService == null)
			timestampService = new ServerTimestampServiceImpl(TIMESTAMP_URI);
		return timestampService;
	}

	protected String getOAuthUrl() {
		return OAUTH_URL;
	}

	@Override
	public String getRequestTokenEndpoint() {
		return getOAuthUrl();
	}

	@Override
	public String getAccessTokenEndpoint() {
		return getOAuthUrl();
	}

	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return getOAuthUrl() + "?oauth_token=" + OAuthEncoder.encode(requestToken.getToken());
	}

	public static class Staging extends GeocachingOAuthProvider {
		private static final String OAUTH_URL = "https://staging.geocaching.com/oauth/mobileoauth.ashx";

		@Override
		protected String getOAuthUrl() {
			return OAUTH_URL;
		}
	}
}
