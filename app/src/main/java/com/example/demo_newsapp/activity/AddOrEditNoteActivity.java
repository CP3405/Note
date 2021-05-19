package com.example.demo_newsapp.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.demo_newsapp.R;
import java.util.Map;
import com.example.demo_newsapp.data.dbHelper;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class AddOrEditNoteActivity extends Activity {

    private ImageView mImgBack, mImgAdd;
    private TextView mTvTitle;

    private EditText et_no;
    private EditText et_name;
    private EditText et_ar;
    private EditText et_pr;
    private Button addBtn;
    private String username;

    private static String DB_NAME = "mydb";
    private com.example.demo_newsapp.data.dbHelper dbHelper;
    private SQLiteDatabase db;

    private String selId;
    private Map<String, Object> listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_or_edit_book);

        if (getIntent() != null) {
            listItem = (Map<String, Object>) getIntent().getSerializableExtra("listItem");
        }

        dbHelper = new dbHelper(this, DB_NAME, null, 1);
        db = dbHelper.getWritableDatabase();

        init();

    }

    private void init() {

        Bmob.initialize(this,"988ae71f79851ac817431bee093c1279");
        mImgAdd = findViewById(R.id.imageView5);
        mImgBack = findViewById(R.id.imageView4);
        mTvTitle = findViewById(R.id.tv);
        addBtn = findViewById(R.id.addBtn);
        et_no = findViewById(R.id.et_no);
        et_name = findViewById(R.id.et_name);
        et_ar = findViewById(R.id.et_ar);
        et_pr = findViewById(R.id.et_pr);
        username = getIntent().getStringExtra("username");

        if (listItem == null) {
            mTvTitle.setText("Add Note");
            addBtn.setText("Add Note");
        } else {
            mTvTitle.setText("Update Note");
            addBtn.setText("Update Note");
            et_no.setText((String) listItem.get("bno"));
            et_name.setText((String) listItem.get("bname"));
            et_ar.setText((String) listItem.get("bar"));
            et_pr.setText((String) listItem.get("bpr"));
            selId = (String) listItem.get("noteId");
        }

        mImgAdd.setVisibility(View.GONE);

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listItem == null){
                    dbAdd();
                }else {
                    dbUpdate();
                }

            }
        });
    }

    protected void dbAdd() {
        String bno = et_no.getText().toString().trim();
        String bname = et_name.getText().toString().trim();
        String bar = et_ar.getText().toString().trim();
        String bpr = et_pr.getText().toString().trim();

        if (TextUtils.isEmpty(bno) || TextUtils.isEmpty(bname) || TextUtils.isEmpty(bar) || TextUtils.isEmpty(bpr)) {
            Toast.makeText(this, "Please complete!", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO Auto-generated method stub
        Note note = new Note();
        note.setUserName(username);
        note.setTitle(bno);
        note.setContent(bname);
        note.setTime(bar);
        note.setDate(bpr);
        note.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
            }
        });
/*        ContentValues values = new ContentValues();
        values.put("bno", bno);
        values.put("bname", bname);
        values.put("bar", bar);
        values.put("bpr", bpr);
        long rowid = db.insert(dbHelper.TB_NAME, null, values);
        if (rowid == -1)
            Toast.makeText(this, "Fail to add!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Successfully added!", Toast.LENGTH_SHORT).show();*/
        Intent intent = new Intent(AddOrEditNoteActivity.this, NoteInformationActivity.class);
        startActivity(intent);
        finish();
    }

    protected void dbUpdate() {

        String bno = et_no.getText().toString().trim();
        String bname = et_name.getText().toString().trim();
        String bar = et_ar.getText().toString().trim();
        String bpr = et_pr.getText().toString().trim();

        if (TextUtils.isEmpty(bno) || TextUtils.isEmpty(bname) || TextUtils.isEmpty(bar) || TextUtils.isEmpty(bpr)) {
            Toast.makeText(this, "Please complete!", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO Auto-generated method stub
        Note note = new Note();
        note.setUserName(username);
        note.setTitle(bno);
        note.setContent(bname);
        note.setTime(bar);
        note.setDate(bpr);
        note.update(selId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
            }
        });
        Intent intent = new Intent(AddOrEditNoteActivity.this, NoteInformationActivity.class);
        startActivity(intent);
        finish();

/*        ContentValues values = new ContentValues();
        values.put("bno", bno);
        values.put("bname", bname);
        values.put("bar", bar);
        values.put("bpr", bpr);
        String where = "_id=" + selId;
        int i = db.update(dbHelper.TB_NAME, values, where, null);
        if (i > 0) {
            Toast.makeText(this, "Update succeeded!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
        }*/
    }
}