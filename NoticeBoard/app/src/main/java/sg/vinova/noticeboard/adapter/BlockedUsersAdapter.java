package sg.vinova.noticeboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.base.BaseAdapter;
import sg.vinova.noticeboard.base.BaseListener;
import sg.vinova.noticeboard.model.BlockedUsers;
import sg.vinova.noticeboard.widgets.AppTextView;
import vn.eazy.core.base.activity.BaseActivity;

/**
 * Created by Ray on 7/17/17.
 */

public class BlockedUsersAdapter extends BaseAdapter<BlockedUsers, BlockedUsersAdapter.BlockedViewHolder> {

    private BaseListener.OnClickListener<BlockedUsers, Integer> listener;

    public BlockedUsersAdapter(BaseActivity mActivity) {
        super(mActivity);
    }

    public void setListener(BaseListener.OnClickListener<BlockedUsers, Integer> listener) {
        this.listener = listener;
    }

    @Override
    public BlockedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BlockedViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blocked_users, parent, false));
    }

    @Override
    public void onBindViewHolder(BlockedViewHolder holder, int position) {
        holder.bind(position);

    }

    class BlockedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTypeCate)
        AppTextView tvTypeCate;
        @BindView(R.id.btnUnlock)
        AppTextView btnUnlock;

        public BlockedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void bind(int position) {
            BlockedUsers user = list.get(position);
            if (user == null)
                return;
            tvTypeCate.setText(user.getUsername());
            btnUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!= null)
                        listener.listener(user, position);
                }
            });
        }
    }
}
