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
    @Query("SELECT * FROM Solution")
    List<Solution> index();
    @Query("SELECT * FROM Solution WHERE solutionName = :solutionName")
    Solution get(String solutionName);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Solution solution);
    @Update
    void update(Solution solution);
    @Delete
    void delete(Solution solution);
    @Query("DELETE FROM Solution")
    void deleteAll();
}