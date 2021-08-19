package com.cops.challengers.view.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cops.challengers.R;
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

public class FacebookDialog extends DialogFragment {


    @BindView(R.id.facebook_dialog_btn_sign_in_facebook)
    LoginButton facebookDialogBtnSignInFacebook;
    private OnFacebookChange onSettingChange;
    private CallbackManager callbackManager;

    public FacebookDialog(OnFacebookChange onSettingChange) {
        this.onSettingChange = onSettingChange;
    }

    @OnClick({R.id.facebook_dialog_btn_exit, R.id.facebook_dialog_btn_sign_in_facebook, R.id.facebook_dialog_btn_later})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.facebook_dialog_btn_exit:
                getDialog().dismiss();
                break;
            case R.id.facebook_dialog_btn_sign_in_facebook:
                setFacebook();
                break;
            case R.id.facebook_dialog_btn_later:
                getDialog().dismiss();
                break;
        }
    }

    public interface OnFacebookChange {
        void getFToken(boolean facebook,String name,String image,String flag,String facebookId,String [] friends);
    }


    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialog_tranceparent));

        super.setupDialog(dialog, style);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_facebook, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


    }


    private void setFacebook() {

        callbackManager = CallbackManager.Factory.create();


        facebookDialogBtnSignInFacebook.setPermissions(Arrays.asList("email", "public_profile", "user_friends"));
        facebookDialogBtnSignInFacebook.setFragment(this);

        // Callback registration
        facebookDialogBtnSignInFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        String name="N";
                        if (getCurrentProfile() !=null) {
                            name=getCurrentProfile().getName();

                        }
                        Log.i("dataXN",name);
                        Log.i("dataXF",friends.toString());
                        String flag = getContext().getResources().getConfiguration().locale.getCountry();

                        onSettingChange.getFToken(true,name,image,flag,facebookId,friends);
                        getDialog().dismiss();
                    }
                });

                request.executeAsync();


            }

            @Override
            public void onCancel() {
                // App code
                onSettingChange.getFToken(false,null,null,null,null,null);
                Log.i("DaTAxx","CAn");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.i("DaTAxx",exception.getMessage());
                onSettingChange.getFToken(false,null,null,null,null,null);


            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
