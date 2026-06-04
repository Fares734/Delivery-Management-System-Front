package com.example.livraisonapp.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livraisonapp.R;
import com.example.livraisonapp.api.DashboardApi;
import com.example.livraisonapp.model.LivraisonList;
import com.example.livraisonapp.utils.RetrofitClient;

import java.util.*;

import retrofit2.*;

public class SearchFragment extends Fragment {

    EditText etFrom, etTo;
    Spinner spinner;
    LinearLayout btnSearch;
    RecyclerView recycler;

    String selectedLivreur = "Tous";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);

        etFrom    = view.findViewById(R.id.etDateFrom);
        etTo      = view.findViewById(R.id.etDateTo);
        spinner   = view.findViewById(R.id.spinnerLivreur);
        btnSearch = view.findViewById(R.id.btnSearch);
        recycler  = view.findViewById(R.id.recyclerSearch);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setNestedScrollingEnabled(false);


        etFrom.setOnClickListener(v -> showDate(etFrom));
        etTo.setOnClickListener(v -> showDate(etTo));


        loadLivreurs();


        btnSearch.setOnClickListener(v -> search());

        return view;
    }

    private void loadLivreurs() {

        String token = requireActivity()
                .getSharedPreferences("app", Context.MODE_PRIVATE)
                .getString("TOKEN", "");

        DashboardApi api = RetrofitClient.getInstance().create(DashboardApi.class);

        api.getLesLivreurs("Bearer " + token).enqueue(new Callback<List<String>>() {

            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {

                if (response.isSuccessful() && response.body() != null) {

                    List<String> list = new ArrayList<>();
                    list.add("Tous");
                    list.addAll(response.body());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            list
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                            selectedLivreur = list.get(pos);
                        }
                        public void onNothingSelected(AdapterView<?> p) {}
                    });

                } else {
                    Toast.makeText(getContext(), "Erreur chargement livreurs", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(getContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDate(EditText et) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (view, y, m, d) -> {
            et.setText(String.format("%04d-%02d-%02d", y, m + 1, d));
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void search() {

        String from    = etFrom.getText().toString().trim();
        String to      = etTo.getText().toString().trim();
        String livreur = selectedLivreur.equals("Tous") ? null : selectedLivreur;

        if (from.isEmpty()) from = null;
        if (to.isEmpty())   to   = null;

        String token = requireActivity()
                .getSharedPreferences("app", Context.MODE_PRIVATE)
                .getString("TOKEN", "");

        DashboardApi api = RetrofitClient.getInstance().create(DashboardApi.class);

        api.search("Bearer " + token, from, to, livreur)
                .enqueue(new Callback<List<LivraisonList>>() {

                    @Override
                    public void onResponse(Call<List<LivraisonList>> call, Response<List<LivraisonList>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            recycler.setAdapter(new LivraisonListAdapter(response.body()));
                        } else {
                            Toast.makeText(getContext(), "Aucun résultat", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LivraisonList>> call, Throwable t) {
                        Toast.makeText(getContext(), "Erreur réseau", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}