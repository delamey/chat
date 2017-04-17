package com.example.dell.chat;

import org.litepal.crud.DataSupport;

/**
 * Created by root on 2017/3/22.
 */

public class information extends DataSupport {
    private String name;
    private String password;
    private String remeber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword(String s) {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getRemeber() {
        return remeber;
    }

    public void setRemeber(String remeber) {
        this.remeber = remeber;
    }
}
