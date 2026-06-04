package com.example.livraisonapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livraisonapp.R;
import com.example.livraisonapp.api.MessageApi;
import com.example.livraisonapp.model.MessageRequest;
import com.example.livraisonapp.model.MessageResponse;
import com.example.livraisonapp.utils.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesFragment extends Fragment {

    EditText etMessage;
    LinearLayout btnSend;
    RecyclerView recycler;
    String token;

    private Handler handler = new Handler();
    private Runnable refreshRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        recycler  = view.findViewById(R.id.recyclerMessages);
        etMessage = view.findViewById(R.id.etMessage);
        btnSend   = view.findViewById(R.id.btnSend);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        btnSend.setOnClickListener(v -> sendMessage());

        token = requireActivity()
                .getSharedPreferences("app", Context.MODE_PRIVATE)
                .getString("TOKEN", "");

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshRunnable = () -> {
            loadMessages();
            handler.postDelayed(refreshRunnable, 10000);
        };
        handler.post(refreshRunnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(refreshRunnable);
    }

    private void sendMessage() {

        String content = etMessage.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(getContext(), "Message vide", Toast.LENGTH_SHORT).show();
            return;
        }

        MessageApi api = RetrofitClient.getInstance().create(MessageApi.class);

        MessageRequest req = new MessageRequest();
        req.description = content;

        api.sendToLivreurs("Bearer " + token, req).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "✅ Message envoyé", Toast.LENGTH_SHORT).show();
                    etMessage.setText("");
                    loadMessages();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMessages() {

        MessageApi api = RetrofitClient.getInstance().create(MessageApi.class);

        api.getMessages("Bearer " + token).enqueue(new Callback<List<MessageResponse>>() {
            @Override
            public void onResponse(Call<List<MessageResponse>> call, Response<List<MessageResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recycler.setAdapter(new MessageAdapter(response.body()));

                    if (!response.body().isEmpty()) {
                        recycler.scrollToPosition(response.body().size() - 1);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MessageResponse>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}