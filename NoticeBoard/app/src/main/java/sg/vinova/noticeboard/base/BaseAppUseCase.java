package sg.vinova.noticeboard.base;

import android.content.Context;

import javax.inject.Inject;

import sg.vinova.noticeboard.MyApplication;
import sg.vinova.noticeboard.network.MyApi;
import sg.vinova.noticeboard.ui.activity.BaseAppActivity;
import vn.eazy.architect.mvp.usecase.BaseUseCase;

/**
 * Created by cuong on 4/27/17.
 */


public class BaseAppUseCase extends BaseUseCase {
    @Inject
    MyApi myApi;

    public BaseAppUseCase(Context context) {
        super(context);
        MyApplication.getInstance().getAppComponent().inject(this);
    }

    public MyApi getMyApi() {
        return myApi;
    }
    public BaseAppActivity getBaseAppActivity(){
        return (BaseAppActivity) context;
    }
}
