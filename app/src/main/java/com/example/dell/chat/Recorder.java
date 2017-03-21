package com.example.dell.chat;

/**
 * Created by dell on 2017/3/20.
 */

public class Recorder {
    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    float time;
    String filePath;

    public Recorder(float time, String filePath) {
        super();
        this.time = time;
        this.filePath=filePath;

    }

}
