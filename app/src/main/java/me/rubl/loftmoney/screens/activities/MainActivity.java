package me.rubl.loftmoney.screens.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import me.rubl.loftmoney.R;
import me.rubl.loftmoney.screens.adapters.BudgetPagerAdapter;

public class MainActivity extends AppCompatActivity {

    TabLayout mTabLayout;
    ViewPager mViewPager;
    BudgetPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.view_pager);
        mPagerAdapter = new BudgetPagerAdapter(getSupportFragmentManager());

        mTabLayout = findViewById(R.id.tabs);

        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setText(R.string.expenses_title);
        mTabLayout.getTabAt(1).setText(R.string.incomes_title);
    }

}