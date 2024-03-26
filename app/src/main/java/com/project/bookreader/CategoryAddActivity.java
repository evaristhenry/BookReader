package com.project.bookreader;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.bookreader.databinding.ActivityCategoryAddBinding;

import java.util.HashMap;

public class CategoryAddActivity extends AppCompatActivity {
    //view binding
    private ActivityCategoryAddBinding binding;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //configure progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressed();
            }
        });

        //Handle back button
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Handle upload category button click
        binding.submitBtn.setOnClickListener(v -> validateData());

    }

    private void backPressed() {
        finish();
    }

    private String category = "";

    private void validateData() {
        //get data
        category = binding.categoryEt.getText().toString().trim();
        //validate if is not empty
        if (TextUtils.isEmpty(category)) {
            Toast.makeText(this, "Please Enter category...", Toast.LENGTH_SHORT).show();
        } else {
            //if data is not empty call upload category method
            addCategoryFirebase();
        }
    }

    private void addCategoryFirebase() {
        //show progress
        progressDialog.setMessage("Adding category...");
        progressDialog.show();

        //get timestamp
        long timestamp = System.currentTimeMillis();

        //setup inf to add in firebase db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", "" + timestamp);
        hashMap.put("category", "" + category);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid", "" +firebaseAuth.getUid());

        //add to firebase db, database Root> Categories > categoryID> category info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child("" + timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(aVoid -> {
                    //hide progress
                    progressDialog.dismiss();
                    //category add success
                    Toast.makeText(CategoryAddActivity.this, "Category Added Successfully...", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    //hide progress
                    //category add failed
                    progressDialog.dismiss();
                    Toast.makeText(CategoryAddActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}