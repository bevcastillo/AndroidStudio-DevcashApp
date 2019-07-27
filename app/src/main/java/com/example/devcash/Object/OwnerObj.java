package com.example.devcash.Object;

import android.net.Uri;

/*
       created by Beverly Castillo on July 7, 2019
 */

public class OwnerObj {
    private Uri owner_image;
    /*
        PK owner_id, FK acct_id, ent_id, task_id
     */
    private int owner_id, acct_id, ent_id, task_id, owner_ctcnum;
    private String owner_lname, owner_fname, owner_dob, owner_gender;

    public OwnerObj(Uri owner_image, int owner_id, int acct_id, int ent_id, int task_id,
                    int owner_ctcnum, String owner_lname, String owner_fname, String owner_dob,
                    String owner_gender) {
        this.owner_image = owner_image;
        this.owner_id = owner_id;
        this.acct_id = acct_id;
        this.ent_id = ent_id;
        this.task_id = task_id;
        this.owner_ctcnum = owner_ctcnum;
        this.owner_lname = owner_lname;
        this.owner_fname = owner_fname;
        this.owner_dob = owner_dob;
        this.owner_gender = owner_gender;
    }

    public Uri getOwner_image() {
        return owner_image;
    }

    public void setOwner_image(Uri owner_image) {
        this.owner_image = owner_image;
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

    public int getEnt_id() {
        return ent_id;
    }

    public void setEnt_id(int ent_id) {
        this.ent_id = ent_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getOwner_ctcnum() {
        return owner_ctcnum;
    }

    public void setOwner_ctcnum(int owner_ctcnum) {
        this.owner_ctcnum = owner_ctcnum;
    }

    public String getOwner_lname() {
        return owner_lname;
    }

    public void setOwner_lname(String owner_lname) {
        this.owner_lname = owner_lname;
    }

    public String getOwner_fname() {
        return owner_fname;
    }

    public void setOwner_fname(String owner_fname) {
        this.owner_fname = owner_fname;
    }

    public String getOwner_dob() {
        return owner_dob;
    }

    public void setOwner_dob(String owner_dob) {
        this.owner_dob = owner_dob;
    }

    public String getOwner_gender() {
        return owner_gender;
    }

    public void setOwner_gender(String owner_gender) {
        this.owner_gender = owner_gender;
    }
}
