package com.example.livraisonapp.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.livraisonapp.R;
import com.example.livraisonapp.api.DashboardApi;
import com.example.livraisonapp.model.*;
import com.example.livraisonapp.utils.RetrofitClient;

import java.util.List;

import retrofit2.*;

public class DashboardFragment extends Fragment {

    TextView txtTotal, txtLivree, txtEncours, txtAnnule;
    LinearLayout topLivreurs, topClients, topRemarques;

    String token;
    int rowIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        txtTotal   = view.findViewById(R.id.txtTotal);
        txtLivree  = view.findViewById(R.id.txtLivree);
        txtEncours = view.findViewById(R.id.txtEncours);
        txtAnnule  = view.findViewById(R.id.txtAnnule);

        topLivreurs  = view.findViewById(R.id.topLivreurs);
        topClients   = view.findViewById(R.id.topClients);
        topRemarques = view.findViewById(R.id.topRemarques);

        token = requireActivity()
                .getSharedPreferences("app", Context.MODE_PRIVATE)
                .getString("TOKEN", "");

        loadData();

        return view;
    }

    private void loadData() {

        DashboardApi api = RetrofitClient.getInstance().create(DashboardApi.class);

        api.getGlobal("Bearer " + token).enqueue(new Callback<DashboardStats>() {
            @Override
            public void onResponse(Call<DashboardStats> call, Response<DashboardStats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DashboardStats d = response.body();
                    txtTotal.setText(String.valueOf(d.total));
                    txtLivree.setText(String.valueOf(d.livree));
                    txtEncours.setText(String.valueOf(d.encours));
                    txtAnnule.setText(String.valueOf(d.annulee));
                }
            }
            @Override
            public void onFailure(Call<DashboardStats> call, Throwable t) { t.printStackTrace(); }
        });


        api.getLivreurs("Bearer " + token).enqueue(new Callback<List<LivreurStats>>() {
            @Override
            public void onResponse(Call<List<LivreurStats>> call, Response<List<LivreurStats>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    topLivreurs.removeAllViews();
                    int i = 1;
                    for (LivreurStats l : response.body()) {
                        topLivreurs.addView(makeRowCard(l.nomLivreur, l.total, i++));
                    }
                }
            }
            @Override
            public void onFailure(Call<List<LivreurStats>> call, Throwable t) {}
        });


        api.getClients("Bearer " + token).enqueue(new Callback<List<ClientStats>>() {
            @Override
            public void onResponse(Call<List<ClientStats>> call, Response<List<ClientStats>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    topClients.removeAllViews();
                    int i = 1;
                    for (ClientStats c : response.body()) {
                        topClients.addView(makeRowCard(c.nomClient, c.total, i++));
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ClientStats>> call, Throwable t) {}
        });


        api.getRemarques("Bearer " + token).enqueue(new Callback<List<Remarque>>() {
            @Override
            public void onResponse(Call<List<Remarque>> call, Response<List<Remarque>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    topRemarques.removeAllViews();
                    for (Remarque r : response.body()) {
                        topRemarques.addView(makeRemarqueCard(
                                "CMD " + r.nocde + " · " + r.client,
                                r.remarque
                        ));
                    }
                }
            }
            @Override
            public void onFailure(Call<List<Remarque>> call, Throwable t) { t.printStackTrace(); }
        });
    }


    private View makeRowCard(String name, long count, int rank) {

        Context ctx = requireContext();
        int dp8  = dp(ctx, 8);
        int dp12 = dp(ctx, 12);
        int dp16 = dp(ctx, 16);
        int dp36 = dp(ctx, 36);
        int dp6  = dp(ctx, 6);


        LinearLayout card = new LinearLayout(ctx);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(android.view.Gravity.CENTER_VERTICAL);
        card.setPadding(dp12, dp12, dp12, dp12);
        card.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_row_card));
        card.setElevation(dp(ctx, 2));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, dp8);
        card.setLayoutParams(cardParams);


        TextView rankView = new TextView(ctx);
        rankView.setText(String.valueOf(rank));
        rankView.setTextColor(0xFFFFFFFF);
        rankView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        rankView.setTypeface(null, Typeface.BOLD);
        rankView.setGravity(android.view.Gravity.CENTER);
        rankView.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_rank_pill));
        LinearLayout.LayoutParams rankParams = new LinearLayout.LayoutParams(dp36, dp36);
        rankParams.setMarginEnd(dp12);
        rankView.setLayoutParams(rankParams);
        card.addView(rankView);


        TextView nameView = new TextView(ctx);
        nameView.setText(name);
        nameView.setTextColor(0xFF111827);
        nameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        nameView.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        nameView.setLayoutParams(nameParams);
        card.addView(nameView);


        TextView countView = new TextView(ctx);
        countView.setText(count + " livr.");
        countView.setTextColor(0xFF3B3DBF);
        countView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        countView.setTypeface(null, Typeface.BOLD);
        countView.setPadding(dp12, dp6, dp12, dp6);
        countView.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_count_pill));
        card.addView(countView);

        return card;
    }


    private View makeRemarqueCard(String title, String desc) {

        Context ctx = requireContext();
        int dp8  = dp(ctx, 8);
        int dp12 = dp(ctx, 12);
        int dp4  = dp(ctx, 4);

        LinearLayout card = new LinearLayout(ctx);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp12, dp12, dp12, dp12);
        card.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_remarque_card));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, dp8);
        card.setLayoutParams(cardParams);


        LinearLayout titleRow = new LinearLayout(ctx);
        titleRow.setOrientation(LinearLayout.HORIZONTAL);
        titleRow.setGravity(android.view.Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        rowParams.setMargins(0, 0, 0, dp4);
        titleRow.setLayoutParams(rowParams);

        TextView emoji = new TextView(ctx);
        emoji.setText("⚠️ ");
        emoji.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        titleRow.addView(emoji);

        TextView titleView = new TextView(ctx);
        titleView.setText(title);
        titleView.setTextColor(0xFFDC2626);
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        titleView.setTypeface(null, Typeface.BOLD);
        titleRow.addView(titleView);

        card.addView(titleRow);


        TextView descView = new TextView(ctx);
        descView.setText(desc != null && !desc.isEmpty() ? desc : "Aucune remarque");
        descView.setTextColor(0xFF6B7280);
        descView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        descView.setLineSpacing(0, 1.4f);
        card.addView(descView);

        return card;
    }


    private int dp(Context ctx, int value) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, value,
                ctx.getResources().getDisplayMetrics());
    }
}