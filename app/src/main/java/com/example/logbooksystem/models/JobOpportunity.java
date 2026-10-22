package com.example.logbooksystem.models;

public class JobOpportunity {
    private int id;
    private String firebaseId; // NEW: Firebase document ID
    private String title;
    private String company;
    private String description;
    private String requirements;
    private String location;
    private String type;
    private String salary;
    private String deadline;
    private String postedDate;
    private int vacancies;
    private String status;

    public JobOpportunity() {}

    // Add getter/setter for firebaseId
    public String getFirebaseId() { return firebaseId; }
    public void setFirebaseId(String firebaseId) { this.firebaseId = firebaseId; }

    // ... rest of getters/setters remain the same
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public String getPostedDate() { return postedDate; }
    public void setPostedDate(String postedDate) { this.postedDate = postedDate; }
    public int getVacancies() { return vacancies; }
    public void setVacancies(int vacancies) { this.vacancies = vacancies; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}