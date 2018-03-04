package sg.vinova.noticeboard.usecase.admin;

import android.content.Context;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.factory.ConstantApp;
import sg.vinova.noticeboard.model.LoginResponse;
import vn.eazy.architect.mvp.usecase.action.GenericsIORequestUseCase;

/**
 * Created by cuong on 4/27/17.
 */

public class PostEstateUseCase extends BaseAppUseCase implements
        GenericsIORequestUseCase<RequestBody,BaseObjectResponse>{



    public PostEstateUseCase(Context context) {
        super(context);
    }


    @Override
    public Flowable<BaseObjectResponse> request(RequestBody body) {
        return getMyApi().postEstateSaleRent(body);
    }

}
