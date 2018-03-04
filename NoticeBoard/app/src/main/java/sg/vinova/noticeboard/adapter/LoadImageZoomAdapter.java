package sg.vinova.noticeboard.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.List;

import sg.vinova.noticeboard.R;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Jacky on 3/15/2017.
 */

public class LoadImageZoomAdapter extends PagerAdapter {

    private List<String> photoUrls;
    Context mContext;
    LayoutInflater mLayoutInflater;
    boolean isTap = false;


    ImageTapListener tapListener;

    public void setTapListener(ImageTapListener tapListener) {
        this.tapListener = tapListener;
    }

    public LoadImageZoomAdapter(Context context, List<String> photoUrls) {
        mContext = context;
        this.photoUrls = photoUrls;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_image_zoom_view, container, false);
        PhotoView imageView = (PhotoView) itemView.findViewById(R.id.imageView);
        String url = photoUrls.get(position);
        Glide.with(mContext).load(url).error(R.mipmap.image_loading)
                .placeholder(R.mipmap.image_loading).into(imageView);
        imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (tapListener != null) {
                    if (!isTap) {
                        isTap = true;
                        tapListener.setOnPhotoTap();
                    } else {
                        isTap = false;
                        tapListener.setOutPhotoTap();
                    }
                }
            }

            @Override
            public void onOutsidePhotoTap() {
                if (tapListener != null) {
                    if (!isTap) {
                        isTap = true;
                        tapListener.setOnPhotoTap();
                    } else {
                        isTap = false;
                        tapListener.setOutPhotoTap();
                    }
                }
            }
        });
        container.addView(itemView);
        return itemView;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return photoUrls.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public interface ImageTapListener {
        void setOnPhotoTap();

        void setOutPhotoTap();
    }


}
