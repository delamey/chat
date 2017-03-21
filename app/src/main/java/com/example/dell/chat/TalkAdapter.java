package com.example.dell.chat;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import static android.media.CamcorderProfile.get;

/**
 * Created by dell on 2017/3/3.
 */

public class TalkAdapter extends RecyclerView.Adapter <TalkAdapter.ViewHolder>{
   private List<Msg> mMsgList;
   private Context mContext;
    private int mMinItemWidth;
    private int mMaxItemWidth;
    private View AnimaView;
    public TalkAdapter(Context context,List<Msg>  mMsgList){
        this.mMsgList=mMsgList;
        this.mContext=context;
        WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mMinItemWidth= (int) (displayMetrics.widthPixels*0.15f);
        mMaxItemWidth= (int) (displayMetrics.widthPixels*0.7f);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext==null){
            mContext=parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview,parent,false);
        final TalkAdapter.ViewHolder holder=new TalkAdapter.ViewHolder(view);
        holder.rightMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AnimaView!=null){
                    AnimaView.setBackgroundResource(R.mipmap.dirctory);
                    AnimaView=null;
                }
                MediaManager.playSound(mMsgList.get(holder.getAdapterPosition()).getFilePath(), new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                      AnimaView.setBackgroundResource(R.mipmap.dirctory);
                    }
                });
            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      Msg  msg=mMsgList.get(position);
        if (msg.getType()==Msg.TYPE_RECEIVED){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }else if(msg.getType()==Msg.TYPE_SENT) {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msg.getContent());
        }else  if (msg.getType()==Msg.TYPE_RECEIVED_YUYIN){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText((int) msg.getTime());
            ViewGroup.LayoutParams lp=holder.viewLeft.getLayoutParams();
            lp.width= (int) (mMinItemWidth+(mMaxItemWidth/60f*msg.getTime()));
        }else  if (msg.getType()==Msg.TYPE_SENT_YUYIN){
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText((int) msg.getTime());
            ViewGroup.LayoutParams lp=holder.viewRight.getLayoutParams();
            lp.width= (int) (mMinItemWidth+(mMaxItemWidth/60f*msg.getTime()));
        }
    }



    @Override
    public int getItemCount() {
        return mMsgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout,rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        ImageView imageViewLeft,imageViewRight;
        View viewLeft,viewRight;
        public ViewHolder(View itemView) {
            super(itemView);
            leftLayout= (LinearLayout) itemView.findViewById(R.id.left_layout);
            rightLayout= (LinearLayout) itemView.findViewById(R.id.right_layout);
            leftMsg= (TextView) itemView.findViewById(R.id.id_recorder_time_left);
            rightMsg= (TextView) itemView.findViewById(R.id.id_recorder_time_right);
            imageViewLeft= (ImageView) itemView.findViewById(R.id.id_left_icon);
            imageViewRight= (ImageView) itemView.findViewById(R.id.id_right_icon);
            viewLeft=itemView.findViewById(R.id.id_recorder_anmin_left);
            viewRight=itemView.findViewById(R.id.id_recorder_anmin_right);
        }
    }
}
