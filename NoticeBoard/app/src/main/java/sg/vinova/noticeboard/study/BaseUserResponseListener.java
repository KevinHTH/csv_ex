package sg.vinova.noticeboard.study;

/**
 * Created by cuong on 4/27/17.
 */

public interface BaseUserResponseListener<D> {
    void onSuccess(D data);

    void onApiError(UserResponseErrorObject responseErrorObject);

    void onNetworkError(String message);
}
