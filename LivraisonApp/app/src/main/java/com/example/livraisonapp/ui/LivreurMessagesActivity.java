package com.example.livraisonapp.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livraisonapp.R;
import com.example.livraisonapp.api.MessageApi;
import com.example.livraisonapp.model.MessageResponse;
import com.example.livraisonapp.utils.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivreurMessagesActivity extends AppCompatActivity {

    RecyclerView recycler;
    TextView txtCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livreur_messages);

        recycler = findViewById(R.id.recyclerMessages);
        txtCount  = findViewById(R.id.txtCount);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        String token = getSharedPreferences("app", MODE_PRIVATE)
                .getString("TOKEN", "");

        MessageApi api = RetrofitClient.getInstance().create(MessageApi.class);

        api.getMessages("Bearer " + token).enqueue(new Callback<List<MessageResponse>>() {

            @Override
            public void onResponse(Call<List<MessageResponse>> call, Response<List<MessageResponse>> response) {
                if (response.isSuccessful()) {
                    recycler.setAdapter(new MessageAdapter(response.body()));
                    txtCount.setText(response.body().size() + " messages");
                }
            }

            @Override
            public void onFailure(Call<List<MessageResponse>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
