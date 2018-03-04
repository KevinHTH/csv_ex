package sg.vinova.noticeboard.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;
import sg.vinova.noticeboard.R;
import sg.vinova.noticeboard.model.Category;

/**
 * Created by Jacky on 4/26/17.
 */

public class GridSaleFragment extends BaseMainFragment {

    private Category category;
    private Map<String, String> mapId = new HashMap<>();

    public static GridSaleFragment newInstance(Category category, Map<String, String> listIdSale) {
        GridSaleFragment fragment = new GridSaleFragment();
        fragment.category = category;
        fragment.mapId = listIdSale;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBaseAppActivity().getFragmentHelper().replaceFragment(R.id.fr_content, GirdItemFragment.newInstanceSale(category, mapId), false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_grid;
    }

    @OnClick(R.id.btnTakeNewItem)
    public void onClickTakeNewItem() {
        getMainActivity().changeFragment(NewItemSaleFragment.newInstance(String.valueOf(category.getId()), mapId), true);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
