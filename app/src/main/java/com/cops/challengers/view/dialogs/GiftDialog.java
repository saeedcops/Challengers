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
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import com.cops.challengers.R;
import com.cops.challengers.viewModel.DailyCoinsViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GiftDialog extends DialogFragment {


    @BindView(R.id.gift_dialog_tv_time)
    TextView tvTime;

    @BindView(R.id.gift_dialog_btn_ok)
    AppCompatButton giftDialogBtnOk;
    private DailyCoinsViewModel dailyCoinsViewModel;
    private String token;
    private GiftDialogTakeCoins giftDialogTakeCoins;
    private MediaPlayer coinsMedia= new MediaPlayer();

    public interface GiftDialogTakeCoins {
        void giftDialogCoins();
    }

    public GiftDialog(String token,GiftDialogTakeCoins giftDialogTakeCoins) {

        this.token=token;
        this.giftDialogTakeCoins=giftDialogTakeCoins;
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
        coinsMedia = MediaPlayer.create(getParentFragment().getContext(), R.raw.daily_coins_league);

        return inflater.inflate(R.layout.dialog_gift, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        dailyCoinsViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(DailyCoinsViewModel.class);
        coinsMedia.start();

    }

    @OnClick({ R.id.gift_dialog_btn_ok, R.id.gift_dialog_btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.gift_dialog_btn_ok:
                dailyCoinsViewModel.dailyCoins(token);
                giftDialogTakeCoins.giftDialogCoins();
                getDialog().dismiss();
                break;
            case R.id.gift_dialog_btn_exit:
                dailyCoinsViewModel.dailyCoins(token);
                giftDialogTakeCoins.giftDialogCoins();
                getDialog().dismiss();
                break;
        }
    }
}
