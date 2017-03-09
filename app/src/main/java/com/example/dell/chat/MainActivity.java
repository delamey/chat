package com.example.dell.chat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.baidu.mapapi.BMapManager.getContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView button,find,directory,talk;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FloatingActionButton floatingActionButton;
    private Fruit[] fruits={new Fruit("1111",R.mipmap.p1),new Fruit("2222",R.mipmap.p2),new Fruit("3333",R.mipmap.p3),new Fruit("4444",R.mipmap.p4),new Fruit("5555",R.mipmap.p5),new Fruit("6666",R.mipmap.p6)};
    private List<Fruit>  fruitList=new ArrayList<>();
    private  FruitAdapter  adapter;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar);
        Intent intent=getIntent();
         name=intent.getStringExtra("name");
        //String password=intent.getStringExtra("password");
        button = (ImageView) findViewById(R.id.yourself);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView= (NavigationView) findViewById(R.id.nav_view);
        drawerLayout= (DrawerLayout) findViewById(R.id.drawer_layout);
        swipeRefreshLayout= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        find= (ImageView) findViewById(R.id.find);
        find.setOnClickListener(this);
        directory= (ImageView) findViewById(R.id.directory);
        directory.setOnClickListener(this);
        talk= (ImageView) findViewById(R.id.talk);
        talk.setOnClickListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
           actionBar.setDisplayHomeAsUpEnabled(true);
       }
        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                return  true;
            }
        });
        recyclerView= (RecyclerView) findViewById(R.id.recycler_view);
        initFruits();
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter=new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);
        button.setOnClickListener(this);


        floatingActionButton= (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);
    }

    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFruits();
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set:
                break;
            case R.id.info:
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
            default:
                break;
        }
        return true;
    }
    private void initFruits() {
        fruitList.clear();
        for(int i=0;i<50;i++){
            Random random=new Random();
            int index=random.nextInt(fruits.length);
            fruitList.add(fruits[index]);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.yourself:
                Intent intent =new Intent(MainActivity.this,yourself_activity.class);
                startActivity(intent);
                break;
            case R.id.find:
                Intent  intent1=new Intent(MainActivity.this,DataActivity.class);
                startActivity(intent1);
                break;
            case  R.id.directory:
                Intent intent2=new Intent(MainActivity.this,Directory.class);
                startActivity(intent2);
                break;
            case R.id.fab:
                Snackbar.make(view,"Data delete",Snackbar.LENGTH_SHORT)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(MainActivity.this,"data restored",Toast.LENGTH_SHORT).show();
                            }
                        }).show();
                break;
            case R.id.talk:
                Intent intent3=new Intent(MainActivity.this,Socket1.class);
                intent3.putExtra("name",name);
               //intent3.putExtra("password",password);

                startActivity(intent3);
            default:
                break;
    }

    }
}
