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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class ViewPost extends AppCompatActivity {

    ListView listpost;
    DatabaseReference dbpost, dbcat;
    ArrayList<Post> arrpost;
    ArrayList<String> arrcat;
    TextView sorttext;
    String sortedby, checkitem;
    Button sorterdate, catbut;
    AlertDialog.Builder dialog;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        listpost = findViewById(R.id.listpost);
        sorttext = findViewById(R.id.sorttext);
        arrpost = new ArrayList<>();
        arrcat = new ArrayList<>();
        dbpost = FirebaseDatabase.getInstance().getReference("Posts");
        dbcat = FirebaseDatabase.getInstance().getReference("Category");
        sortedby = "Any";

        sorterdate = findViewById(R.id.sortbydate);
        catbut = findViewById(R.id.catbut);

        sorterdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortbydate();
            }
        });

        catbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortbycat();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        dbpost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                arrpost.clear();

                for(DataSnapshot postsnapshot : dataSnapshot.getChildren()){
                    Post post = postsnapshot.getValue(Post.class);

                    arrpost.add(post);
                }

                if(sortedby.equals("Any")){
                    PostList adapter = new PostList(ViewPost.this, arrpost);
                    listpost.setAdapter(adapter);
                }
                else if(sortedby.equals("Date"))
                    sortbydate();
                else
                    sortcat(checkitem);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbcat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrcat.clear();

                for(DataSnapshot catesnap : dataSnapshot.getChildren()){
                    arrcat.add(catesnap.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void gotomain(View view) {
        startActivity(new Intent(this,MainActivity.class));
    }

    public void sortbydate() {
        Collections.sort(arrpost, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        sorttext.setText("Sorted By Dates");
        sortedby = "Date";
        PostList adapter = new PostList(ViewPost.this, arrpost);
        listpost.setAdapter(adapter);
    }

    public void sortbycat() {
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
                checkitem = lw.getAdapter().getItem(lw.getCheckedItemPosition()).toString();
                sortcat(checkitem);
            }
        });

        dialog.setTitle("Sort By");
        alertDialog = dialog.create();
        alertDialog.show();
    }

    public void sortcat(String checkitem){
        Collections.sort(arrpost, new Comparator<Post>() {
            @Override
            public int compare(Post o1, Post o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });

        ArrayList<Post> temparr = new ArrayList<>();

        for(int x=0; x<arrpost.size(); x++)
        {
            for(int y=0; y<arrpost.get(x).getCategories().size(); y++)
            {
                if(checkitem.equals(arrpost.get(x).getCategories().get(y)))
                {
                    temparr.add(arrpost.get(x));
                    break;
                }
            }
        }

        sorttext.setText("Sorted By " + checkitem);
        sortedby = checkitem;
        PostList adapter = new PostList(ViewPost.this, temparr);
        listpost.setAdapter(adapter);
    }
}
