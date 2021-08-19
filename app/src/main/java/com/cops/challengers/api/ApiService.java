package com.cops.challengers.api;


import com.cops.challengers.model.room.Create;
import com.cops.challengers.model.room.Friends;
import com.cops.challengers.model.room.League;
import com.cops.challengers.model.room.PlayersAnswer;
import com.cops.challengers.model.room.Profile;
import com.cops.challengers.model.room.Question;
import com.cops.challengers.model.room.Room;
import com.cops.challengers.model.room.Token;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface ApiService {
    @Headers({"Accept: application/json"})
    @POST("user/create/")
    Call<Create> createUser(@Body Create create);

    @POST("user/token/")
    @FormUrlEncoded
    Call<Token> getToken(@Field("user_id") String userId,
                         @Field("password") String password);

    @GET("user/me/")
    Call<Profile> getProfile();

    @GET("user/friends/")
    Call<Friends> getFriends();

    @PUT("user/me/")
    Call<Profile> setOffline();

    @PUT("user/me/")
    @FormUrlEncoded
    Call<Profile> takeGem(@Field("gem") int gem);

    @PUT("user/me/")
    @FormUrlEncoded
    Call<Profile> dailyCoins(@Field("daily_coins") boolean dailyCoins);

    @PUT("user/me/")
    @FormUrlEncoded
    Call<Profile> deleteUser(@Field("delete") int delete);

    @PUT("user/me/")
    @FormUrlEncoded
    Call<Profile> getUserProfile(@Field("user_id") int user_id);

    @PUT("user/me/")
    @FormUrlEncoded
    Call<Profile> leaguePrize(@Field("league_prize") boolean leaguePrize);

    @PUT("user/me/")
    @FormUrlEncoded
    Call<Profile> takeVideoCoins(@Field("video") int video);

    @PUT("user/me/")
    @FormUrlEncoded
    Call<Profile> setProfile(@Field("name") String name,
                             @Field("image") String image,
                             @Field("flag") String flag);

    @PUT("user/me/")
    @FormUrlEncoded
    Call<Profile> setProfile(@Field("name") String name,
                             @Field("image") String image,
                             @Field("flag") String flag,
                             @Field("facebook_id") String facebookId,
                             @Field("send_friend") String[] sendFriend);

    @POST("room/room/")
    @FormUrlEncoded
    Call<Room> createRoom(@Field("category") String category,
                          @Field("player1_prize") int player1Prize,
                          @Field("player1_profile") int player1Profile,
                          @Field("player2_profile") int player2Profile,
                          @Field("is_facebook") boolean isFacebook);

    @PUT("room/room/")
    @FormUrlEncoded
    Call<Room> updateRoom(@Field("player2_profile") int player2Profile,
                          @Field("id") int id);

    @PUT("room/room/")
    @FormUrlEncoded
    Call<Room> deleteRoom(@Field("finished") boolean finished,
                          @Field("id") int id);

    @PUT("room/room/")
    @FormUrlEncoded
    Call<Room> startRoom(@Field("started") boolean started,
                         @Field("id") int id);

    @PUT("room/room/")
    @FormUrlEncoded
    Call<Room> randomRoom(@Field("is_random") boolean isRandom,
                          @Field("id") int id);
//

    @GET("room/question/")
    Call<List<Question>> getQuestions();

    @GET
    Call<Room> getRoomData(@Url String url);

    @GET
    Call<League> getLeague(@Url String url);


    @POST("room/answer/")
    Call<PlayersAnswer> setAnswer(@Body PlayersAnswer answer);


}
