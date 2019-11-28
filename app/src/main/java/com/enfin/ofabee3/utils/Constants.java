package com.enfin.ofabee3.utils;

public class Constants {

    //Shared Preference Values
    public static final String PREFS_NAME = "OFABEE";
    public static final String COUNTRY = "country";

    //Default Values
    public static final String OS = "android";
    public static final String COUNTRY_IN = "IN";
    public static final String SMS_HASH_KEY = "l+q67TnFypZ";

    //For production
    public static final String BASE_URL = "https://testing-ofabee.enfinlabs.com/";
    //public static final String BASE_URL = "https://sglearningapp.com/";
    //public static final String BASE_URL = "https://neyyaracademy.com/";

    public static final String BASE_CD_HOST = "testing-ofabee.enfinlabs.com";
    //public static final String BASE_CD_HOST = "neyyaracademy.com";

    public static final String TERMS_CONDITION_URL = "https://testing-ofabee.enfinlabs.com/terms-and-condition";
    public static final String PRIVACY_POLICY_URL = "https://testing-ofabee.enfinlabs.com/privacy-policy";
    //public static final String TERMS_CONDITION_URL = "https://neyyaracademy.com/terms-and-condition";
    //public static final String PRIVACY_POLICY_URL = "https://neyyaracademy.com/privacy-policy";

    //Api methods
    public static final String GET_LOCATION = "current_location";
    public static final String USER_REGISTRATION = "signup";
    public static final String USER_LOGIN = "login";
    public static final String OTP_VALIDATION = "otp_verification";
    public static final String FORGOT_PASSWORD_GET_OTP = "send_otp";
    public static final String FORGOT_PASSWORD = "forgot_password";
    public static final String MY_COURSES = "my_courses";
    public static final String RESET_PASSWORD = "reset_password";
    public static final String COURSE_CATEGORY = "categories";
    public static final String RESEND_OTP = "send_otp";


    //Response Codes
    public static final String SUCCESS_ = "200";
    public static final String ERROR_404_ = "404";
    public static final String ERROR_401_ = "401";

    //Response Codes
    public static final int SUCCESS = 200;
    public static final int ERROR_404 = 404;
    public static final int ERROR_401 = 401;
    public static final int SUCCESS_FILE_UPLOAD = 200;
    public static final int ERROR_FILE_UPLOAD = 400;
    public static final int ERROR_204 = 204;

    //Intent Keys
    public static final String USER_NUMBER = "user_number";
    public static final String USER_EMAIL = "user_email";


    //Database Keys
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ofabee";
    public static final String TABLE_USER = "user";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_EMAIL = "userEmail";
    public static final String KEY_USER_PHONE_NUMBER = "userNumber";
    public static final String USER_COUNTRY_CODE = "country_code";
    public static final String KEY_USER_IMAGE = "userImage";
    public static final String KEY_USER_STATUS = "userStatus";
    public static final String KEY_DEVICE_ID = "deviceId";
    public static final String KEY_EMAIL_VERIFIED = "isEmailVerified";
    public static final String KEY_CATEGORY_ID = "categoryId";
    public static final String KEY_ACCESS_TOKEN = "accessToken";
    public static final String KEY_USER_CATEGORY_IDS = "userCategory";

    public static final String USER_ID = "user_id";
    public static final String USER_VALID = "user_valid";
    public static final String USER_OTP = "user_otp";
    public static final String USER_PASSWORD = "user_password";
    public static final String USER_SOURCE = "user_source";
    public static final String USER_NAME = "user_name";
    public static final String REGISTRATION_SOURCE = "registration";
    public static final String CATEGORY_ID_LIST = "category_id_list";
    public static final String SIGN_OUT = "signout";

    //fragment tags
    public static final String DEFAULT_FRAGMENT_TAG = "default";
    public static final String HOME_FRAGMENT_TAG = "home";
    public static final String EDIT_PROFILE_FRAGMENT_TAG = "editProfile";
    public static final String MY_COURSE_FRAGMENT_TAG = "myCourses";
    public static final String MY_ACCOUNT_FRAGMENT_TAG = "myAccount";

    //fragment titles
    public static final String EDIT_PROFILE_FRAGMENT_TITLE = "Edit Profile";
    public static final String MY_ACCOUNT_FRAGMENT_TITLE = "My Account";

    public static final String DB_CLEAR = "db_cleared";

    public static final String BEARER = "Bearer ";

    public static final String HOME_DATA = "home_data";
    public static final String MYCOURSE_DATA = "mycourse_data";
    public static final String NO_DATA = "204";
    public static final String EXPLORE_FRAGMENT_TAG = "Explore";
    public static final String COURSE_ID = "courseId";
    public static final String TYPE = "type";
    public static final String SUBJECT_NAME = "subject";

    public static final String DESTINATION = "destination";
    public static final String COURSE_NAME = "name";
    public static final String NOT_PURCHASED = "not_purchased";
    public static final String PURCHASED = "purchased";
    public static final String CATEGORY_IDS = "category_ids";
    public static final String APIKEY = "razorpay_apikey";
}
