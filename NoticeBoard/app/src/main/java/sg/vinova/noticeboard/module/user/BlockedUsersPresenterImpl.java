package sg.vinova.noticeboard.module.user;

import android.content.Context;

import javax.inject.Inject;

import sg.vinova.noticeboard.base.BaseAppPresenter;
import sg.vinova.noticeboard.base.BaseListObjectResponse;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.BaseResponseListener;
import sg.vinova.noticeboard.model.BlockedUsers;
import sg.vinova.noticeboard.model.MessageResponse;
import sg.vinova.noticeboard.usecase.BlockedUsersUseCase;
import sg.vinova.noticeboard.usecase.UnBlockUserUseCase;
import vn.eazy.architect.mvp.base.BasePresenter;

/**
 * Created by Ray on 7/17/17.
 */

public class BlockedUsersPresenterImpl extends BaseAppPresenter<BlockedUserPresenter.View>
        implements BlockedUserPresenter.Presenter {
    BlockedUsersUseCase blockedUsersUseCase;
    UnBlockUserUseCase unBlockUserUseCase;

    public BlockedUsersPresenterImpl(Context context) {
        super(context);
        blockedUsersUseCase = new BlockedUsersUseCase(context);
        unBlockUserUseCase = new UnBlockUserUseCase(context);
    }

    @Override
    public void getBlockedUsersList(int page, int per_page) {
        requestAPI(blockedUsersUseCase.request(page + "", per_page + ""),
                new BaseResponseListener<BaseListObjectResponse<BlockedUsers>>() {
                    @Override
                    public void onSuccess(BaseListObjectResponse<BlockedUsers> data) {
                        if (!isAttached())
                            return;
                        getMVPView().onSuccess(data.getData());
                    }

                    @Override
                    public void onError(String message) {
                        getMVPView().onError(message);
                    }
                });
    }

    @Override
    public void getMoreBlockedUsersList(int page, int per_page) {
        requestAPI(blockedUsersUseCase.request(page + "", per_page + ""),
                new BaseResponseListener<BaseListObjectResponse<BlockedUsers>>() {
                    @Override
                    public void onSuccess(BaseListObjectResponse<BlockedUsers> data) {
                        if (!isAttached())
                            return;
                        getMVPView().getMoreBlockedUserSuccess(data.getData());
                    }

                    @Override
                    public void onError(String message) {
                        getMVPView().onError(message);
                    }
                });
    }

    @Override
    public void unlock(String blockId) {
        requestAPI(unBlockUserUseCase.request(blockId), new BaseResponseListener<BaseObjectResponse<MessageResponse>>() {
            @Override
            public void onSuccess(BaseObjectResponse<MessageResponse> data) {
                if (!isAttached())
                    return;
                getMVPView().unlockSuccess(data.getData().getMessage());
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
