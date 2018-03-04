package sg.vinova.noticeboard.utils;

import android.content.Context;

import sg.vinova.noticeboard.MyApplication;
import sg.vinova.noticeboard.model.ClusterResponse;
import sg.vinova.noticeboard.model.LoginResponse;
import vn.eazy.core.utils.PreferencesUtils;

/**
 * Created by cuong on 4/27/17.
 */

public class CacheUtils {
    public static String getAuthToken(Context context) {
        return PreferencesUtils.getString(context, Constant.AUTH_TOKEN);
    }

    public static void setAuthToken(Context context, String token) {
        PreferencesUtils.putString(context, Constant.AUTH_TOKEN, token);
    }

    public static void setAuthToken(String token) {
        PreferencesUtils.putString(MyApplication.getInstance(), Constant.AUTH_TOKEN, token);
    }

    public static void clearLoggedInData() {

        PreferencesUtils.clearAll(MyApplication.getInstance());
    }

    public static void setClusterToken(Context context, String token) {
        PreferencesUtils.putString(context, Constant.CLUSTER_TOKEN, token);
    }

    public static String getClusterToken(Context context) {
        return PreferencesUtils.getString(context, Constant.CLUSTER_TOKEN);
    }

    public static void setClusterData(ClusterResponse clusterResponse){
        SharedPreferenceHelper.getInstance().putPreferenceObject(Constant.CLUSTER_RESPONSE,clusterResponse);
    }

    public static ClusterResponse getClusterData(){
       return (ClusterResponse) SharedPreferenceHelper.getInstance().getPreferenceObject(Constant.CLUSTER_RESPONSE, ClusterResponse.class);
    }



    // user
    public static void saveDataUser(LoginResponse login) {
        SharedPreferenceHelper.getInstance().putPreferenceObject(Constant.USER.CACHE_USER_DATA, login);
    }

    public static LoginResponse  getDataUser() {
        return (LoginResponse) SharedPreferenceHelper.getInstance().getPreferenceObject(Constant.USER.CACHE_USER_DATA, LoginResponse.class);
    }

    // notification

    public static void setDeviceToken(Context context, String token){
        PreferencesUtils.putString(context,Constant.DEVICE_TOKEN,token);
    }

    public static String getDeviceToken(Context context){
        return PreferencesUtils.getString(context, Constant.DEVICE_TOKEN);
    }

    public static void setFirstPostItem(Context context, boolean isFirstPost){
        PreferencesUtils.putBoolean(context,Constant.FIRST_POST,isFirstPost);
    }

    public static  boolean getFirstPostItem(Context context){
        return PreferencesUtils.getBoolean(context,Constant.FIRST_POST);
    }

    public static void setFooterPhoto(Context context, String url){
        PreferencesUtils.putString(context,Constant.FOOTER_PHOTO,url);
    }

    public static String getFooterPhoto(Context context){
        return PreferencesUtils.getString(context, Constant.FOOTER_PHOTO);
    }

    public static void setVerifyPhone(Context context, boolean isVerify){
        PreferencesUtils.putBoolean(context,Constant.VERIFY_PHONE,isVerify);
    }

    public static boolean getVerifyPhone(Context context){
        return PreferencesUtils.getBoolean(context, Constant.VERIFY_PHONE);
    }

}
