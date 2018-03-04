package sg.vinova.noticeboard.di.component;

import dagger.Component;
import sg.vinova.noticeboard.base.BaseAppUseCase;
import sg.vinova.noticeboard.di.module.AppModule;
import sg.vinova.noticeboard.di.module.CommonModule;
import sg.vinova.noticeboard.di.scope.AppScope;
import sg.vinova.noticeboard.ui.activity.BaseAppActivity;


@AppScope
@Component(modules = {AppModule.class, CommonModule.class})
public interface AppComponent {


    void inject(BaseAppActivity baseAppActivity);

    void inject(BaseAppUseCase baseAppUseCase);
}
