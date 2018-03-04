package sg.vinova.noticeboard.module.user;

import sg.vinova.noticeboard.base.MVPView;

/**
 * Created by cuong on 4/27/17.
 */

public interface ForgotPasswordPresenter {
    interface Presenter{
        void forgotPassword(String email);
    }

    interface View extends MVPView {
        void onForgotPasswordSuccess(String message);
    }
}
