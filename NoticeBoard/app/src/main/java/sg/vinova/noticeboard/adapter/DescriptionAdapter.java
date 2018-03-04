package sg.vinova.noticeboard.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.base.BaseAdapter;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.widgets.AppTextView;
import vn.eazy.core.base.activity.BaseActivity;

/**
 * Created by Jacky on 28/4/17.
 */

public class DescriptionAdapter extends BaseAdapter<Description, DescriptionAdapter.ViewHolder> {


    private final Category category;

    public DescriptionAdapter(BaseActivity mActivity, @NonNull Category category) {
        super(mActivity);
        this.category = category;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_description, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgItem)
        ImageView imgItem;
        @BindView(R.id.tvContent)
        AppTextView tvContent;
        @BindView(R.id.layoutContent)
        LinearLayout layoutContent;
        @BindView(R.id.tvTime)
        AppTextView tvTime;
        @BindView(R.id.viewFooter)
        View viewFooter;
        int width;
        int height;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            width = getContext().getResources().getDimensionPixelOffset(R.dimen.with_image_item);
            height = getContext().getResources().getDimensionPixelOffset(R.dimen.height_image_item);
        }

        public void bind(int position) {
            boolean isTransaction = false;
            Description description = getItem(position);

            if (!TextUtils.isEmpty(description.getPhotoUrl())) {
                Glide.with(getContext())
                        .load(description.getPhotoUrl()).placeholder(R.mipmap.image_loading)
                        .centerCrop().override(width, height)
                        .into(imgItem);
                imgItem.setVisibility(View.VISIBLE);
            } else {
                imgItem.setVisibility(View.GONE);
            }

            if (category != null) {

                String block;
                String sqftTotal;
                String price;
                String content;
                if (category.getTypeMode().equals(Category.TYPE.TRANSACTION_RENT)
                        || category.getTypeMode().equals(Category.TYPE.TRANSACTION_SALE)) {
                    isTransaction = true;
                    float sqftNum = description.getSize();
                    if (sqftNum == 0) {
                        sqftNum = 1;
                    }
                    price = mActivity.getString(R.string.price_sqft, description.getPrice() / sqftNum) + "\n";
                    sqftTotal = mActivity.getString(R.string.total_sqft, description.getSize(), description.getPrice()) + "\n";
                    if (category.getTypeMode().equals(Category.TYPE.TRANSACTION_RENT)) {
                        // price = mActivity.getString(R.string.price_month, description.getPrice()) + "\n";
                        block = description.getNoOfBedRooms() + " Bed Rooms\n";
                    } else {
                        block = "Block " + description.getBlock() + "\n";
                    }

                    content = block + sqftTotal + price;
                } else {
                    tvContent.setMaxLines(3);
                    content = description.getDescription();
                }

                tvContent.setText(content);
            }

            if (isTransaction) {
                if (!TextUtils.isEmpty(description.getDate())) {
                    tvTime.setText(description.getDate().toString());
                }
            } else {
                Date now = Calendar.getInstance().getTime();
                SimpleDateFormat sdfDue = new SimpleDateFormat(Constant.DATE_INPUT_FORMAT);
                sdfDue.setTimeZone(TimeZone.getTimeZone("UTC"));
                Date due = null;
                try {
                    due = sdfDue.parse(description.getCreatedAt().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (due.getTime() > now.getTime()) {
                    tvTime.setText("a few seconds ago");
                } else {
                    tvTime.setText(DateUtils.getRelativeTimeSpanString(due.getTime(), now.getTime(), DateUtils.SECOND_IN_MILLIS));
                }
            }

            viewFooter.setVisibility(position == getItemCount() - 1 ? View.VISIBLE : View.GONE);
        }
    }


}
