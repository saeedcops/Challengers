package com.cops.challengers.view.dialogs;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.cops.challengers.R;
import com.cops.challengers.model.LeagueResult;
import com.cops.challengers.viewModel.LeagueResultViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cops.challengers.utils.Util.format;

public class LeagueResultDialog extends DialogFragment {


    @BindView(R.id.league_result_dialog_iv_league)
    AppCompatImageView leagueResultDialogIvLeague;
    @BindView(R.id.league_result_dialog_tv_league)
    TextView leagueResultDialogTvLeague;
    @BindView(R.id.league_result_dialog_tv_prize)
    TextView leagueResultDialogTvPrize;
    @BindView(R.id.league_result_dialog_tv_total)
    TextView leagueResultDialogTvTotal;
    @BindView(R.id.league_result_dialog_tv_next)
    TextView leagueResultDialogTvNext;
    @BindView(R.id.league_result_dialog_tv_rank)
    TextView leagueResultDialogTvRank;
    private LeagueResult leagueResult;
    private LeagueResultViewModel viewModel;
    private MediaPlayer coinsMedia=new MediaPlayer();


    public LeagueResultDialog(LeagueResult leagueResult) {
        this.leagueResult = leagueResult;
    }



    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {

        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog_tranceparent));
        super.setupDialog(dialog, style);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(LeagueResultViewModel.class);
        coinsMedia.reset();
        coinsMedia = MediaPlayer.create(getParentFragment().getContext(), R.raw.daily_coins_league);

        return inflater.inflate(R.layout.dialog_league_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        coinsMedia.start();
        viewModel.setLeaguePrize(leagueResult.getToken());

        Glide.with(getContext()).load(leagueResult.getImage()).into(leagueResultDialogIvLeague);
        leagueResultDialogTvLeague.setText(leagueResult.getLeague());
        leagueResultDialogTvRank.setText(leagueResult.getRank()+"");
        leagueResultDialogTvPrize.setText(leagueResult.getPrize()+" ");
        leagueResultDialogTvTotal.setText(format(leagueResult.getCoins()));
        leagueResultDialogTvNext.setText(leagueResult.getNextLeague());

    }


    @OnClick({R.id.league_result_dialog_btn_exit, R.id.league_result_dialog_btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.league_result_dialog_btn_exit:
                getDialog().dismiss();
                break;
            case R.id.league_result_dialog_btn_ok:
                getDialog().dismiss();
                break;
        }
    }
}
