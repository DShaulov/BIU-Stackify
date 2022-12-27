package com.example.stackify;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SolutionDao {
    @Query("SELECT * FROM SolutionModel")
    List<SolutionModel> index();
    @Query("SELECT * FROM SolutionModel WHERE solutionName = :solutionName")
    SolutionModel get(String solutionName);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SolutionModel solution);
    @Update
    void update(SolutionModel solution);
    @Delete
    void delete(SolutionModel solution);
}