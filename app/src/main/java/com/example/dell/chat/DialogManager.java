package com.example.dell.chat;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dell on 2017/3/16.
 */

public class DialogManager {
    private Dialog dialog;
    private ImageView mIcon;
    private ImageView mVoice;
    private TextView mLable;
    private Context mContext;
    public  DialogManager(Context context)
    {
        mContext=context;
    }
    public  void  showRecordingDialog()
    {
     dialog=new Dialog(mContext, R.style.Theme_AudioDialog);
        LayoutInflater inflater=LayoutInflater.from(mContext);
        View view=inflater.inflate(R.layout.dialog,null);
        dialog.setContentView(view);
        mIcon= (ImageView) dialog.findViewById(R.id.id_recorder_dialog_icon);
        mLable= (TextView) dialog.findViewById(R.id.id_recorder_dialog_label);
        mVoice= (ImageView) dialog.findViewById(R.id.id_recorder_dialog_voice);
        dialog.show();
    }
    public  void  wantToCancel(){
        if (dialog!=null&&dialog.isShowing()){
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);
            mIcon.setBackgroundResource(R.mipmap.n2);
            mLable.setText("松开手指，取消发送");

        }
    }
    public void  tooShort(){
        if (dialog!=null&&dialog.isShowing()){
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.GONE);
            mLable.setVisibility(View.VISIBLE);
            mIcon.setBackgroundResource(R.mipmap.n2);
            mLable.setText("录音时间过短");
        }
    }
    public void dimissDialog(){
       if (dialog!=null&&dialog.isShowing()){
           dialog.dismiss();
           dialog=null;
       }
    }
    public  void  updateVoiceLevel(int level){
        if (dialog!=null&&dialog.isShowing()){
//            mIcon.setVisibility(View.VISIBLE);
//            mVoice.setVisibility(View.VISIBLE);
//            mLable.setVisibility(View.VISIBLE);
            int resId=mContext.getResources().getIdentifier("n"+level,"mipmap",mContext.getPackageName());
            mVoice.setImageResource(resId);
        }
    }
    public  void  recording(){
        if (dialog!=null&&dialog.isShowing()){
            mIcon.setVisibility(View.VISIBLE);
            mVoice.setVisibility(View.VISIBLE);
            mLable.setVisibility(View.VISIBLE);
            mIcon.setBackgroundResource(R.mipmap.n2);
            mLable.setText("松开手指，取消发送");
        }
    }
}
