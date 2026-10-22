package com.example.logbooksystem.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.logbooksystem.R;
import com.example.logbooksystem.adapters.FeedbackAdapter;
import com.example.logbooksystem.database.DatabaseHelper;
import com.example.logbooksystem.models.Feedback;
import com.example.logbooksystem.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rvFeedback;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private FeedbackAdapter adapter;
    private List<Feedback> feedbackList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

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

        rvFeedback = findViewById(R.id.rvFeedback);

        feedbackList = new ArrayList<>();
        adapter = new FeedbackAdapter(this, feedbackList);

        rvFeedback.setLayoutManager(new LinearLayoutManager(this));
        rvFeedback.setAdapter(adapter);

        loadFeedback();
    }

    private void loadFeedback() {
        feedbackList.clear();
        int userId = sessionManager.getUserId();
        feedbackList.addAll(dbHelper.getUserFeedback(userId));
        adapter.notifyDataSetChanged();

        if (feedbackList.isEmpty()) {
            Toast.makeText(this, "No feedback received yet", Toast.LENGTH_SHORT).show();
        }
    }
}