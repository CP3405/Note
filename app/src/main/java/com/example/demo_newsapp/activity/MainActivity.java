package com.example.demo_newsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.demo_newsapp.R;
import com.example.demo_newsapp.adapter.TitleAdapter;
import com.example.demo_newsapp.entity.Title;
import com.example.demo_newsapp.gson.ApiResponse;
import com.example.demo_newsapp.gson.News;
import com.example.demo_newsapp.utils.HttpUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final int ITEM_SOCIETY = 1;
    private static final int ITEM_COUNTY = 2;
    private static final int ITEM_INTERNATION = 3;
    private static final int ITEM_FUN = 4;
    private static final int ITEM_SPORT = 5;
    private static final int ITEM_NBA = 6;
    private static final int ITEM_FOOTBALL = 7;
    private static final int ITEM_TECHNOLOGY = 8;
    private static final int ITEM_WORK = 9;
    private static final int ITEM_APPLE = 10;
    private static final int ITEM_WAR = 11;
    private static final int ITEM_INTERNET = 12;
    private static final int ITEM_TREVAL = 13;
    private static final int ITEM_HEALTH = 14;
    private static final int ITEM_STRANGE = 15;
    private static final int ITEM_LOOKER = 16;
    private static final int ITEM_VR = 17;
    private static final int ITEM_IT = 18;

    private List<Title> titleList = new ArrayList<Title>();
    private List<Title> findTitleList = new ArrayList<Title>();
    private ListView listView;
    private TitleAdapter adapter;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout refreshLayout;

    String user_objectId;
    String username;
    String loginSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Disappear Status Bar
        Intent intent = getIntent();
        user_objectId = intent.getStringExtra("user_objectId");  //Key variety
        username = intent.getStringExtra("username");
        loginSuccess = intent.getStringExtra("loginSuccess");

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Social News");

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new TitleAdapter(this, R.layout.list_view_item, titleList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            Intent intent = new Intent(MainActivity.this, ContentActivity.class);

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Title title = titleList.get(position);
                intent.putExtra("user_objectId", user_objectId);
                intent.putExtra("descr", title.getDescr());
                intent.putExtra("title", title.getTitle());
                intent.putExtra("imageUrl", title.getImageUrl());
                intent.putExtra("uri", title.getUri());
                intent.putExtra("href", title.getHref());
                intent.putExtra("actionBar_title", actionBar.getTitle());
                startActivity(intent);
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headView = navigationView.getHeaderView(0);

        TextView textViewObjectId = headView.findViewById(R.id.objectId);  //Key variety
        TextView textViewLoginSuccess = headView.findViewById(R.id.loginSuccess);
        ImageView imageView_btn_edit = headView.findViewById(R.id.btn_edit);
        ImageView imageView_btn_about = headView.findViewById(R.id.btn_aboutme);
        ImageView imageView_btn_exit_app = headView.findViewById(R.id.btn_exit_app);
        ImageView imageView_btn_note = headView.findViewById(R.id.btn_note_app);

        textViewObjectId.setText("name:" + username);
        textViewLoginSuccess.setText("Status:" + loginSuccess);

        imageView_btn_edit.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(MainActivity.this, UpdateUserPasswordActivity.class);

            @Override
            public void onClick(View view) {    //
                Toast.makeText(MainActivity.this, "Clicked the change password button", Toast.LENGTH_SHORT).show();
                intent.putExtra("user_objectId1", user_objectId);

                startActivity(intent);
            }
        });

        imageView_btn_about.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(MainActivity.this, UserInfosActivity.class);

            @Override
            public void onClick(View view) {    //
                Toast.makeText(MainActivity.this, "Clicked Search & Edit user information button", Toast.LENGTH_SHORT).show();
                intent.putExtra("user_objectId", user_objectId);
                startActivity(intent);

            }
        });

        imageView_btn_note.setOnClickListener(new View.OnClickListener() {
            Intent intent = new Intent(MainActivity.this, NoteInformationActivity.class);

            @Override
            public void onClick(View view) {    //
                Toast.makeText(MainActivity.this, "Clicked the note button", Toast.LENGTH_SHORT).show();
                intent.putExtra("user_objectId", user_objectId);
                startActivity(intent);
            }
        });

        imageView_btn_exit_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {    //
                Toast.makeText(MainActivity.this, "Clicked the quit button", Toast.LENGTH_SHORT).show();
                System.exit(0);//Quit App
            }
        });


        navigationView.setCheckedItem(R.id.nav_society);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_society:
                        handleCurrentPage("Social News", ITEM_SOCIETY);
                        break;
                    case R.id.nav_county:
                        handleCurrentPage("Local News", ITEM_COUNTY);
                        break;
                    case R.id.nav_internation:
                        handleCurrentPage("Global News", ITEM_INTERNATION);
                        break;
                    case R.id.nav_fun:
                        handleCurrentPage("Entertainment", ITEM_FUN);
                        break;
                    case R.id.nav_sport:
                        handleCurrentPage("Sport News", ITEM_SPORT);
                        break;
                    case R.id.nav_nba:
                        handleCurrentPage("NBA News", ITEM_NBA);
                        break;
                    case R.id.nav_football:
                        handleCurrentPage("Soccer News", ITEM_FOOTBALL);
                        break;
                    case R.id.nav_technology:
                        handleCurrentPage("Technology News", ITEM_TECHNOLOGY);
                        break;
                    case R.id.nav_work:
                        handleCurrentPage("Business News", ITEM_WORK);
                        break;
                    case R.id.nav_apple:
                        handleCurrentPage("Apple News", ITEM_APPLE);
                        break;
                    case R.id.nav_war:
                        handleCurrentPage("Military News", ITEM_WAR);
                        break;
                    case R.id.nav_internet:
                        handleCurrentPage("Mobile Internet", ITEM_INTERNET);
                        break;
                    case R.id.nav_travel:
                        handleCurrentPage("Travel", ITEM_TREVAL);
                        break;
                    case R.id.nav_health:
                        handleCurrentPage("Health", ITEM_HEALTH);
                        break;
                    case R.id.nav_strange:
                        handleCurrentPage("Anecdote", ITEM_STRANGE);
                        break;
                    case R.id.nav_vr:
                        handleCurrentPage("VR Technology", ITEM_VR);
                        break;
                    case R.id.nav_it:
                        handleCurrentPage("IT News", ITEM_IT);
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                int itemName = parseString((String) actionBar.getTitle());
                requestNew(itemName);
            }
        });

        requestNew(ITEM_SOCIETY);

    }

    /**
     * judge if it is the current page, if not, request to manage the data.
     */
    private void handleCurrentPage(String text, int item) {
        ActionBar actionBar = getSupportActionBar();
        if (!text.equals(actionBar.getTitle().toString())) {
            actionBar.setTitle(text);
            requestNew(item);
            refreshLayout.setRefreshing(true);
        }
    }


    /**
     * Request to process data (Key)
     */
    public void requestNew(int itemName) {
        // request and return data according ot the URL returned.
        // TODO: 2021/5/6 获取url更换
        String address = getApiUrl(itemName);    // key
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "loading failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // TODO: 2021/5/6 解析替换
                final ApiResponse apiResponse = new Gson().fromJson(Objects.requireNonNull(response.body()).string(), ApiResponse.class);
                if (TextUtils.equals(apiResponse.getStatus(), "ok")) {
                    titleList.clear();
                    for (News news : apiResponse.getNewsList()) {
                        String user_objectId = getIntent().getStringExtra("user_objectId");
                        String url = news.getUrl();
                        String href = url + "?" + user_objectId + "&id=" + news.getSource().getId() + "&name=" + news.getSource().getName();
                        Title title = new Title(news.getTitle(), news.getDescription(), news.getUrlToImage(), news.getUrl(), user_objectId, href);
                        titleList.add(title);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            listView.setSelection(0);
                            refreshLayout.setRefreshing(false);
                        }
                    });
                } else if (TextUtils.equals(apiResponse.getStatus(), "ok")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            refreshLayout.setRefreshing(false);
                        }
                    });
                }
            }
        });
    }

    /**
     * Return correct URL according to different type.
     */
    private String getApiUrl(int itemName) {
        // TODO: 2021/5/6 api的网络连接，修改和替换
        StringBuilder url = new StringBuilder("https://newsapi.org/v2/everything?q=");
        switch (itemName) {
            case ITEM_SOCIETY:
                url.append("society");
                break;
            case ITEM_COUNTY:
                url.append("US");
                break;
            case ITEM_INTERNATION:
                url.append("international");
                break;
            case ITEM_FUN:
                url.append("funny");
                break;
            case ITEM_SPORT:
                url.append("sport");
                break;
            case ITEM_NBA:
                url.append("nba");
                break;
            case ITEM_FOOTBALL:
                url.append("football");
                break;
            case ITEM_TECHNOLOGY:
                url.append("technology");
                break;
            case ITEM_WORK:
                url.append("work");
                break;
            case ITEM_APPLE:
                url.append("apple");
                break;
            case ITEM_WAR:
                url.append("warfare");
                break;
            case ITEM_INTERNET:
                url.append("internet");
                break;
            case ITEM_TREVAL:
                url.append("travel");
                break;
            case ITEM_HEALTH:
                url.append("health");
                break;
            case ITEM_STRANGE:
                url.append("anecdote");
                break;
            case ITEM_LOOKER:
                url.append("Chinese");
                break;
            case ITEM_VR:
                url.append("VR");
                break;
            case ITEM_IT:
                url.append("IT");
                break;
            default:
        }
        url.append("&from=&sortBy=publishedAt&apiKey=3744d4618d1a44b18dc9df3d52b5ee32&language=en&pageSize=100");
        return url.toString();
    }

    /**
     * Return correct ItemName according to actionbar.getTitle().
     */
    private int parseString(String text) {
        if (text.equals("Social News")) {
            return ITEM_SOCIETY;
        }
        if (text.equals("Local News")) {
            return ITEM_COUNTY;
        }
        if (text.equals("Global News")) {
            return ITEM_INTERNATION;
        }
        if (text.equals("Entertainment")) {
            return ITEM_FUN;
        }
        if (text.equals("Sport News")) {
            return ITEM_SPORT;
        }
        if (text.equals("NBA News")) {
            return ITEM_NBA;
        }
        if (text.equals("Soccer News")) {
            return ITEM_FOOTBALL;
        }
        if (text.equals("Technology News")) {
            return ITEM_TECHNOLOGY;
        }
        if (text.equals("Business News")) {
            return ITEM_WORK;
        }
        if (text.equals("Apple News")) {
            return ITEM_APPLE;
        }
        if (text.equals("Military News")) {
            return ITEM_WAR;
        }
        if (text.equals("Mobile Internet")) {
            return ITEM_INTERNET;
        }
        if (text.equals("Travel")) {
            return ITEM_TREVAL;
        }
        if (text.equals("Health")) {
            return ITEM_HEALTH;
        }
        if (text.equals("Anecdote")) {
            return ITEM_STRANGE;
        }
        if (text.equals("VR Technology")) {
            return ITEM_VR;
        }
        if (text.equals("IT News")) {
            return ITEM_IT;
        }
        return ITEM_SOCIETY;
    }

    /**
     * Manage sidebar button, open sidebar
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;

            case R.id.action_settings_main: //思路点
                Toast.makeText(MainActivity.this, "点击了", Toast.LENGTH_SHORT).show();

                //Do search function
                String sql = "select * from Title where user_objectId = ?";

                //Search result
                new BmobQuery<Title>().doSQLQuery(sql, new SQLQueryListener<Title>() {
                    @Override
                    public void done(BmobQueryResult<Title> bmobQueryResult, BmobException e) {
                        if (e == null) {
                            findTitleList = bmobQueryResult.getResults();
                            System.out.println(findTitleList + "----------------------log");
                            Intent intent = new Intent(MainActivity.this, MyCollectnewsActivity.class);
                            intent.putExtra("findTitleList", (Serializable) findTitleList);
                            startActivity(intent);

                        } else {
                            Log.i("MainActivity", "done: Search Error" + e.getMessage());
                        }
                    }
                }, user_objectId);

            default:
        }
        return true;
    }

    /**
     * Manage return button, close sidebar if it is open, otherwise, close the activity
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            finish();
        }
    }


    /*
     * Add 3 dots on the up right coner of the actionbar, can add more funtions
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);//这里是调用menu文件夹中的.xml，在登陆界面label右上角的三角里显示其他功能
        return true;
    }

}
