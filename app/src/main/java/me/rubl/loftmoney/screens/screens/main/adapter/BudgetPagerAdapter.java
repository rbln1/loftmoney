package me.rubl.loftmoney.screens.screens.main.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import me.rubl.loftmoney.screens.screens.main.fragment.BudgetFragment;
import me.rubl.loftmoney.screens.screens.main.model.BudgetType;
import me.rubl.loftmoney.screens.screens.main.model.ItemModel;

public class BudgetPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGES = 2;

    public BudgetPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return BudgetFragment.newInstance(BudgetType.EXPENSE);
            case 1:
                return BudgetFragment.newInstance(BudgetType.INCOME);

            default: return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
