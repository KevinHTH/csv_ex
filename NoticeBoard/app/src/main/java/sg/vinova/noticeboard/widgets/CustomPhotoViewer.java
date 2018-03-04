package sg.vinova.noticeboard.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.List;

import sg.vinova.noticeboard.adapter.PhotoViewerAdapter;
import sg.vinova.noticeboard.model.Photo;
import vn.eazy.core.base.activity.BaseActivity;

/**
 * Created by Ray on 3/14/17.
 */

public class CustomPhotoViewer extends RecyclerView {
    private PhotoViewerAdapter adapter;
    private int maxNumPhoto;

    public CustomPhotoViewer(Context context) {
        super(context);
        init();
    }

    public CustomPhotoViewer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomPhotoViewer(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 3));
    }

    public CustomPhotoViewer setAdapter(BaseActivity activity, List<Photo> list) {
        if (adapter == null)
            adapter = new PhotoViewerAdapter(activity);
        setAdapter(adapter);
        adapter.setMaxNumPhoto(maxNumPhoto);
        adapter.setList(list);
        return this;
    }

    public List<Photo> getListSelected() {
        if (adapter != null)
            return adapter.getListSelected();
        return null;
    }

    public void setMaxNumPhoto(int maxNumPhoto) {
        this.maxNumPhoto = maxNumPhoto;
    }
}
