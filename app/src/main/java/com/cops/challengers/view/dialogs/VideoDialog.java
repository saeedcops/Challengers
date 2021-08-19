package com.cops.challengers.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;

import com.cops.challengers.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class VideoDialog extends DialogFragment {

    @BindView(R.id.video_dialog_btn_ok)
    AppCompatButton videoDialogBtnOk;
    private OnGiftVideo onGiftVideo;

    private RewardedAd rewardedAd;

    public interface OnGiftVideo {
        void giftReward();
    }

    public VideoDialog(OnGiftVideo onGiftVideo,RewardedAd rewardedAd) {
        this.onGiftVideo = onGiftVideo;
        this.rewardedAd=rewardedAd;
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog_tranceparent));
        super.setupDialog(dialog, style);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.dialog_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if(rewardedAd!=null)
            videoDialogBtnOk.setVisibility(View.VISIBLE);
        else
            generateAD();

    }

    private void generateAD() {

        FullScreenContentCallback fullScreenContentCallback =
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {

                        // Code to be invoked when the ad showed full screen content.
                        videoDialogBtnOk.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {

                        rewardedAd = null;
                        // Code to be invoked when the ad dismissed full screen content.
                        videoDialogBtnOk.setVisibility(View.GONE);
                        generateAD();
                    }
                };

        RewardedAd.load(
                getActivity(),
                "ca-app-pub-8148071716960089/3725800474",
                new AdRequest.Builder().build(),
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedAd ad) {
                        videoDialogBtnOk.setVisibility(View.VISIBLE);
                        rewardedAd = ad;
                        rewardedAd.setFullScreenContentCallback(fullScreenContentCallback);
                    }
                });
    }

    private void onDisplayButtonClicked() {
        if (rewardedAd != null) {
            rewardedAd.show(
                    getActivity(),
                    new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            videoDialogBtnOk.setVisibility(View.GONE);
                            onGiftVideo.giftReward();
                            generateAD();
                        }
                    });
        }
    }
    @OnClick({R.id.video_dialog_btn_exit, R.id.video_dialog_btn_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.video_dialog_btn_exit:
                getDialog().dismiss();
                break;
            case R.id.video_dialog_btn_ok:
                onDisplayButtonClicked();

                break;
        }
    }

}
