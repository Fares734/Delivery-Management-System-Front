package com.example.livraisonapp.ui;

import android.os.Bundle;
import android.widget.*;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.livraisonapp.R;
import com.example.livraisonapp.api.MessageApi;
import com.example.livraisonapp.model.MessageRequest;
import com.example.livraisonapp.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {

    TextView txtCommande, txtClient, txtTel;
    EditText etDescription;
    Button btnSend;

    int nocde;
    String client;
    String tel;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        txtCommande = findViewById(R.id.txtCommande);
        txtClient = findViewById(R.id.txtClient);
        txtTel = findViewById(R.id.txtTel);
        etDescription = findViewById(R.id.etDescription);
        btnSend = findViewById(R.id.btnSend);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());


        txtCommande.setText("N° CMD : " + nocde);
        txtClient.setText(client);
        txtTel.setText(tel);


        com.google.android.material.textfield.TextInputEditText etDescription;

        Intent i = getIntent();

        nocde = i.getIntExtra("nocde", 0);
        client = i.getStringExtra("client");
        tel = i.getStringExtra("telephone");
        token = i.getStringExtra("TOKEN");

        txtCommande.setText("Commande : " + nocde);
        txtClient.setText("Client : " + client);
        txtTel.setText("Tel : " + tel);

        btnSend.setOnClickListener(v -> send());
    }

    private void send() {

        String desc = etDescription.getText().toString();

        if (desc.isEmpty()) {
            Toast.makeText(this, "Description obligatoire", Toast.LENGTH_SHORT).show();
            return;
        }

        MessageApi api = RetrofitClient.getInstance().create(MessageApi.class);

        MessageRequest req = new MessageRequest();
        req.nocde = nocde;
        req.client = client;
        req.telephone = Integer.parseInt(tel);
        req.description = desc;

        api.send("Bearer " + token, req)
                .enqueue(new Callback<Void>() {

                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(MessageActivity.this, "Message envoyé", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(MessageActivity.this, "Erreur réseau", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}