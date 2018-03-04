package sg.vinova.noticeboard.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import sg.vinova.noticeboard.R;


public class SliderAdapter extends PagerAdapter {

    private final List<Integer> photoUrls;
    Context mContext;
    LayoutInflater mLayoutInflater;

    public SliderAdapter(Context context, List<Integer> photoUrls) {
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
        imageView.setImageResource(photoUrls.get(position));
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}