package sg.vinova.noticeboard.utils;

import org.json.JSONObject;

import timber.log.Timber;

/**
 * Created by Ray on 3/6/17.
 */

public class ApiUtils {
    public static String getErrorMessage(String str) {
        try {
            if (Utils.isEmpty(str))
                return null;
            JSONObject obj = new JSONObject(str);
            JSONObject data = obj.getJSONObject("data");
            return data.getString("message");
        } catch (Exception exp) {
            Timber.e(exp.getMessage());
        }
        return null;
    }

    public static void setToken(String token) {
        CacheUtils.setAuthToken(token);
    }
}
