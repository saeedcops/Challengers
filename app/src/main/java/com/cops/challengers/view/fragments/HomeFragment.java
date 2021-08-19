package com.cops.challengers.view.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.cops.challengers.R;
import com.cops.challengers.adapters.HomeAdapter;
import com.cops.challengers.model.LeagueResult;
import com.cops.challengers.model.room.Profile;
import com.cops.challengers.view.MainActivity;
import com.cops.challengers.view.dialogs.FacebookDialog;
import com.cops.challengers.view.dialogs.GiftDialog;
import com.cops.challengers.view.dialogs.InviteDialog;
import com.cops.challengers.view.dialogs.LeagueDialog;
import com.cops.challengers.view.dialogs.LeagueResultDialog;
import com.cops.challengers.view.dialogs.LevelDialog;
import com.cops.challengers.view.dialogs.ProfileDialog;
import com.cops.challengers.view.dialogs.SettingsDialog;
import com.cops.challengers.view.dialogs.VideoDialog;
import com.cops.challengers.viewModel.HomeViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.hbb20.CountryCodePicker;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cops.challengers.utils.Util.format;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements HomeAdapter.HomeAdapterClick, ProfileDialog.OnProfileChange, SettingsDialog.OnSettingChange, VideoDialog.OnGiftVideo, FacebookDialog.OnFacebookChange, InviteDialog.OnInviteClicked, GiftDialog.GiftDialogTakeCoins {


    @BindView(R.id.home_fragment_civ_user)
    CircleImageView homeFragmentCivUser;

    @BindView(R.id.home_fragment_tv_name)
    TextView homeFragmentTvName;
    @BindView(R.id.home_fragment_tv_coin)
    TextView homeFragmentTvCoin;
    @BindView(R.id.home_fragment_tv_trick)
    TextView homeFragmentTvTrick;
    @BindView(R.id.home_fragment_rv)
    RecyclerView homeFragmentRv;
    @BindView(R.id.home_fragment_civ_user_settings)
    ImageView homeFragmentCivUserSettings;

    @BindView(R.id.home_fragment_tv_level)
    TextView homeFragmentTvLevel;
    @BindView(R.id.home_fragment_ccp)
    CountryCodePicker homeFragmentCcp;
    @BindView(R.id.home_fragment_lav_ad)
    LottieAnimationView homeFragmentLavAd;
    private HomeAdapter adapter = new HomeAdapter(this);
    private NavController navController;
    private HomeViewModel homeViewModel;
    private int coins,gem, level, profileId;
    private String name, image, flag,formatCoins;

    private ProfileDialog profileDialog;
    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor;
    private boolean sound, notification, facebook;
    private String appLang, qsnLang;
    private SettingsDialog settingsDialog;
    private int league,lastLevel;
    private String token = "";
    private Integer userID;
    private BroadcastReceiver mMessageReceiver;
    private RewardedAd rewardedAd;



    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public void onStart() {
        super.onStart();

        homeFragmentCcp.setVisibility(View.GONE);
        homeFragmentTvName.setVisibility(View.GONE);
        homeFragmentTvLevel.setVisibility(View.GONE);
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Extract data included in the Intent
                if (intent.getStringExtra("type").equals("MyData")) {

                    LeagueResult leagueResult = new LeagueResult();

                    leagueResult.setCoins(intent.getIntExtra("coins", 0));
                    leagueResult.setImage(intent.getStringExtra("image"));
                    leagueResult.setLeague(intent.getStringExtra("league"));
                    leagueResult.setNextLeague(intent.getStringExtra("next"));
                    leagueResult.setPrize(intent.getIntExtra("prize", 0));
                    leagueResult.setRank(intent.getIntExtra("rank", 1));
                    leagueResult.setToken(token);
                    LeagueResultDialog leagueResultDialog=new LeagueResultDialog(leagueResult);
                    leagueResultDialog.setCancelable(false);
                    leagueResultDialog.show(getChildFragmentManager(), "league");

                } else if (intent.getStringExtra("type").equals("Daily")) {

                    GiftDialog giftDialog=new GiftDialog(token,HomeFragment.this);
                    giftDialog.setCancelable(false);
                    giftDialog.show(getChildFragmentManager(), "gift");
                } else {

                    int coins = intent.getIntExtra("coins", 0);
                    int room = intent.getIntExtra("room", 1);
                    String image = intent.getStringExtra("image");
                    String name = intent.getStringExtra("name");
                    String category = intent.getStringExtra("category");

                    HomeFragmentDirections.ActionHomeFragmentToNotificationDialog action = HomeFragmentDirections.actionHomeFragmentToNotificationDialog();
                    action.setBet(coins);
                    action.setRoomId(room);
                    action.setImage(image);
                    action.setName(name);
                    action.setFlag(flag);
                    action.setLevel(level);
                    action.setCategory(category);
                    action.setQsnLang(qsnLang);
                    action.setSound(sound);
                    action.setUserId(userID);
                    action.setProfileId(profileId);
                    action.setMode("Accept");
                    action.setToken(token);

                    try {
                        navController.navigate(action);
                    }catch (IllegalArgumentException e){
                    }


                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        homeFragmentCcp.setVisibility(View.GONE);
        homeFragmentTvName.setVisibility(View.GONE);
        homeFragmentTvLevel.setVisibility(View.GONE);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("MyData"));

        //Video AD
        generateAD();
//        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
//            @Override
//            public void onRewardedAdLoaded() {
//
//                homeFragmentLavAd.playAnimation();
//            }
//
//            @Override
//            public void onRewardedAdFailedToLoad(LoadAdError adError) {
//                homeFragmentLavAd.cancelAnimation();
//
//            }
//        };
//        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
    }


    @Override
    public void onBack() {

        homeViewModel.setOffline();
        super.onBack();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //Video AD
        generateAD();

        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setUpActivity();

        navController = Navigation.findNavController(view);
        setPreference();
        setSentData();
        homeViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(HomeViewModel.class);
        homeViewModel.getUserInfo(token);


        setData();

        homeFragmentRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        homeFragmentRv.setAdapter(adapter);


//        rewardedAd = new RewardedAd(getActivity(),
//                "ca-app-pub-8148071716960089/3725800474");
//        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
//            @Override
//            public void onRewardedAdLoaded() {
//                // Ad successfully loaded.
//                homeFragmentLavAd.playAnimation();
//
//            }
//
//            @Override
//            public void onRewardedAdFailedToLoad(LoadAdError adError) {
//                // Ad failed to load.
//                homeFragmentLavAd.cancelAnimation();
//            }
//        };
//        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);


    }

    private void generateAD() {

        FullScreenContentCallback fullScreenContentCallback =
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        homeFragmentLavAd.cancelAnimation();
                        // Code to be invoked when the ad showed full screen content.
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        homeFragmentLavAd.cancelAnimation();
                        rewardedAd = null;
                        // Code to be invoked when the ad dismissed full screen content.
                    }
                };

        RewardedAd.load(
                getActivity(),
                "ca-app-pub-8148071716960089/3725800474",
                new AdRequest.Builder().build(),
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedAd ad) {
                        homeFragmentLavAd.playAnimation();
                        rewardedAd = ad;
                        rewardedAd.setFullScreenContentCallback(fullScreenContentCallback);
                    }
                });
    }
    private void setPreference() {

        sharedPreferences = getActivity().getSharedPreferences(
                "challengers", getActivity().MODE_PRIVATE);
        editor = sharedPreferences.edit();


        if (sharedPreferences.getString("appLang", null) == null) {

//            if(Locale.getDefault().getDisplayLanguage().equals("العربية")){
//
//                SaveData(getActivity(),"lang","ar");
//
//            }else{
//
//                SaveData(getActivity(),"lang","en");
//            }
            editor.putString("appLang", Locale.getDefault().getLanguage());
            editor.putString("qsnLang", "ar");
            editor.putInt("lastLevel",1);
            editor.apply();
        }


        lastLevel = sharedPreferences.getInt("lastLevel", 1);
        profileId = sharedPreferences.getInt("profile_id", 0);
        token = sharedPreferences.getString("token", null);
        appLang = sharedPreferences.getString("appLang", null);
        qsnLang = sharedPreferences.getString("qsnLang", null);
        sound = sharedPreferences.getBoolean("sound", true);
        notification = sharedPreferences.getBoolean("notification", true);
        facebook = sharedPreferences.getBoolean("facebook", false);



        settingsDialog = new SettingsDialog(sound, notification, facebook, appLang, qsnLang, this);

    }

    private void setSentData() {

        level = HomeFragmentArgs.fromBundle(getArguments()).getLevel();
        image = HomeFragmentArgs.fromBundle(getArguments()).getImage();
        Log.i("IMAGES",image);
        name = HomeFragmentArgs.fromBundle(getArguments()).getName();
        flag = HomeFragmentArgs.fromBundle(getArguments()).getFlag();
        coins = HomeFragmentArgs.fromBundle(getArguments()).getCoin();
        gem= HomeFragmentArgs.fromBundle(getArguments()).getGem();
        Glide.with(getContext()).load(image).into(homeFragmentCivUser);
        formatCoins=format(coins);
        homeFragmentCcp.setCountryForNameCode(flag);
        homeFragmentTvName.setText(name);
        homeFragmentTvCoin.setText(formatCoins);
        homeFragmentTvTrick.setText(format(HomeFragmentArgs.fromBundle(getArguments()).getGem()));
        homeFragmentTvLevel.setText(String.valueOf(level));

        homeFragmentCcp.setVisibility(View.VISIBLE);
        homeFragmentTvName.setVisibility(View.VISIBLE);
        homeFragmentTvLevel.setVisibility(View.VISIBLE);
    }

    private void setData() {


        homeViewModel.profileMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile user) {



                if (user != null) {
                    homeFragmentCcp.setVisibility(View.VISIBLE);
                    homeFragmentTvName.setVisibility(View.VISIBLE);
                    homeFragmentTvLevel.setVisibility(View.VISIBLE);

                    profileDialog = new ProfileDialog(token, user, HomeFragment.this);

                    profileId = user.getId();
                    userID = user.getUser();
                    image = user.getImage();
                    Log.i("IMAGES",image);
                    name = user.getName();
                    flag = user.getFlag();
                    level = user.getLevel();
                    coins = user.getCoins();
                    gem= user.getGem();
                    league = user.getMyLeague();
                    Glide.with(getContext()).load(user.getImage()).into(homeFragmentCivUser);
                    homeFragmentCcp.setCountryForNameCode(user.getFlag());
                    homeFragmentTvName.setText(user.getName());
                    formatCoins=format(coins);
                    homeFragmentTvCoin.setText(formatCoins);
                    homeFragmentTvTrick.setText(format(user.getGem()));
                    homeFragmentTvLevel.setText(String.valueOf(user.getLevel()));

                    if(user.getDailyCoins()){
                        GiftDialog giftDialog=new GiftDialog(token,HomeFragment.this);
                        giftDialog.setCancelable(false);
                        giftDialog.show(getChildFragmentManager(), "gift");
                    }

                    if(level>lastLevel){
                        editor.putInt("lastLevel",level);
                        editor.apply();
                        new LevelDialog(level).show(getChildFragmentManager(),"level");
                    }

                    if(user.getLeaguePrize()){

                        LeagueResult leagueResult = new LeagueResult();

                        leagueResult.setCoins(user.getlCoins());
                        leagueResult.setImage(user.getlImage());
                        leagueResult.setLeague(user.getlLeague());
                        leagueResult.setNextLeague(user.getlNext());
                        leagueResult.setPrize(user.getlPrize());
                        leagueResult.setRank(user.getlRank());
                        leagueResult.setToken(token);

                        LeagueResultDialog leagueResultDialog=new LeagueResultDialog(leagueResult);
                        leagueResultDialog.setCancelable(false);
                        leagueResultDialog.show(getChildFragmentManager(), "league");
                    }


                }
            }
        });
    }


    @OnClick({R.id.home_fragment_ib_league, R.id.home_fragment_civ_user_settings, R.id.home_fragment_civ_user, R.id.home_fragment_iv_coin, R.id.home_fragment_iv_trick, R.id.home_fragment_civ_group, R.id.home_fragment_lav_ad})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_fragment_civ_user:
                profileDialog.show(getChildFragmentManager(), "profile");

                break;
            case R.id.home_fragment_ib_league:

                new LeagueDialog(league, profileId, token).show(getChildFragmentManager(), "league");
                break;
            case R.id.home_fragment_iv_coin:
                //  homeFragmentLavGift.playAnimation();
                break;
            case R.id.home_fragment_iv_trick:
                break;
            case R.id.home_fragment_civ_group:

                if (!facebook) {

                    new FacebookDialog(this).show(getChildFragmentManager(), "facebook");

                } else {

                    if(coins>=500)
                    new InviteDialog(true,coins, token, this).show(getChildFragmentManager(), "invite");
                    else
                        Toast.makeText(getContext(), getString(R.string.no_enough_coins), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.home_fragment_lav_ad:
                homeFragmentLavAd.cancelAnimation();

                new VideoDialog(this,rewardedAd).show(getChildFragmentManager(), "video");
                break;
            case R.id.home_fragment_civ_user_settings:

                settingsDialog.show(getChildFragmentManager(), "settings");
                break;
        }
    }

    @Override
    public void itemClicked(int position, String title) {

        if (position == 1 && !facebook) {

            new FacebookDialog(this).show(getChildFragmentManager(), "facebook");

        } else {
            if (coins >= 500) {


                HomeFragmentDirections.ActionHomeFragmentToCategoryFragment action = HomeFragmentDirections.actionHomeFragmentToCategoryFragment();
                action.setCoins(coins);
                action.setUserID(userID);
                action.setProfileId(profileId);
                action.setMode(title);
                action.setImage(image);
                action.setFlag(flag);
                action.setName(name);
                action.setLevel(level);
                action.setQsnLang(qsnLang);
                action.setSound(sound);


                navController.navigate(action);

            } else {
                Toast.makeText(getContext(), getString(R.string.no_enough_coins), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void getProfileData(String image, String flag, String name) {

        if (!image.equals("")) {

            this.image = image;
            this.name = name;
            this.flag = flag;

            Glide.with(getContext()).load(image).into(homeFragmentCivUser);
            homeFragmentCcp.setCountryForNameCode(flag);
            homeFragmentTvName.setText(name);
        }
    }

    @Override
    public void getSettingData(boolean sound, boolean notification, boolean facebook,
                               String appLang, String qsnLang) {

        this.sound = sound;
        this.qsnLang = qsnLang;
        editor = sharedPreferences.edit();
        editor.putString("appLang", appLang);
        editor.putString("qsnLang", qsnLang);
        editor.putBoolean("sound", sound);
        editor.putBoolean("notification", notification);
        editor.putBoolean("facebook", facebook);
        editor.apply();

        if (!this.appLang.equals(appLang)) {

            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();
        }


    }

    @Override
    public void getFacebookData(boolean facebook, String name, String image, String flag, String facebookId, String[] friends) {

        this.facebook = facebook;

        editor = sharedPreferences.edit();
        editor.putBoolean("facebook", facebook);
        editor.apply();

        if (facebook) {

            homeViewModel.createFacebookUser(token, name, image, flag, facebookId, friends);

            setData();

        }
    }


    @Override
    public void giftReward() {
        coins += 250;
        gem+=1;
        formatCoins=format(coins);
        homeFragmentTvCoin.setText(formatCoins);
        homeFragmentTvTrick.setText(format(gem));

        homeViewModel.addVideoCoins(token);
    }

    @Override
    public void onDestroy() {
        getViewModelStore().clear();
        super.onDestroy();

    }

    @Override
    public void getFToken(boolean facebook, String name, String image, String flag, String facebookId, String[] friends) {

        this.facebook = facebook;

        editor = sharedPreferences.edit();
        editor.putBoolean("facebook", facebook);
        editor.apply();


        if (facebook) {


            homeViewModel.createFacebookUser(token, name, image, flag, facebookId, friends);

            setData();

        }
    }

    @Override
    public void getProfileId(int id, List<String> categories,int coins) {

        HomeFragmentDirections.ActionHomeFragmentToTableFragment action = HomeFragmentDirections.actionHomeFragmentToTableFragment();
        action.setBet(coins);
        action.setCategory(categories.toString());
        action.setMode(getString(R.string.playfriend));
        action.setUserID(userID);
        action.setProfileId(profileId);
        action.setImage(image);
        action.setFlag(flag);
        action.setName(name);
        action.setLevel(level);
        action.setQsnLang(qsnLang);
        action.setSound(sound);
        action.setFriendId(id);

        navController.navigate(action);
//        homeViewModel.createRoom(token, categories.toString(), coins, userID, id, true);
    }

    @Override
    public void giftDialogCoins() {
        coins+=1000;
        formatCoins=format(coins);
        homeFragmentTvCoin.setText(formatCoins);
    }
}
