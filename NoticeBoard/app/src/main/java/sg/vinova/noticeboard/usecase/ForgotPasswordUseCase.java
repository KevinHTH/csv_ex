package sg.vinova.noticeboard.usecase;

import android.content.Context;

import io.reactivex.Flowable;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.MessagesApi;
import sg.vinova.noticeboard.model.MessageResponse;
import vn.eazy.architect.mvp.usecase.action.StringParamRequestUseCase;

/**
 * Created by cuong on 4/27/17.
 */

public class ForgotPasswordUseCase extends BaseAppUseCase implements StringParamRequestUseCase<BaseObjectResponse<MessageResponse>> {
    public ForgotPasswordUseCase(Context context) {
        super(context);
    }


    @Override
    public Flowable<BaseObjectResponse<MessageResponse>> request(String... strings) {
        return getMyApi().forgotPassword(strings[0]);
    }
}
