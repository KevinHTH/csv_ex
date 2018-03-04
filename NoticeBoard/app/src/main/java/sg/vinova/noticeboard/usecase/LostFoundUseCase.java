package sg.vinova.noticeboard.usecase;

import android.content.Context;

import io.reactivex.Flowable;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseListObjectResponse;
import sg.vinova.noticeboard.model.Description;
import vn.eazy.architect.mvp.usecase.action.StringParamRequestUseCase;

/**
 * Created by cuong on 4/27/17.
 */

public class LostFoundUseCase extends BaseAppUseCase implements StringParamRequestUseCase<BaseListObjectResponse<Description>> {
    public LostFoundUseCase(Context context) {
        super(context);
    }


    @Override
    public Flowable<BaseListObjectResponse<Description>> request(String... strings) {
       return getMyApi().getLostFound(strings[0],strings[1],strings[2],strings[3]);
    }
}
