package com.example.tianqijizhang.tianqijizhang.ui.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxinxi on 2017/6/28.
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();//通过list来暂存活动

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for (Activity activity:activities){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }
}
