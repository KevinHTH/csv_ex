package sg.vinova.noticeboard.usecase;

import android.content.Context;

import io.reactivex.Flowable;
import okhttp3.RequestBody;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.base.MessagesApi;
import vn.eazy.architect.mvp.usecase.action.GenericsIORequestUseCase;

/**
 * Created by cuong on 4/27/17.
 */

public class SendFeedbackUseCase extends BaseAppUseCase implements GenericsIORequestUseCase<RequestBody,BaseObjectResponse<MessagesApi>> {
    public SendFeedbackUseCase(Context context) {
        super(context);
    }


    @Override
    public Flowable<BaseObjectResponse<MessagesApi>> request(RequestBody requestBody) {
        return getMyApi().sendFeedback(requestBody);
    }
}
