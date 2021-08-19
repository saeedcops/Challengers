package com.cops.challengers.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cops.challengers.api.ApiService;
import com.cops.challengers.api.RetrofitClient;

import com.cops.challengers.model.room.Room;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultViewModel extends ViewModel {


    public MutableLiveData<Room> roomMutableLiveData = new MutableLiveData<>();
    private ApiService userService;
    @Override
    protected void onCleared() {
        super.onCleared();

    }

    public void deleteRoom(String token,int roomId){
        userService =
                RetrofitClient.createService(ApiService.class, token);
        userService.deleteRoom(true,roomId).enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                if (response.isSuccessful()) {

                }

            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {

            }
        });

    }
    public void getRoom(String token,int roomId){
        userService =
                RetrofitClient.createService(ApiService.class, token);
        userService.getRoomData("http://192.168.10.143:8080/api/room/room/"+roomId+"/").enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {

                if (response.isSuccessful()) {

                    roomMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {

            }
        });
    }


}
