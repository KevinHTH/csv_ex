package sg.vinova.noticeboard.module.admin;

import java.util.List;

import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.MVPView;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.model.Photo;

/**
 * Created by Vinova on 28/4/17.
 */

public interface TransactionPresenter {

    interface PostPresenter{
        void postTransaction(String type, Description description);
    }

    interface TransactionView extends MVPView{
        void onPostTransactionSuccess(BaseObjectResponse baseObjectResponse);
        void onEditTransactionSuccess(String message);
    }

    interface EditPresenter{
        void editTransaction(String type, Description description);
    }

}
