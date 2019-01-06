package com.example.retrofitrough.api.service;

import com.example.retrofitrough.api.model.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

public interface UserClient {

    @Headers("Cache-Control: 3600")
    @POST("api/users")
    Call<User> createUser(@Body User user);

    @Multipart
    @POST("api/users")
    Call<ResponseBody> createUser(
//            @Part User user,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part avatar
    );

    @Headers({
            "Cache-Control: 3600",
            "User-Agent: Android"
    })
    @Multipart
    @POST("api/users")
    Call<ResponseBody> createUser(
            @Header("userheader") String userHeader,
            @PartMap Map<String, RequestBody> data,
            @Part MultipartBody.Part avatar
    );

    @Multipart
    @POST("api/users")
    Call<ResponseBody> createUser(
            @Part List<MultipartBody.Part> avatar
    );
}
