package sg.vinova.noticeboard.usecase.admin;

import android.content.Context;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.factory.ConstantApp;
import vn.eazy.architect.mvp.usecase.action.GenericsIORequestUseCase;

/**
 * Created by Jacky on 4/27/17.
 */

public class PostTransactionUseCase extends BaseAppUseCase implements
        GenericsIORequestUseCase<RequestBody,BaseObjectResponse>{



    private String type;

    public PostTransactionUseCase(Context context, String type) {
        super(context);
        this.type = type;
    }


    @Override
    public Flowable<BaseObjectResponse> request(RequestBody body) {
        if (getType().equals(ConstantApp.TYPESUBMIT.RENT)){
            return getMyApi().postTransactionRent(body);
        }else {
            return getMyApi().postTransactionSale(body);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
