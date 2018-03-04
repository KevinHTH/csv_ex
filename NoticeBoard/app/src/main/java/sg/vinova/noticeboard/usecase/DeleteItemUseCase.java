package sg.vinova.noticeboard.usecase;

import android.content.Context;
import io.reactivex.Flowable;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.MessagesApi;

import vn.eazy.architect.mvp.usecase.action.StringParamRequestUseCase;

/**
 * Created by Ray on 3/15/17.
 */

public class DeleteItemUseCase extends BaseAppUseCase implements StringParamRequestUseCase<BaseObjectResponse<MessagesApi>> {


    public DeleteItemUseCase(Context context) {
        super(context);
    }


    @Override
    public Flowable<BaseObjectResponse<MessagesApi>> request(String... strings) {
        return getMyApi().deleteItem(strings[0], strings[1]);
    }
}
