package com.example.livraisonapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.livraisonapp.R;
import com.example.livraisonapp.model.MessageResponse;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.VH> {

    List<MessageResponse> list;

    public MessageAdapter(List<MessageResponse> list) {
        this.list = list;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtContenu, txtDate, txtType;
        View accentBar;

        public VH(View v) {
            super(v);
            txtContenu = v.findViewById(R.id.txtContenu);
            txtDate    = v.findViewById(R.id.txtDate);
            txtType    = v.findViewById(R.id.txtType);
            accentBar  = v.findViewById(R.id.accentBar);
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH h, int position) {

        MessageResponse m = list.get(position);
        Context ctx = h.itemView.getContext();


        h.txtContenu.setText(m.contenu != null ? m.contenu : "");
        h.txtDate.setText(m.dateEnvoi != null ? m.dateEnvoi : "");


        String type = m.type != null ? m.type.toUpperCase() : "INFO";

        switch (type) {
            case "URGENT":
                h.txtType.setText("🔴 URGENT");
                h.txtType.setTextColor(0xFFFFFFFF);
                h.txtType.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_badge_urgent));
                h.accentBar.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_urgent_bar));

                h.itemView.findViewById(R.id.msgCardContent)
                        .setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_msg_card_urgent));
                break;

            case "INFO":
            default:
                h.txtType.setText("🔵 INFO");
                h.txtType.setTextColor(0xFFFFFFFF);
                h.txtType.setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_badge_info));
                h.accentBar.setBackgroundColor(0xFF3B3DBF);
                h.itemView.findViewById(R.id.msgCardContent)
                        .setBackground(ContextCompat.getDrawable(ctx, R.drawable.bg_msg_card));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
}