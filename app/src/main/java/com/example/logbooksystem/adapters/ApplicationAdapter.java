package com.example.logbooksystem.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logbooksystem.R;
import com.example.logbooksystem.models.Application;
import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.AppViewHolder> {
    private Context context;
    private List<Application> applicationList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Application application, int position);
    }

    public ApplicationAdapter(Context context, List<Application> applicationList) {
        this.context = context;
        this.applicationList = applicationList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_application, parent, false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        Application app = applicationList.get(position);
        holder.tvJobTitle.setText(app.getJobTitle() != null ? app.getJobTitle() : "N/A");
        holder.tvCompany.setText(app.getCompany() != null ? app.getCompany() : "N/A");
        holder.tvStatus.setText(app.getStatus() != null ? app.getStatus() : "Applied");
        holder.tvAppliedDate.setText(app.getAppliedDate() != null ? "Applied: " + app.getAppliedDate() : "");
        holder.tvLastUpdate.setText(app.getLastUpdate() != null ? "Updated: " + app.getLastUpdate() : "");

        // Status color
        String status = app.getStatus();
        int color = Color.parseColor("#2196F3");
        if ("Under Review".equals(status)) color = Color.parseColor("#FF9800");
        else if ("Shortlisted".equals(status)) color = Color.parseColor("#9C27B0");
        else if ("Accepted".equals(status)) color = Color.parseColor("#4CAF50");
        else if ("Rejected".equals(status)) color = Color.parseColor("#F44336");

        holder.tvStatus.setBackgroundTintList(ColorStateList.valueOf(color));
        holder.tvStatus.setTextColor(Color.WHITE);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(app, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return applicationList != null ? applicationList.size() : 0;
    }

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvCompany, tvStatus, tvAppliedDate, tvLastUpdate;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAppliedDate = itemView.findViewById(R.id.tvAppliedDate);
            tvLastUpdate = itemView.findViewById(R.id.tvLastUpdate);
        }
    }
}