package sg.vinova.noticeboard.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.ui.fragment.TabLostFoundFragment;

/**
 * Created by cuong on 5/6/17.
 */

public class BlankActivity extends BaseAppActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeFragment(TabLostFoundFragment.newInstance(null),false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_blank;
    }
}
