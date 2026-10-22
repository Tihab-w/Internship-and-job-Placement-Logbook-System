package com.example.logbooksystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.logbooksystem.models.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LogbookDB";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, email TEXT UNIQUE, user_id TEXT UNIQUE, password TEXT, role TEXT DEFAULT 'student', department TEXT, phone TEXT, skills TEXT, company_name TEXT, company_position TEXT, created_at DATETIME DEFAULT CURRENT_TIMESTAMP)");
        db.execSQL("CREATE TABLE jobs (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, company TEXT, description TEXT, requirements TEXT, location TEXT, type TEXT, salary TEXT, deadline TEXT, posted_date DATETIME DEFAULT CURRENT_TIMESTAMP, vacancies INTEGER DEFAULT 1, status TEXT DEFAULT 'Open', posted_by INTEGER)");
        db.execSQL("CREATE TABLE applications (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, job_id INTEGER, status TEXT DEFAULT 'Applied', applied_date DATETIME DEFAULT CURRENT_TIMESTAMP, last_update DATETIME DEFAULT CURRENT_TIMESTAMP, resume TEXT, cover_letter TEXT, reviewed_by INTEGER)");
        db.execSQL("CREATE TABLE feedback (id INTEGER PRIMARY KEY AUTOINCREMENT, application_id INTEGER, employer_id INTEGER, rating INTEGER, comment TEXT, feedback_date DATETIME DEFAULT CURRENT_TIMESTAMP, strengths TEXT, improvements TEXT)");
        db.execSQL("CREATE TABLE logbook (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, application_id INTEGER, title TEXT, description TEXT, entry_date DATETIME DEFAULT CURRENT_TIMESTAMP, type TEXT DEFAULT 'Daily', progress TEXT, challenges TEXT, next_steps TEXT)");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String now = sdf.format(new Date());
        ContentValues cv = new ContentValues();

        // ============ ETHIOPIAN USERS ============

        // Admin
        cv.put("name", "System Admin"); cv.put("email", "admin@logbook.com"); cv.put("user_id", "ADM001");
        cv.put("password", "admin123"); cv.put("role", "admin"); cv.put("department", "Administration");
        cv.put("phone", "+251911234567");
        db.insert("users", null, cv);

        // Student - Ethiopian
        cv.clear(); cv.put("name", "Abebe Kebede"); cv.put("email", "abebe@student.com"); cv.put("user_id", "STU001");
        cv.put("password", "john123"); cv.put("role", "student"); cv.put("department", "Computer Science");
        cv.put("phone", "+251922345678"); cv.put("skills", "Java, Android, SQL, Python");
        db.insert("users", null, cv);

        // Employer 1 - Ethio Telecom
        cv.clear(); cv.put("name", "Meron Tadesse"); cv.put("email", "meron@ethiotelecom.et"); cv.put("user_id", "EMP001");
        cv.put("password", "jane123"); cv.put("role", "employer"); cv.put("department", "Human Resources");
        cv.put("phone", "+251933456789"); cv.put("company_name", "Ethio Telecom"); cv.put("company_position", "HR Manager");
        db.insert("users", null, cv);

        // Employer 2 - Safaricom Ethiopia
        cv.clear(); cv.put("name", "Dawit Haile"); cv.put("email", "dawit@safaricom.et"); cv.put("user_id", "EMP002");
        cv.put("password", "bob123"); cv.put("role", "employer"); cv.put("department", "Engineering");
        cv.put("phone", "+251944567890"); cv.put("company_name", "Safaricom Ethiopia"); cv.put("company_position", "Tech Lead");
        db.insert("users", null, cv);

        // Employer 3 - Dashen Bank
        cv.clear(); cv.put("name", "Selam Gebre"); cv.put("email", "selam@dashenbank.com"); cv.put("user_id", "EMP003");
        cv.put("password", "selam123"); cv.put("role", "employer"); cv.put("department", "IT Department");
        cv.put("phone", "+251955678901"); cv.put("company_name", "Dashen Bank"); cv.put("company_position", "IT Director");
        db.insert("users", null, cv);

        // ============ ETHIOPIAN JOBS ============

        String[][] jobs = {
                {"Junior Software Developer", "Ethio Telecom", "Develop and maintain telecom applications and services. Work with senior developers on large-scale projects in Ethiopia's leading telecom company.", "Java, Python, SQL, Bachelor's in CS or related field", "Addis Ababa, Bole", "Full-time", "25,000 ETB/month", "2025-07-15"},

                {"Mobile App Developer Intern", "Safaricom Ethiopia", "Design and develop mobile applications for Android platform. Collaborate with the product team at Ethiopia's newest telecom provider.", "Android, Kotlin, UI/UX basics, Currently enrolled in university", "Addis Ababa, Kazanchis", "Internship", "8,000 ETB/month", "2025-06-30"},

                {"Web Developer", "Dashen Bank", "Build and maintain banking web applications. Ensure security and performance of financial systems for one of Ethiopia's largest banks.", "HTML/CSS, JavaScript, React, 2+ years experience", "Addis Ababa, Mexico", "Full-time", "30,000 ETB/month", "2025-07-01"},

                {"Data Analyst", "Ethiopian Airlines", "Analyze flight data, customer data, and create business intelligence reports for Africa's leading airline.", "SQL, Python, Power BI, Statistics degree", "Addis Ababa, Bole Airport", "Full-time", "28,000 ETB/month", "2025-07-10"},

                {"Network Administrator", "Commercial Bank of Ethiopia", "Manage and maintain the bank's network infrastructure across all branches in Ethiopia.", "CCNA, Network Security, Linux, 3+ years experience", "Addis Ababa, Head Office", "Full-time", "22,000 ETB/month", "2025-07-05"},

                {"Software Engineer Intern", "Ice Addis", "Join our innovation hub to work on startup projects and learn modern software development practices.", "Any programming language, Passion for coding, Currently enrolled", "Addis Ababa, Bole Medhanialem", "Internship", "6,000 ETB/month", "2025-06-25"},

                {"IT Support Specialist", "Awash Bank", "Provide technical support to bank employees and maintain IT equipment across branches.", "Computer Science degree, Hardware knowledge, Customer service skills", "Addis Ababa", "Full-time", "15,000 ETB/month", "2025-07-20"},

                {"UI/UX Designer", "Ride Ethiopia", "Design user interfaces for ride-hailing and delivery applications used across Ethiopian cities.", "Figma, Adobe XD, Portfolio required, 1+ year experience", "Addis Ababa, CMC", "Full-time", "20,000 ETB/month", "2025-07-15"},

                {"Backend Developer", "Amole", "Develop backend services for digital payment and e-commerce platform serving millions of Ethiopians.", "Java Spring Boot, PostgreSQL, REST APIs, Microservices", "Addis Ababa, Bole Rwanda", "Full-time", "35,000 ETB/month", "2025-07-08"},

                {"Digital Marketing Intern", "Addis Ababa University", "Manage social media and digital marketing campaigns for Ethiopia's largest university.", "Marketing knowledge, Social media skills, Currently enrolled", "Addis Ababa, 6 Kilo", "Internship", "5,000 ETB/month", "2025-06-28"},

                {"System Administrator", "Ministry of Innovation & Technology", "Manage government IT infrastructure and cloud services for Ethiopia's digital transformation.", "Linux, Windows Server, Cloud (AWS/Azure), Security clearance", "Addis Ababa, Arat Kilo", "Full-time", "18,000 ETB/month", "2025-07-12"},

                {"Frontend Developer", "Beu Delivery", "Build responsive web and mobile interfaces for food delivery platform operating in Addis Ababa.", "React, HTML/CSS, JavaScript, 1+ year experience", "Addis Ababa, Megenagna", "Part-time", "12,000 ETB/month", "2025-07-05"},

                {"Database Administrator", "Ethio Telecom", "Manage and optimize large-scale databases supporting millions of telecom subscribers.", "Oracle, MySQL, Database tuning, 3+ years experience", "Addis Ababa, Head Office", "Full-time", "27,000 ETB/month", "2025-07-18"},

                {"Cybersecurity Analyst", "CBE", "Protect banking systems from cyber threats and ensure compliance with security standards.", "Security+, CEH, Network security, Banking experience", "Addis Ababa", "Full-time", "32,000 ETB/month", "2025-07-22"}
        };

        // First 4 jobs by EMP001 (Ethio Telecom - user id 3)
        int[] postedBy = {3, 4, 5, 3, 5, 3, 5, 4, 4, 3, 5, 4, 3, 5};

        for (int i = 0; i < jobs.length; i++) {
            String[] job = jobs[i];
            cv.clear();
            cv.put("title", job[0]);
            cv.put("company", job[1]);
            cv.put("description", job[2]);
            cv.put("requirements", job[3]);
            cv.put("location", job[4]);
            cv.put("type", job[5]);
            cv.put("salary", job[6]);
            cv.put("deadline", job[7]);
            cv.put("posted_date", now);
            cv.put("vacancies", 3);
            cv.put("status", "Open");
            cv.put("posted_by", postedBy[i]);
            db.insert("jobs", null, cv);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS logbook"); db.execSQL("DROP TABLE IF EXISTS feedback");
        db.execSQL("DROP TABLE IF EXISTS applications"); db.execSQL("DROP TABLE IF EXISTS jobs");
        db.execSQL("DROP TABLE IF EXISTS users"); onCreate(db);
    }

    // ============ USER METHODS ============
    public long registerUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("name", user.getName()); v.put("email", user.getEmail()); v.put("user_id", user.getUserId());
        v.put("password", user.getPassword()); v.put("role", user.getRole() != null ? user.getRole() : "student");
        v.put("department", user.getDepartment()); v.put("phone", user.getPhone());
        v.put("skills", user.getSkills()); v.put("company_name", user.getCompanyName());
        v.put("company_position", user.getCompanyPosition());
        long r = db.insert("users", null, v); db.close(); return r;
    }

    public User loginUser(String userId, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM users WHERE user_id = ? AND password = ?", new String[]{userId, password});
        User u = null;
        if (c != null && c.moveToFirst()) {
            u = new User(); u.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            u.setName(c.getString(c.getColumnIndexOrThrow("name")));
            u.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
            u.setUserId(c.getString(c.getColumnIndexOrThrow("user_id")));
            u.setRole(c.getString(c.getColumnIndexOrThrow("role")));
            u.setDepartment(c.getString(c.getColumnIndexOrThrow("department")));
            u.setPhone(c.getString(c.getColumnIndexOrThrow("phone")));
            u.setSkills(getCol(c, "skills")); u.setCompanyName(getCol(c, "company_name"));
            u.setCompanyPosition(getCol(c, "company_position"));
            c.close();
        }
        db.close(); return u;
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM users WHERE id = ?", new String[]{String.valueOf(userId)});
        User u = null;
        if (c != null && c.moveToFirst()) {
            u = new User(); u.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            u.setName(c.getString(c.getColumnIndexOrThrow("name")));
            u.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
            u.setUserId(c.getString(c.getColumnIndexOrThrow("user_id")));
            u.setRole(c.getString(c.getColumnIndexOrThrow("role")));
            u.setDepartment(c.getString(c.getColumnIndexOrThrow("department")));
            u.setPhone(c.getString(c.getColumnIndexOrThrow("phone")));
            u.setSkills(getCol(c, "skills")); u.setCompanyName(getCol(c, "company_name"));
            u.setCompanyPosition(getCol(c, "company_position"));
            c.close();
        }
        db.close(); return u;
    }

    public boolean checkUserExists(String email, String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM users WHERE email = ? OR user_id = ?", new String[]{email, userId});
        boolean ex = false; if (c.moveToFirst()) ex = c.getInt(0) > 0; c.close(); db.close(); return ex;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("name", user.getName()); v.put("phone", user.getPhone()); v.put("department", user.getDepartment());
        v.put("skills", user.getSkills()); v.put("company_name", user.getCompanyName());
        v.put("company_position", user.getCompanyPosition());
        int r = db.update("users", v, "id = ?", new String[]{String.valueOf(user.getId())}); db.close(); return r > 0;
    }

    private String getCol(Cursor c, String col) {
        int idx = c.getColumnIndex(col); return idx >= 0 ? c.getString(idx) : null;
    }

    // ============ JOB METHODS ============
    public List<JobOpportunity> getAllJobs() {
        List<JobOpportunity> list = new ArrayList<>(); SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM jobs ORDER BY posted_date DESC", null);
        if (c.moveToFirst()) { do { list.add(cursorToJob(c)); } while (c.moveToNext()); }
        c.close(); db.close(); return list;
    }

    public List<JobOpportunity> getFilteredJobs(String query, String type, String location) {
        List<JobOpportunity> list = new ArrayList<>(); SQLiteDatabase db = this.getReadableDatabase();
        StringBuilder sql = new StringBuilder("SELECT * FROM jobs WHERE 1=1");
        List<String> params = new ArrayList<>();
        if (query != null && !query.isEmpty()) { sql.append(" AND (title LIKE ? OR company LIKE ?)"); params.add("%"+query+"%"); params.add("%"+query+"%"); }
        if (type != null && !type.equals("All")) { sql.append(" AND type = ?"); params.add(type); }
        if (location != null && !location.isEmpty()) { sql.append(" AND location LIKE ?"); params.add("%"+location+"%"); }
        sql.append(" ORDER BY posted_date DESC");
        Cursor c = db.rawQuery(sql.toString(), params.toArray(new String[0]));
        if (c.moveToFirst()) { do { list.add(cursorToJob(c)); } while (c.moveToNext()); }
        c.close(); db.close(); return list;
    }

    public JobOpportunity getJobById(int jobId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM jobs WHERE id = ?", new String[]{String.valueOf(jobId)});
        JobOpportunity j = null; if (c != null && c.moveToFirst()) { j = cursorToJob(c); c.close(); } db.close(); return j;
    }

    public List<JobOpportunity> getJobsByEmployer(int employerId) {
        List<JobOpportunity> list = new ArrayList<>(); SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM jobs WHERE posted_by = ? ORDER BY posted_date DESC", new String[]{String.valueOf(employerId)});
        if (c.moveToFirst()) { do { list.add(cursorToJob(c)); } while (c.moveToNext()); }
        c.close(); db.close(); return list;
    }

    public long addJob(JobOpportunity job, int postedBy) {
        SQLiteDatabase db = this.getWritableDatabase(); ContentValues v = new ContentValues();
        v.put("title", job.getTitle()); v.put("company", job.getCompany()); v.put("description", job.getDescription());
        v.put("requirements", job.getRequirements()); v.put("location", job.getLocation()); v.put("type", job.getType());
        v.put("salary", job.getSalary()); v.put("deadline", job.getDeadline()); v.put("vacancies", job.getVacancies());
        v.put("posted_by", postedBy); v.put("status", "Open");
        v.put("posted_date", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        long r = db.insert("jobs", null, v); db.close(); return r;
    }

    public boolean deleteJob(int jobId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("applications", "job_id = ?", new String[]{String.valueOf(jobId)});
        int r = db.delete("jobs", "id = ?", new String[]{String.valueOf(jobId)}); db.close(); return r > 0;
    }

    private JobOpportunity cursorToJob(Cursor c) {
        JobOpportunity j = new JobOpportunity(); j.setId(c.getInt(c.getColumnIndexOrThrow("id")));
        j.setTitle(c.getString(c.getColumnIndexOrThrow("title"))); j.setCompany(c.getString(c.getColumnIndexOrThrow("company")));
        j.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
        j.setRequirements(c.getString(c.getColumnIndexOrThrow("requirements")));
        j.setLocation(c.getString(c.getColumnIndexOrThrow("location"))); j.setType(c.getString(c.getColumnIndexOrThrow("type")));
        j.setSalary(c.getString(c.getColumnIndexOrThrow("salary"))); j.setDeadline(c.getString(c.getColumnIndexOrThrow("deadline")));
        j.setVacancies(c.getInt(c.getColumnIndexOrThrow("vacancies"))); j.setStatus(c.getString(c.getColumnIndexOrThrow("status")));
        return j;
    }

    // ============ APPLICATION METHODS ============
    public long applyForJob(int userId, int jobId, String resume, String coverLetter) {
        SQLiteDatabase db = this.getWritableDatabase(); ContentValues v = new ContentValues();
        v.put("user_id", userId); v.put("job_id", jobId); v.put("status", "Applied");
        v.put("resume", resume); v.put("cover_letter", coverLetter);
        String d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        v.put("applied_date", d); v.put("last_update", d);
        long r = db.insert("applications", null, v); db.close(); return r;
    }

    public List<Application> getUserApplications(int userId) {
        List<Application> list = new ArrayList<>(); SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT a.*, u.name as sname, j.title as jtitle, j.company as jcomp FROM applications a JOIN users u ON a.user_id=u.id JOIN jobs j ON a.job_id=j.id WHERE a.user_id=? ORDER BY a.applied_date DESC", new String[]{String.valueOf(userId)});
        if (c.moveToFirst()) { do { list.add(cursorToApp(c)); } while (c.moveToNext()); }
        c.close(); db.close(); return list;
    }

    public List<Application> getApplicationsForEmployerJobs(int employerId) {
        List<Application> list = new ArrayList<>(); SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT a.*, u.name as sname, j.title as jtitle, j.company as jcomp FROM applications a JOIN users u ON a.user_id=u.id JOIN jobs j ON a.job_id=j.id WHERE j.posted_by=? ORDER BY a.applied_date DESC", new String[]{String.valueOf(employerId)});
        if (c.moveToFirst()) { do { list.add(cursorToApp(c)); } while (c.moveToNext()); }
        c.close(); db.close(); return list;
    }

    public boolean updateApplicationStatus(int appId, String status, int reviewedBy) {
        SQLiteDatabase db = this.getWritableDatabase(); ContentValues v = new ContentValues();
        v.put("status", status); v.put("reviewed_by", reviewedBy);
        v.put("last_update", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        int r = db.update("applications", v, "id = ?", new String[]{String.valueOf(appId)}); db.close(); return r > 0;
    }

    public boolean hasUserApplied(int userId, int jobId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM applications WHERE user_id=? AND job_id=?", new String[]{String.valueOf(userId), String.valueOf(jobId)});
        boolean ex = false; if (c.moveToFirst()) ex = c.getInt(0) > 0; c.close(); db.close(); return ex;
    }

    public boolean deleteApplication(int applicationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("feedback", "application_id = ?", new String[]{String.valueOf(applicationId)});
        int r = db.delete("applications", "id = ?", new String[]{String.valueOf(applicationId)}); db.close(); return r > 0;
    }

    private Application cursorToApp(Cursor c) {
        Application a = new Application(); a.setId(c.getInt(c.getColumnIndexOrThrow("id")));
        a.setUserId(c.getInt(c.getColumnIndexOrThrow("user_id"))); a.setJobId(c.getInt(c.getColumnIndexOrThrow("job_id")));
        a.setStatus(c.getString(c.getColumnIndexOrThrow("status")));
        a.setAppliedDate(c.getString(c.getColumnIndexOrThrow("applied_date")));
        a.setLastUpdate(c.getString(c.getColumnIndexOrThrow("last_update")));
        a.setResume(c.getString(c.getColumnIndexOrThrow("resume")));
        a.setCoverLetter(c.getString(c.getColumnIndexOrThrow("cover_letter")));
        a.setStudentName(c.getString(c.getColumnIndexOrThrow("sname")));
        a.setJobTitle(c.getString(c.getColumnIndexOrThrow("jtitle")));
        a.setCompany(c.getString(c.getColumnIndexOrThrow("jcomp")));
        return a;
    }

    // ============ FEEDBACK METHODS ============
    public long addFeedback(Feedback f) {
        SQLiteDatabase db = this.getWritableDatabase(); ContentValues v = new ContentValues();
        v.put("application_id", f.getApplicationId()); v.put("employer_id", f.getEmployerId());
        v.put("rating", f.getRating()); v.put("comment", f.getComment());
        v.put("strengths", f.getStrengths()); v.put("improvements", f.getImprovements());
        long r = db.insert("feedback", null, v); db.close(); return r;
    }

    public List<Feedback> getUserFeedback(int userId) {
        List<Feedback> list = new ArrayList<>(); SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT f.*, u.name as ename, u2.name as sname, j.title as jtitle FROM feedback f JOIN users u ON f.employer_id=u.id JOIN applications a ON f.application_id=a.id JOIN users u2 ON a.user_id=u2.id JOIN jobs j ON a.job_id=j.id WHERE a.user_id=? ORDER BY f.feedback_date DESC", new String[]{String.valueOf(userId)});
        if (c.moveToFirst()) { do {
            Feedback f = new Feedback(); f.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            f.setApplicationId(c.getInt(c.getColumnIndexOrThrow("application_id")));
            f.setEmployerId(c.getInt(c.getColumnIndexOrThrow("employer_id")));
            f.setRating(c.getInt(c.getColumnIndexOrThrow("rating")));
            f.setComment(c.getString(c.getColumnIndexOrThrow("comment")));
            f.setFeedbackDate(c.getString(c.getColumnIndexOrThrow("feedback_date")));
            f.setEmployerName(c.getString(c.getColumnIndexOrThrow("ename")));
            f.setStudentName(c.getString(c.getColumnIndexOrThrow("sname")));
            f.setJobTitle(c.getString(c.getColumnIndexOrThrow("jtitle")));
            list.add(f);
        } while (c.moveToNext()); }
        c.close(); db.close(); return list;
    }

    // ============ LOGBOOK METHODS ============
    public long addLogbookEntry(LogbookEntry e) {
        SQLiteDatabase db = this.getWritableDatabase(); ContentValues v = new ContentValues();
        v.put("user_id", e.getUserId()); v.put("application_id", e.getApplicationId());
        v.put("title", e.getTitle()); v.put("description", e.getDescription()); v.put("type", e.getType());
        v.put("progress", e.getProgress()); v.put("challenges", e.getChallenges()); v.put("next_steps", e.getNextSteps());
        long r = db.insert("logbook", null, v); db.close(); return r;
    }

    public List<LogbookEntry> getUserLogbookEntries(int userId) {
        List<LogbookEntry> list = new ArrayList<>(); SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM logbook WHERE user_id=? ORDER BY entry_date DESC", new String[]{String.valueOf(userId)});
        if (c.moveToFirst()) { do {
            LogbookEntry e = new LogbookEntry(); e.setId(c.getInt(c.getColumnIndexOrThrow("id")));
            e.setUserId(c.getInt(c.getColumnIndexOrThrow("user_id"))); e.setTitle(c.getString(c.getColumnIndexOrThrow("title")));
            e.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
            e.setEntryDate(c.getString(c.getColumnIndexOrThrow("entry_date"))); e.setType(c.getString(c.getColumnIndexOrThrow("type")));
            e.setProgress(c.getString(c.getColumnIndexOrThrow("progress")));
            e.setChallenges(c.getString(c.getColumnIndexOrThrow("challenges")));
            e.setNextSteps(c.getString(c.getColumnIndexOrThrow("next_steps")));
            list.add(e);
        } while (c.moveToNext()); }
        c.close(); db.close(); return list;
    }

    // ============ STATS ============
    public int getTotalApplications() { return count("applications", null); }
    public int getTotalJobs() { return count("jobs", null); }
    public int getOpenJobsCount() { return count("jobs", "status='Open'"); }
    public int getTotalUsers() { return count("users", null); }
    public int getTotalUsersByRole(String role) { return count("users", "role='"+role+"'"); }
    public int getApplicationsByStatus(String s) { return count("applications", "status='"+s+"'"); }
    public int getApplicationsCountForEmployer(int eid) { return count("applications a JOIN jobs j ON a.job_id=j.id", "j.posted_by="+eid); }
    public int getApplicationsCountForJob(int jobId) { return count("applications", "job_id="+jobId); }
    public int getPlacementSuccessRate() { int t = getTotalApplications(); return t==0 ? 0 : (getApplicationsByStatus("Accepted")*100)/t; }

    private int count(String table, String where) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + table + (where!=null ? " WHERE "+where : "");
        Cursor c = db.rawQuery(sql, null); int n = 0; if (c.moveToFirst()) n = c.getInt(0); c.close(); db.close(); return n;
    }
}