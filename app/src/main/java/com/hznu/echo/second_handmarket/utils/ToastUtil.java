package com.hznu.echo.second_handmarket.utils;

import android.annotation.SuppressLint;
import android.widget.Toast;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * <pre>
 *     author : qiuyudong
 *     e-mail : qiuyudongjlu@qq.com
 *     time   : 2017/11/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class ToastUtil
{
    private ToastUtil()
    {
        /** cannot be instantiated**/
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    private static Toast mToast;
    public static boolean isShow = true;
    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message)
    {
        if (isShow){
            mToast =Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(String message)
    {
        if (isShow){
            mToast=Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
    /**
     * 短时间显示Toast把前面cancel掉
     *
     * @param message
     */
    public static void showAndCancel(CharSequence message)
    {
        cancel();
        if (isShow){
            mToast=Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }
    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message)
    {
        if (isShow){
            mToast= Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            mToast.show();
        }
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(String message)
    {
        if (isShow){
            mToast= Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
            mToast.show();
        }
    }
    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, int duration)
    {
        if (isShow){
            mToast=Toast.makeText(getApplicationContext(), message, duration);
            mToast.show();
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(int message, int duration)
    {
        if (isShow){
            mToast=Toast.makeText(getApplicationContext(), message, duration);
            mToast.show();
        }
    }
    /**
     * 取消Toast显示
     *
     */
    @SuppressLint("NewApi")
    public static void cancel()
    {
        if (isShow){
            if(null!=mToast)
                mToast.cancel();
        }
    }
}
