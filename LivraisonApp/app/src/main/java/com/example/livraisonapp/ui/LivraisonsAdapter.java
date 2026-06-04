package com.example.livraisonapp.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.*;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.livraisonapp.R;
import com.example.livraisonapp.model.Livraison;
import com.google.android.material.chip.Chip;

import java.util.List;

public class LivraisonsAdapter extends RecyclerView.Adapter<LivraisonsAdapter.ViewHolder> {

    private List<Livraison> list;
    private String token;

    public LivraisonsAdapter(List<Livraison> list, String token) {
        this.list = list;
        this.token = token;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCommande, txtClient, txtVille;
        Chip txtEtat;
        View statusBar;

        public ViewHolder(View view) {
            super(view);
            txtCommande = view.findViewById(R.id.txtCommande);
            txtEtat = view.findViewById(R.id.txtEtat);
            txtClient = view.findViewById(R.id.txtClient);
            txtVille = view.findViewById(R.id.txtVille);
            statusBar   = view.findViewById(R.id.statusBar);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_livraison, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Livraison l = list.get(position);
        Context ctx = holder.itemView.getContext();
        holder.txtCommande.setText("N° CMD : " + l.nocde);
        holder.txtClient.setText(
                l.client != null ? l.client : "N/A"
        );

        holder.txtVille.setText(
                (l.ville != null ? l.ville : "") +
                        " - " +
                        (l.codePostal != null ? l.codePostal : "")
        );
        switch (l.etatliv) {
            case "LI":
                holder.txtEtat.setText("Livrée");
                holder.txtEtat.setChipBackgroundColor(
                        ColorStateList.valueOf(ctx.getColor(R.color.status_green_bg)));
                holder.txtEtat.setTextColor(ctx.getColor(R.color.status_green_text));
                holder.statusBar.setBackgroundColor(ctx.getColor(R.color.status_green_text));
                break;
            case "EC":
                holder.txtEtat.setText("En cours");
                holder.txtEtat.setChipBackgroundColor(
                        ColorStateList.valueOf(ctx.getColor(R.color.status_orange_bg)));
                holder.txtEtat.setTextColor(ctx.getColor(R.color.status_orange_text));
                holder.statusBar.setBackgroundColor(ctx.getColor(R.color.status_orange_text));
                break;
            case "AL":
                holder.txtEtat.setText("Annulée");
                holder.txtEtat.setChipBackgroundColor(
                        ColorStateList.valueOf(ctx.getColor(R.color.status_red_bg)));
                holder.txtEtat.setTextColor(ctx.getColor(R.color.status_red_text));
                holder.statusBar.setBackgroundColor(ctx.getColor(R.color.status_red_text));
                break;
            default:
                holder.txtEtat.setText("En attente");
                holder.txtEtat.setChipBackgroundColor(
                        ColorStateList.valueOf(ctx.getColor(R.color.status_grey_bg)));
                holder.txtEtat.setTextColor(ctx.getColor(R.color.status_grey_text));
                holder.statusBar.setBackgroundColor(ctx.getColor(R.color.status_grey_text));
                break;
        }


        holder.itemView.setOnClickListener(v -> {

            android.content.Intent intent = new android.content.Intent(
                    v.getContext(),
                    DetailLivraisonActivity.class
            );

            intent.putExtra("nocde", l.nocde);

            intent.putExtra("client", l.client != null ? l.client : "N/A");

            intent.putExtra("telephone",
                    l.telephone != null ? String.valueOf(l.telephone) : "N/A");

            intent.putExtra("adresse", l.adresse != null ? l.adresse : "N/A");

            intent.putExtra("etat", l.etatliv != null ? l.etatliv : "N/A");

            intent.putExtra("modepay", l.modepay != null ? l.modepay : "N/A");

            intent.putExtra("total", l.total != null ? l.total : 0.0);

            intent.putExtra("TOKEN", token);


            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
