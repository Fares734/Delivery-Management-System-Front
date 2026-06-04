package com.example.livraisonapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livraisonapp.R;
import com.example.livraisonapp.model.LivraisonList;

import java.util.List;

public class LivraisonListAdapter extends RecyclerView.Adapter<LivraisonListAdapter.VH> {

    List<LivraisonList> list;

    public LivraisonListAdapter(List<LivraisonList> list) {
        this.list = list;
    }

    class VH extends RecyclerView.ViewHolder {
        TextView cmd, etat, client, livreur, date, montant;

        VH(View v) {
            super(v);
            cmd     = v.findViewById(R.id.txtCmd);
            etat    = v.findViewById(R.id.txtEtat);
            client  = v.findViewById(R.id.txtClient);
            livreur = v.findViewById(R.id.txtLivreur);
            date    = v.findViewById(R.id.txtDate);
            montant = v.findViewById(R.id.txtMontant);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listlivraison, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH h, int i) {

        LivraisonList l = list.get(i);
        Context ctx = h.itemView.getContext();


        h.cmd.setText("N° CMD : " + l.nocde);


        applyStatusBadge(h.etat, l.etat, ctx);


        h.client.setText(l.client != null  ? l.client  : "—");
        h.livreur.setText(l.livreur != null ? l.livreur : "—");
        h.date.setText(l.date != null ? l.date : "—");
        h.montant.setText(l.montant + " DT");
    }


    private void applyStatusBadge(TextView badge, String etat, Context ctx) {

        if (etat == null) etat = "";

        switch (etat.toUpperCase()) {
            case "LI":
                badge.setText("Livrée");
                badge.setTextColor(0xFF16A34A);
                badge.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_badge_green));
                break;

            case "EC":
                badge.setText("En cours");
                badge.setTextColor(0xFFD97706);
                badge.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_badge_yellow));
                break;

            case "AL":
                badge.setText("Annulée");
                badge.setTextColor(0xFFDC2626);
                badge.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_badge_red));
                break;

            default:
                badge.setText(etat.isEmpty() ? "Inconnu" : etat);
                badge.setTextColor(0xFF3B3DBF);
                badge.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_badge_blue));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
}