package sg.vinova.noticeboard.module.user;

import java.util.List;

import sg.vinova.noticeboard.base.MVPView;
import sg.vinova.noticeboard.model.BlockedUsers;

/**
 * Created by Ray on 7/17/17.
 */

public interface BlockedUserPresenter {
    interface View extends MVPView {

        void onSuccess(List<BlockedUsers> list);
        void getMoreBlockedUserSuccess(List<BlockedUsers> list);

        void unlockSuccess(String msg);

        @Override
        void onError(String message);
    }

    interface Presenter {
        void getBlockedUsersList(int page, int per_page);
        void getMoreBlockedUsersList(int page, int per_page);

        void unlock(String blockId);
    }
}
