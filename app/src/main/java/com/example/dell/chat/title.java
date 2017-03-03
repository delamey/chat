package com.example.dell.chat;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import java.util.zip.Inflater;

/**
 * Created by dell on 2017/2/28.
 */

public class title extends AppBarLayout {
    public title(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title, this);
    }
}
