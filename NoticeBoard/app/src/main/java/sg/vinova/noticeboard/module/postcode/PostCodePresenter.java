package sg.vinova.noticeboard.module.postcode;

import sg.vinova.noticeboard.base.MVPView;
import sg.vinova.noticeboard.model.ClusterResponse;

/**
 * Created by cuong on 4/27/17.
 */

public interface PostCodePresenter {
    interface Presenter{
        void postCode(String code, String pass_code);
    }

    interface View extends MVPView {
        void onPostcodeSuccess(ClusterResponse clusterResponse);
    }
}
