package com.example.tianqijizhang.tianqijizhang.application;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.blankj.utilcode.util.Utils;
import com.example.tianqijizhang.tianqijizhang.utils.PreferencesUtils;
/**
 * Created by wuxingxi on 2018/07/05.
 */
public class MyApplication  extends Application {

    // 屏幕宽度
    public static int screenWith = 0;
    public static int screenHeight = 0;

    private static  MyApplication  mInstance;
    public static  MyApplication  getInstance(){
        return  mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWith = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        Utils.init(this);//工具类的初始化
        mInstance=this;
    }



    /*获取当前用户*/
    public String getUser(Context context){
        String username= PreferencesUtils.getString(context,"username");
        return  username;
    }
    /*清除当前用户*/
    public boolean removeUser(Context context){
        return PreferencesUtils.clearUser(context,"username");
    }
}
