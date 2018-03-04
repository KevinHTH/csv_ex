package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.adapter.DescriptionAdapter;
import sg.vinova.noticeboard.base.EndlessRecyclerViewScrollListener;
import sg.vinova.noticeboard.listener.RecyclerItemClickListener;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.module.description.DescriptionPresenter;
import sg.vinova.noticeboard.module.description.DescriptionPresenterImpl;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.widgets.AppButton;

/**
 * Created by Jacky on 4/26/17.
 */

public class ViewListFragment extends BaseMainFragment implements DescriptionPresenter.View,
        RecyclerItemClickListener.OnItemClickListener {

    @BindView(R.id.rvAnnoucement)
    RecyclerView rvAnnouncement;
    Unbinder unbinder;
    @BindView(R.id.frame_my_photo)
    FrameLayout frContent;
    @BindView(R.id.btnTakeNewRecipe)
    AppButton btnTakeNewRecipe;

    private int currentPage;
    private boolean needRefresh;
    private Category category;
    private DescriptionPresenterImpl presenter;
    private DescriptionAdapter descriptionAdapter;

    private boolean isAdmin = false;
    private Map<String, String> listIdEstate;


    public static ViewListFragment newInstance(Category category, Map<String, String> listIdEstate) {
        ViewListFragment fragment = new ViewListFragment();
        fragment.category = category;
        fragment.listIdEstate = listIdEstate;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new DescriptionPresenterImpl(getContext());
        descriptionAdapter = new DescriptionAdapter(getMainActivity(), category);
    }


    private void setupActionbar() {
        setTitleToolbar(category.getName());
        getMainActivity().getMainToolbarHelper().showToolbar(true);
        getMainActivity().getMainToolbarHelper().setImageForLeftButton(R.mipmap.back);
        getMainActivity().getMainToolbarHelper().setRightToolbarButton(null, null);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_annoucement;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbind();
        needRefresh = false;
        currentPage = 1;
    }

    @Override
    public void onError(String message) {
        showProgressForView(false);
        showPageMessageForView(getString(R.string.no_data_available));
    }

    @Override
    public void onGetItemsSuccess(List<Description> list) {
        showProgressForView(false);
        if (currentPage == 1) {
            descriptionAdapter.clear();
            descriptionAdapter.notifyDataSetChanged();
            rvAnnouncement.addOnScrollListener(new EndlessRecyclerViewScrollListener(rvAnnouncement.getLayoutManager()) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    currentPage = page;
                    presenter.getItemsById(String.valueOf(category.getId()), String.valueOf(currentPage), String.valueOf(Constant.PER_PAGE));
                }
            });
        }
        descriptionAdapter.addAll(list);

        if (descriptionAdapter.getItemCount() == 0 && currentPage == 1) {
            showPageMessageForView(getString(R.string.no_data_available));
        }
        descriptionAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.bind(this);
        setupActionbar();
        LinearLayoutManager lmm = new LinearLayoutManager(getContext());
        rvAnnouncement.setLayoutManager(lmm);
        rvAnnouncement.setAdapter(descriptionAdapter);
        rvAnnouncement.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), this));
        if (descriptionAdapter.getItemCount() == 0 || needRefresh) {
            setLayoutProgressStatus(frContent);
            currentPage = 1;
            presenter.getItemsById(String.valueOf(category.getId()), String.valueOf(currentPage),
                    String.valueOf(Constant.PER_PAGE));
            needRefresh = false;
        } else {
            showProgressForView(false);
        }


        // check view for admin
        if (category.getFixedName().equals(Category.FIXNAME.RENTAL_TRANSACTIONS) ||
                category.getFixedName().equals(Category.FIXNAME.SALE_TRANSACTIONS) ||
                category.getFixedName().equals(Category.FIXNAME.GROUPTOUR_VIEWING)
                ) {
            if (category.isPermissionAction()) {
                isAdmin = true;
                btnTakeNewRecipe.setVisibility(View.VISIBLE);
            } else {
                isAdmin = false;
                btnTakeNewRecipe.setVisibility(View.GONE);
            }
        } else {
            isAdmin = false;
            btnTakeNewRecipe.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onItemClick(View view, int position) {
        if (category.getTypeMode().equals(Category.TYPE.TRANSACTION_SALE) ||
                category.getTypeMode().equals(Category.TYPE.TRANSACTION_RENT)) {
            getMainActivity().changeFragment(DetailTransactionFragment.newInstance(category, descriptionAdapter.getItem(position), new DetailTransactionFragment.DeleteCallback() {
                @Override
                public void deleteSuccess(boolean isDeleted) {
                    needRefresh = true;
                    SnackBarUtils.getSnackInstance().showSuccess(rootView, "Delete successfully");
                }
            }), true);
        } else {
            getBaseAppActivity().changeFragment(ItemDetailFragment.newInstance(descriptionAdapter.getItem(position)
                    , category.getFixedName(), category.getTypeMode(), isDeleted -> needRefresh = isDeleted), true);
        }

    }

    @OnClick(R.id.btnTakeNewRecipe)
    void onCLickTakeNew() {
        if (isAdmin) {
            getMainActivity().changeFragment(NewTransactionFragment.newInstance(category, listIdEstate, type -> {
                needRefresh = true;
                SnackBarUtils.getSnackInstance().showSuccess(rootView, "Post successfully!");
            }), true);
        } else {
            getMainActivity().changeFragment(NewItemDescriptionFragment.newInstance(category, isSuccess -> {
                needRefresh = true;
                SnackBarUtils.getSnackInstance().showSuccess(rootView, "Post successfully!");
            }), true);
        }

    }

}
