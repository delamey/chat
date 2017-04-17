package com.example.dell.chat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.a.r;
import com.baidu.platform.comapi.map.n;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Socket1 extends AppCompatActivity  {
    private Button send;
    private Toolbar toolbar;
    private FloatingActionButton fb,fb1;
    //private TextView textView9;
    private List<Msg> msgList = new ArrayList<>();
    private TalkAdapter talkAdapter;
    private RecyclerView recyclerView;
    private EditText editText;
    private AudioRecordButton audioRecordButton;
    private static final int GET = 1;
    Socket s = null;
    String out2;
    String in;
    String inn;

String Myname;

    @Override
    protected void onPause() {
        super.onPause();
        MediaManager.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaManager.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaManager.resume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_socket);
        Intent intent = getIntent();
          Myname = intent.getStringExtra("name");
        //String Mypassword = intent.getStringExtra("password");
        editText = (EditText) findViewById(R.id.EditText1);
        //textView9 = (TextView) findViewById(R.id.text9);
        recyclerView = (RecyclerView) findViewById(R.id.socket_RecyclerView);
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
        fb1= (FloatingActionButton) findViewById(R.id.QieHuan1);
        fb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setVisibility(View.VISIBLE);
                send.setVisibility(View.VISIBLE);
                fb.setVisibility(View.VISIBLE);
                fb1.setVisibility(View.GONE);
                audioRecordButton.setVisibility(View.GONE);

            }
        });
        audioRecordButton= (AudioRecordButton) findViewById(R.id.recorder_button);
        audioRecordButton.setAudioFinishRecorderListener(new AudioRecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                Msg msg=new Msg(filePath,seconds,Msg.TYPE_SENT_YUYIN);

                msgList.add(msg);
                talkAdapter.notifyItemInserted(msgList.size()-1);
                recyclerView.scrollToPosition(msgList.size()-1);

            }
        });

        fb= (FloatingActionButton) findViewById(R.id.QieHuan);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              audioRecordButton.setVisibility(View.VISIBLE);
                editText.setVisibility(View.GONE);
                send.setVisibility(View.GONE);
                fb.setVisibility(View.GONE);
                fb1.setVisibility(View.VISIBLE);
            }
        });
        send = (Button) findViewById(R.id.button);
        //send.setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    s = new Socket("192.168.1.104", 9999);
                    ClientThread thread = new ClientThread(s);
                    thread.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
                        try {
                            BufferedWriter out=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                            in= editText.getText().toString();
                            out2 ="say,"+Myname+","+Myname+","+in+"\n";
                            out.write(out2);
                            out.flush();
                            Message message = new Message();
                            message.what = 2;
                            message.obj=in;
                            handler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //String s1=br.readLine();
                       // inn= Http.sendPost("http://10.0.2.2:8080/chat2/Talk","Myname="+Myname+"&yourname="+Myname+"&message="+in);



                    }
                }).start();
            }
        });

    }
private Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case GET:
               String content= msg.obj.toString();
                Msg msg1=new Msg(content,Msg.TYPE_RECEIVED);
                msgList.add(msg1);
                talkAdapter.notifyItemInserted(msgList.size()-1);
                recyclerView.scrollToPosition(msgList.size()-1);
                break;
            case 2:
                String content1= msg.obj.toString();
                Msg msg2=new Msg(content1,Msg.TYPE_SENT);
                msgList.add(msg2);
                talkAdapter.notifyItemInserted(msgList.size()-1);
                recyclerView.scrollToPosition(msgList.size()-1);
                editText.setText("");
             break;
            default:
                break;

        }
    }
};

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



    private class ClientThread extends Thread {

        private Socket socket;
        public ClientThread(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                InputStream inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(
                        inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);
                try {
                    // 信息的格式：(login||logout||say),发送人,收发人,信息体
                    while (true) {
                        String msg=br.readLine();
                        //System.out.println(msg);
                        String[] str = msg.split(",");
                        switch (str[0]) {
                            case "say":
                                Message message=new Message();
                                message.obj=str[3].toString();
                                message.what=GET;
                                handler.sendMessage(message);
                                break;
                            default:
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    }



