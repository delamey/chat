package com.example.dell.chat;

import android.os.Message;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by root on 2017/4/12.
 */

public class ContentHandler extends DefaultHandler {
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

        if (qName.equals("iq")) {
            int i = 1;
            type = attributes.getValue(i);
            Message message = new Message();
            message.what=2;
            message.obj = type;
            Login login=new Login();
            login.handler.sendMessage(message);
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
