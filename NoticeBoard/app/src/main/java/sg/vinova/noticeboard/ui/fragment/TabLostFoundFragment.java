package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.adapter.TabLostFoundAdapter;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.widgets.AppButton;

/**
 * Created by Jacky on 4/26/17.
 */

public class TabLostFoundFragment extends BaseMainFragment {


    @BindView(R.id.vpLostFound)
    ViewPager vpLostFound;

    @BindView(R.id.btnTakeNewItem)
    AppButton btnTakeNewItem;

    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;

    Unbinder unbinder;

    private boolean needRefesh = false;
    private String typeMode;
    private Category category;
    private TabLostFoundAdapter tabLostFoundAdapter;
    private int tabPosition;


    public static TabLostFoundFragment newInstance(Category category) {
        TabLostFoundFragment fragment = new TabLostFoundFragment();
        fragment.category = category;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabLostFoundAdapter = new TabLostFoundAdapter(getChildFragmentManager(), category, typeMode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        vpLostFound.setAdapter(tabLostFoundAdapter);
        viewpagertab.setViewPager(vpLostFound);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (needRefesh) {
            tabLostFoundAdapter = new TabLostFoundAdapter(getChildFragmentManager(), category, typeMode);
            tabLostFoundAdapter.setTypeMode(typeMode);
            tabLostFoundAdapter.notifyDataSetChanged();
            needRefesh = false;
            vpLostFound.setAdapter(tabLostFoundAdapter);
            viewpagertab.setViewPager(vpLostFound);
        }
        viewpagertab.setOnTabClickListener(position -> tabPosition = position);
        setTitleToolbar(category.getName());
        getMainActivity().hideFooter(false);
    }


    @OnClick(R.id.btnTakeNewItem)
    public void onClickTakeNewItem() {
        getBaseAppActivity().changeFragment(NewItemLostFoundFragment
                .newInstance(category, tabPosition, type -> {
                    if (!TextUtils.isEmpty(type)) {
                        needRefesh = true;
                        typeMode = type;
                    }
                }), true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_tab_lostfound;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
