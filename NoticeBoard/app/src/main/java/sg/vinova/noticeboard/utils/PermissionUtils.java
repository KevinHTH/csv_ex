package sg.vinova.noticeboard.utils;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import vn.eazy.core.base.activity.BaseActivity;

/**
 * Created by Ray on 3/14/17.
 */

public class PermissionUtils {

    private static PermissionUtils instance;

    public static PermissionUtils getInstance(){
        if (instance == null)
            instance = new PermissionUtils();
        return instance;
    }

    public Observable<Boolean> requestPermission(BaseActivity activity, String... permission){
        RxPermissions rx = new RxPermissions(activity);
        return rx.request(permission);
    }
}
