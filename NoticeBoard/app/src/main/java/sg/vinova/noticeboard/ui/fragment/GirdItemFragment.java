package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.adapter.GridItemAdapter;
import sg.vinova.noticeboard.base.EndlessRecyclerViewScrollListener;
import sg.vinova.noticeboard.listener.RecyclerItemClickListener;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.module.description.DescriptionPresenter;
import sg.vinova.noticeboard.module.description.DescriptionPresenterImpl;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.widgets.AppButton;
import vn.eazy.core.utils.PreferencesUtils;

/**
 * Created by Jacky on 4/26/17.
 */

public class GirdItemFragment extends BaseMainFragment implements DescriptionPresenter.View,
        RecyclerItemClickListener.OnItemClickListener {


    GridItemAdapter adapter;
    @BindView(R.id.rvPhoto)
    RecyclerView rvItems;
    @BindView(R.id.fr_content)
    FrameLayout frContent;
    @BindView(R.id.frame_my_photo)
    FrameLayout frameMyPhoto;
    Unbinder unbinder;
    @BindView(R.id.btnTakeNewItem)
    AppButton btnTakeNewItem;


    private int currentPage;
    private String typeItem;
    private String TAG = getClass().getSimpleName();
    private Map<String, String> mapId;
    private String typeReload;
    private boolean needRefresh;
    private boolean isAdmin;
    GridLayoutManager layoutManager;
    Category category;
    DescriptionPresenterImpl presenter;
    Map<String, String> listIdEstate;

    // for goscreen/estate property
    public static GirdItemFragment newInstance(Category category, String typeItem, String typeReload) {
        GirdItemFragment fragment = new GirdItemFragment();
        fragment.category = category;
        fragment.typeItem = typeItem;
        fragment.typeReload = typeReload;
        return fragment;
    }


    // for album grid
    public static GirdItemFragment newInstance(Category category, Map<String, String> listIdEstate) {
        GirdItemFragment fragment = new GirdItemFragment();
        fragment.category = category;
        fragment.listIdEstate = listIdEstate;
        return fragment;
    }

    // for sale item
    public static GirdItemFragment newInstanceSale(Category category, Map<String, String> mapId) {
        GirdItemFragment fragment = new GirdItemFragment();
        fragment.category = category;
        fragment.mapId = mapId;
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new DescriptionPresenterImpl(getMainActivity());
        adapter = new GridItemAdapter(getBaseActivity(), category);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_grid_item;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.bind(this);

        layoutManager = new GridLayoutManager(getContext(), 2);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setAdapter(adapter);

        if (typeItem == null) {
            setTitleToolbar(category.getName());
        }
        if (category.getFixedName().equals(Category.FIXNAME.MINIMALL_PRODUCTS)) {
            btnTakeNewItem.setVisibility(View.VISIBLE);
            isAdmin = false;
        }

        // admin can tack item  :  fixname = 4 cate && permission = true
        if (category.getTypeMode().equals(Category.TYPE.ESTATE_RENT) ||
                category.getTypeMode().equals(Category.TYPE.ESTATE_SALE)) {
            if (category.isPermissionAction()) {
                isAdmin = true;
                btnTakeNewItem.setVisibility(View.GONE);
            }
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (adapter.getItemCount() == 0 || !TextUtils.isEmpty(typeItem) || needRefresh) {
            currentPage = 1;
            showProgressPage(true);
            typeReload = null;
            needRefresh = false;
            presenter.getListItem(String.valueOf(category.getId()), String.valueOf(currentPage), String.valueOf(Constant.PER_PAGE), typeItem);
        } else {
            showProgressPage(false);
        }
        rvItems.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager, currentPage) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                currentPage = page;
                presenter.getListItem(String.valueOf(category.getId()), String.valueOf(currentPage), String.valueOf(Constant.PER_PAGE), typeItem);
            }
        });
        rvItems.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), this));
        getMainActivity().showBackButton(true);
        getMainActivity().getMainToolbarHelper().setRightToolbarButton(null, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbind();
        getMainActivity().hideFooter(false);
        getMainActivity().setFooterText(PreferencesUtils.getString(getContext(), Constant.FOOTER_TEXT));
    }

    @Override
    public void onError(String message) {
        showProgressPage(false);
        SnackBarUtils.getSnackInstance().showError(getView(), message);
    }

    @Override
    public void onGetItemsSuccess(List<Description> list) {
        showProgressPage(false);
        if (currentPage == 1) {
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        adapter.addAll(list);
        if (adapter.getItemCount() == 0) {
             showMessagePage(getString(R.string.no_data_available));
        }

    }
    @Override
    protected int getStatusPageView() {
        return R.layout.layout_status_page;
    }


    @Override
    public void onItemClick(View view, int position) {
        String fixName = category.getFixedName();
        ImageView imageView = (ImageView) view.findViewById(R.id.photo);
        Description property = adapter.getItem(position);
        ItemDetailFragment fragment = ItemDetailFragment.newInstance(property, fixName, category.getTypeMode(), isDeleted -> needRefresh = isDeleted);
        fragment.setShareElementName(ViewCompat.getTransitionName(imageView));
        changeFragmentWithShareElement(imageView, fragment);
    }

    public void changeFragmentWithShareElement(View view, BaseAppFragment fragment) {
        if (!TextUtils.isEmpty(typeItem)) {
            getParentFragment().getFragmentManager()
                    .beginTransaction()
                    .addSharedElement(view, ViewCompat.getTransitionName(view))
                    .addToBackStack(TAG)
                    .replace(R.id.fragment_content, fragment)
                    .commit();
        } else {
            getFragmentManager()
                    .beginTransaction()
                    .addSharedElement(view, ViewCompat.getTransitionName(view))
                    .addToBackStack(TAG)
                    .replace(R.id.fragment_content, fragment)
                    .commit();
        }
    }

    @OnClick(R.id.btnTakeNewItem)
    void onClickNewItem() {
        if (isAdmin) {
            getMainActivity().changeFragment(NewEstateFragment.newInstance(category, new NewEstateFragment.postItemCallback() {
                @Override
                public void postSuccess(String typeMode) {
                    needRefresh = true;
                    SnackBarUtils.getSnackInstance().showSuccess(rootView, "Post successfully!");
                }
            }), true);
        } else {
            getMainActivity().changeFragment(NewItemDescriptionFragment.newInstance(category, isSuccess -> needRefresh = isSuccess), true);
        }

    }
}
