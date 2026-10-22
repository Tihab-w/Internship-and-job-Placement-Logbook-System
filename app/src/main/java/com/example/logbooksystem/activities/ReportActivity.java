package com.example.logbooksystem.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.logbooksystem.R;
import com.example.logbooksystem.database.DatabaseHelper;

public class ReportActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tvTotalJobs, tvOpenJobs, tvTotalApplications, tvTotalUsers;
    private TextView tvApplied, tvReview, tvAccepted, tvRejected, tvPlacementRate;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        try {
            dbHelper = new DatabaseHelper(this);

            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("Reports");
            }
            toolbar.setNavigationOnClickListener(v -> finish());

            // Find all views
            tvTotalJobs = findViewById(R.id.tvTotalJobs);
            tvOpenJobs = findViewById(R.id.tvOpenJobs);
            tvTotalApplications = findViewById(R.id.tvTotalApplications);
            tvTotalUsers = findViewById(R.id.tvTotalUsers);
            tvApplied = findViewById(R.id.tvAppliedCount);
            tvReview = findViewById(R.id.tvReviewCount);
            tvAccepted = findViewById(R.id.tvAcceptedCount);
            tvRejected = findViewById(R.id.tvRejectedCount);
            tvPlacementRate = findViewById(R.id.tvPlacementRate);

            loadReportData();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading report: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void loadReportData() {
        try {
            tvTotalJobs.setText(String.valueOf(dbHelper.getTotalJobs()));
            tvOpenJobs.setText(String.valueOf(dbHelper.getOpenJobsCount()));
            tvTotalApplications.setText(String.valueOf(dbHelper.getTotalApplications()));
            tvTotalUsers.setText(String.valueOf(dbHelper.getTotalUsers()));
            tvApplied.setText(String.valueOf(dbHelper.getApplicationsByStatus("Applied")));
            tvReview.setText(String.valueOf(dbHelper.getApplicationsByStatus("Under Review")));
            tvAccepted.setText(String.valueOf(dbHelper.getApplicationsByStatus("Accepted")));
            tvRejected.setText(String.valueOf(dbHelper.getApplicationsByStatus("Rejected")));
            tvPlacementRate.setText(dbHelper.getPlacementSuccessRate() + "%");
        } catch (Exception e) {
            Toast.makeText(this, "Error loading stats", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReportData();
    }
}