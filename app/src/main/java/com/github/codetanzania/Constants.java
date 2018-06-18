package com.github.codetanzania;

public final class Constants {

    public static final int SUCCESS_RESULT = 0;
    public static final String APP_VERSION_CODE = "version_code";
    public static final String KEY_SHARED_PREFS = "shared_prefs";
    private static final String PACKAGE_NAME = "com.github.codetanzania";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";
    public static final String KEY_DEFAULT_LANGUAGE = "default_language";

    // separate majifix and company specific constants. makes it easy to migrate changes
    public static final class MajiFix {
        public static final String REPORTER_NAME    = "reporter.full_name";
        public static final String REPORTER_EMAIL   = "reporter.email";
        public static final String REPORTER_PHONE   = "reporter.phone";
        public static final String REPORTER_WATER_ACCOUNT = "reporter.water.account";
        public static final String CURRENT_USER_ID = "user._id";
        public static final String TICKET = "app.ticket";
        public static final String SMS_VERIFICATION_CODE = "app.otp";
    }

    public static final class Company {
        public static final String USER_VERIFIED = "app.user.verified";
        public static final String COMPANY_CUSTOMER_PASSWORD = "app.user.password";
        public static final String COMPANY_CUSTOMER_SIGNED_IN = "reporter.loggedIn";
    }
}
