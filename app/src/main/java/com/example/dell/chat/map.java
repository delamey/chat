package com.example.dell.chat;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.zip.Inflater;

/**
 * Created by dell on 2017/2/22.
 */

public class map extends LinearLayout{
    public map(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.map, this);
    }
}
