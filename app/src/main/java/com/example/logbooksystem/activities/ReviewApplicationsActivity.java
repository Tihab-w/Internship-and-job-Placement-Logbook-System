package com.example.logbooksystem.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logbooksystem.R;
import com.example.logbooksystem.adapters.ApplicationAdapter;
import com.example.logbooksystem.database.DatabaseHelper;
import com.example.logbooksystem.models.Application;
import com.example.logbooksystem.models.Feedback;
import com.example.logbooksystem.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class ReviewApplicationsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView rvApplications;
    private TextView tvEmpty;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private ApplicationAdapter adapter;
    private List<Application> applicationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_applications);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        rvApplications = findViewById(R.id.rvApplications);
        tvEmpty = findViewById(R.id.tvEmpty);

        // Setup toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Review Applications");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Initialize list
        applicationList = new ArrayList<>();

        // Setup adapter with click listener
        adapter = new ApplicationAdapter(this, applicationList);
        adapter.setOnItemClickListener(new ApplicationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Application application, int position) {
                showReviewDialog(application);
            }
        });

        // Setup RecyclerView
        rvApplications.setLayoutManager(new LinearLayoutManager(this));
        rvApplications.setAdapter(adapter);

        // Load data
        loadApplications();
    }

    private void loadApplications() {
        applicationList.clear();
        int employerId = sessionManager.getUserId();
        List<Application> apps = dbHelper.getApplicationsForEmployerJobs(employerId);
        applicationList.addAll(apps);
        adapter.notifyDataSetChanged();

        // Show/hide empty state
        if (applicationList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvApplications.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvApplications.setVisibility(View.VISIBLE);
        }
    }

    private void showReviewDialog(Application application) {
        // Create dialog
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_review_application);
        dialog.setCancelable(true);

        // Set dialog width
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(
                    (int)(getResources().getDisplayMetrics().widthPixels * 0.92),
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
            );
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Initialize dialog views
        TextView tvStudentName = dialog.findViewById(R.id.tvStudentName);
        TextView tvJobTitle = dialog.findViewById(R.id.tvJobTitle);
        TextView tvStatus = dialog.findViewById(R.id.tvStatus);
        TextView tvAppliedDate = dialog.findViewById(R.id.tvAppliedDate);
        TextView tvResume = dialog.findViewById(R.id.tvResume);
        TextView tvCoverLetter = dialog.findViewById(R.id.tvCoverLetter);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        TextInputEditText etFeedback = dialog.findViewById(R.id.etFeedback);
        TextInputEditText etStrengths = dialog.findViewById(R.id.etStrengths);
        TextInputEditText etImprovements = dialog.findViewById(R.id.etImprovements);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        Button btnShortlist = dialog.findViewById(R.id.btnShortlist);
        Button btnReject = dialog.findViewById(R.id.btnReject);
        Button btnUnderReview = dialog.findViewById(R.id.btnUnderReview);
        Button btnClose = dialog.findViewById(R.id.btnClose);

        // Populate dialog with application data
        tvStudentName.setText(application.getStudentName() != null ?
                "Student: " + application.getStudentName() : "Student: N/A");
        tvJobTitle.setText(application.getJobTitle() != null ?
                "Position: " + application.getJobTitle() : "Position: N/A");
        tvStatus.setText("Status: " + application.getStatus());
        tvAppliedDate.setText("Applied: " + application.getAppliedDate());

        String resume = application.getResume();
        tvResume.setText(resume != null && !resume.isEmpty() ?
                resume : "No resume provided");

        String coverLetter = application.getCoverLetter();
        tvCoverLetter.setText(coverLetter != null && !coverLetter.isEmpty() ?
                coverLetter : "No cover letter provided");

        // Set status color
        int statusColor = getStatusColor(application.getStatus());
        tvStatus.setTextColor(statusColor);

        // Button click listeners
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateApplicationStatus(application.getId(), "Accepted");
                saveFeedback(application, ratingBar, etFeedback, etStrengths, etImprovements);
                dialog.dismiss();
            }
        });

        btnShortlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateApplicationStatus(application.getId(), "Shortlisted");
                saveFeedback(application, ratingBar, etFeedback, etStrengths, etImprovements);
                dialog.dismiss();
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateApplicationStatus(application.getId(), "Rejected");
                saveFeedback(application, ratingBar, etFeedback, etStrengths, etImprovements);
                dialog.dismiss();
            }
        });

        btnUnderReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateApplicationStatus(application.getId(), "Under Review");
                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Show dialog
        dialog.show();
    }

    private void updateApplicationStatus(int applicationId, String newStatus) {
        boolean updated = dbHelper.updateApplicationStatus(
                applicationId,
                newStatus,
                sessionManager.getUserId()
        );

        if (updated) {
            String message = "";
            switch (newStatus) {
                case "Accepted":
                    message = "Candidate accepted!";
                    break;
                case "Rejected":
                    message = "Candidate rejected";
                    break;
                case "Shortlisted":
                    message = "Candidate shortlisted!";
                    break;
                case "Under Review":
                    message = "Application marked as Under Review";
                    break;
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            loadApplications();
        } else {
            Toast.makeText(this, "Failed to update status", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFeedback(Application application, RatingBar ratingBar,
                              TextInputEditText etFeedback,
                              TextInputEditText etStrengths,
                              TextInputEditText etImprovements) {
        int rating = (int) ratingBar.getRating();
        String comment = etFeedback.getText() != null ?
                etFeedback.getText().toString().trim() : "";
        String strengths = etStrengths.getText() != null ?
                etStrengths.getText().toString().trim() : "";
        String improvements = etImprovements.getText() != null ?
                etImprovements.getText().toString().trim() : "";

        // Only save feedback if there's something to save
        if (rating > 0 || !comment.isEmpty() || !strengths.isEmpty() || !improvements.isEmpty()) {
            Feedback feedback = new Feedback();
            feedback.setApplicationId(application.getId());
            feedback.setEmployerId(sessionManager.getUserId());
            feedback.setRating(rating);
            feedback.setComment(comment.isEmpty() ? "No comment provided" : comment);
            feedback.setStrengths(strengths);
            feedback.setImprovements(improvements);

            long result = dbHelper.addFeedback(feedback);
            if (result != -1) {
                Toast.makeText(this, "Feedback saved!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int getStatusColor(String status) {
        if (status == null) return Color.parseColor("#757575");

        switch (status) {
            case "Applied":
                return Color.parseColor("#2196F3");
            case "Under Review":
                return Color.parseColor("#FF9800");
            case "Shortlisted":
                return Color.parseColor("#9C27B0");
            case "Accepted":
                return Color.parseColor("#4CAF50");
            case "Rejected":
                return Color.parseColor("#F44336");
            default:
                return Color.parseColor("#757575");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadApplications();
    }
}