package com.cops.challengers.viewModel;
import androidx.lifecycle.ViewModel;

import com.cops.challengers.api.ApiService;
import com.cops.challengers.api.RetrofitClient;
import com.cops.challengers.model.room.Profile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends ViewModel {


    public void saveData(String token,String image,String flag,String name){

        ApiService userService =
                RetrofitClient.createService(ApiService.class, token);

        userService.setProfile(name,image,flag).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {


            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });

    }



}
