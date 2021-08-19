package com.cops.challengers.viewModel;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cops.challengers.api.ApiService;
import com.cops.challengers.api.RetrofitClient;
import com.cops.challengers.model.room.Create;
import com.cops.challengers.model.room.Profile;
import com.cops.challengers.model.room.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cops.challengers.api.RetrofitClient.getClient;

public class SplashViewModel extends ViewModel {

    private String facebookError="";
    public MutableLiveData<Profile> profileMutableLiveData = new MutableLiveData<>();
    private String userId;
    private String token;

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }


    public String getFacebookError() {
        return facebookError;
    }


    public void generateToken(String userId,String password){

        getClient().getToken(userId,password).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {

                if (response.isSuccessful()) {

                    token=response.body().getToken();

                    ApiService userService =
                            RetrofitClient.createService(ApiService.class, token);

                    userService.getProfile().enqueue(new Callback<Profile>() {
                        @Override
                        public void onResponse(Call<Profile> call, Response<Profile> response) {

                            if (response.isSuccessful()) {

                                profileMutableLiveData.setValue(response.body());
                            }else{
                                profileMutableLiveData.setValue(null);
                            }
                        }

                        @Override
                        public void onFailure(Call<Profile> call, Throwable t) {
                            profileMutableLiveData.setValue(null);
                        }
                    });

                }else{
                    profileMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                profileMutableLiveData.setValue(null);
            }
        });
    }


    public void createUser(Create create){


        getClient().createUser(create).enqueue(new Callback<Create>() {
            @Override
            public void onResponse(Call<Create> call, Response<Create> response) {


                if (response.isSuccessful()) {
                    Log.i("FbCrtUsrOK",response.message());
                    userId=response.body().getUserId();

                    generateToken(userId,create.getPassword());

                }else{
                    Log.i("FbCrtUsrN",response.message());
                    profileMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Create> call, Throwable t) {
                profileMutableLiveData.setValue(null);
                Log.i("FbCrtUsrER",t.getMessage());
            }
        });
    }

}
