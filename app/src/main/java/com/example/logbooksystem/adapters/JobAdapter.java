package com.example.logbooksystem.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logbooksystem.R;
import com.example.logbooksystem.models.JobOpportunity;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {
    private Context context;
    private List<JobOpportunity> jobList;
    private OnJobClickListener listener;

    public interface OnJobClickListener {
        void onJobClick(JobOpportunity job);
    }

    public JobAdapter(Context context, List<JobOpportunity> jobList, OnJobClickListener listener) {
        this.context = context;
        this.jobList = jobList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobOpportunity job = jobList.get(position);
        holder.tvJobTitle.setText(job.getTitle() != null ? job.getTitle() : "");
        holder.tvCompany.setText(job.getCompany() != null ? job.getCompany() : "");
        holder.tvLocation.setText(job.getLocation() != null ? job.getLocation() : "");
        holder.tvSalary.setText(job.getSalary() != null ? job.getSalary() : "");
        holder.tvJobType.setText(job.getType() != null ? job.getType() : "");
        holder.tvDeadline.setText("Deadline: " + (job.getDeadline() != null ? job.getDeadline() : "N/A"));
        holder.tvVacancies.setText(job.getVacancies() + " position(s)");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onJobClick(job);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList != null ? jobList.size() : 0;
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvCompany, tvLocation, tvSalary, tvJobType, tvDeadline, tvVacancies;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            tvJobType = itemView.findViewById(R.id.tvJobType);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);
            tvVacancies = itemView.findViewById(R.id.tvVacancies);
        }
    }
}