package com.example.livraisonapp.api;

import com.example.livraisonapp.model.MessageRequest;
import com.example.livraisonapp.model.MessageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface MessageApi {

    @POST("/messages")
    Call<Void> send(
            @Header("Authorization") String token,
            @Body MessageRequest request
    );
    @GET("/messages")
    Call<List<MessageResponse>> getMessages(
            @Header("Authorization") String token
    );
    @POST("/messages/broadcast")
    Call<Void> sendToLivreurs(
            @Header("Authorization") String token,
            @Body MessageRequest request
    );
}
