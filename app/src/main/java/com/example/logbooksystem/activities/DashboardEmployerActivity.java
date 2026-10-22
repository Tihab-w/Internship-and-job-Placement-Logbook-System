package com.example.logbooksystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logbooksystem.R;
import com.example.logbooksystem.adapters.JobAdapter;
import com.example.logbooksystem.database.DatabaseHelper;
import com.example.logbooksystem.models.JobOpportunity;
import com.example.logbooksystem.models.User;
import com.example.logbooksystem.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class DashboardEmployerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView rvJobs;
    private TextView tvWelcome, tvStatsJobs, tvStatsApplications, tvStatsReviews;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private JobAdapter jobAdapter;
    private List<JobOpportunity> jobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_employer);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("My Jobs");

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView tvNavName = headerView.findViewById(R.id.tvNavName);
        TextView tvNavEmail = headerView.findViewById(R.id.tvNavEmail);
        tvNavName.setText(sessionManager.getUserName());
        tvNavEmail.setText(sessionManager.getUserEmail());

        tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setText("Welcome, " + sessionManager.getUserName() + "!");
        tvStatsJobs = findViewById(R.id.tvStatsJobs);
        tvStatsApplications = findViewById(R.id.tvStatsApplications);
        tvStatsReviews = findViewById(R.id.tvStatsReviews);

        rvJobs = findViewById(R.id.rvJobs);
        jobList = new ArrayList<>();
        jobAdapter = new JobAdapter(this, jobList, job -> {
            Intent intent = new Intent(DashboardEmployerActivity.this, JobDetailActivity.class);
            intent.putExtra("job_id", job.getId());
            startActivity(intent);
        });
        rvJobs.setLayoutManager(new LinearLayoutManager(this));
        rvJobs.setAdapter(jobAdapter);

        findViewById(R.id.cardPostJob).setOnClickListener(v -> startActivity(new Intent(this, PostJobActivity.class)));
        findViewById(R.id.cardReviewApps).setOnClickListener(v -> startActivity(new Intent(this, ReviewApplicationsActivity.class)));
        findViewById(R.id.cardReports).setOnClickListener(v -> startActivity(new Intent(this, ReportActivity.class)));
        findViewById(R.id.cardProfile).setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        FloatingActionButton fab = findViewById(R.id.fabPostJob);
        fab.setOnClickListener(v -> startActivity(new Intent(this, PostJobActivity.class)));

        loadJobs();
    }

    private void loadJobs() {
        jobList.clear();
        int employerId = sessionManager.getUserId();
        User employer = dbHelper.getUserById(employerId);
        String companyName = employer != null ? employer.getCompanyName() : "";

        List<JobOpportunity> allJobs = dbHelper.getAllJobs();

        if (allJobs != null && companyName != null && !companyName.isEmpty()) {
            for (JobOpportunity job : allJobs) {
                if (companyName.equals(job.getCompany())) {
                    boolean alreadyAdded = false;
                    for (JobOpportunity existing : jobList) {
                        if (existing.getId() == job.getId()) {
                            alreadyAdded = true;
                            break;
                        }
                    }
                    if (!alreadyAdded) jobList.add(job);
                }
            }
        }

        jobAdapter.notifyDataSetChanged();
        tvStatsJobs.setText(String.valueOf(jobList.size()));

        int totalApps = 0;
        for (JobOpportunity j : jobList) {
            totalApps += dbHelper.getApplicationsCountForJob(j.getId());
        }
        tvStatsApplications.setText(String.valueOf(totalApps));

        int active = 0;
        for (JobOpportunity j : jobList) {
            if ("Open".equals(j.getStatus())) active++;
        }
        tvStatsReviews.setText(String.valueOf(active));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_post_job) startActivity(new Intent(this, PostJobActivity.class));
        else if (id == R.id.nav_review_apps) startActivity(new Intent(this, ReviewApplicationsActivity.class));
        else if (id == R.id.nav_reports) startActivity(new Intent(this, ReportActivity.class));
        else if (id == R.id.nav_profile) startActivity(new Intent(this, ProfileActivity.class));
        else if (id == R.id.nav_logout) {
            sessionManager.logout();
            startActivity(new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finishAffinity();
        }
        DrawerLayout dl = findViewById(R.id.drawer_layout);
        dl.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadJobs();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout dl = findViewById(R.id.drawer_layout);
        if (dl.isDrawerOpen(GravityCompat.START)) dl.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }
}