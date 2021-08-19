package com.cops.challengers.api;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;


import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit=null;
    private static final String BASE_URL="http://192.168.10.143:8080/api/";


    public synchronized static ApiService getClient(){
        if (retrofit==null) {


            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
//                    .client()
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(
            Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            httpClient.addInterceptor(interceptor);

            builder.client(httpClient.build());
            retrofit = builder.build();

        }


        return retrofit.create(serviceClass);
    }

    public static WebSocket getWebSocket(String token, WebSocketListener listener,int roomId ){
        Log.i("onFailurecalled",token);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("ws://192.168.10.143:8080/ws/room/"+roomId+"/")
                .header("authorization","Token "+ token)
                .build();

        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
//        client.dispatcher().cancelAll();
        return ws;

    }


    private static class AuthenticationInterceptor implements Interceptor {

        private String authToken;

        public AuthenticationInterceptor(String token) {
            this.authToken = token;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();

            Request.Builder builder = original.newBuilder()
                    .header("Authorization","Token "+ authToken);

            Request request = builder.build();
            return chain.proceed(request);
        }
    }
}
