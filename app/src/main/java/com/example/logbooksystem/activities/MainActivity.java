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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.logbooksystem.R;
import com.example.logbooksystem.adapters.JobAdapter;
import com.example.logbooksystem.database.DatabaseHelper;
import com.example.logbooksystem.models.JobOpportunity;
import com.example.logbooksystem.utils.SessionManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private RecyclerView rvJobs;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tvWelcome;
    private FloatingActionButton fabAddJob;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private JobAdapter jobAdapter;
    private List<JobOpportunity> jobList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        rvJobs = findViewById(R.id.rvJobs);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        tvWelcome = findViewById(R.id.tvWelcome);
        fabAddJob = findViewById(R.id.fabAddJob);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Available Opportunities");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Update navigation header
        View headerView = navigationView.getHeaderView(0);
        TextView tvNavName = headerView.findViewById(R.id.tvNavName);
        TextView tvNavEmail = headerView.findViewById(R.id.tvNavEmail);
        tvNavName.setText(sessionManager.getUserName());
        tvNavEmail.setText(sessionManager.getUserEmail());

        tvWelcome.setText("Welcome, " + sessionManager.getUserName() + "! 👋");

        // Show FAB only for admin/employer
        String role = sessionManager.getUserRole();
        if (role != null && (role.equals("admin") || role.equals("employer"))) {
            fabAddJob.setVisibility(View.VISIBLE);
        }

        fabAddJob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Add Job feature - implement dialog", Toast.LENGTH_SHORT).show();
            }
        });

        jobList = new ArrayList<>();
        jobAdapter = new JobAdapter(this, jobList, new JobAdapter.OnJobClickListener() {
            @Override
            public void onJobClick(JobOpportunity job) {
                Intent intent = new Intent(MainActivity.this, JobDetailActivity.class);
                intent.putExtra("job_id", job.getId());
                startActivity(intent);
            }
        });

        rvJobs.setLayoutManager(new LinearLayoutManager(this));
        rvJobs.setAdapter(jobAdapter);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJobs();
                swipeRefresh.setRefreshing(false);
            }
        });

        loadJobs();
    }

    private void loadJobs() {
        jobList.clear();
        jobList.addAll(dbHelper.getAllJobs());
        jobAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_jobs) {
            // Already on jobs screen
        } else if (id == R.id.nav_applications) {
            startActivity(new Intent(this, ApplicationStatusActivity.class));
        } else if (id == R.id.nav_feedback) {
            startActivity(new Intent(this, FeedbackActivity.class));
        } else if (id == R.id.nav_logbook) {
            startActivity(new Intent(this, AddLogbookEntryActivity.class));
        } else if (id == R.id.nav_reports) {
            startActivity(new Intent(this, ReportActivity.class));
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadJobs();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}