package sg.vinova.noticeboard.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.List;

import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.model.ImageObject;


public class SliderPropertyAdapter extends PagerAdapter {

    private final List<ImageObject> photoUrls;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public void setOnItemClick(SliderPropertyAdapter.onItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    onItemClick onItemClick;

    public SliderPropertyAdapter(Context context, List<ImageObject> photoUrls) {
        mContext = context;
        this.photoUrls = photoUrls;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return photoUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View itemView = mLayoutInflater.inflate(R.layout.item_photo_slider, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.ivPhotoSlider);

        Glide.with(mContext).load(photoUrls.get(position).getPhotoUrl()).placeholder(R.mipmap.image_loading).error(R.mipmap.no_image).into(imageView);
        container.addView(itemView);
        imageView.setOnClickListener(v -> {
            if (onItemClick != null) {
                onItemClick.setOnItemClick(position);
            }
        });
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    public interface onItemClick {
        void setOnItemClick(int position);
    }
}