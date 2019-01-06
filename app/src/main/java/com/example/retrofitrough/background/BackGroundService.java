package com.example.retrofitrough.background;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.retrofitrough.api.model.GithubRepo;
import com.example.retrofitrough.api.service.GithubClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackGroundService extends IntentService {
    private static final String TAG = "BackGroundService";

    public BackGroundService() {
        super("BackGroundService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: ");

        Retrofit.Builder retroBuilder = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retroBuilder.build();


        GithubClient client = retrofit.create(GithubClient.class);
        Call<List<GithubRepo>> call = client.reposForUser("enggAkash");

        Response<List<GithubRepo>> response;
        try {
            response = call.execute();
            Log.d(TAG, "onHandleIntent: Retrofit success");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "onHandleIntent: REtrofit failed");
        }
    }
}
