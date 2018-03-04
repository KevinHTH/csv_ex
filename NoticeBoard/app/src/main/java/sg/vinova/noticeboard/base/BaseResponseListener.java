package sg.vinova.noticeboard.base;

/**
 * Created by cuong on 4/27/17.
 */

public interface BaseResponseListener<D> {
    void onSuccess(D data);

    void onError(String message);
}
