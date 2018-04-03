package com.hznu.echo.second_handmarket.bean;

import android.graphics.Bitmap;

import cn.bmob.v3.BmobUser;

/**
 * <pre>
 *     author : qiuyudong
 *     e-mail : qiuyudongjlu@qq.com
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
    private String studentId;
    private Integer role;
    private String signature;

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Bitmap getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(Bitmap head_portrait) {
        this.head_portrait = head_portrait;
    }

    private Bitmap head_portrait;

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

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
