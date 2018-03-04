package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.widgets.AppButton;

/**
 * Created by Jacky on 4/26/17.
 */

public class FirstScreenFragment extends BaseMainFragment {
    @BindView(R.id.btnEnterMyCode)
    AppButton btnEnterMyCode;

    Unbinder unbinder;

    public static FirstScreenFragment newInstance() {
        FirstScreenFragment fragment = new FirstScreenFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFirstScreenActivity().hideFooter(true);
        getFirstScreenActivity().hideToolbar(true);

    }

    @OnClick(R.id.btnEnterMyCode)
    public void onClickEnterMyCode() {
        getFirstScreenActivity().changeFragment(VerifyCodeFragment.newInstance(), true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_enter_my_code;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
