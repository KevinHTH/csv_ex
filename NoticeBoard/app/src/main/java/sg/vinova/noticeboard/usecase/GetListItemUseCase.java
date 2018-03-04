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

public class GetListItemUseCase extends BaseAppUseCase implements StringParamRequestUseCase<BaseListObjectResponse<Description>> {
    public GetListItemUseCase(Context context) {
        super(context);
    }


    @Override
    public Flowable<BaseListObjectResponse<Description>> request(String... strings) {
       return getMyApi().getListItem(strings[0],strings[1],strings[2]);
    }
}
