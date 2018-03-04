package sg.vinova.noticeboard.module.user;

import sg.vinova.noticeboard.base.MVPView;
import sg.vinova.noticeboard.model.LoginResponse;

/**
 * Created by cuong on 4/27/17.
 */

public interface PhoneVerifyPresenter {
    interface Presenter {
        void verifyPhone(String code);
        void resendCode();
    }


    interface View extends MVPView {
        void onPhoneConfirmSuccess(LoginResponse loginResponse);
        void onResendCodeSuccess(String msg);
    }
}
