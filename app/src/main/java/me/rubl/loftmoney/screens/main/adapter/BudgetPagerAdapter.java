package me.rubl.loftmoney.screens.main.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

import me.rubl.loftmoney.screens.main.model.MainFragmentModel;

public class BudgetPagerAdapter extends FragmentPagerAdapter {

    private List<MainFragmentModel> mainFragmentModels;

    public BudgetPagerAdapter(FragmentManager fm, List<MainFragmentModel> mainFragmentModelList) {
        super(fm);

        this.mainFragmentModels = mainFragmentModelList;
    }

    @Override
    public Fragment getItem(int position) {
        return mainFragmentModels.get(position).getFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mainFragmentModels.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return mainFragmentModels.size();
    }

}
