package sg.vinova.noticeboard.module.user;

import android.content.Context;

import sg.vinova.noticeboard.base.BaseAppPresenter;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.BaseResponseListener;
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.usecase.LoginUseCase;
import sg.vinova.noticeboard.utils.LoaderUtils;

/**
 * Created by cuong on 4/27/17.
 */

public class LoginPresenterImpl extends BaseAppPresenter<LoginPresenter.View> implements LoginPresenter.Presenter {

    LoginUseCase loginUseCase;

    public LoginPresenterImpl(Context context) {
        super(context);
        loginUseCase = new LoginUseCase(context);
    }

    @Override
    public void login(String email, String password, String deviceToken, String platform) {
        LoaderUtils.show(context);
        requestAPI(loginUseCase.request(email, password, deviceToken, platform), new BaseResponseListener<BaseObjectResponse<LoginResponse>>() {
            @Override
            public void onSuccess(BaseObjectResponse<LoginResponse> data) {
                LoaderUtils.hide();
                getMVPView().onLoginSuccess(data.getData());
            }

            @Override
            public void onError(String message) {
                LoaderUtils.hide();
                getMVPView().onError(message);
            }
        });
    }
}
