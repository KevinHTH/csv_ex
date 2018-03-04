package sg.vinova.noticeboard.base;

/**
 * Created by Ray on 3/10/17.
 */

public interface BaseListener {
    interface OnItemClickListener<T>{
        void listener(T t);
    }

    interface OnMultiClickListener<T>{
        void groupClick(T t);
        void send(String s, T t);
    }

    interface OnClickListener<T, V>{
        void listener(T t, V v);
    }
}
