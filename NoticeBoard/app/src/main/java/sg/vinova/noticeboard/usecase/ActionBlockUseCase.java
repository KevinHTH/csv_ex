package sg.vinova.noticeboard.usecase;

import android.content.Context;

import io.reactivex.Flowable;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseListObjectResponse;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.model.BlockedUsers;
import vn.eazy.architect.mvp.usecase.action.StringParamRequestUseCase;

/**
 * Created by Ray on 7/17/17.
 */

public class ActionBlockUseCase extends BaseAppUseCase implements StringParamRequestUseCase<BaseObjectResponse<BlockedUsers>> {

    public ActionBlockUseCase(Context context) {
        super(context);
    }

    @Override
    public Flowable<BaseObjectResponse<BlockedUsers>> request(String... strings) {
        return getMyApi().blockUser(strings[0]);
    }
}
