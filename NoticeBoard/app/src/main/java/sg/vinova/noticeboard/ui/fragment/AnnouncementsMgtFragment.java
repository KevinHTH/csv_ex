package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.widgets.AppTextView;
import vn.eazy.core.utils.PreferencesUtils;

/**
 * Created by Jacky on 4/26/17.
 */

public class AnnouncementsMgtFragment extends BaseMainFragment {

    Category category;
    @BindView(R.id.tvMainText)
    AppTextView tvMainText;
    @BindView(R.id.tvPhone)
    AppTextView tvPhone;
    @BindView(R.id.tvEmail)
    AppTextView tvEmail;
    Unbinder unbinder;


    public static AnnouncementsMgtFragment newInstance(Category category) {
        AnnouncementsMgtFragment fragment = new AnnouncementsMgtFragment();
        fragment.category = category;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        setupActionbar();
        getMainActivity().hideFooter(false);
        getMainActivity().setFooterText(PreferencesUtils.getString(getContext(), Constant.FOOTER_TEXT));
        unbinder = ButterKnife.bind(this, viewRoot);
        init();
        return viewRoot;
    }

    private void init(){
        if (!isAdded())
            return;
        tvMainText.setText("Coming your way,\nupon request by EstateMgt");
    }

    private void setupActionbar() {
        try {
            setTitleToolbar(category.getName());
            getBaseActivity().getToolbarHelper().showToolbar(true);
            getBaseActivity().getToolbarHelper().setImageForLeftButton(R.mipmap.back);
            getBaseActivity().getToolbarHelper().setRightToolbarButton(null, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_annoucements_estate_mgt;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
