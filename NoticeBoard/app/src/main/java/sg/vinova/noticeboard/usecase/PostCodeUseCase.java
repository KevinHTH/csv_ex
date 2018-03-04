package sg.vinova.noticeboard.usecase;

import android.content.Context;

import io.reactivex.Flowable;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.model.ClusterResponse;
import vn.eazy.architect.mvp.usecase.action.StringParamRequestUseCase;

/**
 * Created by cuong on 4/27/17.
 */

public class PostCodeUseCase extends BaseAppUseCase implements StringParamRequestUseCase<BaseObjectResponse<ClusterResponse>> {
    public PostCodeUseCase(Context context) {
        super(context);
    }

    @Override
    public Flowable<BaseObjectResponse<ClusterResponse>> request(String... strings) {
        return getMyApi().postcode(strings[0], strings[1]);
    }
}
