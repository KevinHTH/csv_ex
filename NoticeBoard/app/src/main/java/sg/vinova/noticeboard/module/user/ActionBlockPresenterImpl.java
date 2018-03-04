package sg.vinova.noticeboard.module.user;

import android.content.Context;

import sg.vinova.noticeboard.base.BaseAppPresenter;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.BaseResponseListener;
import sg.vinova.noticeboard.model.BlockedUsers;
import sg.vinova.noticeboard.usecase.ActionBlockUseCase;

/**
 * Created by Ray on 7/17/17.
 */

public class ActionBlockPresenterImpl extends BaseAppPresenter<ActionBlockPresenter.View>
        implements ActionBlockPresenter.Presenter {

    private ActionBlockUseCase actionBlockUseCase;

    public ActionBlockPresenterImpl(Context context) {
        super(context);
        actionBlockUseCase = new ActionBlockUseCase(context);
    }

    @Override
    public void blockUser(String blockId) {
        requestAPI(actionBlockUseCase.request(blockId), new BaseResponseListener<BaseObjectResponse<BlockedUsers>>() {
            @Override
            public void onSuccess(BaseObjectResponse<BlockedUsers> data) {
                if (!isAttached())
                    return;
                getMVPView().blockSuccess(data.getData());
            }

            @Override
            public void onError(String message) {
                if (!isAttached())
                    return;
                getMVPView().onError(message);
            }
        });
    }
}
