package com.example.retrofitrough.api.service;

import com.example.retrofitrough.api.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserClient {

    @POST("/api/users")
    Call<User> createUser(@Body User user);

}
