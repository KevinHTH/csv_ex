package sg.vinova.noticeboard.usecase;

import android.content.Context;

import io.reactivex.Flowable;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseListObjectResponse;
import sg.vinova.noticeboard.model.BlockedUsers;
import vn.eazy.architect.mvp.usecase.BaseUseCase;
import vn.eazy.architect.mvp.usecase.action.StringParamRequestUseCase;

/**
 * Created by Ray on 7/17/17.
 */

public class BlockedUsersUseCase extends BaseAppUseCase implements StringParamRequestUseCase<BaseListObjectResponse<BlockedUsers>> {

    public BlockedUsersUseCase(Context context) {
        super(context);
    }

    @Override
    public Flowable<BaseListObjectResponse<BlockedUsers>> request(String... strings) {
        return getMyApi().getBlockedUsers(strings[0], strings[1]);
    }
}
