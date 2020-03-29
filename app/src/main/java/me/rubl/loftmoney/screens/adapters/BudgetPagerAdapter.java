package me.rubl.loftmoney.screens.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import me.rubl.loftmoney.screens.fragments.BudgetFragment;
import me.rubl.loftmoney.screens.model.ItemModel;

public class BudgetPagerAdapter extends FragmentPagerAdapter {

    public BudgetPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return BudgetFragment.newInstance(ItemModel.BudgetType.EXPENSE);
            case 1:
                return BudgetFragment.newInstance(ItemModel.BudgetType.INCOME);

            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
