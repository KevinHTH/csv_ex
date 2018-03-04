package sg.vinova.noticeboard.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import butterknife.OnClick;
import me.leolin.shortcutbadger.ShortcutBadger;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.ui.fragment.HomeFragment;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.Utils;

/**
 * Created by cuong on 4/26/17.
 */

public class EnterMyPostcodeActivity extends BaseAppActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ShortcutBadger.removeCount(getBaseContext());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_enter_my_code;
    }

    @OnClick(R.id.btnEnterMyCode)
    void onClick() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
