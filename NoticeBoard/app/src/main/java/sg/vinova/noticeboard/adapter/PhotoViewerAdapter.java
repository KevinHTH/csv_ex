package sg.vinova.noticeboard.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.base.BaseAdapter;
import sg.vinova.noticeboard.model.Photo;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.utils.Utils;
import vn.eazy.core.base.activity.BaseActivity;

/**
 * Created by Jacky on 6/20/17.
 */

public class PhotoViewerAdapter extends BaseAdapter<Photo, PhotoViewerAdapter.PhotoViewHolder> {


    private List<Photo> list;
    private int itemSize;
    private int maxNumPhoto;

    public PhotoViewerAdapter(BaseActivity mActivity) {
        super(mActivity);
        list = new ArrayList<>();
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo_viewer, parent, false);
        itemSize = Utils.getScreenWidth(getContext()) / 3;
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        holder.bind(position);
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;
        @BindView(R.id.cbPhoto)
        CheckBox cbPhoto;
        @BindView(R.id.rlPhotoviewer)
        RelativeLayout rlPhotoviewer;


        public PhotoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivPhoto.setLayoutParams(new RelativeLayout.LayoutParams(
                    Utils.getScreenWidth(getContext()) / 3, Utils.getScreenWidth(getContext()) / 3));
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(Utils.getScreenWidth(getContext()) / 3, Utils.getScreenWidth(getContext()) / 3);
            cbPhoto.setGravity(Gravity.RIGHT);
            cbPhoto.setPadding(10, 2, 0, 0);
            cbPhoto.setLayoutParams(layoutParams);

        }

        public void bind(int position) {
            Photo photo = getItem(position);
            if (photo == null)
                return;

            cbPhoto.setChecked(photo.isSelected());
            cbPhoto.setOnClickListener(v -> {
                if (!photo.isSelected() && getListSelected().size() == maxNumPhoto) {
                    SnackBarUtils.getSnackInstance().showError(itemView,
                            mActivity.getString(R.string.chooses_10_photo));
                    photo.setSelected(false);
                    notifyDataSetChanged();
                    return;
                }

                if (cbPhoto.isChecked()) {
                    list.add(photo);
                } else {
                    list.remove(photo);
                }
                photo.setSelected(cbPhoto.isSelected());
                getItems().set(position, photo);
            });

            Glide.with(getContext()).load(getItem(position).getPath()).placeholder(R.mipmap.image_loading_square)
                    .override(itemSize, itemSize).centerCrop().error(R.mipmap.image_loading).into(ivPhoto);

        }
    }

    public List<Photo> getListSelected() {
        return list;
    }

    public void setMaxNumPhoto(int maxNumPhoto) {
        this.maxNumPhoto = maxNumPhoto;
    }
}
