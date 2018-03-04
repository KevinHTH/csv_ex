package sg.vinova.noticeboard.di.module;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sg.vinova.noticeboard.BuildConfig;
import sg.vinova.noticeboard.MyApplication;
import sg.vinova.noticeboard.di.scope.AppScope;
import sg.vinova.noticeboard.network.MyApi;
import sg.vinova.noticeboard.utils.CacheUtils;

/**
 * Created by ben on
 */
@Module
@AppScope
public class AppModule {

    private Request request;
    private String app_device;

    public static boolean isNeedAuthToken() {
        return isNeedAuthToken;
    }

    public static void setIsNeedAuthToken(boolean isNeedAuthToken) {
        AppModule.isNeedAuthToken = isNeedAuthToken;
    }

    static boolean isNeedAuthToken;

    public AppModule() {
    }

    @Provides
    @AppScope
    public MyApi provideApi(OkHttpClient okHttpClient) {
        synchronized (MyApi.class) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            return retrofit.create(MyApi.class);
        }
    }


    @Provides
    @AppScope
    protected OkHttpClient provideOkHttp(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggingInterceptor.Builder()
                        .loggable(BuildConfig.DEBUG)
                        .setLevel(Level.BASIC)
                        .log(Log.INFO)
                        .request("Request")
                        .response("Response")
                        .addHeader("version", BuildConfig.VERSION_NAME)
                        .build())
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        request = chain.request();
                        app_device = "android";

                        if (isNeedAuthToken()) {
                            if (!TextUtils.isEmpty(CacheUtils.getAuthToken(context))) {
                                Request newRequest = request.newBuilder()
                                        .addHeader("Http-Auth-Token", CacheUtils.getAuthToken(context))
                                        .addHeader("App-Version", BuildConfig.VERSION_NAME)
                                        .addHeader("App-Device", app_device)
                                        .build();


                                return  chain.proceed(newRequest);
                            }
                        } else {
                            if (!TextUtils.isEmpty(CacheUtils.getClusterToken(context))) {
                                Request newRequest = request.newBuilder()
                                        .addHeader("Http-Auth-Cluster-Token", CacheUtils.getClusterToken(context))
                                        .addHeader("App-Version", BuildConfig.VERSION_NAME)
                                        .addHeader("App-Device", app_device)
                                        .build();
                                return  chain.proceed(newRequest);
                            }
                        }

                       /* if (!TextUtils.isEmpty(CacheUtils.getAuthToken(context))) {
                            Request newRequest = request.newBuilder()
                                    .addHeader("Http-Auth-Token", CacheUtils.getAuthToken(context))
                                    .addHeader("App-Version",BuildConfig.VERSION_NAME)
                                    .addHeader("App-Device",app_device)
                                    .build();


                            return chain.proceed(newRequest);
                        }
                        if (!TextUtils.isEmpty(CacheUtils.getClusterToken(context))) {
                            Request newRequest = request.newBuilder()
                                    .addHeader("Http-Auth-Cluster-Token", CacheUtils.getClusterToken(context))
                                    .addHeader("App-Version",BuildConfig.VERSION_NAME)
                                    .addHeader("App-Device",app_device)
                                    .build();
                            return chain.proceed(newRequest);
                        }*/
                        return chain.proceed(request);
                    }
                })
                .cache(new Cache(new File("cache"), 1024L * 1024L * 100L))
                .connectTimeout(2, TimeUnit.MINUTES)
                .build();
        return okHttpClient;
    }

    @Provides
    Context provideContext() {
        return MyApplication.getInstance();
    }

}
