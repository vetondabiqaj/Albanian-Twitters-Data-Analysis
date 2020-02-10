/**
 * 
 */
package twitterreasoming;

/**
 * Twitter4j*
 */
import twitter4j.*;
import java.util.List;
import java.util.Locale;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.RawStreamListener;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.bson.Document;

import twitter4j.auth.AccessToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import twitter4j.conf.ConfigurationBuilder;

/**
 * @author Veton Dabiqaj
 *
 */
public class TwStructure {

	public static void straming() {
		TwitterStream twitterStream = getTwitterStreamInstance();
		twitterStream.addListener(new StatusListener() {
			@Override
			public void onStatus(Status status) {
				Document document = convertTweetForDb(status);
				if (document != null) {
					DBStructure.insert("all_twitts", document);
					DBStructure.insert("test", document);
					System.out.println(document);
					document.clear();
				}
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
				// System.out.println("Got a status deletion notice id:" +
				// statusDeletionNotice.getStatusId());
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				// System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
				// System.out.println("Got scrub_geo event userId:" + userId + " upToStatusId:"
				// + upToStatusId);
			}

			@Override
			public void onStallWarning(StallWarning warning) {
				// System.out.println("Got stall warning:" + warning);
			}

			@Override
			public void onException(Exception ex) {
				ex.printStackTrace();
			}
		});

		FilterQuery fq = new FilterQuery(10);
		// String keywords[] = {"rtl2","deutschland","usa"};
		// fq.count(10);
		// fq.track(new String[] { "usa" });
		// fq.locations(new double[][]{new double[]{-124.848974, 24.396308}, new
		// double[]{-66.885444, 49.384358}});
		// fq.language(new String[]{"alb","al","sq","sqi","en"});
		// twitterStream.filter(fq);

		FilterQuery tweetFilterQuery = new FilterQuery(); // See
		// tweetFilterQuery.track(new String[]{"Bieber", "Teletubbies"}); // OR on
		// keywords
		tweetFilterQuery.language(new String[] { "al", "und", "en","it","de", "tr" });
		tweetFilterQuery.locations(new double[][] { { -180, -90 }, { 180, 90 }
				// { 19.379981, 40.089955 }, { 20.500442, 42.553468 }
				// ,{41.153332,20.168331}
				// ,{42.665440, 21.165319}
				// ,{41.1495562,18.9656878}
				// , {42.5623025,20.340292}
		});
		// See https://dev.twitter.com/docs/streaming-apis/parameters#locations for
		// proper location doc.
		// Note that not all tweets have location metadata set.
		// tweetFilterQuery.language(new String[]{"en"});

		twitterStream.filter(tweetFilterQuery);

	}

	private static Properties TwOAuth() {
		InputStream input = TwitterReasoming.class.getClassLoader().getResourceAsStream("resources/config.properties");
		if (input == null) {
			System.out.println("Sorry, unable to find config.properties");
			// return;
		}
		Properties prop = new Properties();
		try {
			prop.load(input);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return prop;
	}

	private static TwitterStream getTwitterStreamInstance() {
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		Properties prop = TwOAuth();
		twitterStream.setOAuthConsumer(prop.getProperty("twitter.consumer_key"),
				prop.getProperty("twitter.consumer_secret"));
		twitterStream.setOAuthAccessToken(
				new AccessToken(prop.getProperty("twitter.access_token"), prop.getProperty("twitter.access_secret")));
		return twitterStream;
	}

	private static Twitter getTwitterinstance() {
		Twitter twitter = new TwitterFactory().getInstance();
		Properties prop = TwOAuth();
		twitter.setOAuthConsumer(prop.getProperty("twitter.consumer_key"), prop.getProperty("twitter.consumer_secret"));
		twitter.setOAuthAccessToken(
				new AccessToken(prop.getProperty("twitter.access_token"), prop.getProperty("twitter.access_secret")));
		return twitter;
	}

	public static void search(int count, long id) {
		Twitter twitter = getTwitterinstance();
		Document document = new Document();
		int loop = 0;
		try {
			Query query = new Query();
			// query.setMaxId(id);
			// query.setUntil("2018-12-20");
			// query.setSinceId(id);
			// query.setLang("al");
			// query.setLocale("USA");
			query.setQuery("usa");
			// query.setUntil("2019-12-25");

			// query.setLocale("xks");
			// query.setLang("tr");
			query.setCount(10);
			QueryResult result;
			do {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				for (Status tweet : tweets) {
					document = convertTweetForDb(tweet);
					if (document != null) {
						DBStructure.insert("all_twitts", document);
						document.clear();
					}
				}
				try {
					Thread.sleep(800);
				} catch (InterruptedException ie) {
					// Handle exception

				}
				loop++;

			} while ((query = result.nextQuery()) != null && loop < count);
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		}
	}

	private static Document convertTweetForDb(Status status) {

		Place place = status.getPlace();
		String lang = status.getLang();
		// System.out.print(1);
		Document document = new Document();
		if (place == null && !lang.equals("al")) {
			return null;
		}

		String placeCountryCode = null;
		if (place != null) {
			placeCountryCode = place.getCountryCode().toString();
		}
		if (lang.equals("al") || 
				(place != null && 
					(
							lang.equals("und") 
							|| lang.equals("en")
							|| lang.equals("tr")
							|| lang.equals("it")
							|| lang.equals("de")
							|| lang.equals("sr")
							|| lang.equals("mk")
					) 
				
				&& (
						(placeCountryCode.equals("XK") || placeCountryCode.equals("AL")))
				)
				) {

			document.put("lang", lang);

			HashtagEntity[] hashtagEntities = status.getHashtagEntities();
			if (hashtagEntities != null && hashtagEntities.length > 0) {
				StringBuilder s = new StringBuilder();
				s.append(hashtagEntities[0].getText());

				for (int i = 1; i < hashtagEntities.length; i++) {
					s.append(",");
					s.append(hashtagEntities[i].getText());
				}

				document.put("hashtag", s.toString());
			}

			User user = status.getUser();
			document.put("username", user.getScreenName());
			document.put("profileLocation", user.getLocation());
			document.put("profileLang", user.getLang());
			document.put("profileDescription", status.getUser().getDescription());

			GeoLocation geoLocation = status.getGeoLocation();
			if (geoLocation != null) {
				document.put("latitude", geoLocation.getLatitude());
				document.put("longitude", geoLocation.getLongitude());
			}

			document.put("screenName", status.getUser().getScreenName());
			document.put("text", status.getText());
			document.put("id", status.getId());
			document.put("createdAt", status.getCreatedAt());
			document.put("currentUserRetweetId", status.getCurrentUserRetweetId());
			document.put("inReplyToScreenName", status.getInReplyToScreenName());
			document.put("isRetweet", status.isRetweet());
			document.put("isRetweeted", status.isRetweeted());
			document.put("isPossiblySensitive", status.isPossiblySensitive());
			if (place != null) {
				document.put("placecountry", place.getCountry());
				document.put("placeStreetAddress", place.getStreetAddress());
				document.put("placeName", place.getName());
				document.put("placeId", place.getId());
				document.put("placeFullName", place.getFullName());
				document.put("placeUrl", place.getURL());
				document.put("placeCountryCode", placeCountryCode);
			}

			return document;
		}
		return null;

	}

}
