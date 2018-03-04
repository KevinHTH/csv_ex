package sg.vinova.noticeboard.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import sg.vinova.noticeboard.R;


/**
 * Created by Vinova on 13/4/17.
 */

public class AppCheckbox extends AppCompatImageView implements Checkable, View.OnClickListener {
    private int check_off_drawable ;
    private int check_on_drawable ;

    private boolean isChecked;
    private boolean isRadioButton;
    private OnCheckChangedEvent onCheckChangedEvent;

    @Override
    public void onClick(View v) {
        toggle();
    }

    public interface OnCheckChangedEvent {
        void onCheckChanged(AppCheckbox gkCheckbox, boolean isChecked);
    }

    public AppCheckbox(Context context) {
        super(context);
        init();
    }

    public AppCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        invalidate();
        setChecked(isChecked);
        setOnClickListener(this);
    }


    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
        if (checked) {
            setImageResource(check_on_drawable);
        } else {
            setImageResource(check_off_drawable);
        }
        if (onCheckChangedEvent != null) {
            onCheckChangedEvent.onCheckChanged(this, isChecked);
        }
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        if(isRadioButton){
            if(!isChecked()){
                setChecked(!isChecked);
            }
        }else{
            setChecked(!isChecked);
        }

    }

    public void setOnCheckChangedEvent(OnCheckChangedEvent onCheckChangedEvent) {
        this.onCheckChangedEvent = onCheckChangedEvent;
    }

    public void setRadioButton(boolean radioButton) {
        isRadioButton = radioButton;
    }
}
