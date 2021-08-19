package com.cops.challengers.view.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cops.challengers.R;
import com.cops.challengers.adapters.ResultAdapter;
import com.cops.challengers.model.room.Profile;
import com.cops.challengers.model.room.Room;
import com.cops.challengers.view.dialogs.ProfileDialog;
import com.cops.challengers.viewModel.ResultViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cops.challengers.utils.Util.format;


/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends BaseFragment {


    @BindView(R.id.result_fragment_result)
    TextView resultFragmentResult;
    @BindView(R.id.result_fragment_image_sender)
    CircleImageView resultFragmentImageSender;
    @BindView(R.id.result_fragment_image_receiver)
    CircleImageView resultFragmentImageReceiver;
    @BindView(R.id.result_fragment_tv_sender_name)
    TextView resultFragmentTvSenderName;
    @BindView(R.id.result_fragment_tv_receiver_name)
    TextView resultFragmentTvReceiverName;
    @BindView(R.id.result_fragment_rv)
    RecyclerView resultFragmentRv;
    @BindView(R.id.result_fragment_sender_ccp)
    CountryCodePicker resultFragmentSenderCcp;
    @BindView(R.id.result_fragment_receiver_ccp)
    CountryCodePicker resultFragmentReceiverCcp;
    @BindView(R.id.result_fragment_win)
    TextView resultFragmentWin;
    @BindView(R.id.result_fragment_win_coins)
    TextView resultFragmentWinCoins;
    @BindView(R.id.result_fragment_iv_coins)
    CircleImageView resultFragmentIvCoins;
    @BindView(R.id.result_fragment_pb)
    ProgressBar progressBar;
    private NavController navController;
    private ResultViewModel resultViewModel;
    private int roomID, level;
    private ResultAdapter resultAdapter;
    private MediaPlayer applauseMedia, loserMedia;
    private int userId, profileId;
    private SharedPreferences sharedPreferences;
    private String token, flag;
    private BroadcastReceiver mMessageReceiver;
    private Profile user1;
    private Profile user2;

    public ResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                if (intent.getStringExtra("type").equals("MyData")) {


                } else {

                    int coins = intent.getIntExtra("coins", 0);
                    int room = intent.getIntExtra("room", 1);
                    String image = intent.getStringExtra("image");
                    String name = intent.getStringExtra("name");
                    String category = intent.getStringExtra("category");
                    ResultFragmentDirections.ActionResultFragmentToNotificationDialog action = ResultFragmentDirections.actionResultFragmentToNotificationDialog();
                    action.setBet(coins);
                    action.setRoomId(room);
                    action.setImage(image);
                    action.setName(name);
                    action.setFlag(flag);
                    action.setLevel(level);
                    action.setCategory(category);
                    action.setQsnLang(ResultFragmentArgs.fromBundle(getArguments()).getQsnLang());
                    action.setSound(ResultFragmentArgs.fromBundle(getArguments()).getSound());
                    action.setUserId(ResultFragmentArgs.fromBundle(getArguments()).getUserId());
                    action.setProfileId(ResultFragmentArgs.fromBundle(getArguments()).getProfileId());
                    action.setMode("Accept");


                    try {
                        navController.navigate(action);
                    } catch (IllegalArgumentException e) {

                    }
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("MyData"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sharedPreferences = getActivity().getSharedPreferences(
                "challengers", getActivity().MODE_PRIVATE);
        token = sharedPreferences.getString("token", null);

        // SETTING ADS
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AdView mAdView = view.findViewById(R.id.result_adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ButterKnife.bind(this, view);
        setUpActivity();
        roomID = ResultFragmentArgs.fromBundle(getArguments()).getRoomID();
        userId = ResultFragmentArgs.fromBundle(getArguments()).getUserId();
        profileId = ResultFragmentArgs.fromBundle(getArguments()).getProfileId();
        resultViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ResultViewModel.class);
        navController = Navigation.findNavController(view);

        resultViewModel.getRoom(token, roomID);

        applauseMedia = MediaPlayer.create(getContext(), R.raw.applause);
        loserMedia = MediaPlayer.create(getContext(), R.raw.loser);



        resultViewModel.roomMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Room>() {
            @Override
            public void onChanged(Room room) {
                if (room != null) {

                    setData(room);

                }
            }
        });


    }

    private void setData(Room room) {
        user1 = room.getPlayer1();
        user2 = room.getPlayer2();

        progressBar.setVisibility(View.GONE);
        resultFragmentReceiverCcp.setVisibility(View.VISIBLE);
        resultFragmentSenderCcp.setVisibility(View.VISIBLE);
        resultFragmentTvReceiverName.setVisibility(View.VISIBLE);
        resultFragmentTvSenderName.setVisibility(View.VISIBLE);

        if (room.getPlayer1Profile().equals(userId)) {
            flag = room.getPlayer1().getFlag();
            level = room.getPlayer1().getLevel();



            if (ResultFragmentArgs.fromBundle(getArguments()).getWin().equals(getString(R.string.you_win))) {
                if (ResultFragmentArgs.fromBundle(getArguments()).getSound())
                    applauseMedia.start();

//                resultFragmentBtnAgain.setVisibility(View.GONE);
                resultFragmentWinCoins.setText(format(room.getPlayer1Prize() * 2 ));
                resultFragmentIvCoins.setVisibility(View.VISIBLE);


                resultFragmentWin.setTextColor(getResources().getColor(R.color.colorLightGreen));
                resultFragmentWin.setText(getString(R.string.you_win));


            } else {

                if (room.getPlayer1Score() > room.getPlayer2Score()) {
                    resultFragmentWin.setTextColor(getResources().getColor(R.color.colorLightGreen));
                    resultFragmentWin.setText(getString(R.string.you_win));
                    if (ResultFragmentArgs.fromBundle(getArguments()).getSound())
                        applauseMedia.start();

                    resultFragmentWinCoins.setText(format(room.getPlayer1Prize() * 2 ));
                    resultFragmentIvCoins.setVisibility(View.VISIBLE);



                } else if (room.getPlayer1Score() < room.getPlayer2Score()) {
                    resultFragmentWin.setTextColor(getResources().getColor(R.color.colorRed));
                    resultFragmentWin.setText(getString(R.string.you_loss));
                    if (ResultFragmentArgs.fromBundle(getArguments()).getSound())

                        loserMedia.start();
                } else {

                    resultFragmentWin.setTextColor(getResources().getColor(R.color.colorDivider));
                    resultFragmentWin.setText(getString(R.string.draw));
                    if (ResultFragmentArgs.fromBundle(getArguments()).getSound())

                        loserMedia.start();
                }
            }
        } else {
            flag = room.getPlayer2().getFlag();
            level = room.getPlayer2().getLevel();
            if (ResultFragmentArgs.fromBundle(getArguments()).getWin().equals(getString(R.string.you_win))) {

                if (ResultFragmentArgs.fromBundle(getArguments()).getSound())
                    applauseMedia.start();

                resultFragmentWinCoins.setText(format(room.getPlayer2Prize() * 2 ));
                resultFragmentIvCoins.setVisibility(View.VISIBLE);
//                resultFragmentBtnAgain.setVisibility(View.GONE);

                resultFragmentWin.setTextColor(getResources().getColor(R.color.colorLightGreen));
                resultFragmentWin.setText(getString(R.string.you_win));

            } else {

                if (room.getPlayer2Score() > room.getPlayer1Score()) {
                    resultFragmentWin.setTextColor(getResources().getColor(R.color.colorLightGreen));
                    resultFragmentWin.setText(getString(R.string.you_win));
                    if (ResultFragmentArgs.fromBundle(getArguments()).getSound())
                        applauseMedia.start();

                    resultFragmentWinCoins.setText(format(room.getPlayer2Prize() * 2 ));
                    resultFragmentIvCoins.setVisibility(View.VISIBLE);



                } else if (room.getPlayer2Score() < room.getPlayer1Score()) {
                    resultFragmentWin.setTextColor(getResources().getColor(R.color.colorRed));
                    resultFragmentWin.setText(getString(R.string.you_loss));
                    if (ResultFragmentArgs.fromBundle(getArguments()).getSound())

                        loserMedia.start();
                } else {

                    resultFragmentWin.setTextColor(getResources().getColor(R.color.colorDivider));
                    resultFragmentWin.setText(getString(R.string.draw));
                    if (ResultFragmentArgs.fromBundle(getArguments()).getSound())

                        loserMedia.start();
                }
            }
        }

        resultFragmentResult.setText(room.getPlayer1Score() + " \t - \t " + room.getPlayer2Score());
        resultFragmentTvSenderName.setText(room.getPlayer1().getName());
        resultFragmentTvReceiverName.setText(room.getPlayer2().getName());

        Glide.with(getContext()).load(room.getPlayer1().getImage()).into(resultFragmentImageSender);

        resultFragmentSenderCcp.setCountryForNameCode(room.getPlayer1().getFlag());
        Glide.with(getContext()).load(room.getPlayer2().getImage()).into(resultFragmentImageReceiver);
        resultFragmentReceiverCcp.setCountryForNameCode(room.getPlayer2().getFlag());

        resultAdapter = new ResultAdapter(room.getPlayersAnswer(), ResultFragmentArgs.fromBundle(getArguments()).getQsnLang(), 1, profileId);


        resultFragmentRv.setLayoutManager(new LinearLayoutManager(getContext()));
        resultFragmentRv.setAdapter(resultAdapter);

    }

    @OnClick({R.id.result_fragment_image_sender, R.id.result_fragment_image_receiver, R.id.result_fragment_btn_home})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.result_fragment_image_sender:
                if (user1 != null)
                    new ProfileDialog(user1, true).show(getChildFragmentManager(), "profile");
                break;
            case R.id.result_fragment_image_receiver:
                if (user2 != null)
                    new ProfileDialog(user2, true).show(getChildFragmentManager(), "profile");
                break;
            case R.id.result_fragment_btn_home:

                resultViewModel.deleteRoom(token, roomID);


                navController.navigate(R.id.action_resultFragment_to_homeFragment);
                break;
        }
    }

    @Override
    public void onBack() {
        resultViewModel.deleteRoom(token, roomID);
        super.onBack();
    }

    @Override
    public void onDestroy() {
        getViewModelStore().clear();
        super.onDestroy();


    }
}
