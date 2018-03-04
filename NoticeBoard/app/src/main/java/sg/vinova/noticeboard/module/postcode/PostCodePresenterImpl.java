package sg.vinova.noticeboard.module.postcode;

import android.content.Context;

import sg.vinova.noticeboard.base.BaseAppPresenter;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.BaseResponseListener;
import sg.vinova.noticeboard.model.ClusterResponse;
import sg.vinova.noticeboard.usecase.PostCodeUseCase;
import sg.vinova.noticeboard.utils.LoaderUtils;

/**
 * Created by cuong on 4/27/17.
 */

public class PostCodePresenterImpl extends BaseAppPresenter<PostCodePresenter.View> implements PostCodePresenter.Presenter {

    PostCodeUseCase postCodeUseCase;

    public PostCodePresenterImpl(Context context) {
        super(context);
        postCodeUseCase = new PostCodeUseCase(context);
    }


    @Override
    public void postCode(String code, String pass_code) {
        LoaderUtils.show(context);
        requestAPI(postCodeUseCase.request(code, pass_code), new BaseResponseListener<BaseObjectResponse<ClusterResponse>>() {
            @Override
            public void onSuccess(BaseObjectResponse<ClusterResponse> data) {
                LoaderUtils.hide();
                getMVPView().onPostcodeSuccess(data.getData());
            }

            @Override
            public void onError(String message) {
                LoaderUtils.hide();
                getMVPView().onError(message);

            }
        });

    }
}
