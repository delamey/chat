package com.example.dell.chat;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by dell on 2017/3/17.
 */

public class AudioManager {
    private MediaRecorder mediaRecorder;
    private  boolean isPepared;
    private  String mDir;
    private   String mCurrentFilePath;
    private  static  AudioManager mInstance;
    public String getCurrentFilePath(){
        return  mCurrentFilePath;
    }

    private  AudioManager(String dir){
        mDir=dir;

    }



    public  interface AudioStateListener
    {
        void wellPrepared();
    }
    public  AudioStateListener mListener;
    public  void  setOnAudioStateListener(AudioStateListener mListener){
        this.mListener=mListener;
    }
    public  static  AudioManager getmInstance( String dir){
        if (mInstance==null){
            synchronized (AudioManager.class)
            {
                if (mInstance==null){
                    mInstance=new AudioManager(dir);
                }
            }
        }
        return  mInstance;
    }
    public  void  prepareAudio() throws IOException {
        isPepared=false;
        File dir=new File(mDir);
        if (!dir.exists()){
            dir.mkdir();
        }
        String feleName=generateFileName();

        File file=new File(dir,feleName);
        mCurrentFilePath=file.getAbsolutePath();
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setOutputFile(file.getAbsolutePath());
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        mediaRecorder.prepare();
        mediaRecorder.start();
        isPepared=true;
        if (mListener!=null ){
            mListener.wellPrepared();
        }


    }

    private String generateFileName() {
        return UUID.randomUUID().toString()+".amr";
    }

    public int getVoiceLevel(int maxLevel){
        if (isPepared){
            //mediaRecorder.getMaxAmplitude()返回1-32767
           return  maxLevel*mediaRecorder.getMaxAmplitude()/32768+1;
        }
        return 1;
    }
    public  void release(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;

    }
    public  void cancel(){
          release();
        if (mCurrentFilePath!=null){
            File file=new File(mCurrentFilePath);
            file.delete();
            mCurrentFilePath=null;
        }
    }
}
