package com.example.aipulse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aipulse.database.DiagnosisResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<DiagnosisResult> results;

    public HistoryAdapter(List<DiagnosisResult> results) {
        this.results = results;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBpm, textViewRisk, textViewTime;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewBpm = itemView.findViewById(R.id.textViewBpmItem);
            textViewRisk = itemView.findViewById(R.id.textViewRiskItem);
            textViewTime = itemView.findViewById(R.id.textViewTimeItem);
        }
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder, int position) {
        DiagnosisResult result = results.get(position);
        holder.textViewBpm.setText("BPM : " + result.bpm);
        holder.textViewRisk.setText(result.riskMessage);

        String formattedDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(new Date(result.timestamp));
        holder.textViewTime.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }
}
