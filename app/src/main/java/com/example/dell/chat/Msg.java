package com.example.dell.chat;

/**
 * Created by dell on 2017/3/3.
 */

public class Msg {
    public static  final int TYPE_RECEIVED=0;
    public  static  final int TYPE_SENT=1;
    public  static  final int TYPE_SENT_YUYIN=2;
    public  static  final int TYPE_RECEIVED_YUYIN=3;
    private  String content;
    private int   type;
    private float time;
    private String filePath;

    public Msg(String content,int type){
        this.content=content;
        this.type=type;

    }
    public Msg(String filePath,float time,int type){
        this.time = time;
        this.filePath=filePath;
        this.type=type;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }
}
