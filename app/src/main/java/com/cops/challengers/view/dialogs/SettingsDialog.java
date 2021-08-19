package com.cops.challengers.view.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.DialogFragment;
import com.cops.challengers.R;
import com.cops.challengers.adapters.LangSpinnerAdapter;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.facebook.Profile.getCurrentProfile;

public class SettingsDialog extends DialogFragment {

    @BindView(R.id.settings_dialog_btn_sign_in_facebook)
    LoginButton settingsDialogBtnSignInFacebook;
    @BindView(R.id.settings_dialog_sp_lang)
    AppCompatSpinner settingsDialogSpLang;
    @BindView(R.id.settings_dialog_sp_question_lang)
    AppCompatSpinner settingsDialogSpQuestionLang;
    @BindView(R.id.settings_dialog_chk_sound)
    AppCompatCheckBox settingsDialogChkSound;
    @BindView(R.id.settings_dialog_chk_notify)
    AppCompatCheckBox settingsDialogChkNotify;
    private boolean sound,notifications,facebook;
    private String appLang,qsnLang;
    private LangSpinnerAdapter langSpinnerAdapter;
    private OnSettingChange onSettingChange;
    private CallbackManager callbackManager;



    public interface OnSettingChange{
        void getSettingData( boolean sound, boolean notifications,
                            boolean facebook, String appLang, String qsnLang);
        void getFacebookData(boolean facebook,String name,String image,String flag,String facebookId,String [] friends);
    }

    public SettingsDialog(boolean sound, boolean notifications, boolean facebook, String appLang, String qsnLang,OnSettingChange onSettingChange) {
        this.sound = sound;
        this.notifications = notifications;
        this.facebook = facebook;
        this.appLang = appLang;
        this.qsnLang = qsnLang;
        this.onSettingChange=onSettingChange;
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog_tranceparent));

        super.setupDialog(dialog, style);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        langSpinnerAdapter= new LangSpinnerAdapter(getContext());
        setView();

        onChange();

    }

    private void onChange() {

        settingsDialogChkNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                notifications=isChecked;
                onSettingChange.getSettingData(sound,notifications,facebook,appLang,qsnLang);
            }
        });

        settingsDialogChkSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                sound=isChecked;
                onSettingChange.getSettingData(sound,notifications,facebook,appLang,qsnLang);
            }
        });

        settingsDialogSpLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position==0) {
                    appLang="en";
                }else {
                    appLang="ar";
                }
                onSettingChange.getSettingData(sound,notifications,facebook,appLang,qsnLang);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        settingsDialogSpQuestionLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position==0) {
                    qsnLang="en";
                }else {
                    qsnLang="ar";
                }
                onSettingChange.getSettingData(sound,notifications,facebook,appLang,qsnLang);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setView() {

        if (facebook) {
            settingsDialogBtnSignInFacebook.setVisibility(View.GONE);
        }

        settingsDialogChkSound.setChecked(sound);
        settingsDialogChkNotify.setChecked(notifications);

        settingsDialogSpLang.setAdapter(langSpinnerAdapter);
        settingsDialogSpQuestionLang.setAdapter(langSpinnerAdapter);

        if (appLang.equals("ar"))
            settingsDialogSpLang.setSelection(1);
        else
            settingsDialogSpLang.setSelection(0);

        if (qsnLang.equals("en"))
            settingsDialogSpQuestionLang.setSelection(0);
        else
            settingsDialogSpQuestionLang.setSelection(1);

    }


    private void setFacebook() {

        callbackManager = CallbackManager.Factory.create();

        settingsDialogBtnSignInFacebook.setPermissions(Arrays.asList("email", "public_profile", "user_friends"));
        settingsDialogBtnSignInFacebook.setFragment(this);

        // Callback registration
        settingsDialogBtnSignInFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                String facebookId=loginResult.getAccessToken().getUserId();
                Log.i("FbToken",loginResult.getAccessToken().getToken());
                Log.i("FbPerm",loginResult.getRecentlyGrantedPermissions().toString());
                GraphRequest request = GraphRequest.newMyFriendsRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray objects, GraphResponse response) {

                        String [] friends;
                        String image="https://graph.facebook.com/" + facebookId + "/picture?width=200&height=150";
                        Log.i("dataXI",image);
                        friends=new String[objects.length()];
                        for (int i=0;i<objects.length();i++) {
                            try {

                                JSONObject object = objects.getJSONObject(i);
                                String id=object.getString("id");
                                friends[i]=id;
                                Log.i("FbdataXFN",id);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        String name="N";
                        if (getCurrentProfile() !=null) {
                            name=getCurrentProfile().getName();

                        }
                        Log.i("FbdataXN",name);
                        Log.i("FbdataXF",friends.length+"");
                        String flag = getContext().getResources().getConfiguration().locale.getCountry();

                        facebook=true;
                        onSettingChange.getFacebookData(facebook,name,image,flag,facebookId,friends);
                        getDialog().dismiss();


                    }
                });

                request.executeAsync();

            }

            @Override
            public void onCancel() {
                // App code
                facebook=false;
                onSettingChange.getFacebookData(facebook,null,null,null,null,null);

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                facebook=false;

                onSettingChange.getFacebookData(facebook,null,null,null,null,null);


            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick({R.id.settings_dialog_btn_exit, R.id.settings_dialog_btn_sign_in_facebook, R.id.settings_dialog_btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.settings_dialog_btn_exit:
                getDialog().dismiss();
                break;
            case R.id.settings_dialog_btn_sign_in_facebook:
                setFacebook();
                break;
            case R.id.settings_dialog_btn_logout:

//                TODO delete user & profile & GCM then the rest

                new ExitDialog(getContext().getString(R.string.delete_account)).show(getParentFragmentManager(),"delete");

//
                break;
        }
    }
}
