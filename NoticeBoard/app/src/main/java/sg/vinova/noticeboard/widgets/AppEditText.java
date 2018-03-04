package sg.vinova.noticeboard.widgets;

import android.content.Context;
import android.util.AttributeSet;

import vn.eazy.core.widget.EazyEditText;


/**
 * Created by cuong on 2/23/17.
 */

public class AppEditText extends EazyEditText {
    public AppEditText(Context context) {
        super(context);
    }

    public AppEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppEditText(Context context, AttributeSet attrs, int defStyleAttr) {
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
