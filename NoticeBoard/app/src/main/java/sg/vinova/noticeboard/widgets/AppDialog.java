package sg.vinova.noticeboard.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Checkable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.factory.ConstantApp;

/**
 * Created by Jacky on 6/1/17.
 */

public class AppDialog extends Dialog implements Checkable, View.OnClickListener {

    @BindView(R.id.tvMessage)
    AppTextView tvTitleDialog;

    @BindView(R.id.tvLeftButton)
    AppTextView tvLeftButton;

    @BindView(R.id.tvRightButton)
    AppTextView tvRightButton;

    @BindView(R.id.checkbox)
    CheckBox checkBox;

    @BindView(R.id.tvMainText)
    AppTextView tvMainText;

    @BindView(R.id.tvSubText1)
    AppTextView tvSubText1;

    @BindView(R.id.tvSubText2)
    AppTextView tvSubText2;


    private int colorTextCheckbox;
    private int colorMessage;
    private int dialogBackground;
    private View view;
    private OnClickAppDialog onClickAppDialog;
    private boolean isChecked;

    public AppDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AppDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        init(context);
    }


    protected AppDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = LayoutInflater.from(context).inflate(R.layout.layout_app_dialog, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);

    }

    public AppDialog setTextInCheckbox(String title, String part1, String part2) {
        if (tvMainText != null && title != null) {
            tvMainText.setText(title);
        }
        if (tvSubText1 != null && tvSubText2 != null && part1 != null && part2 != null) {
            tvSubText1.setText(Html.fromHtml(part1 + "<b><font color=#FF0000> <a href=\""
                    +ConstantApp.TERM_URL+"\"> Terms of Use</a></b>."));
            tvSubText1.setMovementMethod(LinkMovementMethod.getInstance());
            tvSubText2.setText(part2);
        }
        return this;
    }

    public AppDialog setTitle(String title) {
        if (tvTitleDialog != null && title != null && !title.equals(""))
            tvTitleDialog.setText(title);
        return this;
    }

    public AppDialog setTextLeftButton(String title) {
        if (tvLeftButton != null && title != null && !title.equals(""))
            tvLeftButton.setText(title);
        return this;
    }

    public AppDialog setTextRightButton(String title) {
        if (tvRightButton != null && title != null && !title.equals(""))
            tvRightButton.setText(title);
        return this;
    }

    @OnClick(R.id.tvLeftButton)
    void onClickLeft() {
        if (onClickAppDialog != null) {
            dismiss();
            onClickAppDialog.onClickLeftButton();
        }
    }

    @OnClick(R.id.tvRightButton)
    void onClickRight() {
        if (onClickAppDialog != null) {
            dismiss();
            onClickAppDialog.onClickRightButton(checkBox.isChecked());
        }
    }


    @OnClick(R.id.checkbox)
    void onCheckedChanged() {
        if (checkBox.isChecked()) {
            isChecked = true;
        } else {
            isChecked = false;
        }
    }

    public void setOnClickAppDialog(OnClickAppDialog onClickAppDialog) {
        this.onClickAppDialog = onClickAppDialog;
    }


    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
        if (checked) {
            tvRightButton.setClickable(true);
        } else {
            tvLeftButton.setClickable(false);
        }

    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        if (checkBox.isChecked()) {
            setChecked(!isChecked);
        } else {
            setChecked(isChecked);
        }
    }

    @Override
    public void onClick(View v) {
        toggle();
    }

    public interface OnClickAppDialog {

        void onClickLeftButton();

        void onClickRightButton(boolean isChecked);
    }


}
