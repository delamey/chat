package com.example.dell.chat;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.litepal.LitePal;

public class DataActivity extends AppCompatActivity implements View.OnClickListener {
private Button create,add;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
      ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        create= (Button) findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LitePal.getDatabase();
            }
        });
        add= (Button) findViewById(R.id.add);
        add.setOnClickListener(this);
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Fruit fruit =new Fruit();
//                fruit.setName("77777");
//                fruit.setImageId(R.mipmap.ic_launcher);
//                fruit.save();
//            }
//        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;

        }
        return  true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:
                Fruit fruit =new Fruit();
                fruit.setName("77777");
                fruit.setImageId(R.mipmap.ic_launcher);
                fruit.save();
                break;
            default:
                break;

        }
    }
}
