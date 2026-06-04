package com.example.livraisonapp.database;

import androidx.room.*;
import android.content.Context;
import com.example.livraisonapp.model.LivraisonEntity;

@Database(entities = {LivraisonEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract LivraisonDao livraisonDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "livraison_db"
            ).allowMainThreadQueries().build();
        }
        return instance;
    }
}
