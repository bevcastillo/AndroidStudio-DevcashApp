package com.example.devcash.Object;
/*
    created by Beverly Castillo on July 7, 2019
 */
public class TaskObj {
    private int task_id;
    private String task_title;

    public TaskObj(int task_id, String task_title) {
        this.task_id = task_id;
        this.task_title = task_title;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }
}
