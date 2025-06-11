package skolard.Config;

/**
 * Global configuration class for application-wide constants.
 * Contains values related to API, authentication, timeout settings,
 * and database paths. This class is not meant to be instantiated.
 */
public class Config {
    
    // Base URL for the SkolarD backend API
    public static final String BASE_URL = "https://api.skolard.com";

    // API version string used in routing
    public static final String API_VERSION = "v1";

    // Full API URL constructed by combining base URL and version
    public static final String API_URL = BASE_URL + "/" + API_VERSION;

    // Key used to store or retrieve the user's authentication token
    public static final String AUTH_TOKEN = "auth_token";

    // Key used to store or retrieve the user's unique ID
    public static final String USER_ID = "user_id";

    // Default timeout value (in seconds) for API requests
    public static final int TIMEOUT_SECONDS = 30;

    // Maximum number of retries allowed for a failed request
    public static final int MAX_RETRIES = 3;

    // File path to the production SQLite database
    public static final String PROD_DB = "skolard/app/src/main/skolard.db";

    // File path to the testing SQLite database
    public static final String TEST_DB = "src/test/test.db";

    /**
     * Private constructor prevents instantiation of this utility class.
     * All members are static and accessed directly.
     */
    private Config() {}
}
