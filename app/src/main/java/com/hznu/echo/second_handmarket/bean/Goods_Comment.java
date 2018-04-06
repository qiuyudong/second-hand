package com.hznu.echo.second_handmarket.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by qiuyudong on 2018/4/5.
 */

public class Goods_Comment extends BmobObject {
    private String comment;
    private User create_user;
    private Second_Goods current_goods;

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

    public Second_Goods getCurrent_goods() {
        return current_goods;
    }

    public void setCurrent_goods(Second_Goods current_goods) {
        this.current_goods = current_goods;
    }

    @Override
    public String toString() {
        return "Goods_Comment{" +
                "comment='" + comment + '\'' +
                ", create_user=" + create_user +
                ", current_goods=" + current_goods +
                '}';
    }
}
