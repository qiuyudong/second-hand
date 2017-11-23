package com.hznu.echo.second_handmarket.activity;

import android.app.Application;

import cn.bmob.v3.Bmob;

/**
 * <pre>
 *     author : qiuyudong
 *     e-mail : qiuyudongjlu@qq.com
 *     time   : 2017/11/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "36db1f7634113b1e9f86a9b986a46518");
    }
}
