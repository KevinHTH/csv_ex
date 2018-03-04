package sg.vinova.noticeboard.module.report;

import sg.vinova.noticeboard.base.MVPView;

/**
 * Created by Ray on 7/17/17.
 */

public interface ReportTackPresenter {
    interface View extends MVPView {
        void reportSuccess(String msg);
    }

    interface Presenter {
        void report(String cateId, String itemId, String des);
    }
}
