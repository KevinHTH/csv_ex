package sg.vinova.noticeboard;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;

import io.fabric.sdk.android.Fabric;
import sg.vinova.noticeboard.di.component.AppComponent;
import sg.vinova.noticeboard.di.component.DaggerAppComponent;
import sg.vinova.noticeboard.di.module.AppModule;
import sg.vinova.noticeboard.di.module.CommonModule;

/**
 * Created by cuong on 4/27/17.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.IS_INSTALL_FABRIC) {
            Fabric.with(this, new Crashlytics());
        }
        if (BuildConfig.IS_CHECK_LEAK) {
            LeakCanary.install(this);
        }

        instance = this;
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule())
                .commonModule(new CommonModule())
                .build();
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
