package com.example.devcash.Object;

import android.net.Uri;

/*
       created by Beverly Castillo on July 7, 2019
 */

public class Employee {

    public Uri emp_imageUri;
    public String emp_lname, emp_fname, emp_task, emp_dob, emp_gender, emp_bdate;
    public int emp_ctcnum;

    public Employee() {
    }

    public Employee(Uri emp_imageUri, String emp_lname, String emp_fname, String emp_task, String emp_dob, String emp_gender, String emp_bdate, int emp_ctcnum) {
        this.emp_imageUri = emp_imageUri;
        this.emp_lname = emp_lname;
        this.emp_fname = emp_fname;
        this.emp_task = emp_task;
        this.emp_dob = emp_dob;
        this.emp_gender = emp_gender;
        this.emp_bdate = emp_bdate;
        this.emp_ctcnum = emp_ctcnum;
    }

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

    public String getEmp_task() {
        return emp_task;
    }

    public void setEmp_task(String emp_task) {
        this.emp_task = emp_task;
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

    public String getEmp_bdate() {
        return emp_bdate;
    }

    public void setEmp_bdate(String emp_bdate) {
        this.emp_bdate = emp_bdate;
    }

    public int getEmp_ctcnum() {
        return emp_ctcnum;
    }

    public void setEmp_ctcnum(int emp_ctcnum) {
        this.emp_ctcnum = emp_ctcnum;
    }
}
