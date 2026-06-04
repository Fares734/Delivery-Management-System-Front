package com.example.livraisonapp.api;

import com.example.livraisonapp.model.Livraison;
import com.example.livraisonapp.model.LivraisonList;
import com.example.livraisonapp.model.UpdateLivraisonRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface LivraisonApi {

    @GET("/livraisons")
    Call<List<Livraison>> getLivraisons(
            @Header("Authorization") String token
    );

    @PUT("/livraisons")
    Call<Void> updateLivraison(
            @Header("Authorization") String token,
            @Body UpdateLivraisonRequest request
    );
}
