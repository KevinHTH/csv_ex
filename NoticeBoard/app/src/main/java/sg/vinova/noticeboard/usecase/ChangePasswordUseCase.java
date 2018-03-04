package sg.vinova.noticeboard.usecase;

import android.content.Context;

import io.reactivex.Flowable;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.MessagesApi;
import vn.eazy.architect.mvp.usecase.action.StringParamRequestUseCase;

/**
 * Created by cuong on 4/27/17.
 */

public class ChangePasswordUseCase extends BaseAppUseCase implements StringParamRequestUseCase<BaseObjectResponse<MessagesApi>> {
    public ChangePasswordUseCase(Context context) {
        super(context);
    }


    @Override
    public Flowable<BaseObjectResponse<MessagesApi>> request(String... strings) {
        return getMyApi().changePassword(strings[0],strings[1],strings[2]);
    }
}
