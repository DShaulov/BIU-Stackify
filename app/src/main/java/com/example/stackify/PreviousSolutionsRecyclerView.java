package com.example.stackify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;

import java.util.List;

public class PreviousSolutionsRecyclerView extends AppCompatActivity {
    private AppDB db;
    private SolutionDao solutionDao;

    List<Solution> solutionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.previous_solutions_recycler_view);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "StackifyDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        solutionDao = db.solutionDao();
        solutionList = solutionDao.index();

        RecyclerView recyclerView = findViewById(R.id.prevSolsRecyclerView);
        SolutionRecyclerViewAdapter adapter = new SolutionRecyclerViewAdapter(this, solutionList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}