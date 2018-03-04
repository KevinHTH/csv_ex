package sg.vinova.noticeboard.module.home;

import android.content.Context;

import sg.vinova.noticeboard.base.BaseAppPresenter;
import sg.vinova.noticeboard.base.BaseListObjectResponse;
import sg.vinova.noticeboard.base.BaseResponseListener;
import sg.vinova.noticeboard.model.Category;

import sg.vinova.noticeboard.usecase.CategoryUseCase;
import sg.vinova.noticeboard.utils.Log;

/**
 * Created by Vinova on 28/4/17.
 */

public class HomePresenterImpl extends BaseAppPresenter<HomePresenter.View>
implements HomePresenter.Presenter {


    CategoryUseCase categoryUseCase;
    public HomePresenterImpl(Context context) {
        super(context);
        categoryUseCase = new CategoryUseCase(context);
    }

    @Override
    public void getAllCategory() {
        Log.d("Jacky","get category");
        requestAPI(categoryUseCase.request(), new BaseResponseListener<BaseListObjectResponse<Category>>() {
            @Override
            public void onSuccess(BaseListObjectResponse<Category> data) {
                getMVPView().onGetAllCategorySuccess(data.getData());
            }

            @Override
            public void onError(String message) {
                getMVPView().onError(message);
            }
        });
    }
}
