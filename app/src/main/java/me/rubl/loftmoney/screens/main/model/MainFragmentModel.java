package me.rubl.loftmoney.screens.main.model;

import androidx.fragment.app.Fragment;

public class MainFragmentModel {

    private Fragment fragment;
    private String title;

    public MainFragmentModel(Fragment fragment, String title) {
        this.fragment = fragment;
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public String getTitle() {
        return title;
    }
}
