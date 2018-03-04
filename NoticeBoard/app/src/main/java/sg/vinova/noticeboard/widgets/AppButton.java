package sg.vinova.noticeboard.widgets;

import android.content.Context;
import android.util.AttributeSet;

import vn.eazy.core.widget.EazyButton;

/**
 * Created by cuong on 2/23/17.
 */

public class AppButton extends EazyButton {
    public AppButton(Context context) {
        super(context);
    }

    public AppButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected String createLightFont() {
        return "fonts/calibril.ttf";
    }

    @Override
    protected String createRegularFont() {
        return "fonts/calibri.ttf";
    }

    @Override
    protected String createMediumFont() {
        return "fonts/calibrib.ttf";
    }
}
