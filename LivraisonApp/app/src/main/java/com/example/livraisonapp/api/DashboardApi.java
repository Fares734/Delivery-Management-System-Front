package com.example.livraisonapp.api;

import com.example.livraisonapp.model.*;

import java.util.List;
import retrofit2.http.Query;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface DashboardApi {

    @GET("/dashboard/global")
    Call<DashboardStats> getGlobal(@Header("Authorization") String token);

    @GET("/dashboard/livreurs")
    Call<List<LivreurStats>> getLivreurs(@Header("Authorization") String token);

    @GET("/dashboard/clients")
    Call<List<ClientStats>> getClients(@Header("Authorization") String token);

    @GET("/dashboard/remarques")
    Call<List<Remarque>> getRemarques(@Header("Authorization") String token);


    @GET("/dashboard/livraisons/today")
    Call<List<LivraisonList>> getTodayLivraisons(@Header("Authorization") String token);
    @GET("/dashboard/livraisons/search")
    Call<List<LivraisonList>> search(
            @Header("Authorization") String token,
            @Query("from") String from,
            @Query("to") String to,
            @Query("livreur") String livreur
    );
    @GET("/dashboard/livreurs/list")
    Call<List<String>> getLesLivreurs(@Header("Authorization") String token);

}
