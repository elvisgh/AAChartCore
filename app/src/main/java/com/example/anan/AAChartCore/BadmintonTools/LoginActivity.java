package com.example.anan.AAChartCore.BadmintonTools;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.anan.AAChartCore.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initPermissions();

        //默认关闭软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startStatisticActivity() {
        Intent to = new Intent(this, StatisticActivity.class);
        startActivity(to);

        finish();
    }

    //权限数组
    private String[] permissions = new String[]{
            //Storage
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    //返回code
    private static final int OPEN_SET_REQUEST_CODE = 100;

    //调用此方法判断是否拥有权限
    private void initPermissions() {
        if (lacksPermission(permissions)) {//判断是否拥有权限
            //请求权限，第二参数权限String数据，第三个参数是请求码便于在onRequestPermissionsResult 方法中根据code进行判断
            ActivityCompat.requestPermissions(this, permissions, OPEN_SET_REQUEST_CODE);
        } else {
            //拥有权限执行操作
            startStatisticActivity();
        }
    }

    //如果返回true表示缺少权限
    public boolean lacksPermission(String[] permissions) {
        for (String permission : permissions) {
            //判断是否缺少权限，true=缺少权限
            if(ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){//响应Code
            case OPEN_SET_REQUEST_CODE:
//                if (grantResults.length > 0) {
//                    for(int i = 0; i < grantResults.length; i++){
//                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
//                            Toast.makeText(getApplicationContext(),"未拥有相应权限",Toast.LENGTH_LONG).show();
//                            return;
//                        }
//                    }
//                    //拥有权限执行操作
//                } else {
//                    Toast.makeText(getApplicationContext(),"未拥有相应权限",Toast.LENGTH_LONG).show();
//                }
                if (!lacksPermission(permissions)) {
                    startStatisticActivity();
                } else {
                    Toast.makeText(getApplicationContext(),"未拥有相应权限",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }
}
