package com.project2.bodycheck.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ToDoListAdapter extends BaseAdapter {

    private Context myContext = null;
    private ArrayList<ToDoListData> myData = new ArrayList<ToDoListData>();

    public ToDoListAdapter(Context context) { this.myContext = context; }

    public void addItem(ToDoListData data) { myData.add(data); }

    @Override
    public int getCount() { return myData.size(); }

    @Override
    public Object getItem(int position) { return myData.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDoListItem itemView;

        if(convertView == null) { itemView = new ToDoListItem(myContext, myData.get(position)); }
        else { itemView = (ToDoListItem)convertView; }

        itemView.setTitle(myData.get(position).getTitle());
        itemView.setContents(myData.get(position).getContents());

        return itemView;
    }

    public void remove(int position) { myData.remove(position); }

    public void clear() { myData.clear(); }
}
