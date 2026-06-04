package com.example.livraisonapp.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.livraisonapp.R;
import com.example.livraisonapp.api.LivraisonApi;
import com.example.livraisonapp.database.AppDatabase;
import com.example.livraisonapp.model.UpdateLivraisonRequest;
import com.example.livraisonapp.utils.RetrofitClient;
import com.google.android.material.card.MaterialCardView;
import retrofit2.*;

public class ModifierLivraisonActivity extends AppCompatActivity {

    TextView txtCommande;
    RadioGroup radioGroup;
    RadioButton radioLivree, radioAnnulee;
    LinearLayout cardLivree, cardAnnulee;
    MaterialCardView cardRemarque;
    com.google.android.material.textfield.TextInputEditText etRemarque;
    com.google.android.material.button.MaterialButton btnSave;

    int nocde;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_livraison);

        txtCommande  = findViewById(R.id.txtCommande);
        radioGroup   = findViewById(R.id.radioGroup);
        radioLivree  = findViewById(R.id.radioLivree);
        radioAnnulee = findViewById(R.id.radioAnnulee);
        cardLivree   = findViewById(R.id.cardLivree);
        cardAnnulee  = findViewById(R.id.cardAnnulee);
        cardRemarque = findViewById(R.id.cardRemarque);
        etRemarque   = findViewById(R.id.etRemarque);
        btnSave      = findViewById(R.id.btnSave);

        nocde = getIntent().getIntExtra("nocde", 0);
        token = getIntent().getStringExtra("TOKEN");
        txtCommande.setText("Commande N° " + nocde);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());


        cardLivree.setOnClickListener(v -> {
            radioLivree.setChecked(true);
            radioAnnulee.setChecked(false);
            cardLivree.setBackground(getDrawable(R.drawable.bg_option_selected_green));
            cardAnnulee.setBackground(getDrawable(R.drawable.bg_option_unselected));
            cardRemarque.setVisibility(View.GONE);
        });


        cardAnnulee.setOnClickListener(v -> {
            radioAnnulee.setChecked(true);
            radioLivree.setChecked(false);
            cardAnnulee.setBackground(getDrawable(R.drawable.bg_option_selected_red));
            cardLivree.setBackground(getDrawable(R.drawable.bg_option_unselected));
            cardRemarque.setVisibility(View.VISIBLE);
        });

        btnSave.setOnClickListener(v -> save());
    }

    private void save() {

        if (!radioLivree.isChecked() && !radioAnnulee.isChecked()) {
            Toast.makeText(this, "Choisir un état", Toast.LENGTH_SHORT).show();
            return;
        }

        if (radioLivree.isChecked()) {
            AppDatabase db = AppDatabase.getInstance(this);
            db.livraisonDao().updateEtat(nocde, "LI");
            Toast.makeText(this, "Livraison marquée livrée", Toast.LENGTH_SHORT).show();
            finish();

        } else {
            String remarque = etRemarque.getText().toString();
            if (remarque.isEmpty()) {
                Toast.makeText(this, "Remarque obligatoire", Toast.LENGTH_SHORT).show();
                return;
            }

            LivraisonApi api = RetrofitClient.getInstance().create(LivraisonApi.class);
            UpdateLivraisonRequest req = new UpdateLivraisonRequest();
            req.nocde    = nocde;
            req.etatliv  = "AL";
            req.remarque = remarque;

            api.updateLivraison("Bearer " + token, req).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        AppDatabase db = AppDatabase.getInstance(ModifierLivraisonActivity.this);
                        db.livraisonDao().updateEtat(nocde, "AL");
                        Toast.makeText(ModifierLivraisonActivity.this,
                                "Annulée envoyée avec succés", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ModifierLivraisonActivity.this,
                            "Erreur réseau", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}