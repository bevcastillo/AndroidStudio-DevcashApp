package com.example.devcash.Object;

import android.net.Uri;

public class Employee_Object {
    Uri emp_imageUri;
    String emp_lname, emp_fname, emp_dob, emp_gender, emp_email, emp_ctcnum, emp_task;

    //contructor
    public Employee_Object(Uri emp_imageUri, String emp_lname, String emp_fname, String emp_dob,
                           String emp_gender, String emp_email, String emp_ctcnum, String emp_task) {
        this.emp_imageUri = emp_imageUri;
        this.emp_lname = emp_lname;
        this.emp_fname = emp_fname;
        this.emp_dob = emp_dob;
        this.emp_gender = emp_gender;
        this.emp_email = emp_email;
        this.emp_ctcnum = emp_ctcnum;
        this.emp_task = emp_task;
    }

    //getters and setters
    public Uri getEmp_imageUri() {
        return emp_imageUri;
    }

    public void setEmp_imageUri(Uri emp_imageUri) {
        this.emp_imageUri = emp_imageUri;
    }

    public String getEmp_lname() {
        return emp_lname;
    }

    public void setEmp_lname(String emp_lname) {
        this.emp_lname = emp_lname;
    }

    public String getEmp_fname() {
        return emp_fname;
    }

    public void setEmp_fname(String emp_fname) {
        this.emp_fname = emp_fname;
    }

    public String getEmp_dob() {
        return emp_dob;
    }

    public void setEmp_dob(String emp_dob) {
        this.emp_dob = emp_dob;
    }

    public String getEmp_gender() {
        return emp_gender;
    }

    public void setEmp_gender(String emp_gender) {
        this.emp_gender = emp_gender;
    }

    public String getEmp_email() {
        return emp_email;
    }

    public void setEmp_email(String emp_email) {
        this.emp_email = emp_email;
    }

    public String getEmp_ctcnum() {
        return emp_ctcnum;
    }

    public void setEmp_ctcnum(String emp_ctcnum) {
        this.emp_ctcnum = emp_ctcnum;
    }

    public String getEmp_task() {
        return emp_task;
    }

    public void setEmp_task(String emp_task) {
        this.emp_task = emp_task;
    }
}
