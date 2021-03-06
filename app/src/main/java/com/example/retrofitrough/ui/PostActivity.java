package com.example.retrofitrough.ui;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.retrofitrough.BuildConfig;
import com.example.retrofitrough.R;
import com.example.retrofitrough.api.model.User;
import com.example.retrofitrough.api.service.UserClient;
import com.example.retrofitrough.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity {

    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    private static final int PICK_IMAGE_FROM_GALLERY = 2;
    private EditText nameEt;
    private EditText jobEt;
    private EditText descriptionEt;
    private Button uploadAvtar;
    private Button createAccountBtn;
    private Uri avatarUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        nameEt = findViewById(R.id.name_et);
        jobEt = findViewById(R.id.job_et);
        descriptionEt = findViewById(R.id.description_et);
        createAccountBtn = findViewById(R.id.create_account_btn);
        uploadAvtar = findViewById(R.id.upload_avtar);

        uploadAvtar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                startActivityForResult(Intent.createChooser(intent, "Choose Avtar"), PICK_IMAGE_FROM_GALLERY);
            }
        });

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User(nameEt.getText().toString(), jobEt.getText().toString());

                if (avatarUri == null)
                    sendNetworkRequest(user);
                else
                    uploadFile(user, avatarUri);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_REQUEST_CODE);
    }

    private void sendNetworkRequest(User user) {

        //Create Okhttp Client
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Log only if app in development mode
        if (BuildConfig.DEBUG) {
            okhttpBuilder.addInterceptor(loggingInterceptor);
        }

        // Create Retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpBuilder.build());

        Retrofit retrofit = builder.build();

        // Get Client & Call Object for the request
        UserClient userClient = retrofit.create(UserClient.class);
        Call<User> call = userClient.createUser(user);

        // Execute Network Request
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

    private void uploadFile(User user, Uri fileUri) {

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (BuildConfig.DEBUG)
            okHttpClientBuilder.addInterceptor(interceptor);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClientBuilder.build());

        Retrofit retrofit = builder.build();

        UserClient client = retrofit.create(UserClient.class);

        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("client", createPartFromString("jsdfj323"));
        partMap.put("secret", createPartFromString("!@Dds392"));

        if (!TextUtils.isEmpty(descriptionEt.getText().toString()))
            partMap.put("description", createPartFromString(descriptionEt.getText().toString()));

        Call<ResponseBody> call = client.createUser(
                "this is user header",
                partMap,
                prepareFilePart("photo", fileUri));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(PostActivity.this, "Yeah", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PostActivity.this, "Shit!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = FileUtil.getFile(this, fileUri);

        RequestBody requestBody = RequestBody.create(
                MediaType.parse(getContentResolver().getType(fileUri)),
                file);

        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case READ_EXTERNAL_STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                }
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // when single file
            avatarUri = data.getData();

            // when allowed multiple selection
//            ClipData clipData = data.getClipData();
//            ArrayList<Uri> fileUris = new ArrayList<>();
//            for (int i=0; i<clipData.getItemCount(); i++) {
//                fileUris.add(clipData.getItemAt(i).getUri());
//            }
//            uploadFile(null, fileUris.get(0));
        }
    }
}
