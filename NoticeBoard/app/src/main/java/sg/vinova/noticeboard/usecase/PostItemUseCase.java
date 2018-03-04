package sg.vinova.noticeboard.usecase;

import android.content.Context;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseListObjectResponse;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.model.LoginResponse;
import vn.eazy.architect.mvp.usecase.action.GenericsIORequestUseCase;
import vn.eazy.architect.mvp.usecase.action.StringParamRequestUseCase;

/**
 * Created by cuong on 4/27/17.
 */

public class PostItemUseCase extends BaseAppUseCase implements GenericsIORequestUseCase<RequestBody,BaseObjectResponse<LoginResponse>> {
    public PostItemUseCase(Context context) {
        super(context);
    }

    @Override
    public Flowable<BaseObjectResponse<LoginResponse>> request(RequestBody requestBody) {
        return getMyApi().postItem(requestBody);
    }
}
