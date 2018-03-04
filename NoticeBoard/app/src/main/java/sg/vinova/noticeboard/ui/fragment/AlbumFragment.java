package sg.vinova.noticeboard.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.Calendar;

import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.model.Photo;
import sg.vinova.noticeboard.module.description.DescriptionPresenter;
import sg.vinova.noticeboard.module.description.PostItemPresenterImpl;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.utils.Utils;

/**
 * Created by Jacky on 4/26/17.
 */

public class AlbumFragment extends BaseMainFragment implements DescriptionPresenter.PostView {
    private Category category;
    private PostItemPresenterImpl presenter;
    private String path;


    public static AlbumFragment newInstance(Category category) {
        AlbumFragment fragment = new AlbumFragment();
        fragment.category = category;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PostItemPresenterImpl(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.bind(this);
        getBaseAppActivity().getFragmentHelper().replaceFragment(R.id.fr_content, GirdItemFragment.newInstance(category, null), false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_album;
    }


    @OnClick(R.id.btnTakeNewPhoto)
    public void onClickTakeNewPhoto() {
        getMainActivity().changeFragment(NewItemDescriptionFragment.newInstance(category,
                isSuccess -> {
                    //needRefresh = isSuccess;
                }), true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onError(String message) {
        SnackBarUtils.getSnackInstance().showError(getView(), message);

    }


    @Override
    public void onTakePhotoSuccess(Photo photo) {
        path = photo.getPath();
    }

    @Override
    public void onPickPhotoSuccess(String path) {
        this.path = path;
        presenter.postItem(category.getId() + "", "", null, path);
    }

    @Override
    public void onPostItemSuccess(LoginResponse loginResponse) {
        getBaseAppActivity().getFragmentHelper().replaceFragment(R.id.fr_content,
                GirdItemFragment.newInstance(category, null), false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == Constant.REQUEST_TAKE_PHOTO) {
            Observable.just(BitmapFactory.decodeFile(path))
                    .subscribeOn(rx.schedulers.Schedulers.io())
                    .map(bitmap -> Utils.scaleBitmap(bitmap, 800))
                    .flatMap(new Func1<Bitmap, Observable<String>>() {
                        @Override
                        public Observable<String> call(Bitmap bitmap) {
                            return Observable.just(Utils.saveBitmap(getContext(),
                                    String.valueOf(Calendar.getInstance().getTimeInMillis()), bitmap));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(path1 -> {
                        if (isAdded()) {
                            onPickPhotoSuccess(path1);
                        }
                    }, throwable -> {
                        if (isAdded()) {
                            onError(throwable.getMessage());
                        }
                    });
        }
    }
}
