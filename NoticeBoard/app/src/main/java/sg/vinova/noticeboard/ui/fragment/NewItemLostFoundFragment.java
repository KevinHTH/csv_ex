package sg.vinova.noticeboard.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.adapter.PhotoAdapter;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.model.LoginResponse;
import sg.vinova.noticeboard.model.Photo;
import sg.vinova.noticeboard.module.description.DescriptionPresenter;
import sg.vinova.noticeboard.module.description.PostItemPresenterImpl;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.utils.Utils;
import sg.vinova.noticeboard.widgets.AppDialog;
import sg.vinova.noticeboard.widgets.AppEditText;
import sg.vinova.noticeboard.widgets.AppTextView;

/**
 * Created by Jacky on 4/26/17.
 */

public class NewItemLostFoundFragment extends BaseMainFragment implements DescriptionPresenter.PostView, PhotoAdapter.OnClickRemovePhoto, AppDialog.OnClickAppDialog {


    @BindView(R.id.rdLost)
    RadioButton rdLost;
    @BindView(R.id.rdFound)
    RadioButton rdFound;
    @BindView(R.id.groupLostFound)
    RadioGroup groupLostFound;
    @BindView(R.id.lnRad)
    LinearLayout lnRad;
    @BindView(R.id.tvContact)
    AppTextView tvContact;
    @BindView(R.id.edtDescription)
    AppEditText edtDescription;
    @BindView(R.id.rvPhoto)
    RecyclerView rvPhoto;
    @BindView(R.id.imgTackPhoto)
    ImageView imgTackPhoto;
    @BindView(R.id.imgAttachPhoto)
    ImageView imgAttachPhoto;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    Unbinder unbinder;

    private PostItemPresenterImpl presenter;
    private String pathPhoto;
    private String typeMode;
    private String imgPath;
    private String categoryId;
    private int tabPosition;

    PhotoAdapter photoAdapter;
    private Category category;
    private postItemCallback postItemCallback;


    public static NewItemLostFoundFragment newInstance(Category category, int tabPosition, postItemCallback callback) {
        NewItemLostFoundFragment fragment = new NewItemLostFoundFragment();
        fragment.categoryId = String.valueOf(category.getId());
        fragment.category = category;
        fragment.tabPosition = tabPosition;
        fragment.postItemCallback = callback;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PostItemPresenterImpl(getMainActivity());
        photoAdapter = new PhotoAdapter(getMainActivity());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_new_item_lostfound;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.bind(this);

        getMainActivity().hideFooter(true);
        setTitleToolbar(category.getName());
        setUpPhoto();
        setUpTab();
        return rootView;
    }

    private void setUpTab() {
        if (tabPosition == 0) {
            rdLost.setChecked(true);
            rdFound.setChecked(false);
            typeMode = "lost";
        } else {
            rdLost.setChecked(false);
            rdFound.setChecked(true);
            typeMode = "found";
        }
    }

    private void setUpPhoto() {
        LinearLayoutManager lmm = new LinearLayoutManager(getContext());
        rvPhoto.setLayoutManager(lmm);
        rvPhoto.setAdapter(photoAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupLostFound.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rdLost:
                    typeMode = "lost";
                    break;
                case R.id.rdFound:
                    typeMode = "found";
                    break;
            }
        });
        photoAdapter.setOnClickRemovePhoto(this);
        tvContact.setText(CacheUtils.getDataUser().getUsername());
    }


    @OnClick(R.id.imgTackPhoto)
    public void onClickTackPhoto() {
        new RxPermissions(getMainActivity()).request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .filter(aBoolean -> aBoolean)
                .subscribe(aBoolean -> pathPhoto = Utils.takePicture(getMainActivity()));
    }

    @OnClick(R.id.imgAttachPhoto)
    public void onClickAttachPhoto() {
        presenter.pickPhoto();
    }

    @OnClick(R.id.tvSubmit)
    public void onClickSubmit() {
        if (TextUtils.isEmpty(edtDescription.getText())) {
            edtDescription.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, getString(R.string.description)), null, null, "", "Ok", false, false);
            return;
        }
        DialogUtils.showDialogSubmit(getContext(), NewItemLostFoundFragment.this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbind();
    }


    @Override
    public void onError(String message) {
        SnackBarUtils.getSnackInstance().showError(getView(), message);
    }

    @Override
    public void onTakePhotoSuccess(Photo photo) {
        photoAdapter.clearAddData(photo);
        imgPath = photo.getPath();
    }

    @Override
    public void onPickPhotoSuccess(String path) {
        imgPath = path;
        photoAdapter.clearAddData(new Photo(path, true));
    }

    @Override
    public void onPostItemSuccess(LoginResponse loginResponse) {
        if (postItemCallback != null) {
            postItemCallback.postSuccess(typeMode);
        }
        getMainActivity().onBackPressed();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == Constant.REQUEST_TAKE_PHOTO) {
            Observable.just(BitmapFactory.decodeFile(pathPhoto))
                    .subscribeOn(Schedulers.io())
                    .map(bitmap -> {
                        Bitmap rotatedBitmap = Utils.getRotatedBitmap(pathPhoto, bitmap);
                        return Utils.scaleBitmap(rotatedBitmap, 800);
                    })
                    .flatMap(new Func1<Bitmap, Observable<String>>() {
                        @Override
                        public Observable<String> call(Bitmap bitmap) {
                            return Observable.just(Utils.saveBitmap(getContext(), String.valueOf(Calendar.getInstance().getTimeInMillis()), bitmap));
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(path -> {
                        if (isAdded()) {
                            Photo photo = new Photo(path, true);
                            onTakePhotoSuccess(photo);
                        }
                    }, throwable -> {
                        if (isAdded()) {
                            onError(throwable.getMessage());
                        }
                    });
        }

    }

    @Override
    public void onClickRemove(Photo photoModel) {
        imgPath = null;
    }


    @Override
    public void onClickLeftButton() {
        getBaseAppActivity().onBackPressed();
    }

    @Override
    public void onClickRightButton(boolean isChecked) {
        if (isChecked) {
            presenter.postItem(categoryId, edtDescription.getText().toString(), typeMode, imgPath);
        } else {
            Toast.makeText(getContext(), getString(R.string.please_agree), Toast.LENGTH_SHORT).show();
        }
    }

    interface postItemCallback {
        void postSuccess(String type);
    }


}
