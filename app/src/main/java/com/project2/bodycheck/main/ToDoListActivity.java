package com.project2.bodycheck.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.project2.bodycheck.DataBaseHelper;
import com.project2.bodycheck.R;

import java.util.ArrayList;

public class ToDoListActivity extends Activity {

    private ToDoListAdapter myAdapter = null;
    private ListView currentList;
    private Button buttonCreate;

    private DataBaseHelper db;
    private ArrayList<String> ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        db = new DataBaseHelper(this);
        myAdapter = new ToDoListAdapter(this);

        buttonCreate = findViewById(R.id.button_create);
        currentList = findViewById(R.id.current_listView);
        currentList.setAdapter(myAdapter);
        currentList.setOnItemLongClickListener(new ListViewItemClickListener());

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ToDoListCreate.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        myAdapter.clear();
        searchData();
    }

    private String tempText;
    private int tempCount;
    private String OnDay = "1";

    private void searchData() {
        Cursor cursor = db.viewListData();
        ID = new ArrayList<String>();

        if(cursor.getCount() == 0) {
            Toast.makeText(ToDoListActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
        } else {
            while(cursor.moveToNext()) {
                tempText = "Every";
                tempCount = 0;
                if(cursor.getString(2).equals(OnDay)) { tempText += "  Sun"; tempCount += 1; }
                if(cursor.getString(3).equals(OnDay)) { tempText += "  Mon"; tempCount += 1; }
                if(cursor.getString(4).equals(OnDay)) { tempText += "  Tue"; tempCount += 1; }
                if(cursor.getString(5).equals(OnDay)) { tempText += "  Wed"; tempCount += 1; }
                if(cursor.getString(6).equals(OnDay)) { tempText += "  Thu"; tempCount += 1; }
                if(cursor.getString(7).equals(OnDay)) { tempText += "  Fri"; tempCount += 1; }
                if(cursor.getString(8).equals(OnDay)) { tempText += "  Sat"; tempCount += 1; }

                if(tempCount == 7) { tempText = "Everyday"; }

                myAdapter.addItem(new ToDoListData(cursor.getString(1), tempText));
                ID.add(cursor.getString(0));
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    private void checkID() {
        ID.clear();
        Cursor cursor = db.viewListData();
        ID = new ArrayList<String>();
        if(cursor.getCount() == 0) {
            //none
        } else {
            while(cursor.moveToNext()) {
                ID.add(cursor.getString(0));
            }
        }
    }

    private Integer selectedPos = -1;

    private class ListViewItemClickListener implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            selectedPos = position;
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
            alertDlg.setTitle("Question");

            alertDlg.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    myAdapter.remove(selectedPos);
                    myAdapter.notifyDataSetChanged();
                    db.deleteListData(ID.get(selectedPos));
                    checkID();
                    dialog.dismiss();
                }
            });

            alertDlg.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alertDlg.setMessage("Are you sure remove data?");
            alertDlg.show();

            return false;
        }
    }
}