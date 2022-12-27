package com.example.stackify;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {SolutionModel.class}, version = 1, exportSchema = false)
@TypeConverters({DataConverter.class})
public abstract class AppDB extends RoomDatabase {
    public abstract SolutionDao solutionDao();
}
