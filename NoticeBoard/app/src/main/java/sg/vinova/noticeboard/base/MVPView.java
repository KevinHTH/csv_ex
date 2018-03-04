package sg.vinova.noticeboard.base;

import vn.eazy.architect.mvp.base.BasePresenter;

/**
 * Created by cuong on 4/27/17.
 */

public interface MVPView extends BasePresenter.View {
    void onError(String message);

}

