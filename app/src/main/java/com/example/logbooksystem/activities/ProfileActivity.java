package com.example.logbooksystem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.logbooksystem.R;
import com.example.logbooksystem.database.DatabaseHelper;
import com.example.logbooksystem.models.User;
import com.example.logbooksystem.utils.SessionManager;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView tvName, tvRole, tvEmail, tvUserId, tvDepartment, tvPhone;
    private TextView tvCompanyName, tvCompanyPosition, tvSkills;
    private LinearLayout layoutStudentExtra, layoutEmployerExtra;
    private Button btnEditProfile, btnLogout;
    private SessionManager sessionManager;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);
        dbHelper = new DatabaseHelper(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("My Profile");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        tvName = findViewById(R.id.tvName);
        tvRole = findViewById(R.id.tvRole);
        tvEmail = findViewById(R.id.tvEmail);
        tvUserId = findViewById(R.id.tvUserId);
        tvDepartment = findViewById(R.id.tvDepartment);
        tvPhone = findViewById(R.id.tvPhone);
        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvCompanyPosition = findViewById(R.id.tvCompanyPosition);
        tvSkills = findViewById(R.id.tvSkills);
        layoutStudentExtra = findViewById(R.id.layoutStudentExtra);
        layoutEmployerExtra = findViewById(R.id.layoutEmployerExtra);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnLogout = findViewById(R.id.btnLogout);

        loadUserProfile();

        btnEditProfile.setOnClickListener(v -> showEditProfileDialog());
        btnLogout.setOnClickListener(v -> logout());
    }

    private void loadUserProfile() {
        User user = sessionManager.getUserDetails();
        if (user != null) {
            User freshUser = dbHelper.getUserById(user.getId());
            if (freshUser == null) freshUser = user;

            tvName.setText(freshUser.getName() != null ? freshUser.getName() : "N/A");
            String roleDisplay = freshUser.getRole() != null ?
                    freshUser.getRole().substring(0, 1).toUpperCase() + freshUser.getRole().substring(1) : "N/A";
            tvRole.setText("Role: " + roleDisplay);
            tvEmail.setText(freshUser.getEmail() != null ? freshUser.getEmail() : "N/A");
            tvUserId.setText(freshUser.getUserId() != null ? freshUser.getUserId() : "N/A");
            tvDepartment.setText(freshUser.getDepartment() != null ? freshUser.getDepartment() : "N/A");
            tvPhone.setText(freshUser.getPhone() != null ? freshUser.getPhone() : "N/A");

            if (freshUser.getRole() != null) {
                if (freshUser.getRole().equals("student")) {
                    layoutStudentExtra.setVisibility(View.VISIBLE);
                    layoutEmployerExtra.setVisibility(View.GONE);
                    tvSkills.setText(freshUser.getSkills() != null ? freshUser.getSkills() : "No skills listed");
                } else if (freshUser.getRole().equals("employer")) {
                    layoutStudentExtra.setVisibility(View.GONE);
                    layoutEmployerExtra.setVisibility(View.VISIBLE);
                    tvCompanyName.setText(freshUser.getCompanyName() != null ? freshUser.getCompanyName() : "N/A");
                    tvCompanyPosition.setText(freshUser.getCompanyPosition() != null ? freshUser.getCompanyPosition() : "N/A");
                } else {
                    layoutStudentExtra.setVisibility(View.GONE);
                    layoutEmployerExtra.setVisibility(View.GONE);
                }
            }
        }
    }

    private void showEditProfileDialog() {
        User user = sessionManager.getUserDetails();
        if (user == null) return;

        // Get fresh data - make it final
        final User freshUser = dbHelper.getUserById(user.getId());
        if (freshUser == null) {
            Toast.makeText(this, "Error loading profile", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Profile");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 10);

        // Name field
        final EditText etName = new EditText(this);
        etName.setHint("Name");
        etName.setText(freshUser.getName() != null ? freshUser.getName() : "");
        layout.addView(etName);

        // Phone field
        final EditText etPhone = new EditText(this);
        etPhone.setHint("Phone");
        etPhone.setText(freshUser.getPhone() != null ? freshUser.getPhone() : "");
        layout.addView(etPhone);

        // Department field
        final EditText etDepartment = new EditText(this);
        etDepartment.setHint("Department");
        etDepartment.setText(freshUser.getDepartment() != null ? freshUser.getDepartment() : "");
        layout.addView(etDepartment);

        // Role-specific fields
        final EditText etSkills = new EditText(this);
        final EditText etCompanyName = new EditText(this);
        final EditText etCompanyPosition = new EditText(this);

        final String role = freshUser.getRole();
        if (role != null) {
            if (role.equals("student")) {
                etSkills.setHint("Skills");
                etSkills.setText(freshUser.getSkills() != null ? freshUser.getSkills() : "");
                layout.addView(etSkills);
            } else if (role.equals("employer")) {
                etCompanyName.setHint("Company Name");
                etCompanyName.setText(freshUser.getCompanyName() != null ? freshUser.getCompanyName() : "");
                layout.addView(etCompanyName);

                etCompanyPosition.setHint("Your Position");
                etCompanyPosition.setText(freshUser.getCompanyPosition() != null ? freshUser.getCompanyPosition() : "");
                layout.addView(etCompanyPosition);
            }
        }

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String department = etDepartment.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(ProfileActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update the user object
            freshUser.setName(name);
            freshUser.setPhone(phone);
            freshUser.setDepartment(department);

            if (role != null) {
                if (role.equals("student")) {
                    freshUser.setSkills(etSkills.getText().toString().trim());
                } else if (role.equals("employer")) {
                    freshUser.setCompanyName(etCompanyName.getText().toString().trim());
                    freshUser.setCompanyPosition(etCompanyPosition.getText().toString().trim());
                }
            }

            // Save to database
            boolean updated = dbHelper.updateUser(freshUser);
            if (updated) {
                sessionManager.updateUserData(freshUser);
                loadUserProfile();
                Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void logout() {
        sessionManager.logout();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}