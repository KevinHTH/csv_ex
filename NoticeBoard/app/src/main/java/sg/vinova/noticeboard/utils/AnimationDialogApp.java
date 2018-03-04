package sg.vinova.noticeboard.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.widgets.AppTextView;
import vn.eazy.core.base.dialog.BaseDialog;

/**
 * Created by cuong on 3/8/17.
 */

public class AnimationDialogApp extends BaseDialog {


    @BindView(R.id.tvMessage)
    AppTextView tvTitleDialog;
    @BindView(R.id.tvLeft)
    AppTextView tvLeft;
    @BindView(R.id.tvRight)
    AppTextView tvRight;


    private View.OnClickListener yesListener;
    private View.OnClickListener noListener;
    private String yesString = "Yes";
    private String noString = "No";
    private String title;
    private String message;

    private boolean isLeftCaps = false;
    private boolean isRightCaps = false;


    public AnimationDialogApp(Context context) {
        super(context);
    }


//    public AnimationDialogApp(Context context, int startAnim, int endAnim) {
//        super(context, startAnim, endAnim);
//    }
//
//    public AnimationDialogApp(Context context, int themeResId, int startAnim, int endAnim) {
//        super(context, R.style.AppTheme, startAnim, endAnim);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvTitleDialog.setText(title);
        tvTitleDialog.setTextColor(ContextCompat.getColor(getContext(),R.color.black));

        tvLeft.setText(noString);
        tvLeft.setTextColor(ContextCompat.getColor(getContext(),R.color.gray_dark));

        tvRight.setText(yesString);
        if (isLeftCaps){
            tvLeft.setAllCaps(true);
        }
        if (isRightCaps){
            tvRight.setAllCaps(true);
        }

        if (TextUtils.isEmpty(noString)) {
            tvLeft.setVisibility(View.GONE);
        }


    }
    public void setLeftCaps(boolean leftCaps) {
        isLeftCaps = leftCaps;
    }

    public void setRightCaps(boolean rightCaps) {
        isRightCaps = rightCaps;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public void setYesListener(View.OnClickListener yesListener) {
        this.yesListener = yesListener;
    }

    public void setNoListener(View.OnClickListener noListener) {
        this.noListener = noListener;
    }


    @OnClick(R.id.tvRight)
    void onClickYes() {
        dismiss();
        if (yesListener != null) {
            yesListener.onClick(tvRight);
        }

    }

    @OnClick(R.id.tvLeft)
    void onClickCancel() {
        dismiss();
        if (noListener != null) {
            noListener.onClick(tvLeft);
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_custom_dialog;
    }

    public void setYesString(String yesString) {
        this.yesString = yesString;
    }

    public void setNoString(String noString) {
        this.noString = noString;
    }
}
