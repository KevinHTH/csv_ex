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

/**
 * Created by cuong on 4/27/17.
 */

public class EditTransactionUseCase extends BaseAppUseCase implements
        GenericsIORequestUseCase<Map<Integer, Object>, BaseObjectResponse<MessagesApi>> {

    private String type;

    public EditTransactionUseCase(Context context, String type) {
        super(context);
        this.type = type;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Flowable<BaseObjectResponse<MessagesApi>> request(Map<Integer, Object> map) {
        if (getType().equals(ConstantApp.TYPESUBMIT.RENT)){
            return getMyApi().updateTransactionRent(map.get(0).toString(), (RequestBody) map.get(1));
        }else {
            return getMyApi().updateTransactionSale(map.get(0).toString(), (RequestBody) map.get(1));
        }
    }
}
