package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import sg.vinova.noticeboard.adapter.BlockedUsersAdapter;
import sg.vinova.noticeboard.base.BaseListener;
import sg.vinova.noticeboard.base.EndlessRecyclerViewScrollListener;
import sg.vinova.noticeboard.model.BlockedUsers;
import sg.vinova.noticeboard.module.user.BlockedUserPresenter;
import sg.vinova.noticeboard.module.user.BlockedUsersPresenterImpl;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.utils.SnackBarUtils;

/**
 * Created by Ray on 7/17/17.
 */

public class BlockedUsersFragment extends BaseMainFragment implements BlockedUserPresenter.View,
        BaseListener.OnClickListener<BlockedUsers, Integer> {

    Unbinder unbinder;
    @BindView(R.id.rvUsers)
    RecyclerView rvUsers;
    @BindView(R.id.view)
    View view;

    private LinearLayoutManager llm;
    private BlockedUsersAdapter adapter;
    private BlockedUsersPresenterImpl presenter;
    private EndlessRecyclerViewScrollListener listener;

    public static BlockedUsersFragment newInstance() {
        BlockedUsersFragment fragment = new BlockedUsersFragment();
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_blocked_users;
    }

    @Override
    protected int getStatusPageView() {
        return R.layout.layout_status_page;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new BlockedUsersPresenterImpl(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        presenter.bind(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupActionbar();
        getMainActivity().hideFooter(false);
        init();
    }

    private void init() {
        if (!isAdded())
            return;
        llm = new LinearLayoutManager(getContext());
        if (adapter == null) {
            adapter = new BlockedUsersAdapter(getBaseActivity());
//            showProgressPage(true);
            presenter.getBlockedUsersList(1, 10);
            adapter.setListener(this);

        } else {
            setEndless();
        }
        rvUsers.setAdapter(adapter);
    }

    private void setEndless() {
        listener = new EndlessRecyclerViewScrollListener(llm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                presenter.getMoreBlockedUsersList(page, 10);
            }
        };
        rvUsers.setLayoutManager(llm);
        rvUsers.addOnScrollListener(listener);
        rvUsers.setAdapter(adapter);
    }

    private void setupActionbar() {
        setTitleToolbar(getString(R.string.blocked_users));
        try {
            getBaseAppActivity().getToolbarHelper().setRightToolbarButton(null, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbind();
    }

    @Override
    public void onSuccess(List<BlockedUsers> list) {
        showProgressPage(false);
        adapter.setList(list);
        setEndless();
    }

    @Override
    public void getMoreBlockedUserSuccess(List<BlockedUsers> list) {
        adapter.addAll(list);
    }

    @Override
    public void unlockSuccess(String msg) {
        showProgressForView(false);
        int pos = (int) view.getTag();
        adapter.remove(pos);
        SnackBarUtils.getSnackInstance().showError(getView(), msg);
    }

    @Override
    public void onError(String message) {
        showProgressPage(false);
        SnackBarUtils.getSnackInstance().showError(getView(), message);
    }

    @Override
    public void listener(BlockedUsers blockedUsers, Integer position) {
        showProgressForView(true);
        DialogUtils.showDialogMessage(getContext(), getString(R.string.un_block_user, blockedUsers.getUsername()), null,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v1) {
                        view.setTag(position);
                        presenter.unlock(blockedUsers.getId());
                    }
                }, "No", "Yes", false, true);
    }
}
