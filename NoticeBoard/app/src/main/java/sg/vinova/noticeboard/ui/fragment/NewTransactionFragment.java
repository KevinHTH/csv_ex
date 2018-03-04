package sg.vinova.noticeboard.ui.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.base.BaseObjectResponse;
import sg.vinova.noticeboard.factory.ConstantApp;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.module.admin.TransactionPresenter;
import sg.vinova.noticeboard.module.admin.TransactionPresenterImpl;
import sg.vinova.noticeboard.utils.Constant;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.widgets.AppButton;
import sg.vinova.noticeboard.widgets.AppEditText;
import sg.vinova.noticeboard.widgets.AppTextView;
import vn.eazy.core.utils.PreferencesUtils;

/**
 * Created by Jacky on 4/26/17.
 */

public class NewTransactionFragment extends BaseMainFragment implements TransactionPresenter.TransactionView {

    Unbinder unbinder;
    @BindView(R.id.rdSale)
    RadioButton rdSale;
    @BindView(R.id.rdRent)
    RadioButton rdRent;
    @BindView(R.id.grSaleRent)
    RadioGroup grSaleRent;
    @BindView(R.id.lnRad)
    LinearLayout lnRad;
    @BindView(R.id.tvTitleDate)
    AppTextView tvTitleDate;

