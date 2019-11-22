package com.example.androidchallenge2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class CreatePost extends AppCompatActivity {

    ArrayList<String> arrcat, urcat;
    EditText posttitle, postdes;

    DatabaseReference dbcate, dbposts;

    AlertDialog.Builder dialog;
    AlertDialog alertDialog;
    EditText inputcat;
    Button crtcat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        dbcate = FirebaseDatabase.getInstance().getReference("Category");
        dbposts = FirebaseDatabase.getInstance().getReference("Posts");
        arrcat = new ArrayList<>();
        urcat = new ArrayList<>();

        posttitle = findViewById(R.id.posttitle);
        postdes = findViewById(R.id.postdes);
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbcate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                arrcat.clear();

                for(DataSnapshot catesnapshot : dataSnapshot.getChildren()){
                    String category = catesnapshot.getValue().toString();

                    arrcat.add(category);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void createpost(View view)
    {
        String title = posttitle.getText().toString().trim();
        String des = postdes.getText().toString().trim();

        String id = dbposts.push().getKey();

        Post post = new Post(id,title,des,urcat,new Date());

        dbposts.child(id).setValue(post);

        Toast.makeText(CreatePost.this, "Post Added", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(CreatePost.this,MainActivity.class));
    }

    public void selectcat(View view)
    {
        dialog = new AlertDialog.Builder(this);

        String[] cat = new String[arrcat.size()];
        for(int x=0; x<arrcat.size(); x++)
        {
            cat[x] = arrcat.get(x);
        }

        dialog.setSingleChoiceItems(cat, 0,  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView lw = ((AlertDialog)dialog).getListView();
                String checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition()).toString();
                checker(checkedItem);
            }
        });

        dialog.setTitle("Select Category");
        alertDialog = dialog.create();
        alertDialog.show();
    }

    public void checker(String item){
        if(urcat.isEmpty()){
            urcat.add(item);
        }
        else{
            boolean exist = false;

            for(int x=0; x<urcat.size(); x++)
            {
                if(urcat.get(x).equals(item)){
                    exist = true;
                }
            }

            if(exist)
                Toast.makeText(this,"Category Already Selected", Toast.LENGTH_SHORT).show();
            else
                urcat.add(item);
        }
    }

    public void setcat(View view) {
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
                        Toast.makeText(CreatePost.this,"Category Added!",Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });
            }
        });

        dialog.setTitle("Create Category");
        alertDialog = dialog.create();
        alertDialog.show();
    }

    public void yourcat(View view) {
        dialog = new AlertDialog.Builder(this);

        String[] cat = new String[urcat.size()];
        for(int x=0; x<urcat.size(); x++)
        {
            cat[x] = urcat.get(x);
        }

        dialog.setItems(cat, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        dialog.setTitle("Your Choosen Categories");
        alertDialog = dialog.create();
        alertDialog.show();
    }
}
