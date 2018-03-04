package sg.vinova.noticeboard.usecase.admin;

import android.content.Context;

import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.MessagesApi;
import sg.vinova.noticeboard.factory.ConstantApp;
import vn.eazy.architect.mvp.usecase.action.GenericsIORequestUseCase;
import vn.eazy.architect.mvp.usecase.action.StringParamRequestUseCase;

/**
 * Created by Jacky on 4/27/17.
 */

public class EditEstateUseCase extends BaseAppUseCase implements GenericsIORequestUseCase<Map<Integer, Object>, BaseObjectResponse<MessagesApi>> {


    public EditEstateUseCase(Context context) {
        super(context);
    }


    @Override
    public Flowable<BaseObjectResponse<MessagesApi>> request(Map<Integer, Object> map) {
        return getMyApi().updateEstateProperty(map.get(0).toString(), (RequestBody) map.get(1));

    }
}
