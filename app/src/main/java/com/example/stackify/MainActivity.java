package com.example.stackify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SolutionDao solutionDao;
    private AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "StackifyDB")
                .allowMainThreadQueries()
                .build();
        solutionDao = db.solutionDao();
    }

    public boolean areSolutionsRetrievable() {
        List<SolutionModel> allSolutions = solutionDao.index();
        if (allSolutions == null) {
            return false;
        }
        return true;
    }

    // Checks if solution has been inserted into the database.
    public boolean isSolutionInserted(String solutionName) {
        SolutionModel solution = solutionDao.get(solutionName);
        if (solution == null) {
            return false;
        }
        return true;
    }

    // Checks if solution has been updated in the database.
    public boolean isSolutionChanged(String solutionName, int newHeight, int newWidth, int newLength, List<BoxModel> newBoxList) {
        SolutionModel solution = solutionDao.get(solutionName);
        if ((solution.getContainerHeight() != newHeight) || (solution.getContainerWidth() != newWidth) || (solution.getContainerLength() != newLength)) {
            return false;
        }
        if (!solution.getBoxList().equals(newBoxList)) {
            return false;
        }
        return true;
    }


}