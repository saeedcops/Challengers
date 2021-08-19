package com.cops.challengers.view.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;
import com.cops.challengers.R;
import com.cops.challengers.model.room.Create;
import com.cops.challengers.model.room.Profile;
import com.cops.challengers.viewModel.SplashViewModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.facebook.Profile.getCurrentProfile;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends BaseFragment {

    @BindView(R.id.splash_fragment_image)
    AppCompatImageView splashFragmentImage;
    @BindView(R.id.splash_fragment_btn_offline)
    AppCompatButton splashFragmentBtnOffline;
    @BindView(R.id.splash_fragment_btn_sign_in_facebook)
    LoginButton splashFragmentBtnSignInFacebook;
    @BindView(R.id.splash_fragment_btn_sign_in_guest)
    AppCompatButton splashFragmentBtnSignInGuest;
    @BindView(R.id.splash_fragment_ll_bottom)
    LinearLayout splashFragmentLlBottom;
    @BindView(R.id.splash_fragment_lav_loading)
    LottieAnimationView splashFragmentLavLoading;
    private Animation animation;
    private NavController navController;
    private SplashViewModel splashViewModel;
    private String token, userId, password, fireToken;
    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor;
    private CallbackManager callbackManager;
    private String qsnLang;
    private boolean sound;
    private String question = "";
    private LoginResult loginResult;


    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_splash, container, false);
        sharedPreferences = getActivity().getSharedPreferences(
                "challengers", getActivity().MODE_PRIVATE);
        editor = sharedPreferences.edit();

        token = sharedPreferences.getString("token", "");
        qsnLang = sharedPreferences.getString("qsnLang", "ar");
        sound = sharedPreferences.getBoolean("sound", true);
        question = sharedPreferences.getString("questions", "");

        password = sharedPreferences.getString("password", "");
        userId = sharedPreferences.getString("user_id", "");


//        callbackManager = CallbackManager.Factory.create();

        ButterKnife.bind(this, view);

//        splashFragmentBtnSignInFacebook.setPermissions(Arrays.asList("email", "public_profile", "user_friends"));
//        splashFragmentBtnSignInFacebook.setFragment(this);

        // Callback registration
//        splashFragmentBtnSignInFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResul) {
//                // App code
//                Log.i("FBMSucs",loginResul.toString());
//                loginResult=loginResul;
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//                Log.i("FBMCans","Cancel");
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//                Log.i("FBMEX",exception.getMessage());
//            }
//        });

