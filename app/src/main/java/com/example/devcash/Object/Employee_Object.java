package com.example.devcash.Object;

import android.net.Uri;

public class Employee_Object {
    private Uri emp_imageUri;
    //PK emp_id
    //FK owner_id, acct_id, task_id
    private int emp_id, owner_id, acct_id, task_id, emp_ctcnum;
    private String emp_lname, emp_fname, emp_dob, emp_gender, emp_email, emp_task;

    public Employee_Object(Uri emp_imageUri, int emp_id, int owner_id, int acct_id, int task_id,
                           int emp_ctcnum, String emp_lname, String emp_fname, String emp_dob,
                           String emp_gender, String emp_email, String emp_task) {
        this.emp_imageUri = emp_imageUri;
        this.emp_id = emp_id;
        this.owner_id = owner_id;
        this.acct_id = acct_id;
        this.task_id = task_id;
        this.emp_ctcnum = emp_ctcnum;
        this.emp_lname = emp_lname;
        this.emp_fname = emp_fname;
        this.emp_dob = emp_dob;
        this.emp_gender = emp_gender;
        this.emp_email = emp_email;
        this.emp_task = emp_task;
    }

    public Uri getEmp_imageUri() {
        return emp_imageUri;
    }

    public void setEmp_imageUri(Uri emp_imageUri) {
        this.emp_imageUri = emp_imageUri;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getAcct_id() {
        return acct_id;
    }

    public void setAcct_id(int acct_id) {
        this.acct_id = acct_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getEmp_ctcnum() {
        return emp_ctcnum;
    }

    public void setEmp_ctcnum(int emp_ctcnum) {
        this.emp_ctcnum = emp_ctcnum;
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

    public String getEmp_task() {
        return emp_task;
    }

    public void setEmp_task(String emp_task) {
        this.emp_task = emp_task;
    }
}
