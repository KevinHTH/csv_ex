package sg.vinova.noticeboard.module.user;

import sg.vinova.noticeboard.base.MVPView;
import sg.vinova.noticeboard.model.LoginResponse;

/**
 * Created by cuong on 4/27/17.
 */

public interface ProfilePresenter {
    interface Presenter{
       void getUser();
        void updateProfile(String username, String phoneNumber);
    }

    interface View extends MVPView {
        void onGetUserSuccess(LoginResponse loginResponse);
        void onUpdateProfileSuccess(LoginResponse loginResponse);
    }
}
