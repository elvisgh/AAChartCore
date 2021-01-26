package com.example.anan.AAChartCore.BadmintonTools.tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("StaticFieldLeak")
public class SharedPreferenceUtil {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor sharedPreferencesEditor;

    @SuppressLint("CommitPrefEdits")
    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
    }

    @Deprecated
    public static boolean setList(String key, List<String> value) {
        sharedPreferencesEditor.putInt(key + "_size", value.size());
        for (int i = 0; i < value.size(); i++) {
            sharedPreferencesEditor.remove(key + "_data_" + i);
            sharedPreferencesEditor.putString(key + "_data_" + i, value.get(i));
        }
        return sharedPreferencesEditor.commit();
    }

    @Deprecated
    public static List<String> getList(String key) {
        List<String> list = new ArrayList<>();

        int list_size = sharedPreferences.getInt(key + "_size", 0);
        for (int i = 0; i < list_size; i++) {
            list.add(sharedPreferences.getString(key + "_data_" + i, null));
        }
        return list;
    }


    /**
     * 清除所有的sharedPreferences数据
     * */
    public static void clear(){
        sharedPreferencesEditor.clear();
    }
}
