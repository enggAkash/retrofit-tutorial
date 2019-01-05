package com.example.retrofitrough.api.service;

import com.example.retrofitrough.api.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserClient {

    @POST("api/users")
    Call<User> createUser(@Body User user);

    @Multipart
    @POST("api/users")
    Call<ResponseBody> createUser(
//            @Part User user,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part avatar
    );
}
