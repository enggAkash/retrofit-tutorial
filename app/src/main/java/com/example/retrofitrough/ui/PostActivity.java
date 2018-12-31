package com.example.retrofitrough.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.retrofitrough.R;
import com.example.retrofitrough.api.model.User;
import com.example.retrofitrough.api.service.UserClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity {

    private EditText nameEt;
    private EditText jobEt;
    private Button createAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        nameEt = findViewById(R.id.name_et);
        jobEt = findViewById(R.id.job_et);
        createAccountBtn = findViewById(R.id.create_account_btn);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(nameEt.getText().toString(), jobEt.getText().toString());

                sendNetworkRequest(user);
            }
        });

    }

    private void sendNetworkRequest(User user) {

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        UserClient userClient = retrofit.create(UserClient.class);

        Call<User> call = userClient.createUser(user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User userResponse = response.body();
                Toast.makeText(PostActivity.this, "Success, Id = " + userResponse.getId() + " Created: " + userResponse.getCreatedAt(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(PostActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
