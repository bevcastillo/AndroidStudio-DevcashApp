package com.example.devcash.Object;

import android.net.Uri;

/*
       created by Beverly Castillo on July 7, 2019
 */

public class Employee {

    public String emp_lname, emp_fname, emp_task, emp_gender, emp_bdate, emp_phone;
//    public int emp_phone;

    public Employee() {
    }

    public Employee(String emp_lname, String emp_fname, String emp_task, String emp_gender, String emp_bdate, String emp_phone) {
        this.emp_lname = emp_lname;
        this.emp_fname = emp_fname;
        this.emp_task = emp_task;
        this.emp_gender = emp_gender;
        this.emp_bdate = emp_bdate;
        this.emp_phone = emp_phone;
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

    public String getEmp_ctcnum() {
        return emp_phone;
    }

    public void setEmp_ctcnum(String emp_ctcnum) {
        this.emp_phone = emp_ctcnum;
    }
}
