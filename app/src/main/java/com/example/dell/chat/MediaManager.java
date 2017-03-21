package com.example.dell.chat;

import android.media.*;
import android.media.AudioManager;

import java.io.IOException;

/**
 * Created by dell on 2017/3/20.
 */

public class MediaManager {
    public  static  MediaPlayer MediaPlayer;
    private static boolean isPause;

    public static void playSound(String filePath, MediaPlayer.OnCompletionListener onCompletionListener) {
         if (MediaPlayer==null){
             MediaPlayer=new MediaPlayer();
             MediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                 @Override
                 public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                     MediaPlayer.reset();
                     return false;
                 }
             });

         }else {
             MediaPlayer.reset();
         }
         MediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        MediaPlayer.setOnCompletionListener(onCompletionListener);
        try {
            MediaPlayer.setDataSource(filePath);
            MediaPlayer.prepare();
            MediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void  pause(){
        if (MediaPlayer!=null&&MediaPlayer.isPlaying())
        {
            MediaPlayer.pause();
            isPause=true;
        }
    }
    public static void resume()
    {
        if (MediaPlayer!=null&&isPause){
            MediaPlayer.start();
            isPause=false;
        }
    }
    public static void   release(){
        if (MediaPlayer!=null)
        {
            MediaPlayer.release();
            MediaPlayer=null;
        }
    }
}
