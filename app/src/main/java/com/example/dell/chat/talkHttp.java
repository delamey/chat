package com.example.dell.chat;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import static com.example.dell.chat.R.id.info;

public class talkHttp extends AppCompatActivity implements View.OnClickListener {
private Button send;
    private EditText message;
    private List<Msg> msgList = new ArrayList<>();
    private TalkAdapter talkAdapter;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private String Myname;
    private  StringBuilder stringBuilder;

    private  ServerSocket serverSocket= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk_http);
        Intent intent = getIntent();
        Myname = intent.getStringExtra("name");
        send= (Button) findViewById(R.id.button_http);
        message= (EditText) findViewById(R.id.EditText1_http);
        recyclerView= (RecyclerView) findViewById(R.id.http_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        talkAdapter = new TalkAdapter(this,msgList);
        recyclerView.setAdapter(talkAdapter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        send.setOnClickListener(this);

        try {
            serverSocket = new ServerSocket(30000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cccc sss=new cccc() ;
        sss.start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String info = null;
//                try {
//                    //int len;
////                    while (true) {
//                        Socket socket = serverSocket.accept();
//                        InputStream inputStream = socket.getInputStream();
//                        InputStreamReader inputStreamReader = new InputStreamReader(
//                                inputStream);
//                        BufferedReader br = new BufferedReader(inputStreamReader);
//                        while ((info = br.readLine()) != null) {
//                            Message message = new Message();
//                            message.what = 2;
//                            message.obj = info;
//                            handler.sendMessage(message);
////                        }
//                    }
//                    } catch(IOException e){
//                        e.printStackTrace();
//                    }
//            }
//        }).start();


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;

        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_http:
                  new Thread(new Runnable() {
                      @Override
                      public void run() {
                          String message1=message.getText().toString();
                          Http.sendPost("http://192.168.1.106:8080/chat2/Talk","message="+message1+"&"+"Myname="+Myname+"&"+"Toname="+Myname);
                          Message message=new Message();
                          message.what=1;
                          message.obj=message1;
                          handler.sendMessage(message);
                      }
                  }).start();



        }
    }
    private android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String content= msg.obj.toString();
                    Msg msg1=new Msg(content,Msg.TYPE_SENT);
                    msgList.add(msg1);
                    talkAdapter.notifyItemInserted(msgList.size()-1);
                    recyclerView.scrollToPosition(msgList.size()-1);
                    break;
                case 2:
                    String content1= msg.obj.toString();
                    Msg msg2=new Msg(content1,Msg.TYPE_RECEIVED);
                    msgList.add(msg2);
                    talkAdapter.notifyItemInserted(msgList.size()-1);
                    recyclerView.scrollToPosition(msgList.size()-1);
            }
        }
    };


    private class cccc extends Thread {
        public void run() {
            int info ;
            char [] cr=new char[1024];
            try {
                //int len;
//                    while (true) {
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(
                        inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);
                while ((info = br.read()) != -1) {
                    br.read(cr,0,info);

                    Message message = new Message();
                    message.what = 2;
                    message.obj = cr;
                    handler.sendMessage(message);
//                        }
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
