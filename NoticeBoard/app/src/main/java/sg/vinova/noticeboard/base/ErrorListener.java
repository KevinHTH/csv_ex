package sg.vinova.noticeboard.base;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.functions.Consumer;
import sg.vinova.noticeboard.MyApplication;
import sg.vinova.noticeboard.ui.activity.EnterMyPostcodeActivity;
import sg.vinova.noticeboard.ui.activity.FirstScreenActivity;
import sg.vinova.noticeboard.utils.ApiUtils;
import sg.vinova.noticeboard.utils.CacheUtils;
import timber.log.Timber;
import vn.eazy.core.utils.PreferencesUtils;

/**
 * Created by Ray on 3/6/17.
 */

public abstract class ErrorListener implements Consumer<Throwable> {
    @Override
    public void accept(Throwable e) throws Exception {
        String message = "No internet connect";
        retrofit2.Response response = null;

        try {

            if (e instanceof SocketTimeoutException) {
                onMessage("Timeout to request!");
                return;
            }
            if (e instanceof ConnectException || e instanceof NetworkErrorException || e instanceof UnknownHostException) {
                message = "Network connection onError!";
                onMessage(message);
                return;
            }

            response = ((HttpException) e).response();

            if (response != null && response.errorBody() != null) {

                if (response.code() == 401) {
                    try {
                        Context context = MyApplication.getInstance().getApplicationContext();
                        CacheUtils.clearLoggedInData();
                        CacheUtils.setAuthToken(MyApplication.getInstance(), "");
                        PreferencesUtils.clearAll(context);
                        Toast.makeText(context, "The session expired!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(context, FirstScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                else if (response.code() == 460){
                    Context context = MyApplication.getInstance().getApplicationContext();
                    CacheUtils.clearLoggedInData();
                    CacheUtils.setAuthToken(MyApplication.getInstance(), "");
                    PreferencesUtils.clearAll(context);
                    Toast.makeText(context, "The session expired!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(context, FirstScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);

                }

                String m = ApiUtils.getErrorMessage(response.errorBody().string());
                if (!TextUtils.isEmpty(m)) {
                    message = m;
                }
                if (m == null)
                    message = response.code() + " Error. Please try again.";
                onMessage(message);
            } else {
                onMessage(e.getLocalizedMessage());
                ApiUtils.setToken("");
            }

        } catch (Exception exp) {
            Timber.e(exp.getMessage());
            onMessage(e.getLocalizedMessage());

        }
    }

    public abstract void onMessage(String message);
}
