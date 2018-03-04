package sg.vinova.noticeboard.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import sg.vinova.noticeboard.model.Category;
import sg.vinova.noticeboard.ui.fragment.GirdItemFragment;

public class TabSaleRentAdapter extends FragmentStatePagerAdapter {

    Category category;

    String typeMode;

    public TabSaleRentAdapter(FragmentManager fm, Category category, String typeMode) {
        super(fm); // super tracks this
        this.category = category;
        this.setTypeMode(typeMode);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return GirdItemFragment.newInstance(category, "sale", getTypeMode());
        }
        return GirdItemFragment.newInstance(category, "rent", getTypeMode());
    }

    @Override
    public int getItemPosition(Object object) {
        if (getTypeMode().equals("sale"))
            return 0;
        return 1;
    }


    public void setTypeMode(String typeMode) {
        this.typeMode = typeMode;
    }

    public String getTypeMode() {
        return typeMode;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return "Sale";
        return "Rent";
    }
}