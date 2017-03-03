package com.example.dell.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dell on 2017/3/3.
 */

public class TalkAdapter extends RecyclerView.Adapter <TalkAdapter.ViewHolder>{
   private List<Msg> mMsgList;

    public TalkAdapter(List<Msg>  mMsgList){
        this.mMsgList=mMsgList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
      Msg  msg=mMsgList.get(position);
        if (msg.getType()==Msg.TYPE_RECEIVED){
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }else {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.rightMsg.setText(msg.getContent());
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
        public ViewHolder(View itemView) {
            super(itemView);
            leftLayout= (LinearLayout) itemView.findViewById(R.id.left_layout);
            rightLayout= (LinearLayout) itemView.findViewById(R.id.right_layout);
            leftMsg= (TextView) itemView.findViewById(R.id.left_Received);
            rightMsg= (TextView) itemView.findViewById(R.id.right_Received);
        }
    }
}
