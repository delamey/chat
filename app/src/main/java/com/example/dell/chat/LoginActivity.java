package com.example.dell.chat;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.example.dell.chat.MyApplication.getContext;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
private Toolbar toolbar;
    private EditText password,account;
    private Button register,login;
    String passwordString;
    String accountString;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String R=msg.obj.toString();
                    if (R.equals("true")){
                  Toast.makeText(getApplicationContext(),"注册成功，请登录",Toast.LENGTH_SHORT).show();
                        password.setText("");
                        account.setText("");
              }else {
                  Toast.makeText(getApplicationContext(),"注册失败，账号或者密码已经存在",Toast.LENGTH_SHORT).show();
              }
              break;
                case 2:
                    String R1=msg.obj.toString();
                    if (R1.equals("true")){
                  Toast.makeText(getContext(),"登录成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("name",account.getText().toString());
                        //intent.putExtra("password",password.getText().toString());
                        startActivity(intent);
              }else {
                  Toast.makeText(getContext(),"登录失败，请重新登录",Toast.LENGTH_SHORT).show();
              }
              break;
                default:
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("欢迎使用Chat");
        password= (EditText) findViewById(R.id.PasswordEdit);
        account= (EditText) findViewById(R.id.AccountEdit);
        register= (Button) findViewById(R.id.ZhuCe);
        login= (Button) findViewById(R.id.loginButton);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        //Http.sendPost("http://localhost:8080/chat/Register","name="+accountString+"&"+"password="+passwordString);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ZhuCe:
                new  Thread(new Runnable() {
                    @Override
                    public void run() {
                        passwordString=password.getText().toString();
                        accountString=account.getText().toString();
                        String receive= Http.sendPost("http://10.0.2.2:8080/chat2/Register","name="+accountString+"&"+"password="+passwordString);
                        Message message=new Message();
                        message.obj=receive;
                        message.what=1;
                        handler.sendMessage(message);
                    }
                }).start();
               //String receive= Http.sendPost("http://localhost:8080/chat/Register","name="+accountString+"&"+"password="+passwordString);
//              if (receive=="true"){
//                  Toast.makeText(this,"注册成功，请登录",Toast.LENGTH_SHORT).show();
//              }else {
//                  Toast.makeText(this,"注册失败，账号或者密码已经存在",Toast.LENGTH_SHORT).show();
//              }
              break;
            case R.id.loginButton:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        passwordString=password.getText().toString();
                        accountString=account.getText().toString();
                        String receive1= Http.sendPost("http://192.168.1.110:8080/chat2/Login","name="+accountString+"&"+"password="+passwordString);
                        Message message=new Message();
                        message.obj=receive1;
                        message.what=2;
                        handler.sendMessage(message);
                    }
                }).start();
               break;
            default:
                break;
//                 if (receive1=="true"){
//                     Intent intent= new Intent(LoginActivity.this,MainActivity.class);
//                     startActivity(intent);
//                 }else {
//                     Toast.makeText(this,"账号密码错误请重新输入",Toast.LENGTH_SHORT).show();
//                     password.setText("");
//                     account.setText("");
//                 }
        }
    }
}
