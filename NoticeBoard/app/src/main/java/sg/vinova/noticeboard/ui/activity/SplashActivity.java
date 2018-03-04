package sg.vinova.noticeboard.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import me.leolin.shortcutbadger.ShortcutBadger;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.utils.CacheUtils;

/**
 * Created by cuong on 4/26/17.
 */

public class SplashActivity extends BaseAppActivity {
    private static final long TIME_SPLASH = 4000;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShortcutBadger.removeCount(getBaseContext());
        Log.d("Log", "SplashActivity");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(() -> {
            if (!isDestroyed()) {
                if (!TextUtils.isEmpty(CacheUtils.getAuthToken(SplashActivity.this))) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, FirstScreenActivity.class));

                }

                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();

            }

        }, TIME_SPLASH);

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }
}
