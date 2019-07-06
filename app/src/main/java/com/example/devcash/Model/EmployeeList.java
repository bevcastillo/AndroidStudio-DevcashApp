package com.example.devcash.Model;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.Serializable;

public class EmployeeList implements Serializable {

    private String emp_lname, emp_fname, emp_email, emp_phone, emp_passw;
    private RadioGroup radioGroupTask;
    private RadioButton radioButtonTask;
    private int emp_img, emp_id;

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

    public String getEmp_email() {
        return emp_email;
    }

    public void setEmp_email(String emp_email) {
        this.emp_email = emp_email;
    }

    public String getEmp_phone() {
        return emp_phone;
    }

    public void setEmp_phone(String emp_phone) {
        this.emp_phone = emp_phone;
    }

    public String getEmp_passw() {
        return emp_passw;
    }

    public void setEmp_passw(String emp_passw) {
        this.emp_passw = emp_passw;
    }

    public RadioGroup getRadioGroupTask() {
        return radioGroupTask;
    }

    public void setRadioGroupTask(RadioGroup radioGroupTask) {
        this.radioGroupTask = radioGroupTask;
    }

    public RadioButton getRadioButtonTask() {
        return radioButtonTask;
    }

    public void setRadioButtonTask(RadioButton radioButtonTask) {
        this.radioButtonTask = radioButtonTask;
    }

    public int getEmp_img() {
        return emp_img;
    }

    public void setEmp_img(int emp_img) {
        this.emp_img = emp_img;
    }

    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }
}
