package sg.vinova.noticeboard.base;

import android.content.Context;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import sg.vinova.noticeboard.study.ApiErrorListener;
import sg.vinova.noticeboard.study.BaseUserResponseListener;
import vn.eazy.architect.mvp.base.BasePresenter;

/**
 * Created by cuong on 4/27/17.
 */

public class BaseAppPresenter<V extends MVPView> extends BasePresenter {
    public BaseAppPresenter(Context context) {
        super(context);
    }


    public V getMVPView() {
        return (V) getView();
    }

    @Override
    public void bind(View view) {
        super.bind((V)view);
    }

    public <D> void requestAPI(Flowable<D> flowable, BaseResponseListener<D> responseListenter) {
       if(isAttached()){
           compositeDisposable.add(flowable.subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(new Consumer<D>() {
                       @Override
                       public void accept(D data) throws Exception {
                           if (isAttached()) {
                               responseListenter.onSuccess(data);

                           }

                       }
                   }, new ErrorListener() {
                       @Override
                       public void onMessage(String message) {
                           if (isAttached()) {
                               responseListenter.onError(message);
                           }

                       }
                   }));
       }
    }

    public <D> void requestUserApi(Flowable<D> flowable, BaseUserResponseListener<D> responseListener){
        if (isAttached()){
            compositeDisposable.add(flowable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<D>() {
                           @Override
                           public void accept(@NonNull D d) throws Exception {
                               if (isAttached()) {
                                   responseListener.onSuccess(d);
                               }
                           }
                       }
               /*     ,new ErrorListener() {
                @Override
                public void onMessage(String message) {
                    if (isAttached()){
                        responseListener.onNetworkError(message);
                    }
                }}*/
                    , new ApiErrorListener() {
                        @Override
                        public void onApiErrorMessage(String messageDetail){
                            responseListener.onNetworkError(messageDetail);
                        }
                    }

            ));
        }
    }

}
