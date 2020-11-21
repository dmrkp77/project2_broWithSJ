package com.project2.bodycheck.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class CheckingListAdapter extends BaseAdapter {

    private Context myContext = null;
    private ArrayList<CheckingListData> myData = new ArrayList<CheckingListData>();

    public CheckingListAdapter(Context context) { this.myContext = context; }

    public void addItem(CheckingListData data) { myData.add(data); }

    @Override
    public int getCount() { return myData.size(); }

    @Override
    public Object getItem(int position) { return myData.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckingListItem itemView;

        if(convertView == null) { itemView = new CheckingListItem(myContext, myData.get(position)); }
        else { itemView = (CheckingListItem)convertView; }

        itemView.setTitle(myData.get(position).getTitle());
        itemView.setCheck(myData.get(position).getCheck());

        return itemView;
    }

    public void remove(int position) { myData.remove(position); }

    public void clear() { myData.clear(); }
}
