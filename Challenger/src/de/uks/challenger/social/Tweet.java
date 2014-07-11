package de.uks.challenger.social;

import java.util.Date;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.os.AsyncTask;

public class Tweet {
	// Singleton
	private static Tweet instance = null;

	// Keys identifizieren die App bei Twitter
	private final String CONSUMER_KEY = "19jOHaRgD6442wii0KsgLkXjG";
	private final String CONSUMER_KEY_SECRET = "h844otd1VkJzFKeXgmjCHy5ZYSAdi5LIuEqoflBI95x9ZWGq8f";
	private Twitter twitter;
	private RequestToken requestToken;

	public static Tweet getInstance() {
		if (instance == null) {
			instance = new Tweet();
		}
		return instance;
	}

	private Tweet() {
		TwitterTask taskNew = new TwitterTask();
		taskNew.execute();

	}

	// Tweet absenden
	public void tweet(String message, String accessToken,
			String accessTokenSecret) {
		try {
			AccessToken oathAccessToken = new AccessToken(accessToken,
					accessTokenSecret);
			twitter.setOAuthAccessToken(oathAccessToken);
			twitter.updateStatus(message + new Date().toString() + " #fit #WM14");
		} catch (TwitterException e) {
			e.printStackTrace();
		}
	}

	// Gibt URL zum Authentifizieren zurueck
	public String newConfig() {
		String link = "";
		link = requestToken.getAuthorizationURL();
		return link;
	}

	// Gibt Accesstoken zum Eindeutigen authentifizieren zurueck, wird spaeter
	// fuer einloggen benutzt
	public AccessToken getLogin(String pin) {
		try {
			AccessToken accessToken = null;
			if (pin.length() > 0) {
				accessToken = twitter.getOAuthAccessToken(requestToken, pin);
			} else {
				accessToken = twitter.getOAuthAccessToken();
			}
			return accessToken;

		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return null;

	}

	// Neuer Twitterverbindung muss asynchron erstellt werden
	private class TwitterTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
			try {
				requestToken = twitter.getOAuthRequestToken();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
