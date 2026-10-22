package com.example.logbooksystem.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logbooksystem.R;
import com.example.logbooksystem.adapters.ApplicationAdapter;
import com.example.logbooksystem.database.DatabaseHelper;
import com.example.logbooksystem.models.Application;
import com.example.logbooksystem.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class ApplicationStatusActivity extends AppCompatActivity {
    private RecyclerView rvApplications;
    private TextView tvEmpty;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private ApplicationAdapter adapter;
    private List<Application> applicationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_status);

        try {
            dbHelper = new DatabaseHelper(this);
            sessionManager = new SessionManager(this);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("My Applications");
            }
            toolbar.setNavigationOnClickListener(v -> finish());

            rvApplications = findViewById(R.id.rvApplications);
            tvEmpty = findViewById(R.id.tvEmpty);

            applicationList = new ArrayList<>();
            adapter = new ApplicationAdapter(this, applicationList);
            adapter.setOnItemClickListener((application, position) -> showApplicationOptions(application));

            rvApplications.setLayoutManager(new LinearLayoutManager(this));
            rvApplications.setAdapter(adapter);

            loadApplications();
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void loadApplications() {
        try {
            int userId = sessionManager.getUserId();
            if (userId == -1) {
                Toast.makeText(this, "Please login again", Toast.LENGTH_SHORT).show();
                return;
            }

            applicationList.clear();
            List<Application> apps = dbHelper.getUserApplications(userId);
            if (apps != null) {
                applicationList.addAll(apps);
            }
            adapter.notifyDataSetChanged();

            if (applicationList.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                rvApplications.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                rvApplications.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading applications", Toast.LENGTH_SHORT).show();
        }
    }

    private void showApplicationOptions(Application app) {
        String status = app.getStatus() != null ? app.getStatus() : "Applied";
        String statusEmoji = "📋";
        if ("Accepted".equals(status)) statusEmoji = "✅";
        else if ("Rejected".equals(status)) statusEmoji = "❌";
        else if ("Shortlisted".equals(status)) statusEmoji = "⭐";
        else if ("Under Review".equals(status)) statusEmoji = "🔍";

        String message = statusEmoji + " Status: " + status + "\n\n" +
                "📄 Position: " + (app.getJobTitle() != null ? app.getJobTitle() : "N/A") + "\n" +
                "🏢 Company: " + (app.getCompany() != null ? app.getCompany() : "N/A") + "\n" +
                "📅 Applied: " + (app.getAppliedDate() != null ? app.getAppliedDate() : "N/A") + "\n" +
                "🕐 Last Update: " + (app.getLastUpdate() != null ? app.getLastUpdate() : "N/A");

        String[] options;
        if ("Accepted".equals(status) || "Rejected".equals(status)) {
            options = new String[]{"View Details", "Delete Application", "Cancel"};
        } else {
            options = new String[]{"View Details", "Withdraw Application", "Cancel"};
        }

        new AlertDialog.Builder(this)
                .setTitle(app.getJobTitle())
                .setMessage(message)
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        showFullDetails(app);
                    } else if (which == 1) {
                        confirmDelete(app);
                    }
                })
                .show();
    }

    private void showFullDetails(Application app) {
        String details = "📋 APPLICATION DETAILS\n\n" +
                "Position: " + (app.getJobTitle() != null ? app.getJobTitle() : "N/A") + "\n" +
                "Company: " + (app.getCompany() != null ? app.getCompany() : "N/A") + "\n" +
                "Status: " + (app.getStatus() != null ? app.getStatus() : "Applied") + "\n" +
                "Applied Date: " + (app.getAppliedDate() != null ? app.getAppliedDate() : "N/A") + "\n" +
                "Last Updated: " + (app.getLastUpdate() != null ? app.getLastUpdate() : "N/A") + "\n\n" +
                "Resume:\n" + (app.getResume() != null && !app.getResume().isEmpty() ? app.getResume() : "N/A") + "\n\n" +
                "Cover Letter:\n" + (app.getCoverLetter() != null && !app.getCoverLetter().isEmpty() ? app.getCoverLetter() : "N/A");

        new AlertDialog.Builder(this)
                .setTitle("Full Details")
                .setMessage(details)
                .setPositiveButton("OK", null)
                .show();
    }

    private void confirmDelete(Application app) {
        String action = "Accepted".equals(app.getStatus()) || "Rejected".equals(app.getStatus()) ? "delete" : "withdraw";
        new AlertDialog.Builder(this)
                .setTitle("Confirm " + action)
                .setMessage("Are you sure you want to " + action + " your application for " + app.getJobTitle() + "?")
                .setPositiveButton("YES", (d, w) -> {
                    if (dbHelper.deleteApplication(app.getId())) {
                        Toast.makeText(this, "Application " + action + "d!", Toast.LENGTH_SHORT).show();
                        loadApplications();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadApplications();
    }
}