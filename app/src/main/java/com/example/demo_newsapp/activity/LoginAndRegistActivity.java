package com.example.demo_newsapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demo_newsapp.R;


import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginAndRegistActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name_edit, password_edit;
    private Button login_btn, regist_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_and_regist_old);
        init();
    }

    private void init() {
        //Bmob Initialization
        Bmob.initialize(this, "988ae71f79851ac817431bee093c1279");
        name_edit = findViewById(R.id.name_edit);
        password_edit = findViewById(R.id.password_edit);
        login_btn = findViewById(R.id.login_btn);
        regist_btn = findViewById(R.id.regist_btn);
        login_btn.setOnClickListener(this);
        regist_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                loginUser(name_edit.getText().toString(), password_edit.getText().toString());
                break;
            case R.id.regist_btn:
                Intent intent = new Intent(LoginAndRegistActivity.this, SignUpActivity.class );
                startActivity(intent);
                finish();
                break;
        }
    }

    private void loginUser(String nameEdit, String passwordEdit) {
        BmobUser user = new BmobUser();
        user.setUsername(nameEdit);
        user.setPassword(passwordEdit);
        user.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null){
                    Toast.makeText(LoginAndRegistActivity.this,bmobUser.getUsername()+"Login successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("user_objectId",bmobUser.getObjectId());
                    intent.putExtra("username", bmobUser.getUsername());
                    intent.putExtra("loginSuccess","Login successfully");
                    intent.setClass(LoginAndRegistActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LoginAndRegistActivity.this, "Login failed：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

/*    private void registUser(String nameEdit, String passwordEdit) {
        BmobUser user = new BmobUser();
        user.setUsername(nameEdit);
        user.setPassword(passwordEdit);
        user.signUp(new SaveListener<BmobUser>() {

            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e==null){
                    Toast.makeText(LoginAndRegistActivity.this,bmobUser.getUsername()+"Sign up successfully",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(LoginAndRegistActivity.this, "Sign up failed：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/


/*    private void registUser(){
        regist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAndRegistActivity.this, SignUpActivity.class );
                startActivity(intent);
            }
        });
    }*/
}