package com.project2.bodycheck.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.project2.bodycheck.R;

public class ToDoListItem extends LinearLayout {

    private TextView myTitle;
    private TextView myContents;

    public ToDoListItem(Context context, ToDoListData data) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.todolist_style, this, true);

        myTitle = findViewById(R.id.list_item_title);
        myContents = findViewById(R.id.list_item_contents);
    }

    public void setTitle(String title) { myTitle.setText(title); }

    public void setContents(String contents) { myContents.setText(contents); }
}
