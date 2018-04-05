package com.hznu.echo.second_handmarket.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * <pre>
 *     author : qiuyudong
 *     e-mail : qiuyudongjlu@qq.com
 *     time   : 2017/11/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class Second_Goods extends BmobObject {
    private String name;  //物品名字
    private String type;  //物品类型
    private User upload_user; //上传用户
    private String imagePath; //上传图片路径
    private String price;  //上传的价格
    private String  description; //物品描述
    private Integer  state;  //物品状态
    private BmobRelation liked_user;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUpload_user() {
        return upload_user;
    }

    public void setUpload_user(User upload_user) {
        this.upload_user = upload_user;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public BmobRelation getLiked_user() {
        return liked_user;
    }

    public void setLiked_user(BmobRelation liked_user) {
        this.liked_user = liked_user;
    }

    @Override
    public String toString() {
        return "Second_Goods{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", upload_user=" + upload_user +
                ", imagePath='" + imagePath + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", state=" + state +
                ", liked_user=" + liked_user +
                '}';
    }
}
