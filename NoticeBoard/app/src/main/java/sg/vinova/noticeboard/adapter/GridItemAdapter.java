package sg.vinova.noticeboard.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.base.BaseAdapter;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.utils.Utils;
import sg.vinova.noticeboard.widgets.AppTextView;
import vn.eazy.core.base.activity.BaseActivity;

/**
 * Created by Jacky on 28/4/17.
 */

public class GridItemAdapter extends BaseAdapter<Description, GridItemAdapter.ViewHolder> {


    private final Category category;

    public GridItemAdapter(BaseActivity mActivity, Category category) {
        super(mActivity);
        this.category = category;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo)
        ImageView photo;
        @BindView(R.id.tvSubmit)
        AppTextView tvTitle;
        @BindView(R.id.tvPrice)
        AppTextView tvPrice;
        int sizePhoto;

        @BindView(R.id.viewFooterGrid)
        View viewFooter;

        LinearLayout frMain;

        private LinearLayout.LayoutParams layoutParams;
        int radius;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            sizePhoto = (int) (Utils.getScreenWidth(mActivity) / 2
                    - (mActivity.getResources().getDimensionPixelOffset(R.dimen.margin_normal) * 2));
            layoutParams = (LinearLayout.LayoutParams) photo.getLayoutParams();
            layoutParams.height = sizePhoto;
            layoutParams.width = sizePhoto;

            photo.setLayoutParams(layoutParams);
            radius = mActivity.getResources().getDimensionPixelOffset(R.dimen.radius_square_photo);

        }

        public void bind(int position) {
            Description description = getItem(position);
            ViewCompat.setTransitionName(photo, description.getId());
            if (category.getFixedName().equals(Category.FIXNAME.PHOTO_SHARE)) {
                tvTitle.setVisibility(View.GONE);
                tvPrice.setVisibility(View.GONE);
            } else {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setGravity(Gravity.LEFT);
                if (category.getFixedName().equals(Category.FIXNAME.PROPERTY_SALE)) {
                    String priceStr = mActivity.getString(R.string.price_size, description.getSalePrice(), description.getSize());
                    priceStr = priceStr.replace(".0", "");
                    tvTitle.setText(priceStr);
                } else if (category.getFixedName().equals(Category.FIXNAME.PROPERTY_SALE_RENT)) {
                    if (description.getType_property().equals(Description.TYPE.SALE)){
                        String lableSale =mActivity.getString(R.string.price_size,description.getSalePrice(),description.getSize());
                        lableSale = lableSale.replace(".0", "");
                        tvTitle.setText(lableSale+" sqft");
                    }else {
                        String priceStr = mActivity.getString(R.string.price_month, description.getRentalPerMonth());
                        priceStr = priceStr.replace(".0", "");
                        tvTitle.setText(priceStr);
                    }

                } else if (category.getFixedName().equals(Category.FIXNAME.GOGREEN_GARAGE_SALE)) {
                    tvTitle.setGravity(Gravity.RIGHT);
                    String priceStr = mActivity.getString(R.string.__price, description.getPrice());
                    priceStr = priceStr.replace(".0", "");
                    tvTitle.setText(priceStr);
                } else {
                    tvTitle.setText(description.getDescription());

                }

            }


            photo.setLayoutParams(layoutParams);
            Glide.with(getContext())
                    .load(description.getPhotoUrl()).asBitmap()
                    //.bitmapTransform(new RoundedCornersTransformation(mActivity, radius, 0))
                    .error(R.mipmap.no_mage_square).placeholder(R.mipmap.image_loading_square)
                    .override(sizePhoto, sizePhoto).centerCrop()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            photo.setImageBitmap(resource);
                        }
                    });

            viewFooter.setVisibility(position == getItemCount() - 1 ? View.VISIBLE : View.GONE);
        }
    }


}
