package com.example.logbooksystem.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.logbooksystem.R;
import com.example.logbooksystem.database.DatabaseHelper;
import com.example.logbooksystem.models.User;
import com.example.logbooksystem.utils.Validations;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    // Role buttons
    private Button btnStudentRole, btnEmployerRole;

    // Common fields
    private TextInputEditText etEmail, etUserId, etPassword, etPhone;

    // Student fields
    private TextInputEditText etName, etDepartment, etSkills;
    private LinearLayout layoutStudentFields;

    // Employer fields
    private TextInputEditText etCompanyName, etContactPerson, etCompanyIndustry;
    private TextInputEditText etCompanyPosition, etCompanyLocation, etCompanyWebsite;
    private LinearLayout layoutEmployerFields;

    private Spinner spinnerRole;
    private TextView tvFormTitle;
    private Button btnRegister;
    private TextView tvLogin;

    private DatabaseHelper dbHelper;
    private String selectedRole = "student";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        // Role buttons
        btnStudentRole = findViewById(R.id.btnStudentRole);
        btnEmployerRole = findViewById(R.id.btnEmployerRole);

        // Common fields
        etEmail = findViewById(R.id.etEmail);
        etUserId = findViewById(R.id.etUserId);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);

        // Student fields
        etName = findViewById(R.id.etName);
        etDepartment = findViewById(R.id.etDepartment);
        etSkills = findViewById(R.id.etSkills);
        layoutStudentFields = findViewById(R.id.layoutStudentFields);

        // Employer fields
        etCompanyName = findViewById(R.id.etCompanyName);
        etContactPerson = findViewById(R.id.etContactPerson);
        etCompanyIndustry = findViewById(R.id.etCompanyIndustry);
        etCompanyPosition = findViewById(R.id.etCompanyPosition);
        etCompanyLocation = findViewById(R.id.etCompanyLocation);
        etCompanyWebsite = findViewById(R.id.etCompanyWebsite);
        layoutEmployerFields = findViewById(R.id.layoutEmployerFields);

        spinnerRole = findViewById(R.id.spinnerRole);
        tvFormTitle = findViewById(R.id.tvFormTitle);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // Setup spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
        spinnerRole.setSelection(0);

        // Role button clicks
        btnStudentRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRole("student");
            }
        });

        btnEmployerRole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRole("employer");
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void selectRole(String role) {
        selectedRole = role;

        if (role.equals("student")) {
            btnStudentRole.setBackgroundResource(R.drawable.bg_button_selected);
            btnStudentRole.setTextColor(getResources().getColor(R.color.white, null));

            btnEmployerRole.setBackgroundResource(R.drawable.bg_button_unselected);
            btnEmployerRole.setTextColor(getResources().getColor(R.color.gray_dark, null));

            layoutStudentFields.setVisibility(View.VISIBLE);
            layoutEmployerFields.setVisibility(View.GONE);
            tvFormTitle.setText("Student Registration");
            spinnerRole.setSelection(0);
        } else {
            btnEmployerRole.setBackgroundResource(R.drawable.bg_button_selected);
            btnEmployerRole.setTextColor(getResources().getColor(R.color.white, null));

            btnStudentRole.setBackgroundResource(R.drawable.bg_button_unselected);
            btnStudentRole.setTextColor(getResources().getColor(R.color.gray_dark, null));

            layoutStudentFields.setVisibility(View.GONE);
            layoutEmployerFields.setVisibility(View.VISIBLE);
            tvFormTitle.setText("Employer/Company Registration");
            spinnerRole.setSelection(1);
        }
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String userId = etUserId.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (email.isEmpty() || userId.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Validations.isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Validations.isValidPassword(password)) {
            Toast.makeText(this, "Password must be at least 8 characters with uppercase, lowercase & number", Toast.LENGTH_LONG).show();
            return;
        }

        if (!phone.isEmpty() && !Validations.isValidPhone(phone)) {
            Toast.makeText(this, "Please enter a valid phone number (10-13 digits)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.checkUserExists(email, userId)) {
            Toast.makeText(this, "Email or User ID already exists", Toast.LENGTH_LONG).show();
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setUserId(userId);
        user.setPassword(password);
        user.setPhone(phone);
        user.setRole(selectedRole);

        if (selectedRole.equals("student")) {
            String name = etName.getText().toString().trim();
            String department = etDepartment.getText().toString().trim();
            String skills = etSkills.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Validations.isValidName(name)) {
                Toast.makeText(this, "Please enter a valid name (letters only)", Toast.LENGTH_SHORT).show();
                return;
            }

            user.setName(name);
            user.setDepartment(department);
            user.setSkills(skills);

        } else {
            String companyName = etCompanyName.getText().toString().trim();
            String contactPerson = etContactPerson.getText().toString().trim();
            String companyIndustry = etCompanyIndustry.getText().toString().trim();
            String companyPosition = etCompanyPosition.getText().toString().trim();
            String companyLocation = etCompanyLocation.getText().toString().trim();
            String companyWebsite = etCompanyWebsite.getText().toString().trim();

            if (companyName.isEmpty() || contactPerson.isEmpty() ||
                    companyIndustry.isEmpty() || companyPosition.isEmpty()) {
                Toast.makeText(this, "Please fill all required company fields (*)", Toast.LENGTH_SHORT).show();
                return;
            }

            user.setName(contactPerson);
            user.setCompanyName(companyName);
            user.setDepartment(companyIndustry);
            user.setCompanyPosition(companyPosition);
        }

        long result = dbHelper.registerUser(user);
        if (result != -1) {
            String msg = selectedRole.equals("student")
                    ? "✅ Student account created! You can now browse and apply for jobs."
                    : "✅ Company account created! You can now post jobs and review applications.";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show();
        }
    }
}