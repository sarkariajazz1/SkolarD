package skolard.Config;

public class Config {
    public static final String BASE_URL = "https://api.skolard.com";
    public static final String API_VERSION = "v1";
    public static final String API_URL = BASE_URL + "/" + API_VERSION;

    public static final String AUTH_TOKEN = "auth_token";
    public static final String USER_ID = "user_id";

    public static final int TIMEOUT_SECONDS = 30;
    public static final int MAX_RETRIES = 3;

    public static final String PROD_DB = "skolard/app/src/main/skolard.db";
    public static final String TEST_DB = "src/test/test.db";

    // Prevent instantiation
    private Config() {}
}
