package sg.vinova.noticeboard.module.home;

import java.util.List;

import sg.vinova.noticeboard.base.MVPView;
import sg.vinova.noticeboard.model.Category;

/**
 * Created by Vinova on 28/4/17.
 */

public interface HomePresenter {
    interface Presenter{
        void getAllCategory();
    }

    interface View extends MVPView{
        void onGetAllCategorySuccess(List<Category> categories);
    }
}
