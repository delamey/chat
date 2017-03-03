package com.example.dell.chat;

import org.litepal.crud.DataSupport;

/**
 * Created by dell on 2017/2/24.
 */

public class Fruit extends DataSupport {
    private String name;
    private int  imageId;
    public  Fruit(){

    }
    public Fruit(String name,int imageId){
        this.name=name;
        this.imageId=imageId;
    }


    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
