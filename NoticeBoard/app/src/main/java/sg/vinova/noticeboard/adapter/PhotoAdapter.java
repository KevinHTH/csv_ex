package sg.vinova.noticeboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.base.BaseAdapter;
import sg.vinova.noticeboard.model.Photo;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.utils.Utils;
import sg.vinova.noticeboard.widgets.AppTextView;
import vn.eazy.core.base.activity.BaseActivity;

/**
 * Created by Jacky on 28/4/17.
 */

public class PhotoAdapter extends BaseAdapter<Photo, PhotoAdapter.ViewHolder> {


    private OnClickRemovePhoto onClickRemovePhoto;

    public PhotoAdapter(BaseActivity mActivity) {
        super(mActivity);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.photo)
        ImageView ivPhoto;
        @BindView(R.id.tvRemove)
        AppTextView tvRemove;
        int sizePhoto;
        private LinearLayout.LayoutParams layoutParams;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            sizePhoto = Utils.getScreenWidth(mActivity) / 4
                    - (mActivity.getResources().getDimensionPixelOffset(R.dimen.margin_normal) * 2);
            layoutParams = (LinearLayout.LayoutParams) ivPhoto.getLayoutParams();
            layoutParams.height = sizePhoto;
            layoutParams.width = sizePhoto;
            layoutParams.setMarginEnd(5);
            ivPhoto.setLayoutParams(layoutParams);
        }

        public void bind(int position) {
            Photo photo = getItem(position);
            if (photo == null)
                return;

            Observable.just(photo)
                    .subscribe(photo1 -> Glide.with(getContext()).load(photo1.getPath()).placeholder(R.mipmap.image_loading_square)
                                    .error(R.mipmap.image_loading_square).override(sizePhoto, sizePhoto).centerCrop().into(ivPhoto),
                            throwable -> throwable.printStackTrace());

            tvRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.showDialogMessage(mActivity, mActivity.getString(R.string.question_delete), null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onClickRemovePhoto != null) {
                                photo.setDestroy(1);
                                list.remove(photo);
                                notifyDataSetChanged();
                                onClickRemovePhoto.onClickRemove(photo);
                            }
                        }
                    }, "No", "Yes", false, true);
                }
            });
        }
    }

    public void clearAddData(Photo photo) {
        this.clear();
        this.add(photo);
        this.notifyDataSetChanged();
    }


    public interface OnClickRemovePhoto {
        void onClickRemove(Photo photoModel);
    }

    public void setOnClickRemovePhoto(OnClickRemovePhoto onClickRemovePhoto) {
        this.onClickRemovePhoto = onClickRemovePhoto;
    }

}
