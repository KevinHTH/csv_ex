package sg.vinova.noticeboard.usecase;

import android.content.Context;

import io.reactivex.Flowable;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseListObjectResponse;
import sg.vinova.noticeboard.model.Category;
import vn.eazy.architect.mvp.usecase.action.NonParamRequestUseCase;

/**
 * Created by cuong on 4/27/17.
 */

public class CategoryUseCase extends BaseAppUseCase implements NonParamRequestUseCase<BaseListObjectResponse<Category>> {
    public CategoryUseCase(Context context) {
        super(context);
    }


    @Override
    public Flowable<BaseListObjectResponse<Category>> request() {
        return getMyApi().getListCategory();
    }
}
