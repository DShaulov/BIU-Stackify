package com.example.stackify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.io.Serializable;
import java.util.ArrayList;

public class SolutionLoadingActivity extends AppCompatActivity {
    private boolean isOrdered;
    private Solution solution;
    private ArrayList<Box> boxList;
    private int containerHeight;
    private int containerWidth;
    private int containerLength;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution_loading);

        Bundle bundle = getIntent().getBundleExtra("Bundle");
        boxList = (ArrayList<Box>) bundle.getSerializable("boxList");
        isOrdered = getIntent().getBooleanExtra("isOrdered", false);
        containerHeight = getIntent().getIntExtra("containerHeight", 0);
        containerWidth = getIntent().getIntExtra("containerWidth", 0);
        containerLength = getIntent().getIntExtra("containerLength", 0);
    }

    private Runnable launchTask = new Runnable() {
        public void run() {
            Intent intent = new Intent(getApplicationContext(), SolutionViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("solution", (Serializable) solution);
            intent.putExtra("Bundle", bundle);
            startActivity(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        calculateSolution();
        Handler handler = new Handler();
        handler.postDelayed(launchTask, 1000);
    }

    public void calculateSolution() {
        if (isOrdered) {
            Solver orderedScannerSolver = new OrderedScannerSolver(boxList, containerHeight, containerWidth, containerLength);
            orderedScannerSolver.solve();
            solution = orderedScannerSolver.getSolution();
        }
        else {
            Solver unorderedScannerSolver = new UnorderedScannerSolver(boxList, containerHeight, containerWidth, containerLength);
            unorderedScannerSolver.solve();
            solution = unorderedScannerSolver.getSolution();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}