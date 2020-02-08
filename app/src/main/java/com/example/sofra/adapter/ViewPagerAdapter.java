package com.example.sofra.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentfragments;
    private List<String> titlefragment;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentfragments = new ArrayList<>();
        titlefragment = new ArrayList<>();
    }

    public void addPager(Fragment fragment, String title) {
        this.fragmentfragments.add(fragment);
        this.titlefragment.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentfragments.get(position);
    }

    @Override
    public int getCount() {
        return fragmentfragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titlefragment.get(position);
    }
}
