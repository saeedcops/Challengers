package com.cops.challengers.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cops.challengers.api.ApiService;
import com.cops.challengers.api.RetrofitClient;
import com.cops.challengers.model.room.Profile;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteViewModel extends ViewModel {


    public MutableLiveData<Profile> userMutableLiveData = new MutableLiveData<>();



    public void deleteUser(String token,int profileId){

        ApiService userService =
                RetrofitClient.createService(ApiService.class, token);

        userService.deleteUser(profileId).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful()) {

                    userMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
            }
        });
    }

}
