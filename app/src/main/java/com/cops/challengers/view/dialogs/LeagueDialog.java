package com.cops.challengers.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cops.challengers.R;
import com.cops.challengers.adapters.LeagueAdapter;
import com.cops.challengers.model.room.League;
import com.cops.challengers.model.room.Profile;
import com.cops.challengers.viewModel.LeagueViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LeagueDialog extends DialogFragment implements LeagueAdapter.OnLeagueClick {


    @BindView(R.id.league_dialog_iv_league)
    AppCompatImageView leagueDialogIvLeague;
    @BindView(R.id.league_dialog_tv_league)
    TextView leagueDialogTvLeague;
    @BindView(R.id.league_dialog_tv_end)
    TextView leagueDialogTvEnd;
    @BindView(R.id.league_dialog_rv)
    RecyclerView leagueDialogRv;
    @BindView(R.id.league_dialog_progress)
    ProgressBar leagueDialogProgress;
    private int league ;
    private LeagueViewModel leagueViewModel;
    private LeagueAdapter leagueAdapter;
    private int profileId;
    private String token;
    private boolean isSet=false;


    public LeagueDialog(int league,int profileId,String token) {
        this.league = league;
        this.profileId=profileId;
        this.token=token;

    }


    @Override
    public void onDestroy() {
        getViewModelStore().clear();
        super.onDestroy();

    }
    @OnClick({R.id.league_dialog_btn_exit, R.id.league_dialog_btn_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.league_dialog_btn_exit:
                getDialog().dismiss();
                break;
            case R.id.league_dialog_btn_info:
                new LeagueInfoDialog().show(getParentFragmentManager(),"info");
                break;
        }
    }

    @Override
    public void leagueClicked(int userId) {
        isSet=false;

        leagueViewModel.getUserInfo(userId,token);

        leagueViewModel.profileMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                if (profile!=null && !isSet ) {

                    isSet=true;
                    new ProfileDialog(profile,true).show(getParentFragmentManager(),"profile");

                }
            }
        });

    }


    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog_tranceparent));
        super.setupDialog(dialog, style);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        leagueViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(LeagueViewModel.class);

        leagueViewModel.getLeague(token,league);
        return inflater.inflate(R.layout.dialog_league, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        leagueDialogRv.setLayoutManager(new LinearLayoutManager(getContext()));
        leagueViewModel.leagueMutableLiveData.observe(getViewLifecycleOwner(), new Observer<League>() {
            @Override
            public void onChanged(League league) {

                if (league != null) {

                    leagueAdapter=new LeagueAdapter(profileId,league.getProfiles(),LeagueDialog.this);
                    leagueDialogProgress.setVisibility(View.GONE);
                    leagueDialogTvLeague.setText(league.getName());

                    Glide.with(getContext()).load(league.getImage()).into(leagueDialogIvLeague);


                    leagueDialogRv.setAdapter(leagueAdapter);
                    leagueDialogRv.setVisibility(View.VISIBLE);
                    leagueDialogTvEnd.setText(getString(R.string.end_friday));

                }
            }
        });

    }


}
