package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.adapter.TabGiveWantedAdapter;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.widgets.AppButton;

/**
 * Created by Jacky on 4/26/17.
 */

public class TabGiveWantedFragment extends BaseMainFragment {


    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;

    @BindView(R.id.vpGiveWanted)
    ViewPager vpGiveWanted;

    @BindView(R.id.btnTakeNewItem)
    AppButton btnTakeNewItem;

    @BindView(R.id.layoutTabGiveWanted)
    RelativeLayout layoutTabGiveWanted;

    Unbinder unbinder;

    private boolean needRefesh = false;
    private String typeMode;
    private Category category;
    private TabGiveWantedAdapter tabGiveWantedAdapter;
    private int tabPosition;


    public static TabGiveWantedFragment newInstance(Category category) {
        TabGiveWantedFragment fragment = new TabGiveWantedFragment();
        fragment.category = category;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabGiveWantedAdapter = new TabGiveWantedAdapter(getChildFragmentManager(), category, typeMode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vpGiveWanted.setAdapter(tabGiveWantedAdapter);
        viewpagertab.setViewPager(vpGiveWanted);
        if (needRefesh) {
            tabGiveWantedAdapter.setTypeMode(typeMode);
            tabGiveWantedAdapter.notifyDataSetChanged();
            needRefesh = false;
            vpGiveWanted.setAdapter(tabGiveWantedAdapter);
            viewpagertab.setViewPager(vpGiveWanted);
        }
        viewpagertab.setOnTabClickListener(position -> tabPosition = position);
        setTitleToolbar(category.getName());
        getMainActivity().hideFooter(false);
    }


    @OnClick(R.id.btnTakeNewItem)
    public void onClickTakeNewItem() {
        getBaseAppActivity().changeFragment(NewItemGiveWantedFragment
                .newInstance(category, tabPosition, type -> {
                    if (!TextUtils.isEmpty(type)) {
                        needRefesh = true;
                        typeMode = type;

                    }
                }), true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab_give_wanted;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