    @BindView(R.id.lnDateFrame)
    LinearLayout lnDateFrame;
    @BindView(R.id.tvTitleSize)
    AppTextView tvTitleSize;
    @BindView(R.id.edSize)
    AppEditText edSize;
    @BindView(R.id.lnSizeFrame)
    LinearLayout lnSizeFrame;
    @BindView(R.id.tvTitleBlock)
    AppTextView tvTitleBlock;
    @BindView(R.id.edBlock)
    AppEditText edBlock;
    @BindView(R.id.lnBlockFrame)
    LinearLayout lnBlockFrame;
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
    @BindView(R.id.imgAttachPhoto)
    ImageView imgAttachPhoto;

    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.lnMain)
    LinearLayout lnMain;

    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.edDate)
    AppButton edDate;


    private String typeSubmit;
    private Category category;
    private boolean isSale = false;
    private boolean isUpdate;
    private Calendar cal;
    private String strDate;
    private Description description;
    private Date currentDate;
    private Map<String, String> listIdEstate;
    private int categoryId;


    DatePickerDialog.OnDateSetListener callback;
    TransactionPresenterImpl presenter;
    postItemCallback postItemCallback;


    // add new
    public static NewTransactionFragment newInstance(Category category, Map<String, String> listIdEstate, postItemCallback callback) {
        NewTransactionFragment fragment = new NewTransactionFragment();
        fragment.category = category;
        fragment.isUpdate = false;
        fragment.postItemCallback = callback;
        fragment.listIdEstate = listIdEstate;
        return fragment;
    }

    // edit
    public static NewTransactionFragment newInstance(Category category, Description description, postItemCallback callback) {
        NewTransactionFragment fragment = new NewTransactionFragment();
        fragment.category = category;
        fragment.isUpdate = true;
        fragment.description = description;
        fragment.postItemCallback = callback;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new TransactionPresenterImpl(getContext(), ConstantApp.TYPESUBMIT.SALE);
        categoryId = category.getId();
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_new_transaction;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.bind(this);
        getMainActivity().hideFooter(true);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView();
        grSaleRent.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rdSale:
                    changeTransactionSaleView();
                    break;
                case R.id.rdRent:
                    changeTransactionRentView();
                    break;
            }
        });

        cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.TRANSACTION_DATE_FORMAT, Locale.getDefault());
        strDate = sdf.format(cal.getTime());
        callback = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                strDate = year + "-" + addZero(month + 1) + "-" + addZero(dayOfMonth);
                edDate.setText(strDate);
            }
        };
    }

    private String addZero(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return String.valueOf(num);
    }

    private void setUpView() {
        if (category.getTypeMode().equals(ConstantApp.CATEGORY.TYPE.TRANSACTION_SALE)) {
            changeTransactionSaleView();
        } else {
            changeTransactionRentView();
        }
        imgAttachPhoto.setVisibility(View.GONE);
        imgTackPhoto.setVisibility(View.GONE);
        line1.setVisibility(View.GONE);
        line2.setVisibility(View.GONE);

    }

    private void setUpTab(boolean isSale) {
        if (isSale) {
            rdSale.setChecked(true);
            rdRent.setChecked(false);
            typeSubmit = ConstantApp.TYPESUBMIT.SALE;
        } else {
            rdSale.setChecked(false);
            rdRent.setChecked(true);
            typeSubmit = ConstantApp.TYPESUBMIT.RENT;
        }
    }


    private void changeTransactionSaleView() {
        if (listIdEstate != null) {
            categoryId = Integer.parseInt(listIdEstate.get(Category.FIXNAME.SALE_TRANSACTIONS));
        }

        isSale = true;
        setUpTab(isSale);
        tvTitleBlock.setText("Block");
        tvTitleSize.setText("Size(sqft)");
        tvTitlePrice.setText("Price(sqft)");
        if (isUpdate) {
            // rdRent.setEnabled(false);
            lnRad.setVisibility(View.GONE);
            edDate.setText(description.getDate().toString());
            edSize.setText(description.getSize() + "");
            edBlock.setText(description.getBlock());
            edPrice.setText(description.getPrice() + "");
            tvSubmit.setText("Save");
        }
    }

    private void changeTransactionRentView() {
        if (listIdEstate != null) {
            categoryId = Integer.parseInt(listIdEstate.get(Category.FIXNAME.RENTAL_TRANSACTIONS));
        }

        isSale = false;
        setUpTab(isSale);
        tvTitleBlock.setText("Size(sqft)");
        tvTitleSize.setText("No. of Bedroom");
        tvTitlePrice.setText("Rental per month(S$)");
        if (isUpdate) {
            //  rdSale.setEnabled(false);
            lnRad.setVisibility(View.GONE);
            edDate.setText(description.getDate().toString());
            edSize.setText(description.getNoOfBedRooms() + ""); // size (sale)
            edBlock.setText(description.getSize() + "");
            edPrice.setText(description.getPrice() + "");
            tvSubmit.setText("Save");
        }
    }

    @OnClick(R.id.edDate)
    public void onClickDate() {
        String s = strDate;
        String strArrtmp[] = s.split("-");
        int year = Integer.parseInt(strArrtmp[0]);
        int month = Integer.parseInt(strArrtmp[1]) - 1;
        int day = Integer.parseInt(strArrtmp[2]);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), callback, year, month, day);
        datePickerDialog.setTitle("Set Date");
        datePickerDialog.show();
    }


    @OnClick(R.id.tvSubmit)
    public void onClickSubmit() {
        if (TextUtils.isEmpty(edDate.getText().toString())) {
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, getString(R.string.description)), null, null, "", "Ok", false, false);
            return;
        }
        if (TextUtils.isEmpty(edSize.getText().toString())) {
            edSize.requestFocus();
            String titleSize;
            if (typeSubmit.equals(ConstantApp.TYPESUBMIT.RENT)) {
                titleSize = "Size";
            } else {
                titleSize = "No. of Bedrooms";
            }
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, titleSize), null, null, "", "Ok", false, false);
            return;
        }
        if (TextUtils.isEmpty(edPrice.getText().toString())) {
            edPrice.requestFocus();
            DialogUtils.showDialogMessage(getContext(), getString(R.string.please_fill_this_field, "Price"), null, null, "", "Ok", false, false);
            return;
        }


        if (isUpdate) {
            presenter.editTransaction(typeSubmit, consumeData(description));
        } else {
            Description desc = new Description();
            category.setId(categoryId);
            desc.setCategory(category);
            presenter.postTransaction(typeSubmit, consumeData(desc));
        }


    }

    private Description consumeData(Description description) {
        description.setCreatedAt(edDate.getText().toString());
        description.setDate(edDate.getText().toString());
        description.setCategory(category); //2017-06-30
        if (typeSubmit.equals(ConstantApp.TYPESUBMIT.SALE)) {
            description.setBlock(edBlock.getText().toString());
            description.setSize(Float.parseFloat(edSize.getText().toString()));
            description.setSalePrice(Float.parseFloat(edPrice.getText().toString()));
            description.setPrice(Float.parseFloat(edPrice.getText().toString()));
            //
        } else {
            description.setSize(Float.parseFloat(edBlock.getText().toString()));
            description.setNoOfBedRooms(Integer.parseInt(edSize.getText().toString()));
            description.setRentalPerMonth(Float.parseFloat(edPrice.getText().toString()));
        }
        return description;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.unbind();
        getMainActivity().hideFooter(false);
        getMainActivity().setFooterText(PreferencesUtils.getString(getContext(), Constant.FOOTER_TEXT));
    }


    @Override
    public void onError(String message) {
        SnackBarUtils.getSnackInstance().showError(getView(), message);
    }


    @Override
    public void onPostTransactionSuccess(BaseObjectResponse baseObjectResponse) {
        if (postItemCallback != null) {
            postItemCallback.postSuccess(typeSubmit);
        }
        getMainActivity().onBackPressed();
    }

    @Override
    public void onEditTransactionSuccess(String message) {
        if (postItemCallback != null) {
            postItemCallback.postSuccess(typeSubmit);
        }
        getMainActivity().onBackPressed();
    }


    interface postItemCallback {
        void postSuccess(String type);
    }

}
