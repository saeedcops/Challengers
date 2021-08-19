package com.cops.challengers.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cops.challengers.api.ApiService;
import com.cops.challengers.api.RetrofitClient;
import com.cops.challengers.model.room.PlayersAnswer;
import com.cops.challengers.model.room.Profile;
import com.cops.challengers.model.room.Room;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableViewModel extends ViewModel {


    public MutableLiveData<Room> roomMutable=new MutableLiveData<>();


    private ApiService userService;


    public void takeGem(String token){

        userService =RetrofitClient.createService(ApiService.class, token);
        userService.takeGem(1).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
            }
        });
    }
    public void createRoom(String token,String category,int player1Prize,int player1Profile,int player2Profile,boolean isFacebook){

        category=category.toLowerCase();

      userService =RetrofitClient.createService(ApiService.class, token);
        userService.createRoom(category,player1Prize,player1Profile, player2Profile,isFacebook).enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {

                if (response.isSuccessful()) {

                    roomMutable.setValue(response.body());

                }else{
                    Log.i("RoomE",response.message());
                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                Log.i("RoomF",t.getMessage());
            }
        });
    }

    public void randomRoom(String token,int roomID) {
        userService =RetrofitClient.createService(ApiService.class, token);

        userService.randomRoom(true,roomID).enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                Log.i("RoomCr",response.message());
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                Log.i("RoomCE",t.getMessage());
            }
        });
    }
    public void updateRoom(String token,int player2Profile,int roomID) {
        userService =RetrofitClient.createService(ApiService.class, token);
        Log.i("RoomID out",roomID+"");
        userService.updateRoom(player2Profile,roomID).enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {

                if (response.isSuccessful()) {
                    Log.i("RoomID in",response.body().getId()+"");
                    roomMutable.setValue(response.body());
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

                    roomMutable.setValue(response.body());

                    userService.startRoom(true,roomId).enqueue(new Callback<Room>() {
                        @Override
                        public void onResponse(Call<Room> call, Response<Room> response) {
                        }

                        @Override
                        public void onFailure(Call<Room> call, Throwable t) {
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
            }
        });
    }


    public void setAnswer(PlayersAnswer answer) {

        userService.setAnswer(answer).enqueue(new Callback<PlayersAnswer>() {
            @Override
            public void onResponse(Call<PlayersAnswer> call, Response<PlayersAnswer> response) {

            }

            @Override
            public void onFailure(Call<PlayersAnswer> call, Throwable t) {
            }
        });
    }
    @Override
    protected void onCleared() {
        super.onCleared();

    }
}
