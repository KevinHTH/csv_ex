package sg.vinova.noticeboard.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.ui.fragment.FirstScreenFragment;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.widgets.AppTextView;
import vn.eazy.core.toolbar.ToolbarHelper;

/**
 * Created by Jacky on 5/6/17.
 */

public class FirstScreenActivity extends BaseMainActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.layoutFooter)
    LinearLayout layoutFooter;

    @BindView(R.id.tvFooter)
    AppTextView tvFooter;

    @BindView(R.id.photoFooter)
    RoundedImageView photoFooter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeFragment(FirstScreenFragment.newInstance(), false);

        if (!TextUtils.isEmpty(CacheUtils.getAuthToken(this)) && CacheUtils.getVerifyPhone(getBaseContext())) {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        } else {
            changeFragment(FirstScreenFragment.newInstance(), false);
        }

        hideFooter(true);
        hideToolbar(true);
    }

    @Override
    public void setTitleToolbar(String s) {
        super.setTitleToolbar(s);
    }

    @Override
    public int onColorOfToolbar() {
        return 0;
    }

    @Override
    public int onImageForLeftButtonToolbar() {
        return R.mipmap.back;
    }

    public void hideToolbar(boolean isHide) {
        if (isHide) {
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    public void setFooterText(String string) {
        if (!TextUtils.isEmpty(string)) {
            tvFooter.setText(string);
        } else {
            tvFooter.setText("");
        }
    }

    public void setPhotoFooter(String url) {
        if (url != null) {
            Glide.with(getBaseContext())
                    .load(url).placeholder(R.mipmap.image_loading)
                    .centerCrop().into(photoFooter);
            photoFooter.setVisibility(View.VISIBLE);
        }
    }

    public void hideFooter(boolean isHide) {
        if (isHide) {
            layoutFooter.setVisibility(View.GONE);
        } else {
            layoutFooter.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public ToolbarHelper getToolbarHelper() throws IllegalAccessException {
        return super.getToolbarHelper();
    }

    public ToolbarHelper getMainToolbarHelper() {
        try {
            return getToolbarHelper();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
