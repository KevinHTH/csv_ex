package sg.vinova.noticeboard.module.user;

import android.content.Context;

import sg.vinova.noticeboard.base.BaseAppPresenter;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.BaseResponseListener;
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.usecase.LoginUseCase;
import sg.vinova.noticeboard.usecase.ProfileUseCase;
import sg.vinova.noticeboard.usecase.UpdateProfileUseCase;
import sg.vinova.noticeboard.utils.LoaderUtils;

/**
 * Created by cuong on 4/27/17.
 */

public class ProfilePresenterImpl extends BaseAppPresenter<ProfilePresenter.View> implements ProfilePresenter.Presenter {

    ProfileUseCase profileUseCase;
    UpdateProfileUseCase updateProfileUseCase;

    public ProfilePresenterImpl(Context context) {
        super(context);
        profileUseCase = new ProfileUseCase(context);
        updateProfileUseCase = new UpdateProfileUseCase(context);
    }


    @Override
    public void getUser() {
        requestAPI(profileUseCase.request(), new BaseResponseListener<BaseObjectResponse<LoginResponse>>() {
            @Override
            public void onSuccess(BaseObjectResponse<LoginResponse> data) {
                getMVPView().onGetUserSuccess(data.getData());
            }

            @Override
            public void onError(String message) {
                getMVPView().onError(message);
            }
        });
    }

    @Override
    public void updateProfile(String username, String phoneNumber) {
        LoaderUtils.show(context);
        requestAPI(updateProfileUseCase.request(username, phoneNumber), new BaseResponseListener<BaseObjectResponse<LoginResponse>>() {
            @Override
            public void onSuccess(BaseObjectResponse<LoginResponse> data) {
                LoaderUtils.hide();
                getMVPView().onUpdateProfileSuccess(data.getData());
            }

            @Override
            public void onError(String message) {
                LoaderUtils.hide();
                getMVPView().onError(message);
            }
        });
    }
}
