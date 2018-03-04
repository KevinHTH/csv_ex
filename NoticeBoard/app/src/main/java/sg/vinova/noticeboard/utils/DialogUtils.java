package sg.vinova.noticeboard.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.ui.fragment.NewItemDescriptionFragment;
import sg.vinova.noticeboard.widgets.AppDialog;

/**
 * Created by cuong on 3/8/17.
 */

public class DialogUtils {
    public static void showDialogMessage(Context context, String title) {
        showDialogMessage(context, title, null, null, "No", null, false, false);
    }

    public static void showDialogMessage(Context context, String title, String strBtn) {
        showDialogMessage(context, title, null, null,null , strBtn, false, false);
    }


    public static void showDialogMessage(Context context, String title, View.OnClickListener cancelListener, View.OnClickListener yesListener, String noStr, String yesStr,
                                         boolean isLeftCaps, boolean isRightCaps) {
        AnimationDialogApp dialogApp = new AnimationDialogApp(context);
        dialogApp.setTitle(title);
        dialogApp.setYesListener(yesListener);
        dialogApp.setNoListener(cancelListener);
        dialogApp.setYesString(yesStr);
        dialogApp.setNoString(noStr);
        dialogApp.setLeftCaps(isLeftCaps);
        dialogApp.setRightCaps(isRightCaps);
        dialogApp.show();
    }

    public static void showDialogSubmit(Context context,Fragment fragment){
        AppDialog appDialog = new AppDialog(context);
        appDialog.setTitle(context.getString(R.string.before_tack));
        appDialog.setTextLeftButton("Cancel");
        appDialog.setTextRightButton("Submit & Tack");

        appDialog.setTextInCheckbox(context.getString(R.string.title_term_tackit),context.getString(R.string.term_part1), context.getString(R.string.term_part2));
        appDialog.setOnClickAppDialog((AppDialog.OnClickAppDialog) fragment);
        appDialog.show();
    }
}
