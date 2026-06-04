package com.example.livraisonapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.example.livraisonapp.model.UpdateLivraisonRequest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.example.livraisonapp.database.AppDatabase;
import com.example.livraisonapp.model.LivraisonEntity;
import com.example.livraisonapp.R;
import com.example.livraisonapp.api.LivraisonApi;
import com.example.livraisonapp.model.Livraison;
import com.example.livraisonapp.utils.RetrofitClient;

import java.util.List;

import retrofit2.*;

public class LivreurActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livreur);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button btnSync = findViewById(R.id.btnSync);
        btnSync.setOnClickListener(v -> syncData());
        Button btnMessages = findViewById(R.id.btnMessages);

        btnMessages.setOnClickListener(v -> {
            startActivity(new Intent(this, LivreurMessagesActivity.class));
        });

        String token = getIntent().getStringExtra("TOKEN");


        LivraisonApi api = RetrofitClient.getInstance().create(LivraisonApi.class);

        api.getLivraisons("Bearer " + token).enqueue(new Callback<List<Livraison>>() {
            @Override
            public void onResponse(Call<List<Livraison>> call, Response<List<Livraison>> response) {

                List<Livraison> apiList = response.body();


                List<LivraisonEntity> localList = new java.util.ArrayList<>();

                for (Livraison l : apiList) {

                    LivraisonEntity e = new LivraisonEntity();

                    e.nocde = l.nocde;
                    e.dateliv = l.dateliv;
                    e.etatliv = l.etatliv;

                    e.client = l.client;
                    e.telephone = l.telephone;
                    e.adresse = l.adresse;

                    e.ville = l.ville;
                    e.codePostal = l.codePostal;


                    e.total = l.total;
                    e.modepay = l.modepay;

                    e.synced = true;

                    localList.add(e);
                }


                AppDatabase db = AppDatabase.getInstance(LivreurActivity.this);
                db.livraisonDao().clearAll();
                db.livraisonDao().insertAll(localList);


                List<LivraisonEntity> data = db.livraisonDao().getAll();


                LivraisonsAdapter adapter = new LivraisonsAdapter(convert(data), token);
                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onFailure(Call<List<Livraison>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_livreur, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        if (item.getItemId() == R.id.logout) {


            getSharedPreferences("app", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();


            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();

        AppDatabase db = AppDatabase.getInstance(this);
        List<LivraisonEntity> data = db.livraisonDao().getAll();

        LivraisonsAdapter adapter =
                new LivraisonsAdapter(convert(data), getIntent().getStringExtra("TOKEN"));

        recyclerView.setAdapter(adapter);
    }
    private void syncData() {

        AppDatabase db = AppDatabase.getInstance(this);
        List<LivraisonEntity> list = db.livraisonDao().getNonSynced();

        if (list.isEmpty()) {
            Toast.makeText(this, "Rien à synchroniser", Toast.LENGTH_SHORT).show();
            return;
        }

        LivraisonApi api = RetrofitClient.getInstance().create(LivraisonApi.class);
        String token = getIntent().getStringExtra("TOKEN");

        for (LivraisonEntity e : list) {

            UpdateLivraisonRequest req = new UpdateLivraisonRequest();
            req.nocde = e.nocde;
            req.etatliv = "LI";

            api.updateLivraison("Bearer " + token, req)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                            if (response.isSuccessful()) {


                                e.synced = true;
                                db.livraisonDao().insertAll(java.util.List.of(e));
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }

        Toast.makeText(this, "Synchronisation lancée", Toast.LENGTH_SHORT).show();
    }
    private List<Livraison> convert(List<LivraisonEntity> list) {

        List<Livraison> res = new java.util.ArrayList<>();


        for (LivraisonEntity e : list) {

            Livraison l = new Livraison();

            l.nocde = e.nocde;
            l.dateliv = e.dateliv;
            l.etatliv = e.etatliv;

            l.client = e.client;
            l.telephone = e.telephone;
            l.adresse = e.adresse;

            l.ville = e.ville;
            l.codePostal = e.codePostal;

            l.total = e.total;
            l.modepay = e.modepay;

            res.add(l);
        }

        return res;
    }

}