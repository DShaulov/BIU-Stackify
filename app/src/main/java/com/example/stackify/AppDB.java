package com.example.stackify;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Solution.class}, version = 3, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class AppDB extends RoomDatabase {
    public abstract SolutionDao solutionDao();
}
