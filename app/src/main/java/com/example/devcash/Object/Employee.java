package com.example.devcash.Object;

import android.net.Uri;

/*
       created by Beverly Castillo on July 7, 2019
 */

public class Employee {

    public String emp_lname, emp_fname, emp_task, emp_gender, emp_phone, emp_workfor, emp_username, emp_imageUrl;
    public Account account;

    public Employee() {
    }



    public Employee(String emp_lname, String emp_fname, String emp_task, String emp_gender, String emp_phone, String emp_workfor, String emp_username) {
        this.emp_lname = emp_lname;
        this.emp_fname = emp_fname;
        this.emp_task = emp_task;
        this.emp_gender = emp_gender;
        this.emp_phone = emp_phone;
        this.account = account;
        this.emp_workfor = emp_workfor;
        this.emp_username = emp_username;
    }


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

    public String getEmp_phone() {
        return emp_phone;
    }

    public void setEmp_phone(String emp_phone) {
        this.emp_phone = emp_phone;
    }

    public String getEmp_workfor() {
        return emp_workfor;
    }

    public void setEmp_workfor(String emp_workfor) {
        this.emp_workfor = emp_workfor;
    }

    public String getEmp_username() {
        return emp_username;
    }

    public void setEmp_username(String emp_username) {
        this.emp_username = emp_username;
    }

    public String getEmp_imageUrl() {
        return emp_imageUrl;
    }

    public void setEmp_imageUrl(String emp_imageUrl) {
        this.emp_imageUrl = emp_imageUrl;
    }
}
