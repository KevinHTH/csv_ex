package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import sg.vinova.noticeboard.R;

/**
 * Created by cuong on 4/26/17.
 */

public class BlankFragment extends BaseAppFragment {
    public static BlankFragment newInstance(){
        BlankFragment fragment = new BlankFragment();
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_blank;
    }
}
