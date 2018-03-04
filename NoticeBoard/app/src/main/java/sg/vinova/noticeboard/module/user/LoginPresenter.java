package sg.vinova.noticeboard.module.user;

import sg.vinova.noticeboard.base.MVPView;
import sg.vinova.noticeboard.model.LoginResponse;

/**
 * Created by cuong on 4/27/17.
 */

public interface LoginPresenter  {
    interface Presenter{
        void login(String email,String password, String deviceToken,String platform);
    }

    interface View extends MVPView {
        void onLoginSuccess(LoginResponse loginResponse);
    }
}
