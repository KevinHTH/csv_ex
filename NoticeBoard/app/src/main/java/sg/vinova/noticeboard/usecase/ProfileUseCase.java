package sg.vinova.noticeboard.usecase;

import android.content.Context;

import io.reactivex.Flowable;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseListObjectResponse;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.model.LoginResponse;
import vn.eazy.architect.mvp.usecase.action.NonParamRequestUseCase;

/**
 * Created by cuong on 4/27/17.
 */

public class ProfileUseCase extends BaseAppUseCase implements NonParamRequestUseCase<BaseObjectResponse<LoginResponse>> {
    public ProfileUseCase(Context context) {
        super(context);
    }


    @Override
    public Flowable<BaseObjectResponse<LoginResponse>> request() {
        return getMyApi().getUser();
    }
}
