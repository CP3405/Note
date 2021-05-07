package com.example.demo_newsapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo_newsapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.dbHelper;

public class NoteInformationActivity extends Activity {

    private ImageView mImgBack,mImgAdd;
    private TextView mTvTitle;

    private static String DB_NAME = "mydb";
    private ArrayList<Map<String, Object>> data;
    private data.dbHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleAdapter listAdapter;
    private View view;
    private ListView listview;
    private Map<String, Object> item;
    private String selId;
    private ContentValues selCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_book_information);

        init();

        dbHelper = new dbHelper(this, DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();
        data = new ArrayList<>();


    }
    private void init() {

        listview = findViewById(R.id.list_book);
        mImgAdd = findViewById(R.id.imageView5);
        mImgBack = findViewById(R.id.imageView4);
        mTvTitle = findViewById(R.id.tv);

        mTvTitle.setText("Notes");

        mImgBack.setVisibility(View.GONE);

        mImgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoteInformationActivity.this,AddOrEditNoteActivity.class));
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // TODO Auto-generated method stub
                Map<String, Object> listItem = (Map<String, Object>) listview.getItemAtPosition(position);
                Intent intent = new Intent(NoteInformationActivity.this,AddOrEditNoteActivity.class);
                intent.putExtra("listItem", (Serializable) listItem);
                startActivity(intent);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> listItem = (Map<String, Object>) listview.getItemAtPosition(position);
                selId = (String) listItem.get("_id");
                dbDel();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbFindAll();
    }


    protected void dbDel() {
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
        alertdialogbuilder.setTitle("HINT");
        alertdialogbuilder.setMessage("Are you sure you want to delete this data?");
        alertdialogbuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String where = "_id=" + selId;
                int i = db.delete(dbHelper.TB_NAME, where, null);
                if (i > 0){
                    Toast.makeText(NoteInformationActivity.this,"Deleted successfully!",Toast.LENGTH_SHORT).show();
                    dbFindAll();
                }
                else{
                    Toast.makeText(NoteInformationActivity.this,"Delete failed!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertdialogbuilder.setNeutralButton("Cancel", null);
        AlertDialog alertdialog1 = alertdialogbuilder.create();
        alertdialog1.show();

    }

    private void showList() {
        // TODO Auto-generated method stub
        listAdapter = new SimpleAdapter(this, data,
                R.layout.list_item, new String[]{"bno","bname","bar", "bpr"}, new int[]{R.id.tvNo, R.id.tvName, R.id.tvAr,R.id.tvPr,});
        listview.setAdapter(listAdapter);
    }



    protected void dbFindAll() {
        // TODO Auto-generated method stub
        data.clear();
        cursor = db.query(dbHelper.TB_NAME, null, null, null, null, null, "_id ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(0);
            String bno = cursor.getString(1);
            String bname = cursor.getString(2);
            String bar = cursor.getString(3);
            String bpr = cursor.getString(4);
            item = new HashMap<String, Object>();
            item.put("_id", id);
            item.put("bno", bno);
            item.put("bname", bname);
            item.put("bar", bar);
            item.put("bpr", bpr);
            data.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        showList();
    }
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(NoteInformationActivity.this,MainActivity.class);
        startActivity(intent);

        NoteInformationActivity.this.finish();
    }
}
