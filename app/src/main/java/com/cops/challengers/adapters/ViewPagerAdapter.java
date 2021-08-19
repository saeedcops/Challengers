package com.cops.challengers.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    //list of Fragments to add fragment class on it
    private final List<Fragment> mFragmentList=new ArrayList<>();

    //List of String to add Fragment title name to show in WordMeaningActivity
    private final List<String> mFragmentTitleList=new ArrayList<>();

    //conestractor
   public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    //method to add Fragment
    public void addFragment(Fragment fr, String string){
        mFragmentList.add(fr);
        mFragmentTitleList.add(string);
    }

    @NonNull

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }


    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Nullable
    //and this method returns the title of Fragment List
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
