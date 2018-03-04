package sg.vinova.noticeboard.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.base.BaseListener;


/**
 * Created by Ray on 3/7/17.
 */

public class CustomGenderView extends LinearLayout {

    @BindView(R.id.cbx_male)
    RadioButton cbx_1;
    @BindView(R.id.cbx_female)
    RadioButton cbx_2;
    @BindView(R.id.cbx_other)
    RadioButton cbx_3;

    private BaseListener.OnItemClickListener<Integer> listener;

    public CustomGenderView(Context context) {
        super(context);
        init();
    }

    public CustomGenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomGenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CustomGenderView setListener(BaseListener.OnItemClickListener<Integer> listener) {
        this.listener = listener;
        return this;
    }

    public CustomGenderView setTwoView() {
        if (cbx_3 != null) {
            cbx_3.setVisibility(GONE);
        }
        return this;
    }

    public CustomGenderView setViewFirstText(String s, boolean isAllCaps) {
        if (cbx_1 != null && s != null && !s.equals("")) {
            cbx_1.setText(s);
            cbx_1.setAllCaps(isAllCaps);
        }
        return this;
    }

    public CustomGenderView setViewSecondText(String s, boolean isAllCaps) {
        if (cbx_2 != null && s != null && !s.equals("")) {
            cbx_2.setText(s);
            cbx_2.setAllCaps(isAllCaps);
        }
        return this;
    }

    public CustomGenderView setViewThirdText(String s) {
        if (cbx_3 != null && s != null && !s.equals(""))
            cbx_3.setText(s);
        return this;
    }

    public CustomGenderView setSelection(int i) {
        if (cbx_1 != null && cbx_2 != null && cbx_3 != null)
            switch (i) {
                case 0:
                    cbx_1.setChecked(true);
                    break;
                case 1:
                    cbx_2.setChecked(true);
                    break;
                case 2:
                    cbx_3.setChecked(true);
                    break;
            }
        return this;
    }

    private void init() {
        View view = inflate(getContext(), R.layout.layout_custom_toggle, this);
        ButterKnife.bind(this, view);

        if (cbx_1 != null) {
            cbx_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (cbx_2 != null && cbx_3 != null && isChecked) {
                        if (listener != null)
                            listener.listener(0);
                        cbx_2.setChecked(false);
                        cbx_3.setChecked(false);
                        return;
                    }
                }
            });
        }
        if (cbx_2 != null) {
            cbx_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (cbx_1 != null && cbx_3 != null && isChecked) {
                        if (listener != null)
                            listener.listener(1);
                        cbx_1.setChecked(false);
                        cbx_3.setChecked(false);
                        return;
                    }
                }
            });
        }
        if (cbx_3 != null) {
            cbx_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (cbx_1 != null && cbx_2 != null && isChecked) {
                        cbx_1.setChecked(false);
                        cbx_2.setChecked(false);
                        return;
                    }
                }
            });
        }
    }

    public String getText() {
        if (cbx_1 != null && cbx_2 != null && cbx_3 != null) {
            return (cbx_1.isChecked() ? cbx_1.getText().toString().toLowerCase() :
                    cbx_2.isChecked() ? cbx_2.getText().toString().toLowerCase() :
                            cbx_3.isChecked() ? cbx_3.getText().toString().toLowerCase() : "");
        }
        return "";
    }
}