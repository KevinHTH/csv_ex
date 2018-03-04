package sg.vinova.noticeboard.usecase;

import android.content.Context;

import io.reactivex.Flowable;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.model.MessageResponse;
import vn.eazy.architect.mvp.usecase.action.StringParamRequestUseCase;

/**
 * Created by Ray on 7/17/17.
 */

public class ReportTackUseCase extends BaseAppUseCase implements StringParamRequestUseCase<BaseObjectResponse<MessageResponse>> {

    public ReportTackUseCase(Context context) {
        super(context);
    }

    @Override
    public Flowable<BaseObjectResponse<MessageResponse>> request(String... strings) {
        return getMyApi().report(strings[0], strings[1], strings[2]);
    }
}
