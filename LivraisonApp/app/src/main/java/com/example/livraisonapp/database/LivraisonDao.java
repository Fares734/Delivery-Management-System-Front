package com.example.livraisonapp.database;

import androidx.room.*;
import com.example.livraisonapp.model.LivraisonEntity;
import java.util.List;

@Dao
public interface LivraisonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LivraisonEntity> list);
    @Query("DELETE FROM livraisons")
    void clearAll();

    @Query("SELECT * FROM livraisons ORDER BY codePostal ASC")
    List<LivraisonEntity> getAll();

    @Query("UPDATE livraisons SET etatliv = :etat, synced = 0 WHERE nocde = :id")
    void updateEtat(int id, String etat);

    @Query("SELECT * FROM livraisons WHERE synced = 0 AND etatliv = 'LI'")
    List<LivraisonEntity> getNonSynced();
}
