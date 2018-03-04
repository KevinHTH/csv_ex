package sg.vinova.noticeboard.usecase;

import android.content.Context;

import io.reactivex.Flowable;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseListObjectResponse;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.model.BlockedUsers;
import sg.vinova.noticeboard.model.MessageResponse;
import vn.eazy.architect.mvp.usecase.action.StringParamRequestUseCase;

/**
 * Created by Ray on 7/17/17.
 */

public class UnBlockUserUseCase extends BaseAppUseCase implements StringParamRequestUseCase<BaseObjectResponse<MessageResponse>> {

    public UnBlockUserUseCase(Context context) {
        super(context);
    }

    @Override
    public Flowable<BaseObjectResponse<MessageResponse>> request(String... strings) {
        return getMyApi().unlockUser(strings[0]);
    }
}
