package com.example.dell.chat;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

/**
 * Created by dell on 2017/3/16.
 */

public class AudioRecordButton extends android.support.v7.widget.AppCompatButton implements AudioManager.AudioStateListener {
   private  static final  int  STATE_NORMAL=1;
    private  static final  int  STATE_RECORDING=2;
    private  static final  int  STATE_WANT_TO_CANCEL=3;
    private  int mCurStat=STATE_NORMAL;
     private boolean isRecording;
    private  static  final int DISTANCE_Y_CANCEL=50;
    private  DialogManager dialogManager;
    private  AudioManager audioManager;
    private  float mTime;
    private  boolean mReady;
    public AudioRecordButton(Context context) {
       this(context,null);
    }

    public  AudioRecordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        dialogManager=new DialogManager(getContext());
        String dir= Environment.getExternalStorageDirectory()+"/immc_recorder_audios";
        audioManager= AudioManager.getmInstance(dir);
        audioManager.setOnAudioStateListener(this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                try {
                    mReady=true;
                    audioManager.prepareAudio();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
    }
public  interface AudioFinishRecorderListener{
    void  onFinish(float seconds,String filePath);
}
private  AudioFinishRecorderListener  mListener;
    public  void  setAudioFinishRecorderListener(AudioFinishRecorderListener listener){
        mListener=listener;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();
        int x= (int) event.getX();
        int y= (int) event.getY();
        switch (action){
            case  MotionEvent.ACTION_DOWN:
                changeState(STATE_RECORDING);
                break;
            case  MotionEvent.ACTION_MOVE:
                if (isRecording) {
                    if (wantToCancael(x, y)) {
                        changeState(STATE_WANT_TO_CANCEL);
                    } else {
                        changeState(STATE_RECORDING);
                    }
                }
                    break;
                    case MotionEvent.ACTION_UP:
                        if (!mReady)
                        {
                            reset();
                            return super.onTouchEvent(event);
                        }
                        if (!isRecording){
                            dialogManager.tooShort();
                            audioManager.cancel();
                            handler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS,1300);
                        }else if (mTime<0.5f){
                            dialogManager.tooShort();
                            audioManager.cancel();
                            handler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS,1300);
                        }
                        if (mCurStat==STATE_RECORDING )
                        {
                            if (mListener!=null)
                            {
                                mListener.onFinish(mTime,audioManager.getCurrentFilePath());
                            }
                            audioManager.release();
                            dialogManager.dimissDialog();
                            changeState(STATE_NORMAL);

                        }else if (mCurStat==STATE_WANT_TO_CANCEL){
                            dialogManager.dimissDialog();
                            audioManager.cancel();
                           // changeState(STATE_NORMAL);
                        }
                        reset();
                        break;

        }
        return super.onTouchEvent(event);
    }

    private boolean wantToCancael(int x, int y) {
        if (x<0||x>getWidth())
        {
            return true;
        }
        if (y<-DISTANCE_Y_CANCEL||y>getWidth()+DISTANCE_Y_CANCEL){
            return true;
        }

        return false;
    }

    private void changeState(int stateRecording) {
        if (mCurStat != stateRecording)
        {
            mCurStat=stateRecording;
            switch (stateRecording){
                case  STATE_NORMAL:
                    setBackgroundResource(R.drawable.btn_recorder_normal);
                    setText(R.string.str_recorder_normal);
                    break;
                case  STATE_RECORDING:
                    setBackgroundResource(R.drawable.btn_recordering);
                    setText(R.string.str_recorder_recording);
                    if (isRecording){
                       dialogManager.recording();

                    }
                    break;
                case  STATE_WANT_TO_CANCEL:
                    setBackgroundResource(R.drawable.btn_recordering);
                    setText(R.string.str_recorder_cancel);
                    dialogManager.wantToCancel();
                    break;
            }
        }
    }
    private  void reset(){
        isRecording=false;
        mTime=0;
        mReady=false;
        changeState(STATE_NORMAL);
    }
 private  Runnable mGetVoiceLevelRunnable    =new Runnable() {
     @Override
     public void run() {
         while (isRecording){
             try {

                 Thread.sleep(100);
                 mTime+=0.1f;
                 handler.sendEmptyMessage(MSG_VOICE_CHANEGE);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }

     }
 };
    private static  final  int MSG_AUDIO_PREPARED=0X110;
    private static  final  int MSG_VOICE_CHANEGE=0X111;
    private static  final  int MSG_DIALOG_DIMISS=0X112;
private Handler  handler=new Handler()
{
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case MSG_AUDIO_PREPARED:
                dialogManager.showRecordingDialog();
                isRecording=true;
                new Thread(mGetVoiceLevelRunnable).start();
                break;
            case MSG_VOICE_CHANEGE:
                dialogManager.updateVoiceLevel(audioManager.getVoiceLevel(7));
                break;
            case MSG_DIALOG_DIMISS:
                break;
        }
    }
};

    @Override
    public void wellPrepared() {
     handler.sendEmptyMessage(MSG_AUDIO_PREPARED);
    }
}
