package com.hznu.echo.second_handmarket.base.IM.event;

import cn.bmob.newim.bean.BmobIMUserInfo;


public class ChatEvent {

    public BmobIMUserInfo info;

    public ChatEvent(BmobIMUserInfo info){
        this.info=info;
    }
}
