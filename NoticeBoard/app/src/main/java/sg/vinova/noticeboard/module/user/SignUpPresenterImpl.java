package sg.vinova.noticeboard.module.user;

import android.content.Context;

import sg.vinova.noticeboard.base.BaseAppPresenter;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.BaseResponseListener;
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.usecase.SignUpUseCase;
import sg.vinova.noticeboard.utils.LoaderUtils;

/**
 * Created by cuong on 4/27/17.
 */

public class SignUpPresenterImpl extends BaseAppPresenter<SignUpPresenter.View> implements SignUpPresenter.Presenter {

    SignUpUseCase signUpUseCase;

    public SignUpPresenterImpl(Context context) {
        super(context);
        signUpUseCase = new SignUpUseCase(context);
    }


    @Override
    public void signUp(String email, String password, String passwordConfirmation, String username, String phoneNumber, String deviceToken, String platform ) {
        LoaderUtils.show(context);
        requestAPI(signUpUseCase.request(email, password, passwordConfirmation, username, phoneNumber, deviceToken,platform),
                new BaseResponseListener<BaseObjectResponse<LoginResponse>>() {
            @Override
            public void onSuccess(BaseObjectResponse<LoginResponse> data) {
                LoaderUtils.hide();
                getMVPView().onSignUpSuccess(data.getData());
            }

            @Override
            public void onError(String message) {
                LoaderUtils.hide();
                getMVPView().onError(message);
            }
        });
    }
}
