package com.example.logbooksystem.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.logbooksystem.R;
import com.example.logbooksystem.database.DatabaseHelper;
import com.example.logbooksystem.models.JobOpportunity;
import com.example.logbooksystem.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

public class JobDetailActivity extends AppCompatActivity {
    private TextView tvJobTitle, tvCompany, tvType, tvLocation, tvSalary;
    private TextView tvDeadline, tvVacancies, tvDescription, tvRequirements;
    private Button btnApply, btnDeleteJob;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private int jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Job Details");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        tvJobTitle = findViewById(R.id.tvJobTitle);
        tvCompany = findViewById(R.id.tvCompany);
        tvType = findViewById(R.id.tvType);
        tvLocation = findViewById(R.id.tvLocation);
        tvSalary = findViewById(R.id.tvSalary);
        tvDeadline = findViewById(R.id.tvDeadline);
        tvVacancies = findViewById(R.id.tvVacancies);
        tvDescription = findViewById(R.id.tvDescription);
        tvRequirements = findViewById(R.id.tvRequirements);
        btnApply = findViewById(R.id.btnApply);
        btnDeleteJob = findViewById(R.id.btnDeleteJob);

        jobId = getIntent().getIntExtra("job_id", -1);
        if (jobId != -1) loadJobDetails();

        String role = sessionManager.getUserRole();

        // Show/Hide buttons based on role
        if ("student".equals(role)) {
            btnApply.setVisibility(Button.VISIBLE);
            btnDeleteJob.setVisibility(Button.GONE);
            btnApply.setOnClickListener(v -> showApplyDialog());
        } else if ("admin".equals(role)) {
            btnApply.setVisibility(Button.GONE);
            btnDeleteJob.setVisibility(Button.VISIBLE);
            btnDeleteJob.setText("🗑 DELETE JOB");
            btnDeleteJob.setOnClickListener(v -> confirmDelete());
        } else if ("employer".equals(role)) {
            btnApply.setVisibility(Button.GONE);
            btnDeleteJob.setVisibility(Button.VISIBLE);
            btnDeleteJob.setText("🗑 DELETE JOB");
            btnDeleteJob.setOnClickListener(v -> confirmDelete());
        }
    }

    private void loadJobDetails() {
        JobOpportunity job = dbHelper.getJobById(jobId);
        if (job != null) {
            tvJobTitle.setText(job.getTitle() != null ? job.getTitle() : "N/A");
            tvCompany.setText(job.getCompany() != null ? job.getCompany() : "N/A");
            tvType.setText(job.getType() != null ? job.getType() : "N/A");
            tvLocation.setText(job.getLocation() != null ? job.getLocation() : "N/A");
            tvSalary.setText(job.getSalary() != null ? job.getSalary() : "N/A");
            tvDeadline.setText("Deadline: " + (job.getDeadline() != null ? job.getDeadline() : "N/A"));
            tvVacancies.setText(job.getVacancies() + " position(s)");
            tvDescription.setText(job.getDescription() != null ? job.getDescription() : "N/A");
            tvRequirements.setText(job.getRequirements() != null ? job.getRequirements() : "N/A");

            if ("student".equals(sessionManager.getUserRole())) {
                if (dbHelper.hasUserApplied(sessionManager.getUserId(), jobId)) {
                    btnApply.setText("ALREADY APPLIED");
                    btnApply.setEnabled(false);
                    btnApply.setAlpha(0.5f);
                }
            }
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Job")
                .setMessage("Are you sure you want to delete this job?\nThis cannot be undone.")
                .setPositiveButton("YES, DELETE", (d, w) -> {
                    if (dbHelper.deleteJob(jobId)) {
                        Toast.makeText(this, "Job deleted!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    private void showApplyDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_apply_job);
        dialog.getWindow().setLayout((int)(getResources().getDisplayMetrics().widthPixels * 0.9), android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        TextInputEditText etResume = dialog.findViewById(R.id.etResume);
        Button btnSubmit = dialog.findViewById(R.id.btnSubmit);
        dialog.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());
        btnSubmit.setOnClickListener(v -> {
            String resume = etResume.getText().toString().trim();
            if (resume.isEmpty()) { Toast.makeText(this, "Enter resume", Toast.LENGTH_SHORT).show(); return; }
            dbHelper.applyForJob(sessionManager.getUserId(), jobId, resume, "");
            Toast.makeText(this, "Applied!", Toast.LENGTH_SHORT).show();
            btnApply.setText("ALREADY APPLIED");
            btnApply.setEnabled(false);
            dialog.dismiss();
        });
        dialog.show();
    }
}