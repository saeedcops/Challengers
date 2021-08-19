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

public class LevelDialog extends DialogFragment {


    @BindView(R.id.level_dialog_tv_level)
    TextView levelDialogTvLevel;
    private MediaPlayer coinsMedia = new MediaPlayer();
    private int level;

    public LevelDialog(int level) {

        this.level = level;

    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog_tranceparent));
        super.setupDialog(dialog, style);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        coinsMedia.reset();
        coinsMedia = MediaPlayer.create(getParentFragment().getContext(), R.raw.new_level);

        return inflater.inflate(R.layout.dialog_level, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        coinsMedia.start();
        levelDialogTvLevel.setText(level+"");

    }

    @OnClick({R.id.level_dialog_btn_exit, R.id.level_dialog_btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.level_dialog_btn_exit:
                getDialog().dismiss();
                break;
            case R.id.level_dialog_btn_ok:
                getDialog().dismiss();
                break;
        }
    }
}
