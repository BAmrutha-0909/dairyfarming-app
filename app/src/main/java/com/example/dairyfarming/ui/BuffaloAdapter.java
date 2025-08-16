// ui/BuffaloAdapter.java
package com.example.dairyfarming.ui;

import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dairyfarming.R;
import com.example.dairyfarming.data.Buffalo;
import java.util.ArrayList;
import java.util.List;

public class BuffaloAdapter extends RecyclerView.Adapter<BuffaloAdapter.VH> {
    public interface Listener{
        void onClick(Buffalo b);
        void onLongClick(Buffalo b);
    }
    private final Listener listener;
    private final List<Buffalo> items = new ArrayList<>();

    public BuffaloAdapter(Listener l){ this.listener = l; }

    public void submit(List<Buffalo> list){
        items.clear(); if(list!=null) items.addAll(list);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder{
        TextView name, liters, predicted;
        VH(View v){ super(v);
            name = v.findViewById(R.id.tvName);
            liters = v.findViewById(R.id.tvLiters);
            predicted = v.findViewById(R.id.tvPredicted);
        }
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int viewType){
        View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_buffalo, p, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos){
        Buffalo b = items.get(pos);
        h.name.setText(b.name);
        h.liters.setText(String.format("%.1f L/day", b.litersPerDay));
        String pred = b.predictedLitersNextDay == null ? "-" : String.format("%.1f L (pred.)", b.predictedLitersNextDay);
        h.predicted.setText(pred);
        h.itemView.setOnClickListener(v -> listener.onClick(b));
        h.itemView.setOnLongClickListener(v -> { listener.onLongClick(b); return true; });
    }

    @Override public int getItemCount(){ return items.size(); }
}
