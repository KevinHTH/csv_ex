package sg.vinova.noticeboard.utils;

/**
 * Created by Ray on 3/6/17.
 */

public interface Constant {
    Integer PER_PAGE = 10;
    int SHARE_REQUEST_CODE = 312;
    String IMAGE_DIR = "imageDir";
    int  MAX_PHOTO_POST = 10;
    int REQUEST_TAKE_PHOTO = 11;
    final static int REQEST_SENDMAIL=12;
    int REQUEST_EMAIL_LOGIN=3;
    int MEDIA_TYPE_IMAGE = 1;
    int MEDIA_TYPE_VIDEO = 2;
    String LIST_PHOTO = "list photo";

    String SERVER_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'.000Z'"; //"2016-08-09T11:43:16.000Z"

    String DATE_INPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss+00:00";       //"2017-05-10T07:26:13.860Z" // 2017-05-18T02:09:34+00:00"
    String DATE_OUTPUT_FORMAT = "dd MMM yyyy";
    String TRANSACTION_DATE_FORMAT = "yyyy-MM-dd";

    String IS_USER = "is user";
    String AUTH_TOKEN = "auth_token";
    String CLUSTER_TOKEN = "cluster_token";
    String CLUSTER_NAME = "name";
    String CLUSTER_RESPONSE ="cluster_response";
    String DEVICE_TOKEN = "device_token";
    String FIRST_POST = "first_post";
    String POSTAL_CODE ="postal_code";
    String FOOTER_TEXT ="footer_text";
    String FOOTER_PHOTO ="footer_photo";
    String VERIFY_PHONE ="verify_phone";

    interface LOGIN {
        String EMAIL = "email";
        String PASSWORD = "password";
        String USERNAME = "username";
        String PASSWORD_CONFIRMATION = "password_confirmation";

        String OLD_PASSWORD="old_password";
        String NEW_PASSWORD="new_password";
        String NEW_PASSWORD_CONFIRMATION="new_password_confirmation";

    }

    interface USER {
        String CACHE_USER_DATA = "USER_CACHE_USER_DATA";
        String ID = "USER_ID";
        String USERNAME = "USERNAME";
        String AVATAR = "USER_AVATAR";
        String NATIONAL_ID = "USER_NATIONAL_ID";
        String NATIONAL_NAME = "USER_NATIONAL_NAME";
        String PROFESSION_ID = "USER_PROFESSION_ID";
        String PROFESSION_NAME = "USER_PROFESSION_NAME";
        String ABOUT = "USER_ABOUT";
        String TOTAL_COMPLAINT = "USER_TOTAL_COMPLAINT";
        String TOTAL_FEEDBACK = "USER_TOTAL_FEEDBACK";
        String EMAIL = "USER_EMAIL";
        String GENDER = "USER_GENDER";
        String YEAR_OF_BIRTH = "USER_YEAR_OF_BIRTH";
        String API_TOKEN = "YEAR_OF_BIRTH";
        String CREATED_AT = "USER_CREATED_AT";
    }


}
