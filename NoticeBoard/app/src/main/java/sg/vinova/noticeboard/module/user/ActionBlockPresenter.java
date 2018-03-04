package sg.vinova.noticeboard.module.user;

import sg.vinova.noticeboard.base.MVPView;
import sg.vinova.noticeboard.model.BlockedUsers;

/**
 * Created by Ray on 7/17/17.
 */

public interface ActionBlockPresenter {

    interface View extends MVPView{
        void blockSuccess(BlockedUsers user);
    }

    interface Presenter{
        void blockUser(String blockId);
    }
}
