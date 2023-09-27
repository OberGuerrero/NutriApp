package com.example.nutriapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NutriAPI {
        @GET("https://api.github.com/users/public-apis/repos")
        Call<Respuesta> obtenerDatos(@Path("id") int id);
    }
