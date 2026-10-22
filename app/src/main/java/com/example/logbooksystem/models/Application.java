package com.example.logbooksystem.models;

public class Application {
    private int id;
    private int userId;
    private int jobId;
    private String studentName;
    private String jobTitle;
    private String company;
    private String status;
    private String appliedDate;
    private String lastUpdate;
    private String resume;
    private String coverLetter;

    public Application() {}

    public Application(int id, int userId, int jobId, String status, String appliedDate) {
        this.id = id;
        this.userId = userId;
        this.jobId = jobId;
        this.status = status;
        this.appliedDate = appliedDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAppliedDate() { return appliedDate; }
    public void setAppliedDate(String appliedDate) { this.appliedDate = appliedDate; }

    public String getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }

    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }

    public String getCoverLetter() { return coverLetter; }
    public void setCoverLetter(String coverLetter) { this.coverLetter = coverLetter; }
}