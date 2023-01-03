package com.example.stackify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;

public class SolutionLoadingActivity extends AppCompatActivity {
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
        containerHeight = getIntent().getIntExtra("containerHeight", 0);
        containerWidth = getIntent().getIntExtra("containerWidth", 0);
        containerLength = getIntent().getIntExtra("containerLength", 0);
    }
}