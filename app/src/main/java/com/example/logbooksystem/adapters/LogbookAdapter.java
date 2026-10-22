package com.example.logbooksystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logbooksystem.R;
import com.example.logbooksystem.models.LogbookEntry;
import java.util.List;

public class LogbookAdapter extends RecyclerView.Adapter<LogbookAdapter.LogbookViewHolder> {
    private Context context;
    private List<LogbookEntry> entryList;

    public LogbookAdapter(Context context, List<LogbookEntry> entryList) {
        this.context = context;
        this.entryList = entryList;
    }

    @NonNull
    @Override
    public LogbookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_logbook, parent, false);
        return new LogbookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogbookViewHolder holder, int position) {
        LogbookEntry entry = entryList.get(position);
        holder.tvTitle.setText(entry.getTitle());
        holder.tvDescription.setText(entry.getDescription());
        holder.tvType.setText(entry.getType());
        holder.tvDate.setText("Date: " + entry.getEntryDate());
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public static class LogbookViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvType, tvDate;

        public LogbookViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvType = itemView.findViewById(R.id.tvType);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}