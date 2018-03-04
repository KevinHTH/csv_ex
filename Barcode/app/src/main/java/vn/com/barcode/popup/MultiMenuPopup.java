package vn.com.barcode.popup;

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
import android.widget.TextView;

import vn.com.barcode.R;


public class MultiMenuPopup extends RelativePopupWindow {


    TextView tvEditMenu;
    TextView tvDeleteMenu;
    TextView tvBlockMenu;
    TextView tvReportMenu;
    View view3;
    View view4;

    private OnClickPopupMenuListener onClickPopupMenuListener;

    public MultiMenuPopup(Context context, View v, boolean hideEditMenu, boolean isRole) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_multi_menu_popup, null);
        tvEditMenu = view.findViewById(R.id.tvEditMenu);
        tvDeleteMenu = view.findViewById(R.id.tvDeleteMenu);
        tvBlockMenu = view.findViewById(R.id.tvBlockMenu);
        tvReportMenu = view.findViewById(R.id.tvReportMenu);
        view3 = view.findViewById(R.id.view3);
        view4 = view.findViewById(R.id.view4);

        context.getResources().getDisplayMetrics();
        setContentView(view);

        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (hideEditMenu) {
            tvEditMenu.setVisibility(View.GONE);
        } else {
            tvEditMenu.setVisibility(View.VISIBLE);
        }

        /**
         * If user have permission is admin, will show 4 items.
         * For user have member permission, will show 2 items is block author and report
         */
        if (isRole) {
            tvEditMenu.setVisibility(View.VISIBLE);
            tvDeleteMenu.setVisibility(View.VISIBLE);
            view3.setVisibility(View.VISIBLE);
            view4.setVisibility(View.VISIBLE);
        } else {
            tvEditMenu.setVisibility(View.GONE);
            tvDeleteMenu.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);
            view4.setVisibility(View.GONE);
        }

        onClickDelete();
        onClickEdit();
        onBlock();
        onReport();
    }

    public MultiMenuPopup hideEditMenu(boolean isHide) {
        if (isHide) {
            tvEditMenu.setVisibility(View.GONE);
        } else {
            tvEditMenu.setVisibility(View.VISIBLE);
        }
        return this;
    }


    void onClickDelete() {
        if (onClickPopupMenuListener != null) {
            dismiss();
            onClickPopupMenuListener.onClickDelete();
        }
    }


    void onClickEdit() {
        if (onClickPopupMenuListener != null) {
            dismiss();
            onClickPopupMenuListener.onClickEdit();
        }
    }


    void onBlock() {
        if (onClickPopupMenuListener != null) {
            dismiss();
            onClickPopupMenuListener.blockAuthor();
        }
    }


    void onReport() {
        if (onClickPopupMenuListener != null) {
            dismiss();
            onClickPopupMenuListener.report();
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

        void blockAuthor();

        void report();
    }

    public void setOnClickPopupMenuListener(OnClickPopupMenuListener onClickPopupMenuListener) {
        this.onClickPopupMenuListener = onClickPopupMenuListener;
    }

}