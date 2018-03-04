package sg.vinova.noticeboard.ui.fragment;

import sg.vinova.noticeboard.ui.activity.BaseAppActivity;
import vn.eazy.core.base.fragment.BaseMainFragment;

/**
 * Created by cuong on 4/25/17.
 */

public class BaseAppFragment extends BaseMainFragment {
    @Override
    public int getLayoutId() {
        return 0;
    }

    public BaseAppActivity getBaseAppActivity(){
        return (BaseAppActivity) getActivity();
    }
}
