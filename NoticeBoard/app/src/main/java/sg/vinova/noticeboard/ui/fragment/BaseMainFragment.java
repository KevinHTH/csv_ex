package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.ui.activity.FirstScreenActivity;
import sg.vinova.noticeboard.ui.activity.MainActivity;
import sg.vinova.noticeboard.widgets.AppTextView;
import vn.eazy.core.base.OnBaseActionListener;
import vn.eazy.core.base.activity.BaseMainActivity;
import vn.eazy.core.toolbar.ToolbarHelper;

public abstract class BaseMainFragment extends BaseAppFragment implements OnBaseActionListener {
    private int miliSecondDelayShowContent;
    private FrameLayout container;
    private AppTextView tvMessage;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getBaseAppActivity();
    }

    public FirstScreenActivity getFirstScreenActivity() {
        return (FirstScreenActivity) getBaseAppActivity();
    }

    public BaseMainFragment() {
    }

    public void setTitleToolbar(String msg) {
        this.getBaseActivity().setTitleToolbar(msg);
    }

    public void setTitleMainColor(int color) {
        this.getBaseActivity().setTitleMainColor(color);
    }

    public void showBackButton(boolean isShow) {
        this.getBaseActivity().showBackButton(isShow);
    }

    public void setTitleToolbar(String msg, String font) {
        this.getBaseActivity().setTitleToolbar(msg, font);
    }

    public void showMenu(boolean isShow) {
        this.getBaseActivity().showMenu(isShow);
    }

    public boolean isShowMenu() {
        return true;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.showMenu(this.isShowMenu());
    }

    public ToolbarHelper getToolbarHelper() {
        return this.getBaseActivity() instanceof BaseMainActivity ? ((BaseMainActivity) this.getBaseActivity()).toolbarHelper : null;
    }

    public void setLayoutProgressStatus(View view, int miliSecondDelay) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        miliSecondDelayShowContent = miliSecondDelay;
        View statusLayout = LayoutInflater.from(getContext()).inflate(R.layout.layout_progress_status, null);
        container = (FrameLayout) statusLayout.findViewById(R.id.container);
        tvMessage = (AppTextView) statusLayout.findViewById(R.id.tvMessage);
        //tvMessage.setOnClickListener(view1 -> retryAPI());
        progressBar = (ProgressBar) statusLayout.findViewById(R.id.progress);
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) layoutParams;
            LinearLayout linearLayout = ((LinearLayout) view.getParent());

            linearLayout.removeView(view);
            FrameLayout.LayoutParams frameLp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(frameLp);

            linearLayout.addView(statusLayout);
            statusLayout.setLayoutParams(lp);
            container.addView(view);
            container.setVisibility(View.GONE);


        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) layoutParams;
            RelativeLayout relativeLayout = ((RelativeLayout) view.getParent());

            relativeLayout.removeView(view);
            FrameLayout.LayoutParams frameLp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(frameLp);

            relativeLayout.addView(statusLayout);
            statusLayout.setLayoutParams(lp);
            container.addView(view);
            container.setVisibility(View.GONE);

        } else {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) layoutParams;
            FrameLayout frameLayout = ((FrameLayout) view.getParent());

            frameLayout.removeView(view);
            FrameLayout.LayoutParams frameLp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(frameLp);

            frameLayout.addView(statusLayout);
            statusLayout.setLayoutParams(lp);
            container.addView(view);
            container.setVisibility(View.GONE);
        }
    }


    public void showPageMessageForView(String message) {
        new Handler().postDelayed(() -> {
            if (progressBar == null) {
                //   ToastUtil.showSort(message);
                return;
            }
            progressBar.setVisibility(View.GONE);
            container.setVisibility(View.GONE);
            if (message != null) {
                tvMessage.setText(message);
                tvMessage.setVisibility(View.VISIBLE);
            }
        }, miliSecondDelayShowContent);

    }



    public void showProgressForView(boolean isShow) {
        new Handler().postDelayed(() -> {
            if (progressBar == null)
                return;
            if (isShow) {
                progressBar.setVisibility(View.VISIBLE);
                tvMessage.setVisibility(View.GONE);
                container.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.GONE);
                tvMessage.setVisibility(View.GONE);
                container.setVisibility(View.VISIBLE);
            }
        }, miliSecondDelayShowContent);

    }

    public void setLayoutProgressStatus(View view) {
        setLayoutProgressStatus(view, 100);
    }
}
