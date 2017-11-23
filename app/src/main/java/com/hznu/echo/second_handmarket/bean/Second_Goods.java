package com.hznu.echo.second_handmarket.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

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
    private String upload_user; //上传用户
    private String upload_user_nickname;  //上传用户的昵称
    private String upload_time;  //上传时间
    private String imagePath; //上传图片路径
    private String price;  //上传的价格
    private String  description; //物品描述
    private Integer  state;  //物品状态
    private String address;  //所在位置
    private String TransactionType; //交易类型
    private List<String> goods_comment; //商品评论


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

    public String getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(String upload_time) {
        this.upload_time = upload_time;
    }

    public String getUpload_user_nickname() {
        return upload_user_nickname;
    }

    public void setUpload_user_nickname(String upload_user_nickname) {
        this.upload_user_nickname = upload_user_nickname;
    }

    public String getUpload_user() {
        return upload_user;
    }

    public void setUpload_user(String upload_user) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    public List<String> getGoods_comment() {
        return goods_comment;
    }

    public void setGoods_comment(List<String> goods_comment) {
        this.goods_comment = goods_comment;
    }

    @Override
    public String toString() {
        return "Second_Goods{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", upload_user='" + upload_user + '\'' +
                ", upload_user_nickname='" + upload_user_nickname + '\'' +
                ", upload_time='" + upload_time + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", state=" + state +
                ", address='" + address + '\'' +
                ", TransactionType='" + TransactionType + '\'' +
                ", goods_comment=" + goods_comment +
                '}';
    }
}
