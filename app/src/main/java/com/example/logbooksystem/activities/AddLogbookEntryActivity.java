package com.example.logbooksystem.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logbooksystem.R;
import com.example.logbooksystem.adapters.LogbookAdapter;
import com.example.logbooksystem.database.DatabaseHelper;
import com.example.logbooksystem.models.LogbookEntry;
import com.example.logbooksystem.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class AddLogbookEntryActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextInputEditText etTitle, etDescription, etProgress, etChallenges, etNextSteps;
    private Spinner spinnerType;
    private Button btnSave;
    private RecyclerView rvEntries;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private LogbookAdapter adapter;
    private List<LogbookEntry> entryList;
    private String selectedType = "Daily";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_logbook);

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etProgress = findViewById(R.id.etProgress);
        etChallenges = findViewById(R.id.etChallenges);
        etNextSteps = findViewById(R.id.etNextSteps);
        spinnerType = findViewById(R.id.spinnerType);
        btnSave = findViewById(R.id.btnSave);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this,
                R.array.logbook_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedType = "Daily";
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEntry();
            }
        });
    }

    private void saveEntry() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String progress = etProgress.getText().toString().trim();
        String challenges = etChallenges.getText().toString().trim();
        String nextSteps = etNextSteps.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            return;
        }

        LogbookEntry entry = new LogbookEntry();
        entry.setUserId(sessionManager.getUserId());
        entry.setTitle(title);
        entry.setDescription(description);
        entry.setType(selectedType);
        entry.setProgress(progress);
        entry.setChallenges(challenges);
        entry.setNextSteps(nextSteps);

        long result = dbHelper.addLogbookEntry(entry);
        if (result != -1) {
            Toast.makeText(this, "Logbook entry saved!", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Failed to save entry", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        etTitle.setText("");
        etDescription.setText("");
        etProgress.setText("");
        etChallenges.setText("");
        etNextSteps.setText("");
    }
}