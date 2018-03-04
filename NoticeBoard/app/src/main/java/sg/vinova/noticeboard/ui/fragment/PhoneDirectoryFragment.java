package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.adapter.DirectoryAdapter;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.module.description.DescriptionPresenter;
import sg.vinova.noticeboard.module.description.DescriptionPresenterImpl;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.utils.SnackBarUtils;

/**
 * Created by Jacky on 4/26/17.
 */

public class PhoneDirectoryFragment extends BaseMainFragment implements DescriptionPresenter.View {

    @BindView(R.id.rvPhoneDirectory)
    RecyclerView rvPhoneDirectory;

    private DirectoryAdapter adapter;
    private DescriptionPresenterImpl presenter;

    Unbinder unbinder;
    private String categoryId;


    public static PhoneDirectoryFragment newInstance(Category category) {
        PhoneDirectoryFragment fragment = new PhoneDirectoryFragment();
        fragment.categoryId = String.valueOf(category.getId());
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new DirectoryAdapter(getBaseActivity());
        presenter = new DescriptionPresenterImpl(getBaseActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = super.onCreateView(inflater, container, savedInstanceState);
        setupActionbar();
        unbinder = ButterKnife.bind(this, viewRoot);
        presenter.bind(this);
        LinearLayoutManager lmm = new LinearLayoutManager(getContext());
        rvPhoneDirectory.setLayoutManager(lmm);
        rvPhoneDirectory.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvPhoneDirectory.getContext(), DividerItemDecoration.VERTICAL);
        rvPhoneDirectory.addItemDecoration(dividerItemDecoration);
        rvPhoneDirectory.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (adapter.getItemCount() == 0) {
            showProgressPage(true);
            presenter.getItemsById(categoryId, "1", String.valueOf(Constant.PER_PAGE));
        }
    }

    private void setupActionbar() {
        setTitleToolbar("Phone Directory");
        getMainActivity().getMainToolbarHelper().showToolbar(true);
        getMainActivity().getMainToolbarHelper().setImageForLeftButton(R.mipmap.back);
        getMainActivity().getMainToolbarHelper().setRightToolbarButton(null, null);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_phone_directory;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbind();
    }

    @Override
    public void onError(String message) {
        showProgressPage(false);
        SnackBarUtils.getSnackInstance().showError(getView(), message);
    }

    @Override
    public void onGetItemsSuccess(List<Description> list) {
        showProgressPage(false);
        if (list.size() == 0) {
            showMessagePage(getString(R.string.no_data_available));
        } else {
            adapter.clear();
            adapter.addAll(list);

        }
    }


    @Override
    protected int getStatusPageView() {
        return R.layout.layout_status_page;
    }
}
