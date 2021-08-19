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
import androidx.fragment.app.DialogFragment;

import com.cops.challengers.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WinDialog extends DialogFragment {


    @BindView(R.id.win_dialog_tv_left)
    TextView winDialogTvLeft;
    private String playerName;
    private OnWinChange onExitChange;

    @OnClick(R.id.win_dialog_btn_ok)
    public void onViewClicked() {
        onExitChange.sendToResult();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public interface OnWinChange {
        void sendToResult();
    }

    public WinDialog(String playerName, OnWinChange onExitChange) {
        this.onExitChange = onExitChange;
        this.playerName = playerName;
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog_tranceparent));

        super.setupDialog(dialog, style);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_win, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        MediaPlayer leftMedia = MediaPlayer.create(getContext(), R.raw.leave);
        winDialogTvLeft.setText(playerName+"\t"+getString(R.string.gived_up));
    }


}
