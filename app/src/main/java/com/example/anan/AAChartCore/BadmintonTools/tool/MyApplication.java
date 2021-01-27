package com.example.anan.AAChartCore.BadmintonTools.tool;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.anan.AAChartCore.BadmintonTools.data.DBUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("xxx", "MyApplication onCreate");

        context = getApplicationContext();

        SharedPreferenceUtil.init(context);

        DBUtil.GameDBManager.initialize(context);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context getContext() {
        return context;
    }
}
