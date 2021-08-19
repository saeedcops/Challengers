package com.cops.challengers.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cops.challengers.api.ApiService;
import com.cops.challengers.api.RetrofitClient;
import com.cops.challengers.model.room.Friends;
import com.cops.challengers.model.room.Profile;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InviteViewModel extends ViewModel {


    public MutableLiveData<List<Profile>> userMutableLiveData = new MutableLiveData<>();



    public void getUserFriends(String token){

        ApiService userService =
                RetrofitClient.createService(ApiService.class, token);

        userService.getFriends().enqueue(new Callback<Friends>() {
            @Override
            public void onResponse(Call<Friends> call, Response<Friends> response) {

                if (response.isSuccessful()) {

                    userMutableLiveData.setValue(response.body().getFriends());
                }
            }

            @Override
            public void onFailure(Call<Friends> call, Throwable t) {
            }
        });
    }

}
