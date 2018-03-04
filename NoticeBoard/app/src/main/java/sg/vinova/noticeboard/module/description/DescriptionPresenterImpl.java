package sg.vinova.noticeboard.module.description;

import android.content.Context;

import sg.vinova.noticeboard.base.BaseAppPresenter;
import sg.vinova.noticeboard.base.BaseListObjectResponse;
import sg.vinova.noticeboard.base.BaseResponseListener;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.study.BaseUserResponseListener;
import sg.vinova.noticeboard.study.UserResponseErrorObject;
import sg.vinova.noticeboard.usecase.GetListItemUseCase;
import sg.vinova.noticeboard.usecase.LostFoundUseCase;


/**
 * Created by Vinova on 28/4/17.
 */

public class DescriptionPresenterImpl extends BaseAppPresenter<DescriptionPresenter.View>
implements DescriptionPresenter.Presenter {

    GetListItemUseCase getListItemUseCase;
    LostFoundUseCase lostFoundUseCase;

    public DescriptionPresenterImpl(Context context) {
        super(context);
        getListItemUseCase = new GetListItemUseCase(context);
        lostFoundUseCase = new LostFoundUseCase(context);
    }


    @Override
    public void getItemsById(String categoryId, String page, String pagePer) {
        requestAPI(getListItemUseCase.request(categoryId, page, pagePer), new BaseResponseListener<BaseListObjectResponse<Description>>() {
            @Override
            public void onSuccess(BaseListObjectResponse<Description> data) {
                getMVPView().onGetItemsSuccess(data.getData());
            }

            @Override
            public void onError(String message) {
                getMVPView().onError(message);
            }
        });
    }

    @Override
    public void getListItem(String categoryId, String page, String pagePer, String type) {
        requestAPI(lostFoundUseCase.request(categoryId, page, pagePer, type), new BaseResponseListener<BaseListObjectResponse<Description>>() {
            @Override
            public void onSuccess(BaseListObjectResponse<Description> data) {
                getMVPView().onGetItemsSuccess(data.getData());
            }

            @Override
            public void onError(String message) {
                getMVPView().onError(message);
            }
        });
        requestUserApi(lostFoundUseCase.request("", "", ""), new BaseUserResponseListener<BaseListObjectResponse<Description>>() {
            @Override
            public void onSuccess(BaseListObjectResponse<Description> data) {

            }

            @Override
            public void onApiError(UserResponseErrorObject responseErrorObject) {

            }

            @Override
            public void onNetworkError(String message) {

            }
        });

    }




}
