package com.cops.challengers.viewModel;

import androidx.lifecycle.ViewModel;

import com.cops.challengers.api.ApiService;
import com.cops.challengers.api.RetrofitClient;
import com.cops.challengers.model.room.Profile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeagueResultViewModel extends ViewModel {


    public void setLeaguePrize(String token){

        ApiService userService =
                RetrofitClient.createService(ApiService.class, token);

        userService.leaguePrize(true).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
            }
        });
    }

}
