package com.cops.challengers.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.cops.challengers.R;
import com.cops.challengers.adapters.ViewPagerAdapter;

import com.cops.challengers.view.fragments.PrizeFragment;
import com.cops.challengers.view.fragments.RulesFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeagueInfoDialog extends DialogFragment {


    @BindView(R.id.league_info_dialog_tabLayout)
    TabLayout leagueInfoDialogTabLayout;
    @BindView(R.id.league_info_dialog_viewPager)
    ViewPager leagueInfoDialogViewPager;

    public LeagueInfoDialog() {


    }


    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog_tranceparent));
        super.setupDialog(dialog, style);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.dialog_league_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        if (leagueInfoDialogViewPager != null) {
            setUpViewPager(leagueInfoDialogViewPager);
        }

        leagueInfoDialogTabLayout.setupWithViewPager(leagueInfoDialogViewPager);


        leagueInfoDialogTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //here we get the selected tab and set it to a current view

                leagueInfoDialogViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new PrizeFragment(), getString(R.string.prizes));
        adapter.addFragment(new RulesFragment(), getString(R.string.rules));

        viewPager.setAdapter(adapter);
    }

    @OnClick(R.id.league_info_dialog_btn_exit)
    public void onViewClicked() {

        getDialog().dismiss();
    }
}
