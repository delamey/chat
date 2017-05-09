package com.example.dell.chat;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import static android.R.id.content;
import static com.example.dell.chat.R.id.fruit_content_text;

public class FruitActivity extends AppCompatActivity {
public  static  final String FRUIT_NAME="fruit_name";
    public  static  final String FRUIT_IMAGE_ID="fruit_image_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);
        Intent intent=getIntent();
        String fruitName=intent.getStringExtra(FRUIT_NAME);
        int fruitImageId=intent.getIntExtra(FRUIT_IMAGE_ID,0);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView imageView= (ImageView) findViewById(R.id.fruit_image_view);
        TextView  textView= (TextView) findViewById(fruit_content_text);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(fruitName);
        Glide.with(this).load(fruitImageId).into(imageView);
        textView.setText(fruitName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                this.setContentView(R.layout.activity_data);
//                Button create= (Button) findViewById(R.id.create);
//                create.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(FruitActivity.this,"Ssssssss",Toast.LENGTH_LONG).show();
//                    }
//                });
                finish();
                return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}
