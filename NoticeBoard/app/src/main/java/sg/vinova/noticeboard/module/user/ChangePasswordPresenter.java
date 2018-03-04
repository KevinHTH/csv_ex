package sg.vinova.noticeboard.module.user;

import sg.vinova.noticeboard.base.MVPView;

/**
 * Created by cuong on 4/27/17.
 */

public interface ChangePasswordPresenter {



    interface Presenter{
        void changePassword(String email, String newPass, String confirmPass);
    }

    interface View extends MVPView {
        void onChangePasswordSuccess(String message);
    }
}
