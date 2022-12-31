package com.example.stackify;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SolutionDao solutionDao;
    private Button uploadBtn;
    private Button prevSolsBtn;
    private AppDB db;
    private ArrayList<BoxModel> boxList;

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
                    // Skip first 4 filler lines
                    for (int i = 0; i < 4; i++) {
                        nextLine = reader.readNext();
                    }
                    // Read lines from file and create boxes
                    while ((nextLine = reader.readNext()) != null) {
                        int unpackOrder = Integer.parseInt(nextLine[0]);
                        int height = Integer.parseInt(nextLine[1]);
                        int width = Integer.parseInt(nextLine[2]);
                        int length = Integer.parseInt(nextLine[3]);
                        System.out.println(unpackOrder + " " + height + " " + width + " " + length + " ");
                        BoxModel newBox = new BoxModel(unpackOrder, height, width, length);
                        boxList.add(newBox);
                    }
                    System.out.println(boxList.size());
                    startSolDisplayActivity();
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

    private void startSolDisplayActivity() {
        Intent intent = new Intent(this, SolutionDisplayActivity.class);
        startActivity(intent);
    }

    private void startPrevSolutionsActivity() {

    }





    ////////////////////////////////////
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