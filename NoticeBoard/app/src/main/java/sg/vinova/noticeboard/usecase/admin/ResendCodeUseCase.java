package sg.vinova.noticeboard.usecase.admin;

import android.content.Context;

import io.reactivex.Flowable;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.MessagesApi;
import vn.eazy.architect.mvp.usecase.action.NonParamRequestUseCase;
import vn.eazy.architect.mvp.usecase.action.StringParamRequestUseCase;

/**
 * Created by cuong on 4/27/17.
 */

public class ResendCodeUseCase extends BaseAppUseCase implements
        NonParamRequestUseCase<BaseObjectResponse<MessagesApi>> {
    public ResendCodeUseCase(Context context) {
        super(context);
    }


    @Override
    public Flowable<BaseObjectResponse<MessagesApi>> request() {
        return getMyApi().resendCode();
    }
}
