package com.example.androidchallenge2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PostList extends ArrayAdapter<Post>
{
    private Activity context;
    private ArrayList<Post> postArrayList;


    public PostList(Activity context, ArrayList<Post> postArrayList) {
        super(context, R.layout.list_layout, postArrayList);
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewPost = inflater.inflate(R.layout.list_layout,null,true);

        TextView viewtitle = listViewPost.findViewById(R.id.viewtitle);
        TextView viewdes = listViewPost.findViewById(R.id.viewdes);
        TextView viewcat = listViewPost.findViewById(R.id.viewcat);
        TextView viewdate = listViewPost.findViewById(R.id.viewdate);
        Button delbut = listViewPost.findViewById(R.id.delbut);

        final Post post = postArrayList.get(position);

        viewtitle.setText(post.getTitle());
        viewdes.setText(post.getDes());

        for(int x=0; x<post.getCategories().size(); x++)
        {
            if(x==0)
                viewcat.setText(post.getCategories().get(x));
            else
                viewcat.append(", " + post.getCategories().get(x));
        }

        SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy ; h:mm a");
        viewdate.setText(format.format(post.getDate()));

        delbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("Posts").child(post.getId());
                dbref.removeValue();
                Toast.makeText(context,"Post Deleted", Toast.LENGTH_SHORT).show();
            }
        });

        return listViewPost;
    }
}
