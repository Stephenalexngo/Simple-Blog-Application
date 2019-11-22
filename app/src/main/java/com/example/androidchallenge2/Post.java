package com.example.androidchallenge2;

import java.util.ArrayList;
import java.util.Date;

public class Post
{
    private String id ,title, des;
    private Date date;
    private ArrayList<String> categories;

    public Post(){
    }

    public Post(String id, String title, String des, ArrayList<String> categories, Date date) {
        this.id = id;
        this.title = title;
        this.des = des;
        this.categories = categories;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDes() {
        return des;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public Date getDate() {
        return date;
    }
}
