package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.adapter.TabSaleRentAdapter;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.widgets.AppButton;

/**
 * Created by Jacky on 4/26/17.
 */

public class TabSaleRentFragment extends BaseMainFragment {


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
    private TabSaleRentAdapter tabSaleRentAdapter;
    private int tabPosition;


    public static TabSaleRentFragment newInstance(Category category) {
        TabSaleRentFragment fragment = new TabSaleRentFragment();
        fragment.category = category;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tabSaleRentAdapter = new TabSaleRentAdapter(getChildFragmentManager(), category, typeMode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vpLostFound.setAdapter(tabSaleRentAdapter);
        viewpagertab.setViewPager(vpLostFound);
        if (needRefesh) {
            tabSaleRentAdapter.setTypeMode(typeMode);
            tabSaleRentAdapter.notifyDataSetChanged();
            needRefesh = false;
            vpLostFound.setAdapter(tabSaleRentAdapter);
            viewpagertab.setViewPager(vpLostFound);
        }
        viewpagertab.setOnTabClickListener(position -> tabPosition = position);
        setTitleToolbar(category.getName());
        getMainActivity().hideFooter(false);

        // check admin access
        if (category.getFixedName().equals(Category.FIXNAME.PROPERTY_SALE_RENT)) {
            if (!category.isPermissionAction()) {
                btnTakeNewItem.setVisibility(View.GONE);
            } else {
                btnTakeNewItem.setVisibility(View.VISIBLE);
            }
        }
    }


    @OnClick(R.id.btnTakeNewItem)
    public void onClickTakeNewItem() {
        getMainActivity().changeFragment(NewEstateFragment.newInstance(category, new NewEstateFragment.postItemCallback() {
            @Override
            public void postSuccess(String type) {
                needRefesh = true;
                typeMode = type;
                SnackBarUtils.getSnackInstance().showSuccess(rootView, "Post successfully!");
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
