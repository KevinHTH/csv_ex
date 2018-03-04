package sg.vinova.noticeboard.usecase;

import android.content.Context;

import io.reactivex.Flowable;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.MessagesApi;
import sg.vinova.noticeboard.model.LoginResponse;
import vn.eazy.architect.mvp.usecase.action.StringParamRequestUseCase;

/**
 * Created by cuong on 4/27/17.
 */

public class PhoneConfirmationUseCase extends BaseAppUseCase implements StringParamRequestUseCase<BaseObjectResponse<LoginResponse>> {
    public PhoneConfirmationUseCase(Context context) {
        super(context);
    }


    @Override
    public Flowable<BaseObjectResponse<LoginResponse>> request(String... strings) {
        return getMyApi().phoneConfirmation(strings[0]);
    }
}
