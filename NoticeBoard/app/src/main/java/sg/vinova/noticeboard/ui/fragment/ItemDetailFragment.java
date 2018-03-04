package sg.vinova.noticeboard.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.adapter.SliderPropertyAdapter;
import sg.vinova.noticeboard.model.BlockedUsers;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.model.ImageObject;
import sg.vinova.noticeboard.module.description.DeleteItemPresenterImpl;
import sg.vinova.noticeboard.module.description.DescriptionPresenter;
import sg.vinova.noticeboard.module.user.ActionBlockPresenter;
import sg.vinova.noticeboard.module.user.ActionBlockPresenterImpl;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.widgets.AppTextView;
import sg.vinova.noticeboard.widgets.MenuPopup;
import sg.vinova.noticeboard.widgets.MultiMenuPopup;
import sg.vinova.noticeboard.widgets.RelativePopupWindow;

/**
 * Created by Jacky on 4/26/17.
 */

public class ItemDetailFragment extends BaseMainFragment implements SliderPropertyAdapter.onItemClick,
        DescriptionPresenter.DeleteView, MenuPopup.OnClickPopupMenuListener, MultiMenuPopup.OnClickPopupMenuListener,
        ActionBlockPresenter.View {
    @BindView(R.id.ivCoverDetail)
    ViewPager ivCoverDetail;
    @BindView(R.id.tvName)
    AppTextView tvContact;
    @BindView(R.id.tvTitleDescription)
    AppTextView tvTitleDescription;
    @BindView(R.id.tvDescription)
    AppTextView tvDescription;
    @BindView(R.id.tvNumBed)
    AppTextView tvNumBed;
    @BindView(R.id.tvPrice)
    AppTextView tvPrice;
    @BindView(R.id.tvSqft)
    AppTextView tvSqft;
    @BindView(R.id.tvFurnish)
    AppTextView tvFurnish;
    @BindView(R.id.tvAvailable)
    AppTextView tvAvailable;
    @BindView(R.id.tvSize)
    AppTextView tvSize;
    @BindView(R.id.tvPhone)
    AppTextView tvPhone;
    @BindView(R.id.labelTitle)
    AppTextView labelTitle;

    Unbinder unbinder;

    String typeProperty;
    String typeMode;
    String priceStr;
    String bedroomStr;
    List<ImageObject> urlPhoto;

    private String shareElementName;
    private String TAG = getClass().getSimpleName();
    private SliderPropertyAdapter sliderAdapter;
    private DeleteItemPresenterImpl deleteItemPresenter;
    private ActionBlockPresenterImpl actionBlockPresenter;
    private Description property;
    private DeleteCallback deleteCallback;
    private String typeEdit;
    private boolean hideEditMenu;

    // detail for any category
    public static ItemDetailFragment newInstance(Description property, String fixName, String typeMode,
                                                 DeleteCallback deleteCallback) {
        ItemDetailFragment fragment = new ItemDetailFragment();
        fragment.property = property;
        fragment.typeProperty = fixName;
        fragment.typeMode = typeMode;
        fragment.deleteCallback = deleteCallback;
        return fragment;
    }

    public void setShareElementName(String name) {
        shareElementName = name;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deleteItemPresenter = new DeleteItemPresenterImpl(getContext());
        actionBlockPresenter = new ActionBlockPresenterImpl(getContext());
        postponeEnterTransition();
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        setSharedElementReturnTransition(null);
        urlPhoto = new ArrayList<>();
        if (property.getImages() == null) {
            ImageObject imageObject = new ImageObject();
            imageObject.setId(Integer.parseInt(property.getId()));
            imageObject.setPhotoUrl(property.getPhotoUrl());
            urlPhoto.add(imageObject);

        } else {
            urlPhoto.addAll(property.getImages());
        }
        sliderAdapter = new SliderPropertyAdapter(getBaseAppActivity(), urlPhoto);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deleteItemPresenter.bind(this);
        actionBlockPresenter.bind(this);
        setupData();
        sliderAdapter.setOnItemClick(this);
        ivCoverDetail.setTransitionName(shareElementName);
        setTitleToolbar(getString(R.string.detail));

        // check to edit/ delete
        if (CacheUtils.getDataUser().getUsername().equals(property.getUser().getUsername())) {
            if (!property.isOwnerItem())
                getMainActivity().getMainToolbarHelper().setRightToolbarButton(R.mipmap.more, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (CacheUtils.getDataUser().getRole().equals("member")) {
                            hideEditMenu = true;
                        } else {
                            hideEditMenu = false;
                        }
                        MultiMenuPopup menuPopup = new MultiMenuPopup(getContext(), v, hideEditMenu, !hideEditMenu);
                        menuPopup.setOnClickPopupMenuListener(ItemDetailFragment.this);
                        menuPopup.showOnAnchor(v, RelativePopupWindow.VerticalPosition.BELOW,
                                RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT);
                    }
                });
            else
                getMainActivity().getMainToolbarHelper().setRightToolbarButton(R.mipmap.more, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (CacheUtils.getDataUser().getRole().equals("member")) {
                            hideEditMenu = true;
                        } else {
                            hideEditMenu = false;
                        }
                        MenuPopup menuPopup = new MenuPopup(getContext(), v, hideEditMenu);
                        menuPopup.setOnClickPopupMenuListener(ItemDetailFragment.this);
                        menuPopup.showOnAnchor(v, RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT);
                    }
                });
        } else {
            if (!property.isOwnerItem())
                getMainActivity().getMainToolbarHelper().setRightToolbarButton(R.mipmap.more, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CacheUtils.getDataUser().getRole().equals("member")) {
                            hideEditMenu = true;
                        } else {
                            hideEditMenu = false;
                        }
                        MultiMenuPopup menuPopup = new MultiMenuPopup(getContext(), v, hideEditMenu, false);
                        menuPopup.setOnClickPopupMenuListener(ItemDetailFragment.this);
                        menuPopup.showOnAnchor(v, RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT);
                    }
                });
        }
    }

    private void setupData() {
        tvContact.setText(property.getUser().getUsername());
        tvTitleDescription.setText(R.string.description);
        tvDescription.setText(property.getDescription());
        if (property.getNoOfBedRooms() > 1) {
            bedroomStr = getString(R.string.bed_rooms);
        } else {
            bedroomStr = getString(R.string.bed_room);
        }
        tvNumBed.setText("- " + property.getNoOfBedRooms() + " " + bedroomStr);
        if (typeProperty.equals(Category.FIXNAME.PROPERTY_RENT)) {
            tvSqft.setVisibility(View.GONE);
            tvFurnish.setVisibility(View.GONE);
            tvSize.setVisibility(View.GONE);
            tvPhone.setText(", " + property.getUser().getPhoneNumber());
            priceStr = getBaseAppActivity().getString(R.string.per_month, property.getRentalPerMonth());
            priceStr = priceStr.replace(".0", "");
            tvPrice.setText(priceStr);
        } else if (typeProperty.equals(Category.FIXNAME.GOGREEN_GARAGE_SALE)) {
            tvSqft.setVisibility(View.GONE);
            tvNumBed.setVisibility(View.GONE);
            tvSize.setVisibility(View.GONE);
            labelTitle.setVisibility(View.VISIBLE);
            tvPhone.setVisibility(View.GONE);
            priceStr = getBaseAppActivity().getString(R.string._price, property.getPrice());
            priceStr = priceStr.replace(".0", "");
            tvPrice.setText(priceStr);

        } else if (typeProperty.equals(Category.FIXNAME.PROPERTY_SALE_RENT)) {
            priceStr = getBaseAppActivity().getString(R.string._price, property.getSalePrice());
            tvSize.setVisibility(View.GONE);
            tvSqft.setVisibility(View.VISIBLE);
            tvPhone.setText(", " + property.getUser().getPhoneNumber());

            if (property.getType_property().equals(Description.TYPE.SALE)) {
                String sqft = getBaseAppActivity().getString(R.string.per_sqft, (property.getSalePrice() / property.getSize()));
                tvSqft.setText(sqft);
                priceStr = priceStr.replace(".0", "");
                String size = String.valueOf(property.getSize());
                size = size.replace(".0", "");
                tvPrice.setText(priceStr + " / " + size + " " + getBaseAppActivity().getString(R.string.sqft));
            } else {
                String priceStr = getBaseAppActivity().getString(R.string.price_month, property.getRentalPerMonth());
                priceStr = priceStr.replace(".0", "");
                tvPrice.setText("- " + priceStr);
            }


        } else if (typeProperty.equals(Category.FIXNAME.RENTAL_TRANSACTIONS) ||
                typeProperty.equals(Category.FIXNAME.SALE_TRANSACTIONS)) {
            tvPhone.setText(", " + property.getUser().getPhoneNumber());

        } else {
            tvSqft.setVisibility(View.GONE);
            tvFurnish.setVisibility(View.GONE);
            tvSize.setVisibility(View.GONE);
            tvNumBed.setVisibility(View.GONE);
            tvPrice.setVisibility(View.GONE);
            tvPhone.setVisibility(View.GONE);
        }
        try {
            if (sliderAdapter.getCount() != 0) {
                ivCoverDetail.setAdapter(sliderAdapter);
            } else {
                ivCoverDetail.setVisibility(View.GONE);
            }
            if (urlPhoto.size() == 0 || TextUtils.isEmpty(urlPhoto.get(0).getPhotoUrl())) {
                ivCoverDetail.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_grid_detail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        actionBlockPresenter.unbind();
    }

    @Override
    public void setOnItemClick(int position) {
        ViewPager viewPager = (ViewPager) getView().findViewById(R.id.ivCoverDetail);
        LoadImageFragment fragment = LoadImageFragment.newInstance(position, property);
        viewPager.setTransitionName("detail");
        fragment.setShareTransactionName(ViewCompat.getTransitionName(viewPager));
        changeFragmentWithShareElement(viewPager, fragment);
    }

    private void changeFragmentWithShareElement(ViewPager viewPager, LoadImageFragment fragment) {
        getFragmentManager()
                .beginTransaction()
                .addSharedElement(viewPager, ViewCompat.getTransitionName(viewPager))
                .addToBackStack(TAG)
                .replace(R.id.fragment_content, fragment)
                .commit();
    }

    @OnClick(R.id.tvPhone)
    void onClickPhone() {
        Intent dial = new Intent();
        dial.setAction("android.intent.action.DIAL");
        dial.setData(Uri.parse("tel:" + tvPhone.getText()));
        startActivity(dial);
    }

    @Override
    public void onError(String message) {
        SnackBarUtils.getSnackInstance().showError(getView(), message);
    }

    @Override
    public void onDeleteItemSuccess(String message) {
        SnackBarUtils.getSnackInstance().showSuccess(getView(), message);

        if (deleteCallback != null) {
            deleteCallback.deleteSuccess(true);
        }
        getMainActivity().onBackPressed();
    }

    @Override
    public void onClickEdit() {

        if (typeMode.equals(Category.TYPE.ESTATE_PROPERTY)) {
            getMainActivity().changeFragment(NewEstateFragment.newInstance(property, typeMode, new NewEstateFragment.editItemCallback() {
                @Override
                public void editSuccess() {
                    SnackBarUtils.getSnackInstance().showSuccess(rootView, "Edit successfully");
                }
            }), true);

        }
    }

    @Override
    public void onClickDelete() {
        DialogUtils.showDialogMessage(getContext(), getString(R.string.are_you_want_to_delete), null,
                v1 -> deleteItemPresenter.deleteItem(
                        String.valueOf(property.getCategory().getId()), property.getId()), "No", "Yes", false, true);


    }

    @Override
    public void blockAuthor() {
        showProgressForView(true);
        DialogUtils.showDialogMessage(getContext(), getString(R.string.block_user, property.getUser().getUsername()), null,
                v1 -> actionBlockPresenter.blockUser(property.getUser().getId() + ""), "No", "Yes", false, true);
    }

    @Override
    public void report() {
        getMainActivity().changeFragment(ReportTackFragment.newInstance(property), true);
    }

    @Override
    public void blockSuccess(BlockedUsers user) {
        showProgressForView(false);
        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
        if (deleteCallback != null) {
            deleteCallback.deleteSuccess(true);
        }
        getMainActivity().onBackPressed();
    }

    interface DeleteCallback {
        void deleteSuccess(boolean isDeleted);
    }
}
