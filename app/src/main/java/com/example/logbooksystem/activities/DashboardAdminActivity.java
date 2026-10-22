package com.example.logbooksystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logbooksystem.R;
import com.example.logbooksystem.adapters.JobAdapter;
import com.example.logbooksystem.database.DatabaseHelper;
import com.example.logbooksystem.models.JobOpportunity;
import com.example.logbooksystem.utils.SessionManager;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class DashboardAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private TextView tvWelcome, tvTotalUsers, tvTotalEmployers, tvTotalStudents;
    private TextView tvTotalJobs, tvOpenJobs, tvTotalApplications, tvPlacementRate;
    private CardView cardManageUsers, cardManageJobs, cardReports, cardSettings;
    private RecyclerView rvAllJobs;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private JobAdapter jobAdapter;
    private List<JobOpportunity> allJobsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvTotalEmployers = findViewById(R.id.tvTotalEmployers);
        tvTotalStudents = findViewById(R.id.tvTotalStudents);
        tvTotalJobs = findViewById(R.id.tvTotalJobs);
        tvOpenJobs = findViewById(R.id.tvOpenJobs);
        tvTotalApplications = findViewById(R.id.tvTotalApplications);
        tvPlacementRate = findViewById(R.id.tvPlacementRate);
        cardManageUsers = findViewById(R.id.cardManageUsers);
        cardManageJobs = findViewById(R.id.cardManageJobs);
        cardReports = findViewById(R.id.cardReports);
        cardSettings = findViewById(R.id.cardSettings);
        rvAllJobs = findViewById(R.id.rvAllJobs);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Admin Dashboard");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView tvNavName = headerView.findViewById(R.id.tvNavName);
        TextView tvNavEmail = headerView.findViewById(R.id.tvNavEmail);
        tvNavName.setText(sessionManager.getUserName());
        tvNavEmail.setText(sessionManager.getUserEmail());
        tvWelcome.setText("Welcome, Admin!");

        loadDashboardStats();

        allJobsList = new ArrayList<>();
        // ✅ Click goes to JobDetail, delete available there
        jobAdapter = new JobAdapter(this, allJobsList, job -> {
            Intent intent = new Intent(DashboardAdminActivity.this, JobDetailActivity.class);
            intent.putExtra("job_id", job.getId());
            startActivity(intent);
        });

        rvAllJobs.setLayoutManager(new LinearLayoutManager(this));
        rvAllJobs.setAdapter(jobAdapter);

        cardManageUsers.setOnClickListener(v -> Toast.makeText(this, "Use Database Inspector to manage users", Toast.LENGTH_SHORT).show());
        cardReports.setOnClickListener(v -> startActivity(new Intent(this, ReportActivity.class)));
        cardSettings.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        loadAllJobs();
    }

    private void loadDashboardStats() {
        try {
            tvTotalUsers.setText(String.valueOf(dbHelper.getTotalUsers()));
            tvTotalEmployers.setText(String.valueOf(dbHelper.getTotalUsersByRole("employer")));
            tvTotalStudents.setText(String.valueOf(dbHelper.getTotalUsersByRole("student")));
            tvTotalJobs.setText(String.valueOf(dbHelper.getTotalJobs()));
            tvOpenJobs.setText(String.valueOf(dbHelper.getOpenJobsCount()));
            tvTotalApplications.setText(String.valueOf(dbHelper.getTotalApplications()));
            tvPlacementRate.setText(dbHelper.getPlacementSuccessRate() + "%");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllJobs() {
        allJobsList.clear();
        allJobsList.addAll(dbHelper.getAllJobs());
        jobAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_dashboard) { }
        else if (id == R.id.nav_jobs) { }
        else if (id == R.id.nav_reports) { startActivity(new Intent(this, ReportActivity.class)); }
        else if (id == R.id.nav_profile) { startActivity(new Intent(this, ProfileActivity.class)); }
        else if (id == R.id.nav_logout) {
            sessionManager.logout();
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finishAffinity();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardStats();
        loadAllJobs();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }
}