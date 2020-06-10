package com.nayuu.tasklist.orm;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Created by Akatsuki on 2020/6/4 10:33.
 */
@Dao
public interface TaskDao {
    @Query("SELECT * FROM Task")
    List<Task> getAllTask();

    @Query("delete from Task where taskName = :taskName")
    void deleteTask(String taskName);

    @Insert
    void insert(Task... tasks);

    @Update
    void update(Task... tasks);

    @Delete
    void delete(Task... tasks);

}