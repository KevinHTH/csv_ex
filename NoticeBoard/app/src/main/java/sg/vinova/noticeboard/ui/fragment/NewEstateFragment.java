package sg.vinova.noticeboard.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
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

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import sg.vinova.noticeboard.base.BaseCallback;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.factory.ConstantApp;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.model.ImageObject;
import sg.vinova.noticeboard.model.Photo;
import sg.vinova.noticeboard.module.admin.EstatePresenter;
import sg.vinova.noticeboard.module.admin.EstatePresenterImpl;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.utils.LoaderUtils;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.utils.Utils;
import sg.vinova.noticeboard.widgets.AppDialog;
import sg.vinova.noticeboard.widgets.AppEditText;
import sg.vinova.noticeboard.widgets.AppTextView;
import vn.eazy.core.utils.PreferencesUtils;

/**
 * Created by Jacky on 4/26/17.
 */

public class NewEstateFragment extends BaseMainFragment implements EstatePresenter.EstateView, AppDialog.OnClickAppDialog {


    Unbinder unbinder;
    @BindView(R.id.rdSale)
    RadioButton rdSale;
    @BindView(R.id.rdRent)
    RadioButton rdRent;
    @BindView(R.id.grSaleRent)
    RadioGroup grSaleRent;
    @BindView(R.id.lnRad)
    LinearLayout lnRad;
    @BindView(R.id.edtItemDescription)
    AppEditText edtItemDescription;
    @BindView(R.id.lnDescriptionFrame)
    LinearLayout lnDescriptionFrame;
    @BindView(R.id.tvTitleBedroom)
    AppTextView tvTitleBedroom;
    @BindView(R.id.edNoBedroom)
    AppEditText edNoBedroom;
    @BindView(R.id.lnSize)
    LinearLayout lnSize;
    @BindView(R.id.tvTitleSize)
    AppTextView tvTitleSize;
    @BindView(R.id.edSize)
    AppEditText edSize;
    @BindView(R.id.lnSizeFrame)
    LinearLayout lnSizeFrame;
    @BindView(R.id.lnSizeBlock)
    LinearLayout lnSizeBlock;
    @BindView(R.id.tvTitlePrice)
    AppTextView tvTitlePrice;
    @BindView(R.id.edPrice)
    AppEditText edPrice;
    @BindView(R.id.lnPriceFrame)
    LinearLayout lnPriceFrame;
    @BindView(R.id.rvPhoto)
    RecyclerView rvPhoto;
    @BindView(R.id.imgTackPhoto)
    ImageView imgTackPhoto;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.imgAttachPhoto)
    ImageView imgAttachPhoto;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.lnMain)
    LinearLayout lnMain;

    private String pathPhoto;
    private String typeMode;
    private String imgPath;
    private int idCategory;

    private PhotoAdapter photoAdapter;
    private Category category;
    private postItemCallback postItemCallback;
    private EstatePresenterImpl postPresenter;

    private Description description;
    private boolean isUpdate = false;
    private boolean isSale;
    private List<Photo> photos;
    private List<ImageObject> imageResponse;
    private String typeSubmit;
    private editItemCallback editItemCallback;


    // add new
    public static NewEstateFragment newInstance(Category category, postItemCallback callback) {
        NewEstateFragment fragment = new NewEstateFragment();
        fragment.category = category;
        fragment.postItemCallback = callback;
        fragment.isUpdate = false;
        return fragment;
    }

    // edit estate
    public static NewEstateFragment newInstance(Description description, String typeMode, editItemCallback callback) {
        NewEstateFragment fragment = new NewEstateFragment();
        fragment.description = description;
        fragment.typeMode = typeMode;
        fragment.editItemCallback = callback;
        fragment.isUpdate = true;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photos = new ArrayList<>();
        postPresenter = new EstatePresenterImpl(getMainActivity());
        imageResponse = new ArrayList<>();
        photoAdapter = new PhotoAdapter(getMainActivity());
        if (!isUpdate && category != null) {
            idCategory = category.getId();
            typeSubmit = Description.TYPE.SALE; // set default
        } else {
            idCategory = description.getCategory().getId();
            if (description.getType_property().equals(Description.TYPE.SALE)) {
                typeSubmit = Description.TYPE.SALE;
            } else {
                typeSubmit = Description.TYPE.RENT;
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_new_estate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        postPresenter.bind(this);
        setUpPhoto();
        return rootView;
    }

    private void setUpPhoto() {
        if (isUpdate) {
            imageResponse = description.getImages();
        }

        LinearLayoutManager lmm = new LinearLayoutManager(getContext());
        lmm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvPhoto.setLayoutManager(lmm);

        if (photos.size() == 0) {
            if (isUpdate) {
                if (description.getImages().size() != 0) {
                    photos.addAll(convertImageList(description.getImages()));
                }
            }
        }

        photoAdapter.setOnClickRemovePhoto(new PhotoAdapter.OnClickRemovePhoto() {
            @Override
            public void onClickRemove(Photo photoModel) {
                if (!TextUtils.isEmpty(photoModel.getId())) {
                    setDestroyPhoto(photoModel.getId());
                }
            }
        });
        photoAdapter.setList(photos);
        rvPhoto.setAdapter(photoAdapter);

    }

    private void setDestroyPhoto(String id) {
        if (imageResponse == null || imageResponse.size() == 0) {
            return;
        }
        for (ImageObject photo : imageResponse) {
            if (String.valueOf(photo.getId()).equals(id)) {
                photo.set_destroy(1);
                break;
            }
        }
    }

    public List<Photo> convertImageList(List<ImageObject> imageList) {
        List<Photo> listPhoto = new ArrayList<>();
        for (ImageObject imageObject : imageList) {
            listPhoto.add(new Photo(String.valueOf(imageObject.getId()), imageObject.getPhotoUrl()));
        }
        return listPhoto;
    }

    private void setUpTab(boolean isSale) {
        if (isSale) {
            rdSale.setChecked(true);
            rdRent.setChecked(false);
            typeSubmit = Description.TYPE.SALE;
        } else {
            rdSale.setChecked(false);
            rdRent.setChecked(true);
            typeSubmit = Description.TYPE.RENT;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().hideFooter(true);
        getMainActivity().getMainToolbarHelper().setRightToolbarButton(null, null);

        setUpView();
        if (!isUpdate) {
            setTitleToolbar(category.getName());
            grSaleRent.setOnCheckedChangeListener((group, checkedId) -> {
                switch (checkedId) {
                    case R.id.rdSale:
                        changeEstateSaleView();
                        break;
                    case R.id.rdRent:
                        changeEstateRentView();
                        break;
                }
            });
        } else {
            setTitleToolbar(description.getCategory().getName());
            tvSubmit.setText("Save");
        }

    }

    private void setUpView() {
        if (!isUpdate) {
            typeMode = category.getTypeMode();
        }

        if (typeSubmit.equals(ConstantApp.TYPESUBMIT.SALE)) {
            changeEstateSaleView();
        } else {
            changeEstateRentView();
        }
    }

    private void changeEstateSaleView() {
        isSale = true;
        setUpTab(isSale);
        tvTitlePrice.setText("Price(S$)");
        lnSize.setVisibility(View.VISIBLE);
        lnSizeFrame.setVisibility(View.VISIBLE);
        if (isUpdate) {
            //  rdRent.setEnabled(false);
            lnRad.setVisibility(View.GONE);
            edtItemDescription.setText(description.getDescription());
            edSize.setText(description.getSize() + "");
            edNoBedroom.setText(description.getNoOfBedRooms() + "");
            edPrice.setText(description.getSalePrice() + "");
        }
    }

    private void changeEstateRentView() {
        isSale = false;
        setUpTab(isSale);
        lnSizeFrame.setVisibility(View.GONE);
        tvTitlePrice.setText("Rental per month(S$)");
        if (isUpdate) {
            // rdSale.setEnabled(false);
            lnRad.setVisibility(View.GONE);
            edtItemDescription.setText(description.getDescription());
            edNoBedroom.setText(description.getNoOfBedRooms() + "");
            edPrice.setText((int) description.getRentalPerMonth() + "");
        }
    }

    @OnClick(R.id.imgTackPhoto)
    public void onClickTackPhoto() {
        new RxPermissions(getMainActivity()).request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .filter(aBoolean -> aBoolean)
                .subscribe(aBoolean -> pathPhoto = Utils.takePicture(getMainActivity()));
    }

    @OnClick(R.id.imgAttachPhoto)
    public void onClickAttachPhoto() {
        // postPresenter.pickPhoto();
        getMainActivity().changeFragment(AddPhotoFragment.newInstance(new BaseCallback.Callback<List<Photo>>() {
            @Override
            public void callback(List<Photo> photos) {
                photoAdapter.addAll(photos);
            }
        }, ConstantApp.MAX_PHOTO_POST - photoAdapter.getItemCount()), true);
    }

    @OnClick(R.id.tvSubmit)
    public void onClickSubmit() {
        if (TextUtils.isEmpty(edtItemDescription.getText().toString())) {
            edtItemDescription.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, getString(R.string.description)), null, null, "", "Ok", false, false);
            return;
        } else if (TextUtils.isEmpty(edNoBedroom.getText().toString())) {
            edNoBedroom.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, "No. of Bedroom"), null, null, "", "Ok", false, false);
            return;
        } else if (TextUtils.isEmpty(edPrice.getText().toString())) {
            edPrice.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, "Price"), null, null, "", "Ok", false, false);
            return;
        }
        if (typeSubmit.equals(ConstantApp.TYPESUBMIT.SALE)) {
            if (TextUtils.isEmpty(edSize.getText().toString())) {
                edSize.requestFocus();
                DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, "Size"), null, null, "", "Ok", false, false);
                return;
            }
        }

        if (isUpdate) {
            postPresenter.editEstate(typeSubmit, consumeData(description), photoAdapter.getList());
        } else {
            Description newDescription = new Description();
            category.setId(idCategory);
            newDescription.setCategory(category);
            postPresenter.postEstate(typeSubmit, consumeData(newDescription), photoAdapter.getList());
        }

    }

    private Description consumeData(Description description) {
        description.setDescription(edtItemDescription.getText().toString());
        description.setNoOfBedRooms(Integer.parseInt(edNoBedroom.getText().toString()));
        if (typeSubmit.equals(ConstantApp.TYPESUBMIT.SALE)) {
            description.setSize(Float.parseFloat(edSize.getText().toString()));
            description.setSalePrice(Float.parseFloat(edPrice.getText().toString()));
        } else {
            description.setRentalPerMonth(Float.parseFloat(edPrice.getText().toString()));
        }
        return description;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        postPresenter.unbind();

        getMainActivity().hideFooter(false);
        getMainActivity().setFooterText(PreferencesUtils.getString(getContext(), Constant.FOOTER_TEXT));
    }


    @Override
    public void onError(String message) {
        SnackBarUtils.getSnackInstance().showError(getView(), message);
    }

    @Override
    public void onTakePhotoSuccess(Photo photo) {
        if (photo == null)
            return;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoaderUtils.hide();
                if (isAdded()) {
                    photoAdapter.add(photo);
                }
            }
        }, 250);
    }


    @Override
    public void onPostSuccess(BaseObjectResponse baseObjectResponse) {
        if (postItemCallback != null) {
            postItemCallback.postSuccess(typeMode);
        }
        getMainActivity().onBackPressed();
    }

    @Override
    public void onEditSuccess(String message) {
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
                            return Observable.just(Utils.saveBitmap(getContext(),
                                    String.valueOf(Calendar.getInstance().getTimeInMillis()), bitmap));
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
    public void onClickLeftButton() {
        getBaseAppActivity().onBackPressed();
    }

    @Override
    public void onClickRightButton(boolean isChecked) {

    }

    interface postItemCallback {
        void postSuccess(String typeMode);
    }

    interface editItemCallback {
        void editSuccess();
    }

}
