package com.example.livraisonapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "livraisons")
public class LivraisonEntity {

    @PrimaryKey
    public int nocde;

    public String dateliv;
    public String etatliv;
    public String client;
    public Integer telephone;
    public String adresse;
    public String ville;
    public Integer codePostal;

    public Double total;
    public String modepay;

    public boolean synced;
}
