package com.example.demo_newsapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
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

import com.example.demo_newsapp.data.dbHelper;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class NoteInformationActivity extends Activity {

    private ImageView mImgBack,mImgAdd;
    private TextView mTvTitle;

    private static String DB_NAME = "mydb";
    private ArrayList<Map<String, String>> data;
    private com.example.demo_newsapp.data.dbHelper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private SimpleAdapter listAdapter;
    private View view;
    private ListView listview;
    private HashMap<String, String> item;
    private String selId;
    private ContentValues selCV;
    private String username;
    private List<Note> noteLists = new ArrayList<>();

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

        Bmob.initialize(this,"988ae71f79851ac817431bee093c1279");
        listview = findViewById(R.id.list_book);
        mImgAdd = findViewById(R.id.imageView5);
        mImgBack = findViewById(R.id.imageView4);
        mTvTitle = findViewById(R.id.tv);
        username = getIntent().getStringExtra("username");

        mTvTitle.setText("Notes");

        mImgBack.setVisibility(View.GONE);

        mImgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteInformationActivity.this,AddOrEditNoteActivity.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // TODO Auto-generated method stub
                Map<String, Object> listItem = (Map<String, Object>) listview.getItemAtPosition(position);
                Intent intent = new Intent(NoteInformationActivity.this,AddOrEditNoteActivity.class);
                intent.putExtra("listItem", (Serializable) listItem);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> listItem = (Map<String, String>) listview.getItemAtPosition(position);
                selId = (String) listItem.get("noteId");
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
        alertdialogbuilder.setMessage("Are you sure you want to delete this com.example.demo_newsapp.data?");
        alertdialogbuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Note note = new Note();
                note.setObjectId(selId);
                note.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                    }
                });
                /*String where = "_id=" + selId;
                int i = db.delete(dbHelper.TB_NAME, where, null);
                if (i > 0){
                    Toast.makeText(NoteInformationActivity.this,"Deleted successfully!",Toast.LENGTH_SHORT).show();
                    dbFindAll();
                }
                else{
                    Toast.makeText(NoteInformationActivity.this,"Delete failed!",Toast.LENGTH_SHORT).show();
                }*/
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
        BmobQuery<Note> query = new BmobQuery<>();
        query.addWhereEqualTo("UserName", username);
        query.setLimit(50);
        query.findObjects(new FindListener<Note>() {
            @Override
            public void done(List<Note> list, BmobException e) {
                noteLists = list;
            }
        });
        data.clear();

        for (Note note: noteLists){
            item = new HashMap<String, String>();
            item.put("noteId", note.getObjectId());
            item.put("bno", note.getTitle());
            item.put("bname", note.getContent());
            item.put("bar", note.getTime());
            item.put("bpr", note.getDate());
            data.add(item);
        }
        showList();

/*        data.clear();
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
        cursor.close();*/
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setClass(NoteInformationActivity.this,MainActivity.class);
        startActivity(intent);
        NoteInformationActivity.this.finish();
    }
}
