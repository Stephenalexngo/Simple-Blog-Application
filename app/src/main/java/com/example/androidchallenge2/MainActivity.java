package com.example.androidchallenge2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseReference dbcate;

    // for the alert dialog
    AlertDialog.Builder dialog;
    AlertDialog alertDialog;
    EditText inputcat;
    Button crtcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbcate = FirebaseDatabase.getInstance().getReference("Category");
    }


    public void gotopost(View view) {
        startActivity(new Intent(this,CreatePost.class));
    }

    public void setcategory(View view) {
        dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.createcategory,null);
        dialog.setView(dialogView);

        inputcat = dialogView.findViewById(R.id.inputcat);
        crtcat = dialogView.findViewById(R.id.crtbut);

        crtcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbcate.child(inputcat.getText().toString()).setValue(inputcat.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"Category Added!",Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
            }
        });

        dialog.setTitle("Create Category");
        alertDialog = dialog.create();
        alertDialog.show();
    }

    public void gotoview(View view) {
        startActivity(new Intent(this,ViewPost.class));
    }
}
