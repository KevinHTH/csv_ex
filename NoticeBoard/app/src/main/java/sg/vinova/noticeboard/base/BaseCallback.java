package sg.vinova.noticeboard.base;

/**
 * Created by Ray on 3/6/17.
 */

public interface BaseCallback {

    interface EmptyValueCallback{
        void callback();
    }

    interface Callback<T>{
        void callback(T t);
    }
}
