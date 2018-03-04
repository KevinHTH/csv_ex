package sg.vinova.noticeboard.module.user;

import android.content.Context;

import sg.vinova.noticeboard.base.BaseAppPresenter;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.BaseResponseListener;
import sg.vinova.noticeboard.base.MessagesApi;
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.usecase.PhoneConfirmationUseCase;
import sg.vinova.noticeboard.usecase.SignUpUseCase;
import sg.vinova.noticeboard.usecase.admin.ResendCodeUseCase;
import sg.vinova.noticeboard.utils.LoaderUtils;

/**
 * Created by cuong on 4/27/17.
 */

public class PhoneVerifyPresenterImpl extends BaseAppPresenter<PhoneVerifyPresenter.View>
        implements PhoneVerifyPresenter.Presenter {

    PhoneConfirmationUseCase phoneConfirmationUseCase;
    ResendCodeUseCase resendCodeUseCase;

    public PhoneVerifyPresenterImpl(Context context) {
        super(context);
       phoneConfirmationUseCase = new PhoneConfirmationUseCase(context);
        resendCodeUseCase = new ResendCodeUseCase(context);
    }


    @Override
    public void verifyPhone(String code) {
        LoaderUtils.show(context);
        requestAPI(phoneConfirmationUseCase.request(code), new BaseResponseListener<BaseObjectResponse<LoginResponse>>() {
            @Override
            public void onSuccess(BaseObjectResponse<LoginResponse> data) {
                LoaderUtils.hide();
                getMVPView().onPhoneConfirmSuccess(data.getData());
            }

            @Override
            public void onError(String message) {
                LoaderUtils.hide();
                getMVPView().onError(message);
            }
        });
    }

    @Override
    public void resendCode() {

        requestAPI(resendCodeUseCase.request(), new BaseResponseListener<BaseObjectResponse<MessagesApi>>() {
            @Override
            public void onSuccess(BaseObjectResponse<MessagesApi> data) {

                getMVPView().onResendCodeSuccess(data.getData().getMessage());
            }

            @Override
            public void onError(String message) {

                getMVPView().onError(message);
            }
        });
    }
}
