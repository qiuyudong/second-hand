package com.hznu.echo.second_handmarket.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by qiuyudong on 2018/4/5.
 */

public class Goods_Comment extends BmobObject {
    private String comment;
    private User create_user;
    private Second_Goods second_goods;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getCreate_user() {
        return create_user;
    }

    public void setCreate_user(User create_user) {
        this.create_user = create_user;
    }


    public Second_Goods getSecond_goods() {
        return second_goods;
    }

    public void setSecond_goods(Second_Goods second_goods) {
        this.second_goods = second_goods;
    }

    @Override
    public String toString() {
        return "Goods_Comment{" +
                "comment='" + comment + '\'' +
                ", create_user=" + create_user +
                ", second_goods=" + second_goods +
                '}';
    }
}
