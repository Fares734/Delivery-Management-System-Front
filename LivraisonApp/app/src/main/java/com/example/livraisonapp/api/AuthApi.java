package com.example.livraisonapp.api;

import com.example.livraisonapp.model.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);
}
