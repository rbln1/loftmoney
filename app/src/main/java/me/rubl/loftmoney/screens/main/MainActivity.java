package me.rubl.loftmoney.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import me.rubl.loftmoney.R;
import me.rubl.loftmoney.screens.add.AddItemActivity;
import me.rubl.loftmoney.screens.main.adapter.BudgetPagerAdapter;
import me.rubl.loftmoney.screens.main.fragment.BalanceFragment;
import me.rubl.loftmoney.screens.main.fragment.BudgetFragment;
import me.rubl.loftmoney.common.BudgetType;
import me.rubl.loftmoney.screens.main.model.MainFragmentModel;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    FloatingActionButton mFabMain;

    BudgetPagerAdapter mBudgetPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.activity_main_toolbar);
        mTabLayout = findViewById(R.id.activity_main_tab_layout);
        mViewPager = findViewById(R.id.activity_main_view_pager);
        mFabMain = findViewById(R.id.activity_main_fab);

        List<MainFragmentModel> mainFragmentModels = new ArrayList<>(3);
        mainFragmentModels.add(new MainFragmentModel(
                BudgetFragment.getInstance(BudgetType.EXPENSE), getString(R.string.expenses_title)
        ));
        mainFragmentModels.add(new MainFragmentModel(
                BudgetFragment.getInstance(BudgetType.INCOME), getString(R.string.incomes_title)
        ));
        mainFragmentModels.add(new MainFragmentModel(
                BalanceFragment.getInstance(), getString(R.string.balance_title)
        ));

        mBudgetPagerAdapter = new BudgetPagerAdapter(getSupportFragmentManager(), mainFragmentModels);
        mViewPager.setAdapter(mBudgetPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mBudgetPagerAdapter.getItem(position) instanceof BalanceFragment) mFabMain.hide();
                else mFabMain.show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mFabMain.setOnClickListener((view) ->  {
            final int activeFragmentIndex = mViewPager.getCurrentItem();
            Fragment activeFragment = mBudgetPagerAdapter.getItem(activeFragmentIndex);

            if (activeFragment instanceof BudgetFragment) {
                startActivity(new Intent(getApplicationContext(), AddItemActivity.class)
                        .putExtra(BudgetFragment.KEY_BUDGET_TYPE, ((BudgetFragment)activeFragment).getBudgetType()));
                overridePendingTransition(R.anim.fadeout, R.anim.fadein);
            }
        });
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey_blue));
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color. dark_grey_blue));
        mFabMain.hide();
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        mTabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color. colorPrimary));
        mFabMain.show();
    }
}