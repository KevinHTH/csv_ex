package sg.vinova.noticeboard.usecase;

import android.content.Context;

import io.reactivex.Flowable;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.model.LoginResponse;
import vn.eazy.architect.mvp.usecase.action.StringParamRequestUseCase;

/**
 * Created by cuong on 4/27/17.
 */

public class SignUpUseCase extends BaseAppUseCase implements StringParamRequestUseCase<BaseObjectResponse<LoginResponse>> {
    public SignUpUseCase(Context context) {
        super(context);
    }


    @Override
    public Flowable<BaseObjectResponse<LoginResponse>> request(String... strings) {
        return  getMyApi().signUp(strings[0], strings[1],strings[2],strings[3],strings[4],strings[5],strings[6]);
    }
}
