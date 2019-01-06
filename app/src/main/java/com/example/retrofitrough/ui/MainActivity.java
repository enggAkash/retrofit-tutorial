package com.example.retrofitrough.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.retrofitrough.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void retrofitGet(View view) {
        startActivity(new Intent(this, GetActivity.class));
    }

    public void retrofitPost(View view) {
        startActivity(new Intent(this, PostActivity.class));
    }

    public void retrofitDownload(View view) {
        startActivity(new Intent(this, DownloadActivity.class));
    }
}
