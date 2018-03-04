package sg.vinova.noticeboard.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.makeramen.roundedimageview.RoundedImageView;

import butterknife.BindView;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.ui.fragment.HomeFragment;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.Utils;
import sg.vinova.noticeboard.widgets.AppEditText;
import sg.vinova.noticeboard.widgets.AppTextView;
import sg.vinova.noticeboard.widgets.WidgetUtils;
import vn.eazy.core.toolbar.ToolbarHelper;

public class MainActivity extends BaseMainActivity {

    @BindView(R.id.layoutFooter)
    LinearLayout layoutFooter;
    @BindView(R.id.tvFooter)
    AppTextView tvFooter;
    @BindView(R.id.photoFooter)
    RoundedImageView photoFooter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Utils.isEmpty(CacheUtils.getAuthToken(this)) && CacheUtils.getVerifyPhone(getBaseContext())) {
            changeFragment(HomeFragment.newInstance(), false);
        } else {
            startActivity(new Intent(this, FirstScreenActivity.class));
            finish();
        }
        hideFooter(false);
    }

    @Override
    public void setTitleToolbar(String msg) {
        super.setTitleToolbar(msg);
    }

    @Override
    public int onColorOfToolbar() {
        return 0;
    }

    @Override
    public int onImageForLeftButtonToolbar() {
        return R.mipmap.back;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof AppEditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    WidgetUtils.hideSoftKeyboard(MainActivity.this);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheUtils.clearLoggedInData();
    }

    public void hideFooter(boolean isHide) {
        if (!isHide) {
            layoutFooter.setVisibility(View.VISIBLE);
        } else {
            layoutFooter.setVisibility(View.GONE);
        }
    }

    @Override
    public ToolbarHelper getToolbarHelper() throws IllegalAccessException {
        return super.getToolbarHelper();
    }

    public void setFooterText(String string) {
        tvFooter.setText(string);
    }

    public void setPhotoFooter(String url) {
        if (url != null) {
            Glide.with(getBaseContext())
                    .load(url).placeholder(R.mipmap.image_loading).centerCrop().into(photoFooter);
            photoFooter.setVisibility(View.VISIBLE);

        }
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
