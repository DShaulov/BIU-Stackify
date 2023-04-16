package com.example.stackify;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SolutionDao solutionDao;
    private Button blankSolBtn;
    private Button uploadBtn;
    private Button prevSolsBtn;
    private ImageButton threeDotBtn;
    private AppDB db;
    private ArrayList<Box> boxList;
    private int containerHeight;
    private int containerWidth;
    private int containerLength;
    private Dialog dialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boxList = new ArrayList<>();

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "StackifyDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        solutionDao = db.solutionDao();

        blankSolBtn = findViewById(R.id.blankBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        prevSolsBtn = findViewById(R.id.prevSolsBtn);
        threeDotBtn = findViewById(R.id.threeDotBtn);
        auth = FirebaseAuth.getInstance();

        blankSolBtn.setOnClickListener(view -> {launchBlankSolContainerDialog();});
        uploadBtn.setOnClickListener(view -> getContent.launch("*/*"));
        prevSolsBtn.setOnClickListener(view -> startPrevSolutionsActivity());
        threeDotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchOptionsDialog();
            }
        });

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
                    launchOrdPickDialog();
                }
                catch (Exception e) {
                    System.out.println(e);
                    Toast.makeText(MainActivity.this,"File poorly formatted", Toast.LENGTH_SHORT).show();
                    // Delete all previous boxes
                    boxList.clear();
                }
            }
            catch (Exception e) {
                Toast.makeText(MainActivity.this,"File type not supported", Toast.LENGTH_SHORT).show();
            }
        }
    });

    private void startSolutionLoadingActivity(boolean isOrdered) {
//        // DELETE LATER
//        BoxGenerator boxGenerator = new BoxGenerator();
//        boxList = boxGenerator.generateTypeABoxesMany();
//        List<Integer> dimList = boxGenerator.generateTypeAContainerMany();
//        containerHeight = dimList.get(0);
//        containerWidth = dimList.get(1);
//        containerLength = dimList.get(2);
//        //
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
        List<Solution> prevSolutions = solutionDao.index();
        if (prevSolutions.isEmpty()) {
            Toast.makeText(MainActivity.this,"No previous solutions found", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, PreviousSolutionsRecyclerView.class);
        startActivity(intent);
    }

    /**
     * Launches a dialog that prompts the user to choose a order mode
     */
    private void launchOrdPickDialog() {
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(this);
        dialogBuilder.setView(R.layout.dialog);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                uploadBtn.setVisibility(View.VISIBLE);
                prevSolsBtn.setVisibility(View.VISIBLE);
                blankSolBtn.setVisibility(View.VISIBLE);
                boxList.clear();
            }
        });
        uploadBtn.setVisibility(View.INVISIBLE);
        prevSolsBtn.setVisibility(View.INVISIBLE);
        blankSolBtn.setVisibility(View.INVISIBLE);
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

    /**
     * Launches a dialog that prompts the user to enter container dimensions for a blank solution
     */
    private void launchBlankSolContainerDialog() {
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(this);
        dialogBuilder.setView(R.layout.dialog_enter_container_dim);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                uploadBtn.setVisibility(View.VISIBLE);
                prevSolsBtn.setVisibility(View.VISIBLE);
                blankSolBtn.setVisibility(View.VISIBLE);
                boxList.clear();
            }
        });
        uploadBtn.setVisibility(View.INVISIBLE);
        prevSolsBtn.setVisibility(View.INVISIBLE);
        blankSolBtn.setVisibility(View.INVISIBLE);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button okBtn = alertDialog.findViewById(R.id.enterContainerDimOkBtn);
                EditText heightEditText = alertDialog.findViewById(R.id.heightEditText);
                EditText widthEditText = alertDialog.findViewById(R.id.widthEditText);
                EditText lengthEditText = alertDialog.findViewById(R.id.lengthEditText);

                okBtn.setOnClickListener(view -> {
                    // Hide the soft keyboard
                    lengthEditText.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(lengthEditText.getWindowToken(), 0);

                    String heightString = heightEditText.getText().toString();
                    String widthString = heightEditText.getText().toString();
                    String lengthString = heightEditText.getText().toString();
                    if (heightString.isEmpty() || widthString.isEmpty() || lengthString.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int height = Integer.parseInt(heightEditText.getText().toString());
                    int width = Integer.parseInt(widthEditText.getText().toString());
                    int length = Integer.parseInt(lengthEditText.getText().toString());
                    if (height == 0 || width == 0 || length == 0) {
                        Toast.makeText(MainActivity.this, "Dimensions cannot be 0", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    containerHeight = height;
                    containerWidth = width;
                    containerLength = length;
                    boxList = new ArrayList<>();
                    lengthEditText.clearFocus();
                    launchOrdPickDialog();
                });
            }
        });
        alertDialog.show();
    };

    

    private void launchOptionsDialog() {
        AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(this);
        dialogBuilder.setView(R.layout.dialog_main_options);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                uploadBtn.setVisibility(View.VISIBLE);
                prevSolsBtn.setVisibility(View.VISIBLE);
                blankSolBtn.setVisibility(View.VISIBLE);
                boxList.clear();
            }
        });
        uploadBtn.setVisibility(View.INVISIBLE);
        prevSolsBtn.setVisibility(View.INVISIBLE);
        blankSolBtn.setVisibility(View.INVISIBLE);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button logoutBtn = alertDialog.findViewById(R.id.logoutBtn);
                logoutBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        logout();
                    }
                });
            }
        });
        alertDialog.show();
    }

    public void logout() {
        try {
            auth.signOut();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "An error has occurred.", Toast.LENGTH_SHORT);
            return;
        }
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        return;
    }
}