//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//                        Log.i("FBMSucs",loginResult.toString());
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                        Log.i("FBMCans","Cancel");
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                        Log.i("FBMEX",exception.getMessage());
//                    }
//                });




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setUpActivity();
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        splashFragmentImage.setAnimation(animation);
        navController = Navigation.findNavController(view);
        splashViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(SplashViewModel.class);
        generateFireToken();
        if (!userId.equals("")) {

            splashViewModel.generateToken(userId, password);
            splashFragmentLavLoading.setVisibility(View.VISIBLE);
            splashFragmentLlBottom.setVisibility(View.GONE);
            splashFragmentBtnOffline.setVisibility(View.GONE);

            splashViewModel.profileMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Profile>() {
                @Override
                public void onChanged(Profile profile) {

                    if (profile != null) {

                        editor.putString("token", splashViewModel.getToken());
                        editor.apply();
                        SplashFragmentDirections.ActionSplashFragmentToHomeFragment action = SplashFragmentDirections.actionSplashFragmentToHomeFragment();
                        action.setCoin(profile.getCoins());
                        action.setFlag(profile.getFlag());
                        action.setGem(profile.getGem());
                        action.setImage(profile.getImage());
                        action.setLevel(profile.getLevel());
                        action.setName(profile.getName());
                        navController.navigate(action);

                    } else {

                        if (question.equals("ok")) {

                            splashFragmentLavLoading.setVisibility(View.GONE);

                            splashFragmentBtnOffline.setVisibility(View.VISIBLE);

                        } else {
                            Toast.makeText(baseActivity, getString(R.string.download_questions_online), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }



    }

    private void generateFireToken() {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(baseActivity, "No Token", Toast.LENGTH_SHORT).show();
//                            fireToken = "eZHABgEWTB6qoL9z8aIPCK:APA91bHEImUnXAYk0H8F9savO-LRZQbWm92jumW4bjOsp5A6TmFNzUDsHGMAE40ObY9FDW6NI8PZVtHTqtwXiitphtxrzbUoY6lptUNq_6QtclAicJ3zI6BABnzoJ3I_gaWBtIfNWUPB";//                            generateFireToken();
//                            return;
                        }else

                        fireToken = task.getResult();

                    }
                });
    }

    private void setFacebook() {


        splashFragmentLavLoading.setVisibility(View.VISIBLE);
        splashFragmentLlBottom.setVisibility(View.GONE);
        splashFragmentBtnOffline.setVisibility(View.GONE);
        callbackManager = CallbackManager.Factory.create();
        splashFragmentBtnSignInFacebook.setPermissions(Arrays.asList("email", "user_friends", "public_profile"));
        splashFragmentBtnSignInFacebook.setFragment(this);
//        String facebookId = loginResult.getAccessToken().getUserId();

//        GraphRequest request = GraphRequest.newMyFriendsRequest(loginResult.getAccessToken(),
//                new GraphRequest.GraphJSONArrayCallback() {
//                    @Override
//                    public void onCompleted(JSONArray objects, GraphResponse response) {
//
//                        String[] friends;
//                        String image = "";
//                        image = "https://graph.facebook.com/" + facebookId + "/picture?width=200&height=150";
//
//                        Log.i("FbJSONArray",objects.toString());
//                        Log.i("FbResponse",response.getRawResponse());
//                        friends = new String[objects.length()];
////                        friends[0]="123456789";
//
//                        for (int i = 0; i < objects.length(); i++) {
//                            try {
//
//                                JSONObject object = objects.getJSONObject(i);
//                                String id = object.getString("id");
//                                friends[i] = id;
//                                Log.i("FbFrnd",friends[i]);
//
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        String flag = getContext().getResources().getConfiguration().locale.getCountry();
//                        String password = generateRandomPassword();
//                        Create create = new Create();
//                        create.setToken(fireToken);
//                        create.setFacebookId(facebookId);
//                        create.setFriend(friends);
//                        create.setFlag(flag);
//                        Log.i("FbFirTkn",fireToken);
//                        create.setFacebookImage(image);
//                        String name = "N";
//                        if (getCurrentProfile() != null) {
//                            name = getCurrentProfile().getName();
//
//                        }
//                        create.setFacebookName(name);
//                        create.setPassword(password);
//                        Log.i("FbName",name);
//                        Log.i("FbFr",friends.toString());
//                        Log.i("FbFr",friends.length+"");
//
//                        splashViewModel.createUser(create);
//
//                        splashViewModel.profileMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Profile>() {
//                            @Override
//                            public void onChanged(Profile profile) {
//
//                                if (profile != null) {
//
//                                    editor.putBoolean("facebook", true);
//                                    editor.putString("user_id", splashViewModel.getUserId());
//                                    editor.putInt("profile_id", profile.getId());
//                                    editor.putString("password", password);
//                                    editor.putString("token", splashViewModel.getToken());
//                                    editor.apply();
//                                    SplashFragmentDirections.ActionSplashFragmentToHomeFragment action = SplashFragmentDirections.actionSplashFragmentToHomeFragment();
//                                    action.setCoin(profile.getCoins());
//                                    action.setFlag(profile.getFlag());
//                                    action.setGem(profile.getGem());
//                                    action.setImage(profile.getImage());
//                                    action.setLevel(profile.getLevel());
//                                    action.setName(profile.getName());
//                                    navController.navigate(action);
//                                } else {
//
//                                    splashFragmentLavLoading.setVisibility(View.GONE);
//                                    splashFragmentLlBottom.setVisibility(View.VISIBLE);
////                                    splashFragmentBtnOffline.setVisibility(View.VISIBLE);
//
//                                    Toast.makeText(baseActivity, splashViewModel.getFacebookError(), Toast.LENGTH_LONG).show();
//                                }
//                            }
//                        });
//
//                    }
//                });
//
//        request.executeAsync();

//         Callback registration
        splashFragmentBtnSignInFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                String facebookId = loginResult.getAccessToken().getUserId();
                Log.i("FbToken",loginResult.getAccessToken().getToken());
                Log.i("FbPerm",loginResult.getRecentlyGrantedPermissions().toString());
                GraphRequest request = GraphRequest.newMyFriendsRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray objects, GraphResponse response) {

                        String[] friends;
                        String image = "";
                        image = "https://graph.facebook.com/" + facebookId + "/picture?width=200&height=150";

                        Log.i("FbJSONArray",objects.toString());
                        Log.i("FbResponse",response.getRawResponse());
                        friends = new String[objects.length()];

                        for (int i = 0; i < friends.length; i++) {
                            try {

                                JSONObject object = objects.getJSONObject(i);
                                String id = object.getString("id");
                                friends[i] = id;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        String flag = getContext().getResources().getConfiguration().locale.getCountry();
                        String password = generateRandomPassword();
                        Create create = new Create();
                        create.setToken(fireToken);
                        create.setFacebookId(facebookId);
                        create.setFriend(friends);

                        create.setFlag(flag);
                        create.setFacebookImage(image);
                        String name = "N";

                        if (getCurrentProfile() != null) {
                            name = getCurrentProfile().getName();

                        }
                        create.setFacebookName(name);
                        create.setPassword(password);


                        splashViewModel.createUser(create);

                        splashViewModel.profileMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Profile>() {
                            @Override
                            public void onChanged(Profile profile) {

                                if (profile != null) {

                                    editor.putBoolean("facebook", true);
                                    editor.putString("user_id", splashViewModel.getUserId());
                                    editor.putInt("profile_id", profile.getId());
                                    editor.putString("password", password);
                                    editor.putString("token", splashViewModel.getToken());
                                    editor.apply();
                                    SplashFragmentDirections.ActionSplashFragmentToHomeFragment action = SplashFragmentDirections.actionSplashFragmentToHomeFragment();
                                    action.setCoin(profile.getCoins());
                                    action.setFlag(profile.getFlag());
                                    action.setGem(profile.getGem());
                                    action.setImage(profile.getImage());
                                    action.setLevel(profile.getLevel());
                                    action.setName(profile.getName());
                                    navController.navigate(action);
                                } else {

                                    splashFragmentLavLoading.setVisibility(View.GONE);
                                    splashFragmentLlBottom.setVisibility(View.VISIBLE);
//                                    splashFragmentBtnOffline.setVisibility(View.VISIBLE);

                                    Toast.makeText(baseActivity, splashViewModel.getFacebookError(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                });

                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                splashFragmentLavLoading.setVisibility(View.GONE);
                splashFragmentLlBottom.setVisibility(View.VISIBLE);
                splashFragmentBtnOffline.setVisibility(View.VISIBLE);
                Log.i("FbCAc","Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                splashFragmentLavLoading.setVisibility(View.GONE);
                splashFragmentLlBottom.setVisibility(View.VISIBLE);
                splashFragmentBtnOffline.setVisibility(View.VISIBLE);
                Log.i("FbError",exception.getMessage());

                Toast.makeText(baseActivity, exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }

    private String generateRandomPassword() {
        // ASCII range - alphanumeric (0-9, a-z, A-Z)
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()_+-=abcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.splash_fragment_btn_offline, R.id.splash_fragment_btn_sign_in_facebook, R.id.splash_fragment_btn_sign_in_guest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.splash_fragment_btn_offline:

                SplashFragmentDirections.ActionSplashFragmentToCategoryFragment action = SplashFragmentDirections.actionSplashFragmentToCategoryFragment();
                action.setHome(false);

                action.setCoins(500);
                action.setMode(getString(R.string.offline));
                action.setQsnLang(qsnLang);
                action.setSound(sound);
                navController.navigate(action);

                break;
            case R.id.splash_fragment_btn_sign_in_facebook:
                setFacebook();
                break;
            case R.id.splash_fragment_btn_sign_in_guest:

                guestLogin();

                break;
        }
    }

    private void guestLogin() {

        splashFragmentLavLoading.setVisibility(View.VISIBLE);
        splashFragmentLlBottom.setVisibility(View.GONE);
        splashFragmentBtnOffline.setVisibility(View.GONE);

        String flag = getContext().getResources().getConfiguration().locale.getCountry();
        Create create = new Create();
        String password = generateRandomPassword();
        create.setToken(fireToken);
        create.setPassword(password);
        create.setFlag(flag);
        create.setFriend(new String[]{});
        create.setFacebookId("");
        splashViewModel.createUser(create);
        splashViewModel.profileMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {

                if (profile != null) {
                    editor.putString("user_id", splashViewModel.getUserId());
                    editor.putInt("profile_id", profile.getId());
                    editor.putString("password", password);
                    editor.putString("token", splashViewModel.getToken());
                    editor.apply();
                    SplashFragmentDirections.ActionSplashFragmentToHomeFragment action = SplashFragmentDirections.actionSplashFragmentToHomeFragment();
                    action.setCoin(profile.getCoins());
                    action.setFlag(profile.getFlag());
                    action.setGem(profile.getGem());
                    action.setImage(profile.getImage());
                    action.setLevel(profile.getLevel());
                    action.setName(profile.getName());
                    profile=null;
                    navController.navigate(action);
                } else {
                    splashViewModel.profileMutableLiveData.removeObservers(getViewLifecycleOwner());
                    splashFragmentLavLoading.setVisibility(View.GONE);
                    splashFragmentLlBottom.setVisibility(View.VISIBLE);
                    if (question.equals("ok"))
                        splashFragmentBtnOffline.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        getViewModelStore().clear();
        super.onDestroy();

    }
}
