package com.java.zhangshihang;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentPagerAdapter {
    List<String> list;
    List<Fragment> fragments = new ArrayList<>();

    public MyPagerAdapter(FragmentManager fm, List<String> list, List<Fragment> fragments) {
        super(fm);
        this.list = list;
        this.fragments = fragments;

    }
    public void set(List<String> list1, List<Fragment> fragments1)
    {
        list.clear();
        fragments.clear();
        for(int i=0;i<list1.size();i++)
        {
            list.add(list1.get(i));
            fragments.add(fragments1.get(i));
        }
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return fragments != null ? fragments.size() : 0;
    }
}
