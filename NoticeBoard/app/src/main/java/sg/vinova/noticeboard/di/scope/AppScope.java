package sg.vinova.noticeboard.di.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by ben
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface AppScope {
}
