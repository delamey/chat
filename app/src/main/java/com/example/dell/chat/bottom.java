package com.example.dell.chat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;



/**
 * Created by dell on 2017/2/21.
 */

public class bottom extends LinearLayout {
    public bottom(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.bottom,this);
    }
}
