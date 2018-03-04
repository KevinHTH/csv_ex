package sg.vinova.noticeboard.utils;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import vn.eazy.core.helper.snackbar.SnackbarColor;

/**
 * Created by Ray on 3/6/17.
 */

public class SnackBarUtils {
    private static SnackBarUtils instance = null;


    public static SnackBarUtils getSnackInstance() {
        if (instance == null)
            instance = new SnackBarUtils();
        return instance;
    }

    public static SnackbarColor setSnackBarColor(Context context, int bgColorId, int msgColorId) {
        SnackbarColor snackbarColor = new SnackbarColor(context);
        snackbarColor.setMessageColor(msgColorId);
        snackbarColor.setBackgroundColor(bgColorId);
        return snackbarColor;
    }

    public void showSuccess(View view, String msg) {
//        TopSnackbar snackBar = new TopSnackbar();
//        try {
//            snackBar.show(view, msg,
//                    SnackBarUtils.setSnackBarColor(view.getContext(),
//                            ContextCompat.getColor(view.getContext(), R.color.colorPrimary),
//                            ContextCompat.getColor(view.getContext(), R.color.white)));
//        } catch (Exception e) {
//            Log.e("Snackbar", e.getMessage());
//        }
        Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();

    }

    public void showError(View view, String msg) {
//        TopSnackbar snackBar = new TopSnackbar();
//        try {
//            snackBar.show(view, msg,
//                    SnackBarUtils.setSnackBarColor(view.getContext(),
//                            ContextCompat.getColor(view.getContext(), R.color.red),
//                            ContextCompat.getColor(view.getContext(), R.color.white)));
//        } catch (Exception e) {
//            Log.e("Snackbar", e.getMessage());
//        }

        Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();

    }
}
