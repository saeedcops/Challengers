package com.cops.challengers.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cops.challengers.api.ApiService;
import com.cops.challengers.api.RetrofitClient;
import com.cops.challengers.model.room.Profile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeViewModel extends ViewModel {


    public MutableLiveData<Profile> profileMutableLiveData = new MutableLiveData<>();
    private ApiService userService;

    public void createFacebookUser(String token,String name, String image, String flag, String facebookId, String[] friends){


         userService =
                RetrofitClient.createService(ApiService.class, token);

        Log.i("Tokenss",friends.length+"");
//        Log.i("Tokenss1",id);
        userService.setProfile(name,image,flag,facebookId,friends).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful()) {


                    profileMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });

    }


    public void addVideoCoins(String token){

         userService =
                RetrofitClient.createService(ApiService.class, token);

        userService.takeVideoCoins(1).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });

    }

    public void setOffline(){

        userService.setOffline().enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
            }
        });
    }


    public void getUserInfo(String token){

         userService =
                RetrofitClient.createService(ApiService.class, token);

        userService.getProfile().enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {

                if (response.isSuccessful()) {

                    profileMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });
    }

}
