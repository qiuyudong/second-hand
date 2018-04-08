package com.hznu.echo.second_handmarket.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * <pre>
 *     author : wengqian
 *     e-mail : wengqianjlu@qq.com
 *     time   : 2017/11/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class User extends BmobUser {
    private String sex;
    private String nickname;
    private String headPortraitPath;
    private String school;
    private Integer role;
    private String signature;
    private BmobRelation liked_goods;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadPortraitPath() {
        return headPortraitPath;
    }

    public void setHeadPortraitPath(String headPortraitPath) {
        this.headPortraitPath = headPortraitPath;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public BmobRelation getLiked_goods() {
        return liked_goods;
    }

    public void setLiked_goods(BmobRelation liked_goods) {
        this.liked_goods = liked_goods;
    }

    @Override
    public String toString() {
        return "User{" +
                "sex='" + sex + '\'' +
                ", nickname='" + nickname + '\'' +
                ", headPortraitPath='" + headPortraitPath + '\'' +
                ", school='" + school + '\'' +
                ", role=" + role +
                ", signature='" + signature + '\'' +
                ", liked_goods=" + liked_goods +
                '}';
    }
}
