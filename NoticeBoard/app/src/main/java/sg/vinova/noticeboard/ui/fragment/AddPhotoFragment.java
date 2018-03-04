package sg.vinova.noticeboard.ui.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.functions.Consumer;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.base.BaseCallback;
import sg.vinova.noticeboard.model.Photo;
import sg.vinova.noticeboard.module.photo.PhotoPresenter;
import sg.vinova.noticeboard.module.photo.PhotoPresenterImpl;
import sg.vinova.noticeboard.utils.LoaderUtils;
import sg.vinova.noticeboard.utils.PermissionUtils;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.widgets.CustomPhotoViewer;

/**
 * Created by Jacky on 4/26/17.
 */

public class AddPhotoFragment extends BaseMainFragment implements PhotoPresenter.PhotoView {


    @BindView(R.id.cpvPhotoviewer)
    CustomPhotoViewer rvPhotoviewer;

    private BaseCallback.Callback<List<Photo>> callback;
    private int maxNumPhoto;
    private PhotoPresenterImpl photoPresenter;

    Unbinder unbinder;

    public static AddPhotoFragment newInstance(BaseCallback.Callback<List<Photo>> callback, int maxNumPhoto) {
        AddPhotoFragment fragment = new AddPhotoFragment();
        fragment.callback = callback;
        fragment.maxNumPhoto = maxNumPhoto;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoPresenter = new PhotoPresenterImpl(getContext());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_add_photo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        photoPresenter.bind(this);
        init();
        return rootView;
    }

    private void init() {
        if (!isAdded())
            return;
        setTitleToolbar("Select up to 10 photos");
        getMainActivity().getMainToolbarHelper().setImageForLeftButton(R.mipmap.close);
        getMainActivity().getMainToolbarHelper().setRightToolbarButton("Upload", v -> {
            if (rvPhotoviewer.getListSelected() != null && rvPhotoviewer != null && callback != null) {
                callback.callback(rvPhotoviewer.getListSelected());
                getMainActivity().onBackPressed();
            }
        });

        PermissionUtils.getInstance().requestPermission(getBaseActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {
                        new android.os.Handler().postDelayed(() -> {
                            if (getMainActivity() == null)
                                return;
                            LoaderUtils.show(getContext());
                            photoPresenter.loadPhotos(getContext());
                        }, 200);

                    } else {
                        getMainActivity().onBackPressed();
                    }
                }, throwable -> throwable.printStackTrace());

        rvPhotoviewer.setMaxNumPhoto(maxNumPhoto);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getMainActivity().getMainToolbarHelper().setRightToolbarButton(null, null);
        getMainActivity().getMainToolbarHelper().setImageForLeftButton(R.mipmap.back);
    }

    @Override
    public void loadPhotosSuccess(List<Photo> photos) {
        LoaderUtils.hide();
        if (rvPhotoviewer != null) {
            rvPhotoviewer.setAdapter(getBaseActivity(), photos);
        }
    }

    @Override
    public void error(String msg) {
        LoaderUtils.hide();
        SnackBarUtils.getSnackInstance().showError(getView(), msg);
    }
}
