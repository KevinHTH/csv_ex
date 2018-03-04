package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.factory.ConstantApp;
import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.model.Description;
import sg.vinova.noticeboard.module.description.DeleteItemPresenterImpl;
import sg.vinova.noticeboard.module.description.DescriptionPresenter;
import sg.vinova.noticeboard.utils.CacheUtils;
import sg.vinova.noticeboard.utils.DialogUtils;
import sg.vinova.noticeboard.utils.SnackBarUtils;
import sg.vinova.noticeboard.widgets.AppTextView;
import sg.vinova.noticeboard.widgets.MenuPopup;
import sg.vinova.noticeboard.widgets.RelativePopupWindow;

/**
 * Created by Jacky on 4/26/17.
 */

public class DetailTransactionFragment extends BaseMainFragment implements MenuPopup.OnClickPopupMenuListener,
        DescriptionPresenter.DeleteView {

    Unbinder unbinder;
    @BindView(R.id.tvName)
    AppTextView tvName;
    @BindView(R.id.tvPhone)
    AppTextView tvPhone;
    @BindView(R.id.tvTitleDescription)
    AppTextView tvTitleDescription;
    @BindView(R.id.tvDescription)
    AppTextView tvDescription;

    private Category category;
    private Description description;
    DeleteItemPresenterImpl deleteItemPresenter;
    private String typeSubmit;
    private DeleteCallback deleteCallback;
    private boolean hideEditMenu;


    public static DetailTransactionFragment newInstance(Category category, Description description, DeleteCallback deleteCallback) {
        DetailTransactionFragment fragment = new DetailTransactionFragment();
        fragment.category = category;
        fragment.description = description;
        fragment.deleteCallback = deleteCallback;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (category.getTypeMode().equals(Category.TYPE.TRANSACTION_SALE)) {
            typeSubmit = ConstantApp.TYPESUBMIT.SALE;
        } else {
            typeSubmit = ConstantApp.TYPESUBMIT.RENT;
        }
        deleteItemPresenter = new DeleteItemPresenterImpl(getContext());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_detail_transaction;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        deleteItemPresenter.bind(this);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillData();
        if (CacheUtils.getDataUser().getUsername().equals(description.getUser().getUsername())) {

            getMainActivity().getMainToolbarHelper().setRightToolbarButton(R.mipmap.more, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CacheUtils.getDataUser().getRole().equals("member")) {
                        hideEditMenu = true;
                    } else {
                        hideEditMenu = false;
                    }
                    MenuPopup menuPopup = new MenuPopup(getContext(), v, hideEditMenu);
                    menuPopup.setOnClickPopupMenuListener(DetailTransactionFragment.this);
                    menuPopup.showOnAnchor(v, RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT);
                }
            });
        }
    }

    private void fillData() {
        if (description != null) {
            String block;
            String sqftTotal;
            String price;
            String content;
            String date;

            float sqftNum = description.getSize();
            if (sqftNum == 0) {
                sqftNum = 1;
            }
            price = getString(R.string.price_sqft, description.getPrice() / sqftNum) + "\n";
            sqftTotal = getString(R.string.total_sqft, description.getSize(), description.getPrice()) + "\n";
            if (category.getTypeMode().equals(Category.TYPE.TRANSACTION_RENT)) {
                block = description.getNoOfBedRooms() + " Bed Rooms\n";
            } else {
                block = "Block " + description.getBlock() + "\n";
            }
            date = "on " + description.getDate();
            content = block + sqftTotal + price + date;
            tvDescription.setMaxLines(5);
            tvDescription.setText(content);
            tvName.setText(description.getUser().getUsername());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClickEdit() {
        getMainActivity().changeFragment(NewTransactionFragment.newInstance(category, description, new NewTransactionFragment.postItemCallback() {
            @Override
            public void postSuccess(String type) {
                SnackBarUtils.getSnackInstance().showSuccess(rootView, "Edit successfully!");
            }
        }), true);
    }

    @Override
    public void onClickDelete() {
        DialogUtils.showDialogMessage(getContext(), getString(R.string.are_you_want_to_delete), null,
                v1 -> deleteItemPresenter.deleteItem(
                        String.valueOf(description.getCategory().getId()), description.getId()), "No", "Yes", false, true);

    }

    @Override
    public void onError(String message) {

    }

    @Override
    public void onDeleteItemSuccess(String message) {
        SnackBarUtils.getSnackInstance().showSuccess(getView(), message);

        if (deleteCallback != null) {
            deleteCallback.deleteSuccess(true);
        }
        getMainActivity().onBackPressed();
    }

    interface DeleteCallback {
        void deleteSuccess(boolean isDeleted);
    }
}
