package essentials.streamProcessing.support;

import java.util.Properties;

public class Util {
    public static Properties loadCredentials() {
        Properties tokens = new Properties();
        tokens.setProperty("consumerKey", System.getenv("TWITTER_API_KEY"));
        tokens.setProperty("consumerSecret", System.getenv("TWITTER_API_SECRET"));
        tokens.setProperty("token", System.getenv("TWITTER_TOKEN"));
        tokens.setProperty("tokenSecret", System.getenv("TWITTER_TOKEN_SECRET"));
        return tokens;
    }
}
