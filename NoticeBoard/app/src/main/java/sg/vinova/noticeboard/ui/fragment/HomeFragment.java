package sg.vinova.noticeboard.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.leolin.shortcutbadger.ShortcutBadger;
import me.relex.circleindicator.CircleIndicator;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.adapter.CategoryAdapter;
import sg.vinova.noticeboard.adapter.SliderAdapter;
import sg.vinova.noticeboard.di.module.AppModule;
import sg.vinova.noticeboard.factory.ConstantApp;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.module.home.HomePresenter;
import sg.vinova.noticeboard.module.home.HomePresenterImpl;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.utils.Utils;
import vn.eazy.core.utils.PreferencesUtils;

/**
 * Created by Jacky on 4/26/17.
 */

public class HomeFragment extends BaseMainFragment implements HomePresenter.View {


    @BindView(R.id.slider)
    ViewPager slider;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.rlSlider)
    RelativeLayout rlSlider;
    @BindView(R.id.rvCategory)
    RecyclerView rvCategory;
    @BindView(R.id.scrollHome)
    NestedScrollView scrollHome;
    Unbinder unbinder;

    int numPages = 0;
    int currentPage = 0;
    private Timer swipeTimer;

    final long DELAY_SLIDE = 500;
    final long PERIOD_SLIDE = 6000;


    private List<Integer> urlBanner;
    private CategoryAdapter categoryAdapter;
    private SliderAdapter sliderAdapter;
    private HomePresenterImpl presenter;
    private boolean firstLoad;


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HomePresenterImpl(getContext());
        urlBanner = new ArrayList<>();
        sliderAdapter = new SliderAdapter(getContext(), urlBanner);
        categoryAdapter = new CategoryAdapter(getMainActivity());
        firstLoad = true;
        AppModule.setIsNeedAuthToken(true);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.bind(this);
        setupActionbar();
        if (!TextUtils.isEmpty(CacheUtils.getFooterPhoto(getContext()))) {
            getMainActivity().setPhotoFooter(CacheUtils.getFooterPhoto(getContext()));
        }
        return rootView;
    }

    private void setupActionbar() {
        String clusterName = PreferencesUtils.getString(getContext(), sg.vinova.noticeboard.utils.Constant.CLUSTER_NAME);
        getMainActivity().setFooterText(PreferencesUtils.getString(getContext(), Constant.FOOTER_TEXT));
        setTitleToolbar(clusterName + getString(R.string.title_app));
        getMainActivity().getMainToolbarHelper().showToolbar(true);
        getMainActivity().getMainToolbarHelper().setImageForLeftButton(R.mipmap.ic_settings);
        getMainActivity().getMainToolbarHelper().showBackButton(true, () -> getMainActivity().changeFragment(SettingFragment.newInstance(), true));
        getMainActivity().getMainToolbarHelper().setRightToolbarButton(R.mipmap.tackit_logo_small, v -> {
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!CacheUtils.getVerifyPhone(getContext())) {
            getMainActivity().changeFragment(VerifyPasscodeFragment.newInstance(CacheUtils.getDataUser().getUsername(), true, false), false);
        }


        if (categoryAdapter.getItemCount() == 0 || firstLoad) {
            firstLoad = false;
            showProgressPage(true);
        } else {
            showProgressPage(false);
        }
        presenter.getAllCategory();
        LinearLayout.LayoutParams sliderLayout = (LinearLayout.LayoutParams) rlSlider.getLayoutParams();
        sliderLayout.height = (int) (Utils.getScreenWidth(getBaseActivity()) * 0.55);

        urlBanner.add(R.mipmap.photo1);
        urlBanner.add(R.mipmap.photo2);
        slider.setAdapter(sliderAdapter);
        numPages = urlBanner.size();
        setBannerAutoSlide();


        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvCategory.setLayoutManager(layoutManager);
        rvCategory.setNestedScrollingEnabled(false);
        rvCategory.setAdapter(categoryAdapter);
        categoryAdapter.setListener(category -> {
            String fixName = category.getFixedName();
            String typeMode = category.getTypeMode();
            String url = "";
            switch (typeMode) {
                case ConstantApp.CATEGORY.TYPE.DESCRIPTION:
                    if (fixName.equals(ConstantApp.CATEGORY.FIXNAME.LOST_FOUND)) {
                        getMainActivity().changeFragment(TabLostFoundFragment.newInstance(category), true);
                    } else if (category.getFixedName().equals(ConstantApp.CATEGORY.FIXNAME.PHOTO_SHARE)) {
                        getMainActivity().changeFragment(AlbumFragment.newInstance(category), true);
                    } else if (fixName.equals(Category.FIXNAME.GOGREEN_ITEM_GIVEAWAY)) {
                        getMainActivity().changeFragment(TabGiveWantedFragment.newInstance(category), true);
                    } else if (fixName.equals(Category.FIXNAME.MINIMALL_PRODUCTS)) {
                        getMainActivity().changeFragment(GirdItemFragment.newInstance(category, getListIdEstate()), true);
                    } else {
                        getMainActivity().changeFragment(ViewListFragment.newInstance(category, getListIdEstate()), true);
                    }
                    break;
                case ConstantApp.CATEGORY.TYPE.GO_GREEN_SALE:
                    getMainActivity().changeFragment(GridSaleFragment.newInstance(category, getListIdSale()), true);
                    break;
                case ConstantApp.CATEGORY.TYPE.ESTATE_PROPERTY:
                    getMainActivity().changeFragment(TabSaleRentFragment.newInstance(category), true);
                    break;
                case ConstantApp.CATEGORY.TYPE.WEBVIEW:
                    if (fixName.equals(ConstantApp.CATEGORY.FIXNAME.FEEDBACK)) {
                        getMainActivity().changeFragment(NewFeedbackFragment.newInstance(ConstantApp.FEEDBACK.FEEDBACK_MCST), true);
                        break;
                    }

                    if (fixName.equals(ConstantApp.CATEGORY.FIXNAME.ANNOUNCEMENTS_BY_ESTAEMGT)) {
                        getMainActivity().changeFragment(AnnouncementsMgtFragment.newInstance(category), true);
                        break;
                    }

                    if (fixName.equals(ConstantApp.CATEGORY.FIXNAME.AGENT_DIRECTORY)) {
                        url = ConstantApp.CATEGORY.URLWEB.AGENT_DIRECTORY;
                    } else if (fixName.equals(ConstantApp.CATEGORY.FIXNAME.COOK_BOOK)) {
                        url = ConstantApp.CATEGORY.URLWEB.COOKBOOK;
                    } else if (fixName.equals(ConstantApp.CATEGORY.FIXNAME.GAME)) {
                        url = ConstantApp.CATEGORY.URLWEB.GAME;
                    } else {
                        url = ConstantApp.CATEGORY.URLWEB.BUTLER_BOOK;
                    }

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                    break;
                case ConstantApp.CATEGORY.TYPE.CONTACT:
                    getMainActivity().changeFragment(PhoneDirectoryFragment.newInstance(category), true);
                    break;

                case ConstantApp.CATEGORY.TYPE.TRANSACTION_RENT:
                case ConstantApp.CATEGORY.TYPE.TRANSACTION_SALE:
                    getMainActivity().changeFragment(ViewListFragment.newInstance(category, getListIdEstate()), true);
                    break;
            }
        });

    }

    public Map<String, String> getListIdSale() {
        List<Category> categories = categoryAdapter.getList();
        Map<String, String> mapId = new HashMap<>();
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getFixedName().equals(Category.FIXNAME.GOGREEN_GARAGE_SALE) ||
                    categories.get(i).getFixedName().equals(Category.FIXNAME.GOGREEN_ITEM_WANTED) ||
                    categories.get(i).getFixedName().equals(Category.FIXNAME.GOGREEN_ITEM_GIVEAWAY)) {
                mapId.put(categories.get(i).getFixedName(), String.valueOf(categories.get(i).getId()));
            }
        }
        return mapId;
    }

    public Map<String, String> getListIdEstate() {
        List<Category> categories = categoryAdapter.getList();
        Map<String, String> mapId = new HashMap<>();
        for (Category category : categories) {
            switch (category.getFixedName()) {
                case Category.FIXNAME.PROPERTY_SALE:
                case Category.FIXNAME.PROPERTY_RENT:
                case Category.FIXNAME.SALE_TRANSACTIONS:
                case Category.FIXNAME.RENTAL_TRANSACTIONS:
                    mapId.put(category.getFixedName(), String.valueOf(category.getId()));
                    break;
            }
        }
        return mapId;
    }

    private void setBannerAutoSlide() {
        Handler handler = new Handler();
        Runnable Update = () -> {
            if (!isAdded()) {
                return;
            }
            if (currentPage == numPages) {
                currentPage = 0;
            }
            slider.setCurrentItem(currentPage++, true);
        };
        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isAdded()) {
                    swipeTimer.cancel();
                } else {
                    handler.post(Update);
                }
            }

        }, DELAY_SLIDE, PERIOD_SLIDE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getMainActivity().getMainToolbarHelper().setImageForLeftButton(R.mipmap.back);
        swipeTimer.cancel();
        unbinder.unbind();
        presenter.unbind();

    }

    @Override
    public void onError(String message) {
        SnackBarUtils.getSnackInstance().showError(getView(), message);
        showProgressPage(false);
    }

    @Override
    public void onGetAllCategorySuccess(List<Category> categories) {
        showProgressPage(false);
        categoryAdapter.clear();
        categoryAdapter.addAll(categories);
        if (categoryAdapter.getItemCount() == 0) {
            showMessagePage(getString(R.string.no_data_available));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getMainActivity().hideFooter(false);
        ShortcutBadger.removeCount(getMainActivity());
    }

    @Override
    protected int getStatusPageView() {
        return R.layout.layout_status_page;
    }
}
