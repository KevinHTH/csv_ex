package sg.vinova.noticeboard.utils;

import android.content.Context;
import android.text.TextUtils;

import java.lang.ref.WeakReference;

import vn.eazy.loader.EzLoader;

/**
 * Created by cuong on 3/7/17.
 */

public class LoaderUtils {
    private static EzLoader ezLoader = new EzLoader();

    public static void show(Context context) {
        show(context, "");

    }

    public static void show(Context context, String title) {

        try {
            if (TextUtils.isEmpty(title)) {
                ezLoader.showLoading(new WeakReference<>(context).get());
            } else

            {
                ezLoader.showLoading(title, new WeakReference<>(context).get());
            }
        } catch (Exception e) {
            Log.e("Loader", e.getMessage());
        }

    }

    public static void hide() {
        ezLoader.hideLoading();
    }
}
