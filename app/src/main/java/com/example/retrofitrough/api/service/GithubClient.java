package com.example.retrofitrough.api.service;

import com.example.retrofitrough.api.model.GithubRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface GithubClient {

    /*@GET("/users/{user}/repos")
    Call<List<GithubRepo>> reposForUser(@Path("user") String user);*/

    @GET
    Call<List<GithubRepo>> reposForUser(@Url String url);

}
