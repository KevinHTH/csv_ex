package sg.vinova.noticeboard.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import sg.vinova.noticeboard.MyApplication;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.ui.fragment.HomeFragment;
import vn.eazy.core.base.activity.BaseActivity;
import vn.eazy.core.base.fragment.BaseFragment;
import vn.eazy.core.helper.FragmentHelper;
import vn.eazy.core.toolbar.ToolbarHelper;

/**
 * Created by cuong on 4/25/17.
 */

public class BaseAppActivity extends BaseActivity {
    protected BaseFragment currentFragment;
    public FragmentHelper fragmentHelper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.fragmentHelper = new FragmentHelper(this.getSupportFragmentManager(), R.id.fragment_content);
        MyApplication.getInstance().getAppComponent().inject(this);
    }

    @Override
    public void setUpViewsAndData() {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void setTitleToolbar(String s) {

    }

    @Override
    public void setTitleToolbar(String s, String s1) {

    }

    @Override
    public void setTitleMainColor(int i) {

    }

    @Override
    public void showBackButton(boolean b) {
    }

    @Override
    public void showMenu(boolean b) {

    }

    public void changeFragment(BaseFragment fragment, boolean isBackStack) {
        currentFragment = fragment;
        if (isBackStack) {
            showBackButton(true);

        } else {
            showBackButton(false);
        }
        fragmentHelper.replaceFragment(fragment, isBackStack, R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public ToolbarHelper getToolbarHelper() throws IllegalAccessException {
        return null;
    }

    public boolean useFragmentState() {
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(currentFragment!=null){
            currentFragment.onActivityResult(requestCode,resultCode,data);
        }
    }


    public void showAlertDialog(String message, String posStr, DialogInterface.OnClickListener posClick, String negaStr, DialogInterface.OnClickListener negaClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.layout_custom_dialog);
        builder.setMessage(message);
        builder.setPositiveButton(posStr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (posClick != null) {
                    posClick.onClick(dialog, id);
                    dialog.dismiss();
                }

            }
        });
        builder.setNegativeButton(negaStr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (negaClick != null) {
                    negaClick.onClick(dialog, id);
                    dialog.dismiss();
                }

            }
        });

        builder.create().show();
    }

    public void showAlertDialog(String message, String posStr, DialogInterface.OnClickListener posClick, String negaStr) {
        showAlertDialog(message, posStr, posClick, negaStr, null);
    }

    public FragmentHelper getFragmentHelper() {
        if(fragmentHelper==null){
            this.fragmentHelper = new FragmentHelper(this.getSupportFragmentManager(), R.id.fragment_content);
        }
        return fragmentHelper;
    }


}
