package com.example.logbooksystem.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.logbooksystem.R;
import com.example.logbooksystem.database.DatabaseHelper;
import com.example.logbooksystem.models.JobOpportunity;
import com.example.logbooksystem.models.User;
import com.example.logbooksystem.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

public class PostJobActivity extends AppCompatActivity {
    private TextInputEditText etTitle, etDescription, etRequirements, etLocation, etSalary, etDeadline, etVacancies;
    private Spinner spinnerType;
    private Button btnPostJob;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private String selectedType = "Full-time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Post New Job");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etRequirements = findViewById(R.id.etRequirements);
        etLocation = findViewById(R.id.etLocation);
        etSalary = findViewById(R.id.etSalary);
        etDeadline = findViewById(R.id.etDeadline);
        etVacancies = findViewById(R.id.etVacancies);
        spinnerType = findViewById(R.id.spinnerType);
        btnPostJob = findViewById(R.id.btnPostJob);

        String[] jobTypes = {"Full-time", "Part-time", "Internship", "Contract", "Remote"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jobTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                selectedType = jobTypes[position];
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        btnPostJob.setOnClickListener(v -> postJob());
    }

    private void postJob() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String requirements = etRequirements.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String salary = etSalary.getText().toString().trim();
        String deadline = etDeadline.getText().toString().trim();
        String vacanciesStr = etVacancies.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || location.isEmpty() || deadline.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields (*)", Toast.LENGTH_SHORT).show();
            return;
        }

        int vacancies = 1;
        try { if (!vacanciesStr.isEmpty()) vacancies = Integer.parseInt(vacanciesStr); } catch (Exception e) {}

        User user = dbHelper.getUserById(sessionManager.getUserId());
        String companyName;

        if (user != null && user.getCompanyName() != null && !user.getCompanyName().isEmpty()) {
            companyName = user.getCompanyName();
        } else {
            Toast.makeText(this, "Please update your company name in Profile first", Toast.LENGTH_LONG).show();
            return;
        }

        JobOpportunity job = new JobOpportunity();
        job.setTitle(title);
        job.setCompany(companyName);
        job.setDescription(description);
        job.setRequirements(requirements);
        job.setLocation(location);
        job.setType(selectedType);
        job.setSalary(salary);
        job.setDeadline(deadline);
        job.setVacancies(vacancies);

        long result = dbHelper.addJob(job, sessionManager.getUserId());
        if (result != -1) {
            Toast.makeText(this, "Job posted successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to post job", Toast.LENGTH_SHORT).show();
        }
    }
}