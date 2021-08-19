package com.cops.challengers.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cops.challengers.api.ApiService;
import com.cops.challengers.api.RetrofitClient;

import com.cops.challengers.model.room.League;
import com.cops.challengers.model.room.Profile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LeagueViewModel extends ViewModel {


    public MutableLiveData<League> leagueMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Profile> profileMutableLiveData = new MutableLiveData<>();



    public void getUserInfo(int userID,String token){
        profileMutableLiveData.setValue(null);
        ApiService userService =
                RetrofitClient.createService(ApiService.class, token);

        userService.getUserProfile(userID).enqueue(new Callback<Profile>() {
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



    public void getLeague(String token,int leagueId){

            ApiService userService =
                    RetrofitClient.createService(ApiService.class, token);


            userService.getLeague("http://192.168.10.143:8080/api/room/league/"+leagueId+"/").enqueue(new Callback<League>() {
                @Override
                public void onResponse(Call<League> call, Response<League> response) {

                    if (response.isSuccessful()) {
                        leagueMutableLiveData.setValue(response.body());
                    }

                }

                @Override
                public void onFailure(Call<League> call, Throwable t) {

                }
            });


    }


}
