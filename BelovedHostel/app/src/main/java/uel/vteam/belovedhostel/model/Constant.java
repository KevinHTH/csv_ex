package uel.vteam.belovedhostel.model;

/**
 * Created by Hieu on 11/10/2016.
 */
public class Constant {

    public final static int REQUEST_UPDATE_AVATAR = 1112;
    public final static int RESULT_UPDATE_AVATAR = 1113;

    //MODEL KEY
    public final static String ROOM_ID = "roomId";
    public final static String ROOM_AREA = "area";
    public final static String ROOM_BEDROOM = "bedroom";
    public final static String ROOM_FURNITURE = "furniture";
    public final static String ROOM_DISCOUNT = "discount";
    public final static String ROOM_PRICE = "price";
    public final static String ROOM_STATUS = "isBooked";
    public final static String ROOM_IMAGE = "IMAGE";
    public final static String ROOM_IMAGE_NAME = "imageName";
    public final static String ROOM_IMAGE_LINK = "imageLink";


    //=================Customer info=========================
    public final static String URL_PHONECODE = "https://gist.githubusercontent.com/Goles/3196253/raw/9ca4e7e62ea5ad935bb3580dc0a07d9df033b451/" +
            "CountryCodes.jsonhttps://gist.githubusercontent.com/Goles/3196253/raw/9ca4e7e62ea5ad935bb3580dc0a07d9df033b451/CountryCodes.json";
    public final static String URL_STORAGE = "gs://belovedhostel-4b23a.appspot.com";
    public final static String URL_DEFAULT_AVATAR="https://firebasestorage.googleapis.com/v0/b/belovedhostel-4b23a.appspot.com" +
            "/o/avatar_default.gif?alt=media&token=1ac6c0e1-0840-42ac-a0ff-615edde7de15";
    // bookin list
    public final static String UPDATE_FROM_LISTBOOKING_DIALOG = "Update List Room";


    // BUNDLE DATA KEY
    // data key
    public final static String BUNDLE_USER_ID = "USER_ID";
    public final static String BUNDLE_USER_PHONE = "USER_PHONE";
    public final static String BUNDLE_USER_NAME = "USER_NAME";
    public final static String BUNDLE_ROOM = "ROOM";
    public final static String BUNDLE_LIST_BOOKING = "LIST_BOOKING";
    public final static String BUNDLE_RESULT_UPDATE_PROFILE = "ketqua_update_profile";
    public final static String BUNDLE_MY_ACCOUNT="myAccount";
    public final static String BUNDLE_SELECTED_ACCOUNT="UserSelected";
    public final static String TYPE_ROOM="TYPE_ROOM";
    public final static String BUNDLE_ROOM_SELECTED="ROOM_SELECTED";

    //value
    public final static String BUNDLE_UPDATE_VALUE="BookingDialog";
    // action
    public final static String BUNDLE_ACTION_CONTROL="control";
    public final static String BUNDLE_ACTION_CONTROL_CONTINUE="continue";
    public final static String BUNDLE_ACTION_CONTROL_OPEN="open";
    public final static String BUNDLE_ACTION_NEW_MSG="new message";
    public final static String BUNDLE_ACTION_UPDATE_LIST="action_Update_list";




    // FIREBASE DATA KEY
    public final static  String FB_KEY_MANAGEMENT_ROOT ="MANAGEMENT";
    public final static  String FB_KEY_ROOT_MANAGEMENT_VOUCHER="VOUCHER";
    public final static String FB_KEY_USER_ROOT="USER";
    public final static String FB_KEY_USER_BILL="BILLS";
    public final static String FB_KEY_USER_ROOM="ROOM";
    public final static String FB_KEY_USER_INFO="USER_INFO";
    public final static String FB_KEY_USER_IDENTITY_NUMBER="identityNumber";
    public final static String FB_KEY_USER_NAME="userName";
    public final static String FB_KEY_USER_PHONE="userPhone";
    public final static String FB_KEY_USER_EMAIL="userEmail";
    public final static String FB_KEY_USER_PASSWORD="userPassword";
    public final static String FB_KEY_USER_AVATAR="userAvatar";
    public final static String FB_KEY_USER_STATUS="status";
    public final static String FB_KEY_USER_SESSION_CHAT="SessionChat";
    public final static String FB_KEY_USER_PERMISSION="permission";

    public final static String FB_KEY_USER_FRIEND_INFO="FRIEND_INFO";
    public final static String FB_KEY_USER_CHAT_BOX="CHAT_BOX";

    public final static String FB_ROOM_SINGLE="SINGLE_ROOM";
    public final static String FB_ROOM_SHARED="SHARED_ROOM";




    // key use only one time
    public final static String LAN_ENGLISH="English";
    public final static String LAN_VIETNAM="VietNam";
    public final static String KEY_ADULT="Adult";
    public final static String KEY_CHILDREN="Children";
    public final static String KEY_NGUOILON="Người lớn";
    public final static String KEY_TREEM="Trẻ em";


}
