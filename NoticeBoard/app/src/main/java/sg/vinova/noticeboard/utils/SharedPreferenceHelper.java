package sg.vinova.noticeboard.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import sg.vinova.noticeboard.MyApplication;


/**
 * Created by mb on 11/3/14.
 */
@SuppressLint("CommitPrefEdits")
public class SharedPreferenceHelper {

    public static final String DEFAULT_STRING_VALUE = "";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static SharedPreferenceHelper instance;

    public SharedPreferenceHelper(Context context) {
        sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public Integer getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public void putBool(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBool(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public void removeValue(String key) {
        editor.remove(key);
        editor.commit();
    }

    /* CLEAR ALL VALUE IN SHARED PREFERENCE */
    public void clearSharedPreference() {
        editor.clear();
        editor.commit();
    }

    /* REMOVE VALUE IN SHARED PREFERENCE */
    public void removeValueSharedPreference(String key) {
        editor.remove(key);
        editor.commit();
    }

    /* SAVE OBJECT IN SHARED PREFERENCE */
    public <T> Boolean putPreferenceObject(String key,
                                           Object object) {
        try {
            String jsonString = ParserUtil.toJson(object);
            editor.putString(key, jsonString);
            return editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }





    public <T> Object getPreferenceObject(String key, Class<T> classOfT) {
        if (!sharedPreferences.contains(key))
            return null;
        else {
            String jsonString = sharedPreferences.getString(key,
                    DEFAULT_STRING_VALUE);
            try {
                return ParserUtil.fromJson(jsonString, classOfT);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static SharedPreferenceHelper getInstance() {
        if (instance == null) {
            synchronized (SharedPreferenceHelper.class) {
                if (instance == null) {
                    if(MyApplication.getInstance() == null)
                        return null;
                    instance = new SharedPreferenceHelper(MyApplication.getInstance());
                }
            }
        }
        return instance;
    }

}
