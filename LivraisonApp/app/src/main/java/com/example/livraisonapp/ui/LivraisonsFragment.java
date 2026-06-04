package com.example.livraisonapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livraisonapp.R;
import com.example.livraisonapp.api.DashboardApi;
import com.example.livraisonapp.model.LivraisonList;
import com.example.livraisonapp.utils.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivraisonsFragment extends Fragment {

    RecyclerView recycler;
    Spinner spinner;
    List<LivraisonList> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_livraisons, container, false);

        recycler = view.findViewById(R.id.recyclerLivraisons);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        spinner = view.findViewById(R.id.spinnerSort);


        String[] options = {"Numéro", "État", "Livreur", "Client"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                options
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);


        String token = requireActivity()
                .getSharedPreferences("app", Context.MODE_PRIVATE)
                .getString("TOKEN", "");

        DashboardApi api = RetrofitClient.getInstance().create(DashboardApi.class);

        api.getTodayLivraisons("Bearer " + token).enqueue(new Callback<List<LivraisonList>>() {
            @Override
            public void onResponse(Call<List<LivraisonList>> call, Response<List<LivraisonList>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data = response.body();
                    recycler.setAdapter(new LivraisonListAdapter(data));
                }
            }

            @Override
            public void onFailure(Call<List<LivraisonList>> call, Throwable t) {
                t.printStackTrace();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int pos, long id) {
                if (data == null) return;

                switch (pos) {
                    case 0: data.sort((a, b) -> a.nocde - b.nocde); break;
                    case 1: data.sort((a, b) -> safeStr(a.etat).compareTo(safeStr(b.etat))); break;
                    case 2: data.sort((a, b) -> safeStr(a.livreur).compareTo(safeStr(b.livreur))); break;
                    case 3: data.sort((a, b) -> safeStr(a.client).compareTo(safeStr(b.client))); break;
                }

                recycler.setAdapter(new LivraisonListAdapter(data));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    private String safeStr(String s) {
        return s != null ? s : "";
    }
}