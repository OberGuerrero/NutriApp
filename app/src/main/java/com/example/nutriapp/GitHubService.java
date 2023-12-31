package com.example.nutriapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubService {
  @GET("users/{user}/repos")
  Call<Respuesta> listRepos(@Path("user") String user);
}
