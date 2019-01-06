package com.example.retrofitrough.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.retrofitrough.R;
import com.example.retrofitrough.api.model.GithubRepo;
import com.example.retrofitrough.api.service.GithubClient;
import com.example.retrofitrough.background.BackGroundService;
import com.example.retrofitrough.ui.adapter.GithubRepoAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetActivity extends AppCompatActivity {
    private static final String TAG = "GetActivity";

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get);

        listView = findViewById(R.id.pagination_list);

        Retrofit.Builder retroBuilder = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retroBuilder.build();


        GithubClient client = retrofit.create(GithubClient.class);
//        Call<List<GithubRepo>> call = client.reposForUser("enggAkash");
        Call<List<GithubRepo>> call = client.reposForUser("https://api.github.com/users/enggAkash/repos");


        call.enqueue(new Callback<List<GithubRepo>>() {
            @Override
            public void onResponse(Call<List<GithubRepo>> call, Response<List<GithubRepo>> response) {

                List<GithubRepo> repos = response.body();

                listView.setAdapter(new GithubRepoAdapter(GetActivity.this, repos));

            }

            @Override
            public void onFailure(Call<List<GithubRepo>> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                Toast.makeText(GetActivity.this, "error :(", Toast.LENGTH_SHORT).show();
            }
        });

        /*Log.d(TAG, "onCreate: starting background service");
        Intent intent = new Intent(this, BackGroundService.class);
        startService(intent);*/

    }
}
