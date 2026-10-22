package com.example.logbooksystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.logbooksystem.R;
import com.example.logbooksystem.database.DatabaseHelper;
import com.example.logbooksystem.models.User;
import com.example.logbooksystem.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputEditText etUserId = findViewById(R.id.etUserId);
        TextInputEditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SessionManager sessionManager = new SessionManager(this);

        btnLogin.setOnClickListener(v -> {
            String uid = etUserId.getText().toString().trim();
            String pwd = etPassword.getText().toString().trim();

            if (uid.isEmpty() || pwd.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = dbHelper.loginUser(uid, pwd);
            if (user != null && user.getRole() != null) {
                sessionManager.createLoginSession(user);
                Toast.makeText(this, "Welcome, " + user.getName() + "!", Toast.LENGTH_SHORT).show();

                Intent intent;
                switch (user.getRole()) {
                    case "admin": intent = new Intent(this, DashboardAdminActivity.class); break;
                    case "employer": intent = new Intent(this, DashboardEmployerActivity.class); break;
                    default: intent = new Intent(this, DashboardStudentActivity.class); break;
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid User ID or Password", Toast.LENGTH_SHORT).show();
            }
        });

        tvRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }
}