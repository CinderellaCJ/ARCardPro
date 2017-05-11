package com.cj.arcard.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by chenjing on 2017/4/6.
 */

public class MyUser extends BmobUser{
    private String avatorUrl;
    private BmobFile userAvator;
    private String userNickname;

    public String getAvatorUrl() {
        return avatorUrl;
    }

    public void setAvatorUrl(String avatorUrl) {
        this.avatorUrl = avatorUrl;
    }

    public BmobFile getUserAvator() {
        return userAvator;
    }

    public void setUserAvator(BmobFile userAvator) {
        this.userAvator = userAvator;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
