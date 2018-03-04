package sg.vinova.noticeboard.module.user;

import android.content.Context;

import sg.vinova.noticeboard.base.BaseAppPresenter;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.BaseResponseListener;
import sg.vinova.noticeboard.model.MessageResponse;
import sg.vinova.noticeboard.usecase.ForgotPasswordUseCase;

/**
 * Created by cuong on 4/27/17.
 */

public class ForgotPasswordPresenterImpl extends BaseAppPresenter<ForgotPasswordPresenter.View> implements ForgotPasswordPresenter.Presenter {

    ForgotPasswordUseCase forgotPasswordUseCase;

    public ForgotPasswordPresenterImpl(Context context) {
        super(context);
        forgotPasswordUseCase = new ForgotPasswordUseCase(context);
    }


    @Override
    public void forgotPassword(String email) {
        requestAPI(forgotPasswordUseCase.request(email), new BaseResponseListener<BaseObjectResponse<MessageResponse>>() {
            @Override
            public void onSuccess(BaseObjectResponse<MessageResponse> data) {
                getMVPView().onForgotPasswordSuccess(data.getData().getMessage());
            }

            @Override
            public void onError(String message) {
                getMVPView().onError(message);
            }
        });
    }
}
