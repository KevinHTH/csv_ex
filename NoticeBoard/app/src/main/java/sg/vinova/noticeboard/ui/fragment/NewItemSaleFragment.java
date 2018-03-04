package sg.vinova.noticeboard.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
import java.util.HashMap;
import java.util.Map;

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
import vn.eazy.core.utils.PreferencesUtils;

/**
 * Created by Jacky on 4/26/17.
 */

public class NewItemSaleFragment extends BaseMainFragment implements DescriptionPresenter.PostView, PhotoAdapter.OnClickRemovePhoto,
        AppDialog.OnClickAppDialog {


    @BindView(R.id.rdGiveAway)
    RadioButton rdGiveAway;
    @BindView(R.id.rdWanted)
    RadioButton rdWanted;
    @BindView(R.id.grGiveWanted)
    RadioGroup grGiveWanted;
    @BindView(R.id.lnRad)
    LinearLayout lnRad;
    @BindView(R.id.tvContact)
    AppTextView tvContact;
    @BindView(R.id.edtItemDescription)
    AppEditText edtItemDescription;
    @BindView(R.id.tvTitlePrice)
    AppTextView tvTitlePrice;
    @BindView(R.id.edtItemPrice)
    AppEditText edtItemPrice;
    @BindView(R.id.rvPhoto)
    RecyclerView rvPhoto;
    @BindView(R.id.imgTackPhoto)
    ImageView imgTackPhoto;
    @BindView(R.id.imgAttachPhoto)
    ImageView imgAttachPhoto;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    Unbinder unbinder;

    // private AppDialog appDialog;
    private PostItemPresenterImpl presenter;
    private String pathPhoto;
    private String typeMode;
    private String imgPath;
    private String categoryId = "";

    PhotoAdapter photoAdapter;
    Map<String, String> mapId = new HashMap<>();
    boolean isSale = true;

    public static NewItemSaleFragment newInstance(String categoryId, Map<String, String> mapId) {
        NewItemSaleFragment fragment = new NewItemSaleFragment();
        fragment.categoryId = categoryId;
        fragment.mapId = mapId;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PostItemPresenterImpl(getBaseAppActivity());
        photoAdapter = new PhotoAdapter(getMainActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.bind(this);
        setupActionbar();
        getMainActivity().hideFooter(true);
        setUpPhoto();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvContact.setText(CacheUtils.getDataUser().getUsername());
        if (categoryId.equals(mapId.get(Category.FIXNAME.GOGREEN_GARAGE_SALE))) {
            hideTab();
        } else if (categoryId.equals(mapId.get(Category.FIXNAME.GOGREEN_ITEM_WANTED))) {
            showBuyTab();
        } else {
            showGiveAwayTab();
        }
        grGiveWanted.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                setCategoryId(checkedId);
            }
        });
        photoAdapter.setOnClickRemovePhoto(this);
    }

    private void hideTab() {
        isSale = true;
        lnRad.setVisibility(View.GONE);
    }

    private void showGiveAwayTab() {
        isSale = false;
        rdGiveAway.setChecked(false);
        rdGiveAway.setChecked(true);
        tvTitlePrice.setVisibility(View.GONE);
        edtItemPrice.setVisibility(View.GONE);
    }

    private void showBuyTab() {
        isSale = false;
        rdGiveAway.setChecked(false);
        rdGiveAway.setChecked(false);
        tvTitlePrice.setVisibility(View.GONE);
        edtItemPrice.setVisibility(View.GONE);
    }

    private void showSaleTab() {
        isSale = true;
        rdGiveAway.setChecked(true);
        rdGiveAway.setChecked(false);
        tvTitlePrice.setVisibility(View.VISIBLE);
        edtItemPrice.setVisibility(View.VISIBLE);
    }

    private void setCategoryId(int checkedId) {
        switch (checkedId) {
            case R.id.rdGiveAway:
                categoryId = mapId.get(Category.FIXNAME.GOGREEN_GARAGE_SALE);
                showSaleTab();
                break;

            case R.id.rdWanted:
                categoryId = mapId.get(Category.FIXNAME.GOGREEN_ITEM_GIVEAWAY);
                showGiveAwayTab();
                break;
        }
    }

    private void setupActionbar() {
        getMainActivity().setTitleToolbar("");
        getMainActivity().getMainToolbarHelper().showToolbar(true);
        getMainActivity().getMainToolbarHelper().setImageForLeftButton(R.mipmap.back);
        getMainActivity().getMainToolbarHelper().setRightToolbarButton(null, null);
    }

    private void setUpPhoto() {
        LinearLayoutManager lmm = new LinearLayoutManager(getContext());
        lmm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPhoto.setLayoutManager(lmm);
        rvPhoto.setAdapter(photoAdapter);
    }

    @OnClick(R.id.imgAttachPhoto)
    public void onClickAttachPhoto() {
        presenter.pickPhoto();
    }

    @OnClick(R.id.imgTackPhoto)
    public void onClickTackPhoto() {
        new RxPermissions(getMainActivity()).request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .filter(aBoolean -> aBoolean)
                .subscribe(aBoolean -> {
                    pathPhoto = Utils.takePicture(getMainActivity());
                });
    }

    @OnClick(R.id.tvSubmit)
    public void onClickSubmit() {
        if (TextUtils.isEmpty(edtItemDescription.getText())) {
            edtItemDescription.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, getString(R.string.description)), null, null, "", "OK", false, false);
            return;
        }
        if (TextUtils.isEmpty(edtItemPrice.getText())) {
            edtItemPrice.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, getString(R.string.price)), null, null, "", "OK", false, false);
            return;
        }
        DialogUtils.showDialogSubmit(getContext(), NewItemSaleFragment.this);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_new_item;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unbind();
        unbinder.unbind();
        getMainActivity().hideFooter(false);
        getMainActivity().setFooterText(PreferencesUtils.getString(getContext(), Constant.FOOTER_TEXT));
    }

    @Override
    public void onError(String message) {
        SnackBarUtils.getSnackInstance().showError(getView(), message);
    }

    @Override
    public void onTakePhotoSuccess(Photo photo) {
        photoAdapter.clear();
        photoAdapter.add(photo);
        photoAdapter.notifyDataSetChanged();
        imgPath = photo.getPath();
    }

    @Override
    public void onPickPhotoSuccess(String path) {
        imgPath = path;
        Photo p = new Photo(path, true);
        photoAdapter.clear();
        photoAdapter.add(p);
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    public void onPostItemSuccess(LoginResponse loginResponse) {
        SnackBarUtils.getSnackInstance().showSuccess(getView(), getString(R.string.post_success));
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


    public void onClickRightButton(boolean isChecked) {
        if (isChecked) {
            presenter.postItemSale(categoryId, edtItemDescription.getText().toString(), edtItemPrice.getText().toString(), imgPath);
        } else {
            Toast.makeText(getContext(), getString(R.string.please_agree), Toast.LENGTH_SHORT).show();
        }
    }

}
