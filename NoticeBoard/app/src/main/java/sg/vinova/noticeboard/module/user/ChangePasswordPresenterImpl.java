package sg.vinova.noticeboard.module.user;

import android.content.Context;

import sg.vinova.noticeboard.base.BaseAppPresenter;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.BaseResponseListener;
import sg.vinova.noticeboard.base.MessagesApi;
import sg.vinova.noticeboard.usecase.ChangePasswordUseCase;
import sg.vinova.noticeboard.utils.LoaderUtils;

/**
 * Created by cuong on 4/27/17.
 */

public class ChangePasswordPresenterImpl extends BaseAppPresenter<ChangePasswordPresenter.View> implements ChangePasswordPresenter.Presenter {

    ChangePasswordUseCase changePasswordUseCase;
    public ChangePasswordPresenterImpl(Context context) {
        super(context);
        changePasswordUseCase = new ChangePasswordUseCase(context);
    }

    @Override
    public void changePassword(String oldPass, String newPass, String confirmPass) {

        LoaderUtils.show(context);
        requestAPI(changePasswordUseCase.request(oldPass, confirmPass, confirmPass), new BaseResponseListener<BaseObjectResponse<MessagesApi>>() {
            @Override
            public void onSuccess(BaseObjectResponse<MessagesApi> data) {
                LoaderUtils.hide();
                getMVPView().onChangePasswordSuccess(data.getData().getMessage());
            }

            @Override
            public void onError(String message) {
                LoaderUtils.hide();
                getMVPView().onError(message);
            }
        });
    }
}
