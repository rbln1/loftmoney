package me.rubl.loftmoney.screens.screens.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import me.rubl.loftmoney.R;
import me.rubl.loftmoney.screens.screens.add_item.AddItemActivity;
import me.rubl.loftmoney.screens.screens.main.adapter.BudgetPagerAdapter;
import me.rubl.loftmoney.screens.screens.main.fragment.BudgetFragment;

public class MainActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    FloatingActionButton mFabMain;

    BudgetPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.view_pager);
        mPagerAdapter = new BudgetPagerAdapter(getSupportFragmentManager());

        mTabLayout = findViewById(R.id.tabs_main);

        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setText(R.string.expenses_title);
        mTabLayout.getTabAt(1).setText(R.string.incomes_title);

        mFabMain = findViewById(R.id.fab_main);
        mFabMain.setOnClickListener((view) ->  {
            final int activeFragmentIndex = mViewPager.getCurrentItem();
            BudgetFragment activeFragment = (BudgetFragment) getSupportFragmentManager().getFragments().get(activeFragmentIndex);
            startActivity(new Intent(getApplicationContext(), AddItemActivity.class)
                    .putExtra(BudgetFragment.KEY_BUDGET_TYPE, activeFragment.getBudgetType()));
        });
    }
}