package com.example.dell.chat;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import static com.baidu.location.d.j.s;
import static com.example.dell.chat.MyApplication.getContext;

public class Login extends AppCompatActivity implements View.OnClickListener {
private Button zhuce,login;
    private ClientThread thread;
    private EditText AccountEdit,PasswordEdit;
    private  String test,password;
    private Socket socket;
    public   Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(getContext(),"发送成功",Toast.LENGTH_LONG).show();
                    AccountEdit.setText("");
                    PasswordEdit.setText("");
                    break;
                case 1:
                    String str=msg.obj.toString();
                    parseXMLWithSAX(str);
                    break;
                case 2:
                    String str2=msg.obj.toString();
                    if (str2.equals("ok")){
                        Intent intent=new Intent(getContext(),MainActivity.class);
                        intent.putExtra("name",test);
                        startActivity(intent);
                    }
                    break;
                default:
                    break;
            }

        }
    };

    private void parseXMLWithSAX(String str) {
        SAXParserFactory factory=SAXParserFactory.newInstance();
        try {
            XMLReader xmlReader= null;
            try {
                xmlReader = factory.newSAXParser().getXMLReader();
                ContentHandler contentHandler=new ContentHandler();
                xmlReader.setContentHandler(contentHandler);
                try {
                    xmlReader.parse(new InputSource(new StringReader(str)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        zhuce= (Button) findViewById(R.id.ZhuCe1);
        login= (Button) findViewById(R.id.loginButton1);
        AccountEdit= (EditText) findViewById(R.id.AccountEdit1);
        PasswordEdit= (EditText) findViewById(R.id.PasswordEdit1);
        login.setOnClickListener(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket=new Socket("mail.digiland.net.cn",8001);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                 thread = new ClientThread(socket);

            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton1:
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        test=AccountEdit.getText().toString();
                        password=PasswordEdit.getText().toString();
                        String xml=null;
                        try {
                           DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
                            xml="<iq method=\"auth\" id=\"101\"><username>"+test+"</username><password>"+password+"</password></iq>";
                            byte []data;
                            data=xml.getBytes();
//                           File file=new File(getExternalCacheDir(),"file.xml");
//                            if (file.exists()){
//                                file.delete();
//                            }
//                            FileWriter fileWriter=new FileWriter(file);
//                            fileWriter.write(xml);
//                            DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
                           BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            writer.write(xml);
                            writer.flush();
//                            dataOutputStream.write(data,0,data.length);
                            handler.sendEmptyMessage(0);
                            thread.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

              // String Str =Http.sendPost("http://192.168.1.106","<iq method="+"\""+"auth"+"\""+"id="+"\""+test+"\""+"><username>"+test+"</username><password>"+password+"</password></iq>");

        }
    }

    public class ClientThread extends Thread {
        private Socket socket;
        public ClientThread(Socket socket) {
            this.socket=socket;
        }
        public void run(){
            String source = null;
            String len;
            String stringBuilder="";
            InputStream inputStream = null;
            try {
                inputStream = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(
                        inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);
                while ((len=br.readLine())!=null){
                    source+=len;
                }
                Message message=new Message();
                message.what=1;
                message.obj=source;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
