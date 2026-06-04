package com.example.livraisonapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import com.google.android.material.chip.Chip;
import android.content.res.ColorStateList;
import androidx.appcompat.app.AppCompatActivity;

import com.example.livraisonapp.R;

public class DetailLivraisonActivity extends AppCompatActivity {

    TextView txtCommande, txtClient, txtTel, txtAdresse, txtTotal, txtMode;
    Button btnCall, btnMap, btnModifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_livraison);

        txtCommande = findViewById(R.id.txtCommande);
        txtClient = findViewById(R.id.txtClient);
        txtTel = findViewById(R.id.txtTel);
        txtAdresse = findViewById(R.id.txtAdresse);
        txtTotal = findViewById(R.id.txtTotal);
        txtMode = findViewById(R.id.txtMode);

        btnCall = findViewById(R.id.btnCall);
        btnMap = findViewById(R.id.btnMap);
        btnModifier = findViewById(R.id.btnModifier);



        Intent i = getIntent();
        Chip chipEtat = findViewById(R.id.chipEtat);
        String etat = i.getStringExtra("etat");
        switch (etat != null ? etat : "") {
            case "LI":
                chipEtat.setText("Livrée");
                chipEtat.setChipBackgroundColor(ColorStateList.valueOf(getColor(R.color.status_green_bg)));
                chipEtat.setTextColor(getColor(R.color.status_green_text));
                break;
            case "EC":
                chipEtat.setText("En cours");
                chipEtat.setChipBackgroundColor(ColorStateList.valueOf(getColor(R.color.status_orange_bg)));
                chipEtat.setTextColor(getColor(R.color.status_orange_text));
                break;
            case "AL":
                chipEtat.setText("Annulée");
                chipEtat.setChipBackgroundColor(ColorStateList.valueOf(getColor(R.color.status_red_bg)));
                chipEtat.setTextColor(getColor(R.color.status_red_text));
                break;
        }


        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        int nocde = i.getIntExtra("nocde", 0);
        String client = i.getStringExtra("client");
        String tel = i.getStringExtra("telephone");
        String adresse = i.getStringExtra("adresse");
        String mode = i.getStringExtra("modepay");
        double total = i.getDoubleExtra("total", 0);
        Button btnUrgence = findViewById(R.id.btnUrgence);
        btnUrgence.setOnClickListener(v -> {

            Intent intent = new Intent(this, MessageActivity.class);

            intent.putExtra("nocde", nocde);
            intent.putExtra("client", client);
            intent.putExtra("telephone", tel);
            intent.putExtra("TOKEN", getIntent().getStringExtra("TOKEN"));

            startActivity(intent);
        });
        txtCommande.setText("Commande N° " + nocde);
        txtClient.setText(client);
        txtTel.setText(tel);
        txtAdresse.setText(adresse);
        txtMode.setText("Mode : " + mode);
        txtTotal.setText("Total : " + total + " DT");


        btnCall.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + tel));
            startActivity(callIntent);
        });


        btnMap.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + adresse);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });


        btnModifier.setOnClickListener(v -> {

            Intent intent = new Intent(this, ModifierLivraisonActivity.class);

            intent.putExtra("nocde", nocde);
            intent.putExtra("TOKEN", getIntent().getStringExtra("TOKEN"));

            startActivity(intent);
        });
    }
}
