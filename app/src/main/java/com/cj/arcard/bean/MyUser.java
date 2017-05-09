package com.cj.arcard.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by chenjing on 2017/4/6.
 */

public class MyUser extends BmobUser{
    private String avatorUrl;

    public String getAvatorUrl() {
        return avatorUrl;
    }

    public void setAvatorUrl(String avatorUrl) {
        this.avatorUrl = avatorUrl;
    }
}
