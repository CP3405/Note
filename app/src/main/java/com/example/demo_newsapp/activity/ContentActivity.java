package com.example.demo_newsapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.demo_newsapp.R;
import com.example.demo_newsapp.entity.Title;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

public class ContentActivity extends AppCompatActivity {


    Boolean notCollect = true;//Not yet collected status

    private String objectId;
    private String user_objectId;
    private String descr;
    private String title;
    private String imageUrl;
    private String uri;
    private String href;
    private String actionBar_title;

    String username;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_home);
        }

        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String sql = "select * from _User where objectId = ?";
        new BmobQuery<BmobUser>().doSQLQuery(sql, new SQLQueryListener<BmobUser>() {
            @SuppressLint("LongLogTag")
            @Override
            public void done(BmobQueryResult<BmobUser> bmobQueryResult, BmobException e) {
                // TODO: 2021/5/6 fix the crash problem when clicking
                if (e == null && bmobQueryResult.getResults() != null && bmobQueryResult.getResults().size() > 0) {
                    BmobUser bmobUser = bmobQueryResult.getResults().get(0);
                    user_objectId = bmobUser.getObjectId();
                    username = bmobUser.getUsername();
                } else if (e != null) {
                    Log.i("CollectNewsContentActivity", "done: Search Error" + e.getMessage());
                }
            }
        });

        objectId = getIntent().getStringExtra("objectId");
        user_objectId = getIntent().getStringExtra("user_objectId");
        descr = getIntent().getStringExtra("descr");
        title = getIntent().getStringExtra("title");
        imageUrl = getIntent().getStringExtra("imageUrl");
        uri = getIntent().getStringExtra("uri");
        href = getIntent().getStringExtra("href");
        actionBar_title = getIntent().getStringExtra("actionBar_title");

        System.out.println(user_objectId + ":" + objectId + ":" + ":" + descr + ":" + title + ":" + imageUrl + ":" + uri + ":" + href + ":" + actionBar_title + "------------log");

        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.show();
        actionBar.setTitle(actionBar_title);
        webView.loadUrl(uri);
    }

    /**
     * Manage return button
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ContentActivity.this, MainActivity.class);
                intent.putExtra("user_objectId", user_objectId);
                intent.putExtra("username", username);
                intent.putExtra("loginSuccess", "Login Successfully");
                startActivity(intent);
                finish();
                break;

            case R.id.action_settings_content_add: //思路点
                Toast.makeText(ContentActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                if (notCollect) {
                    init_insert();
                    notCollect = false;
                } else {
                    Toast.makeText(ContentActivity.this, "Already added", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
        return true;
    }


    /*
     * the 3 dots on the top right conner beside actionbar, can add more functions
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_content_menu, menu);//Call the .xml in menu folder, show other functions once touch the 3 dots on the login page.
        return true;

    }


    private void init_insert() {
        //Bmob Initialization
        Bmob.initialize(this, "988ae71f79851ac817431bee093c1279");
        //Insert com.example.demo_newsapp.data to Title form
        Title temptitle = new Title(title, descr, imageUrl, uri, user_objectId, href);
        insertInfoToTableTitle(temptitle);

    }


    private void insertInfoToTableTitle(Title temptitle) {
        System.out.println(temptitle.toString() + "---------------------------log");
        temptitle.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Toast.makeText(ContentActivity.this, "Insert successfully", Toast.LENGTH_SHORT).show();

                } else {
                    System.out.println(e.getMessage() + "----------------log");
                    Toast.makeText(ContentActivity.this, "Insert failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
