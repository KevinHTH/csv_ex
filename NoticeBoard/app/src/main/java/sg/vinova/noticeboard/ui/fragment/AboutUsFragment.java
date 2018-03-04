package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import sg.vinova.noticeboard.R;

/**
 * Created by Jacky on 4/26/17.
 */

public class AboutUsFragment extends BaseAppFragment {
    public static AboutUsFragment newInstance() {
        AboutUsFragment fragment = new AboutUsFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getBaseAppActivity().getToolbarHelper().setTitle(getString(R.string.setting_about_us));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_about_us;
    }
}
