package com.example.stackify;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SolutionDao solutionDao;
    private Button uploadBtn;
    private Button prevSolsBtn;
    private AppDB db;
    private ArrayList<Box> boxList;
    int containerHeight;
    int containerWidth;
    int containerLength;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boxList = new ArrayList<>();

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "StackifyDB")
                .allowMainThreadQueries()
                .build();
        solutionDao = db.solutionDao();

        uploadBtn = findViewById(R.id.uploadBtn);
        prevSolsBtn = findViewById(R.id.prevSolsBtn);

        uploadBtn.setOnClickListener(view -> getContent.launch("*/*"));
        prevSolsBtn.setOnClickListener(view -> startPrevSolutionsActivity());
    }

    ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            try {
                InputStream input = getContentResolver().openInputStream(uri);
                CSVReader reader = new CSVReader(new InputStreamReader(input));
                String[] nextLine;
                try {
                    // Read label line
                    nextLine = reader.readNext();
                    // Get container dimensions
                    nextLine = reader.readNext();
                    containerHeight = Integer.parseInt(nextLine[1]);
                    containerWidth = Integer.parseInt(nextLine[2]);
                    containerLength = Integer.parseInt(nextLine[3]);
                    // Skip next 2 filler lines
                    nextLine = reader.readNext();
                    nextLine = reader.readNext();
                    // Read lines from file and create boxes
                    while ((nextLine = reader.readNext()) != null) {
                        int unpackOrder = Integer.parseInt(nextLine[0]);
                        int height = Integer.parseInt(nextLine[1]);
                        int width = Integer.parseInt(nextLine[2]);
                        int length = Integer.parseInt(nextLine[3]);
                        System.out.println(unpackOrder + " " + height + " " + width + " " + length + " ");
                        Box newBox = new Box(unpackOrder, height, width, length);
                        boxList.add(newBox);
                    }
                    System.out.println(boxList.size());
                    //startSolutionLoadingActivity(containerHeight, containerWidth, containerLength);
                    launchDialog();
                }
                catch (Exception e) {
                    System.out.println(e);
                    Toast.makeText(MainActivity.this,"File poorly formatted", Toast.LENGTH_LONG).show();
                    // Delete all previous boxes
                    boxList.clear();
                }
            }
            catch (Exception e) {
                Toast.makeText(MainActivity.this,"File type not supported", Toast.LENGTH_LONG).show();
            }
        }
    });

    private void startSolutionLoadingActivity(boolean isOrdered) {
        Intent intent = new Intent(this, SolutionLoadingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("boxList", (Serializable) boxList);
        intent.putExtra("Bundle", bundle);
        intent.putExtra("isOrdered", isOrdered);
        intent.putExtra("containerHeight", containerHeight);
        intent.putExtra("containerWidth", containerWidth);
        intent.putExtra("containerLength", containerLength);
        startActivity(intent);
    }
    private void startPrevSolutionsActivity() {

    }

    private void launchDialog() {
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(this);
        dialogBuilder.setView(R.layout.dialog);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                uploadBtn.setVisibility(View.VISIBLE);
                prevSolsBtn.setVisibility(View.VISIBLE);
            }
        });
        uploadBtn.setVisibility(View.INVISIBLE);
        prevSolsBtn.setVisibility(View.INVISIBLE);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button orderedBtn = alertDialog.findViewById(R.id.orderedSolBtn);
                Button unorderedBtn = alertDialog.findViewById(R.id.unorderedSolBtn);
                orderedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startSolutionLoadingActivity(true);
                    }
                });
                unorderedBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startSolutionLoadingActivity(false);
                    }
                });
            }
        });
        alertDialog.show();
    }
}