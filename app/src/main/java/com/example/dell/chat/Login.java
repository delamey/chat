package com.example.dell.chat;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
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
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import static com.baidu.location.d.j.b;
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
                    byte[] re= (byte[]) msg.obj;
                    try {
                        String str3=new String(re,"UTF-8");
                        String[] str1=str3.split("<");
                        parseXMLWithSAX("<"+str1[1]);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    break;
                case 2:
                    String str2=msg.obj.toString();
                    if (str2.equals("ok")){
                        Intent intent=new Intent(Login.this,MainActivity.class);
                        intent.putExtra("name",test);
                        startActivity(intent);

                    }
                    break;
                case 3:
                    String s=msg.obj.toString();
                    test=s;
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
        LitePal.getDatabase();
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
                        Message message=new Message();
                        message.what=3;
                        message.obj=test;
                        handler.sendMessage(message);
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
                           // BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            byte[] b={0x0c,0,0,0,(byte)xml.length(),0,0,0,0x06,0,0,0};
                            dataOutputStream.write(b);
                            dataOutputStream.write(data);
                            dataOutputStream.flush();
                            //dataOutputStream.close();
                            //writer.flush();
//                            dataOutputStream.write   (data,0,data.length);
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
            int len;
//            String stringBuilder="";
            InputStream inputStream = null;
            byte [] b=new byte[1024];
            try {
                DataInputStream in=new DataInputStream(socket.getInputStream());
                in.read(b);


//                inputStream = socket.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(
//                        inputStream,"UTF-8");
//                BufferedReader br = new BufferedReader(inputStreamReader);
//                source=br.readLine();
////                while (( in.read(b))!=-1){
//                    source=new String(b,0,b.length);
//                }

               // in.close();

                Message message=new Message();
                message.what=1;
                message.obj=b;
                handler.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    /**
     * Created by root on 2017/4/12.
     */

     class ContentHandler extends DefaultHandler {
        private String nodeName;
        // private StringBuilder id;
        //private StringBuilder name;
        //private StringBuilder version;
        private String type;


        @Override
        public void startDocument() throws SAXException {
            // id=new StringBuilder();
            // name=new StringBuilder();
            // version=new StringBuilder();

        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            if (localName.equals("iq")) {
                int i = 1;
                type = attributes.getValue(i);
                Message message = new Message();
                message.what=2;
                message.obj = type;
                handler.sendMessage(message);
            }


        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
//        if ("app".equals(localName)){
//            Log.d("ContentHandler","id is"+id.toString().trim());
//            Log.d("ContentHandler","name is"+name.toString().trim());
//            Log.d("ContentHandler","version is"+version.toString().trim());
//            id.setLength(0);
//            name.setLength(0);
//            version.setLength(0);
//        }
            if ("iq".equals(localName)) {
                Log.d("ContentHandler","type is"+type.trim());
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
//        if ("id".equals(nodeName)){
//            id.append(ch,start,length);
//        }else  if ("name".equals(nodeName)){
//            name.append(ch,start,length);
//
//        }else if ("version".equals(nodeName)){
//            version.append(ch,start,length);
//        }
        }
    }

}
