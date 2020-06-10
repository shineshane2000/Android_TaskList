package com.nayuu.tasklist.orm;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Created by Akatsuki on 2020/6/4 10:27.
 */
@Entity(tableName = "Task")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String sort;
    private String taskName;
    private String startTime;
    private String endTime;
    private int spendTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(int spendTime) {
        this.spendTime = spendTime;
    }
}
