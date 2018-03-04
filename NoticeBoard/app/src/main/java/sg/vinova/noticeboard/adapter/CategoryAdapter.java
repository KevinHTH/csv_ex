package sg.vinova.noticeboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.base.BaseAdapter;
import sg.vinova.noticeboard.base.BaseListener;
import sg.vinova.noticeboard.factory.ConstantApp;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.utils.Utils;
import sg.vinova.noticeboard.widgets.AppTextView;
import vn.eazy.core.base.activity.BaseActivity;

/**
 * Created by Jacky on 28/4/17.
 */

public class CategoryAdapter extends BaseAdapter<Category, CategoryAdapter.ViewHolder> {


    private BaseListener.OnItemClickListener<Category> listener;

    public void setListener(BaseListener.OnItemClickListener<Category> listener) {
        this.listener = listener;
    }


    public CategoryAdapter(BaseActivity mActivity) {
        super(mActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private FrameLayout.LayoutParams layoutParams;

        @BindView(R.id.ivCate)
        ImageView ivCate;

        @BindView(R.id.imgIconCate)
        ImageView imgIconCate;

        @BindView(R.id.tvTypeCate)
        AppTextView tvTypeCate;

        @BindView(R.id.lnItemCategory)
        RelativeLayout lnItemCategory;

        @BindView(R.id.tvCount)
        AppTextView tvCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            layoutParams = (FrameLayout.LayoutParams) lnItemCategory.getLayoutParams();
            layoutParams.height = (int) (Utils.getScreenWidth(mActivity) / 2 / 1.47);
            lnItemCategory.setLayoutParams(layoutParams);
            itemView.setOnClickListener(v -> {
                if (listener != null)
                    listener.listener(getItem(getAdapterPosition()));
            });
        }

        public void bind(int position) {
            Category c = getItem(position);
            if (position % 2 == 0) {
                layoutParams.setMarginEnd(mActivity.getResources().getDimensionPixelOffset(R.dimen.divider_category));
            } else {
                layoutParams.setMarginEnd(0);
            }

            String fixName = c.getFixedName();
            String cateName = c.getName();
            switch (c.getTypeMode()) {
                case ConstantApp.CATEGORY.TYPE.WEBVIEW:
                    if (fixName.equals(ConstantApp.CATEGORY.FIXNAME.AGENT_DIRECTORY)) {
                        cateName = sortText(c.getName());
                    } else if (fixName.equals(ConstantApp.CATEGORY.FIXNAME.COOK_BOOK)) {
                        cateName = sortText(c.getName());
                    } else if (fixName.equals(ConstantApp.CATEGORY.FIXNAME.GAME)) {
                        cateName = sortText(c.getName());
                    } else {
                        cateName = sortText(c.getName());
                    }
                    break;
            }
            tvTypeCate.setText(cateName);


            if (c.getIconMode().equalsIgnoreCase(ConstantApp.CATEGORY.ICON.NOTICE)) {
                imgIconCate.setBackgroundResource(R.mipmap.tackit_logo_small);
            } else if (c.getIconMode().equalsIgnoreCase(ConstantApp.CATEGORY.ICON.BUTLER)) {
                imgIconCate.setBackgroundResource(R.mipmap.residentbutler_grid);
            } else {
                imgIconCate.setBackgroundResource(R.mipmap.residentagent_grid);
            }
            Glide.with(getContext())
                    .load(c.getPhotoUrl())
                    .into(ivCate);


            if (c.getNumberUnread() != 0) {
                tvCount.setVisibility(View.VISIBLE);
            } else {
                tvCount.setVisibility(View.INVISIBLE);
            }
            if (c.getNumberUnread() > 20) {
                tvCount.setText("20+");
            } else {
                tvCount.setText(c.getNumberUnread() + "");
            }
        }
    }

    public String sortText(final String data) {
        final int len = data.length();
        StringBuffer text = new StringBuffer();
        for (int i = 0; i < len; i++) {
            if (data.charAt(i) == 'E') {
                text.append("\n" + data.charAt(i));
            } else {
                text.append(data.charAt(i));
            }
        }
        return text.toString();
    }

}
