package sg.vinova.noticeboard.widgets;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import sg.vinova.noticeboard.R;


public class MenuPopup extends RelativePopupWindow {


    @BindView(R.id.tvEditMenu)
    AppTextView tvEditMenu;
    @BindView(R.id.tvDeleteMenu)
    AppTextView tvDeleteMenu;
    private OnClickPopupMenuListener onClickPopupMenuListener;

    public MenuPopup(Context context, View v, boolean hideEditMenu) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_menu_popup, null);
        context.getResources().getDisplayMetrics();
        setContentView(view);
        ButterKnife.bind(this, view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (hideEditMenu){
            tvEditMenu.setVisibility(View.GONE);
        }else {
            tvEditMenu.setVisibility(View.VISIBLE);
        }
    }

    public MenuPopup hideEditMenu(boolean isHide){
        if (isHide){
            tvEditMenu.setVisibility(View.GONE);
        }else {
            tvEditMenu.setVisibility(View.VISIBLE);
        }
        return this;
    }

    @OnClick(R.id.tvDeleteMenu)
    void onClickDelete(){
        if(onClickPopupMenuListener!=null){
            dismiss();
            onClickPopupMenuListener.onClickDelete();
        }
    }

    @OnClick(R.id.tvEditMenu)
    void onClickEdit(){
        if(onClickPopupMenuListener!=null){
            dismiss();
            onClickPopupMenuListener.onClickEdit();
        }
    }

    @Override
    public void showOnAnchor(@NonNull View anchor, int vertPos, int horizPos, int x, int y) {
        super.showOnAnchor(anchor, vertPos, horizPos, x, y);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circularReveal(anchor);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void circularReveal(@NonNull final View anchor) {
        final View contentView = getContentView();
        contentView.post(new Runnable() {
            @Override
            public void run() {
                final int[] myLocation = new int[2];
                final int[] anchorLocation = new int[2];
                contentView.getLocationOnScreen(myLocation);
                anchor.getLocationOnScreen(anchorLocation);
                final int cx = anchorLocation[0] - myLocation[0] + anchor.getWidth() / 2;
                final int cy = anchorLocation[1] - myLocation[1] + anchor.getHeight() / 2;

                contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                final int dx = Math.max(cx, contentView.getMeasuredWidth() - cx);
                final int dy = Math.max(cy, contentView.getMeasuredHeight() - cy);
                final float finalRadius = (float) Math.hypot(dx, dy);
                Animator animator = ViewAnimationUtils.createCircularReveal(contentView, cx, cy, 0f, finalRadius);
                animator.setDuration(300);
                animator.start();
            }
        });
    }

    public interface OnClickPopupMenuListener {
        void onClickEdit();
        void onClickDelete();
    }

    public void setOnClickPopupMenuListener(OnClickPopupMenuListener onClickPopupMenuListener) {
        this.onClickPopupMenuListener = onClickPopupMenuListener;
    }

}