package sg.vinova.noticeboard.module.user;

import sg.vinova.noticeboard.base.MVPView;
import sg.vinova.noticeboard.model.LoginResponse;

/**
 * Created by cuong on 4/27/17.
 */

public interface SignUpPresenter {
    interface Presenter{
        void signUp(String email, String password, String passwordConfirmation, String username, String phoneNumber, String deviceToken, String platform);
    }

    interface View extends MVPView {
        void onSignUpSuccess(LoginResponse loginResponse);
    }
}
