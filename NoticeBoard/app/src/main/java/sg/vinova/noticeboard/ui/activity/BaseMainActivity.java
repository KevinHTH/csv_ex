package sg.vinova.noticeboard.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.utils.Utils;
import vn.eazy.core.toolbar.OnCallBackToolbarAction;
import vn.eazy.core.toolbar.ToolbarHelper;

public abstract class BaseMainActivity<T extends ToolbarHelper> extends BaseAppActivity implements OnCallBackToolbarAction {
    private final String NULL_TOOLBAR_EX = "Can\'t find toolbar of this activity. Please checking it. Note: With raw id : R.id.toolbar";
    public ToolbarHelper toolbarHelper;
    protected Toolbar toolbar;


    public BaseMainActivity() {
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        View statusBar = toolbar.findViewById(R.id.viewStatusBar);
        ((LinearLayout.LayoutParams) statusBar.getLayoutParams()).height = Utils.getStatusBarHeight(this);
        statusBar.requestLayout();


        try {
            this.setupToolbar();
        } catch (IllegalAccessException var3) {
            var3.printStackTrace();
        }

    }

    public void onCallBackToolbar() {

        this.fragmentHelper.popBackStack();

    }

    public void setTitleToolbar(String msg) {
        this.toolbarHelper.setTitle(msg);
    }

    public void setTitleToolbar(@NonNull String msg, @NonNull String font) {
        this.toolbarHelper.setTitle(msg, font);
    }

    public void showBackButton(boolean isShow) {
        this.toolbarHelper.showBackButton(isShow, this);
    }

    public void setTitleMainColor(int color) {
        this.toolbarHelper.setTitleMainColor(color);
    }

    private void setupToolbar() throws IllegalAccessException {
        if (this.toolbar == null) {
            throw new NullPointerException("Can\'t find toolbar of this activity. Please checking it. Note: With raw id : R.id.toolbar");
        } else {
            this.setSupportActionBar(this.toolbar);
            this.toolbar.setBackgroundResource(this.onColorOfToolbar());
            this.toolbarHelper = this.getToolbarHelper();
            if (this.toolbarHelper == null) {
                this.toolbarHelper = new ToolbarHelper(this.toolbar);
            }

            this.toolbarHelper.setImageForLeftButton(this.onImageForLeftButtonToolbar());
        }
    }

    public abstract int onColorOfToolbar();

    public abstract int onImageForLeftButtonToolbar();

    public T getToolbarHelper() throws IllegalAccessException {
        return (T) this.toolbarHelper;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}