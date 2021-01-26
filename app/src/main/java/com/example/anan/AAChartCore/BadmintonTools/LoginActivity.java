package com.example.anan.AAChartCore.BadmintonTools;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.anan.AAChartCore.BadmintonTools.data.DBUtil;
import com.example.anan.AAChartCore.BadmintonTools.data.Game;
import com.example.anan.AAChartCore.BadmintonTools.fragment.H2HCoupleFragment;
import com.example.anan.AAChartCore.BadmintonTools.fragment.H2HSingleFragment;
import com.example.anan.AAChartCore.BadmintonTools.fragment.RateFragment;
import com.example.anan.AAChartCore.BadmintonTools.fragment.TotalPointsFragment;
import com.example.anan.AAChartCore.BadmintonTools.tool.ReadExcelUtil;
import com.example.anan.AAChartCore.R;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private Fragment fragment_now = null;
    private H2HSingleFragment h2HSingleFragment;
    private H2HCoupleFragment h2HCoupleFragment;
    private TotalPointsFragment totalPointsFragment;
    private RateFragment rateFragment;

    private void changePageFragment(int id) {
        switch (id) {
            case R.id.bottom_single:
                if (h2HSingleFragment == null) {
                    h2HSingleFragment = new H2HSingleFragment();
                }
                switchFragment(fragment_now, h2HSingleFragment);
                break;
            case R.id.bottom_couple:
                if (h2HCoupleFragment == null) {
                    h2HCoupleFragment = new H2HCoupleFragment();
                }
                switchFragment(fragment_now, h2HCoupleFragment);
                break;
            case R.id.bottom_points:
                if (totalPointsFragment == null) {
                    totalPointsFragment = new TotalPointsFragment();
                }
                switchFragment(fragment_now, totalPointsFragment);
                break;
            case R.id.bottom_rate:
                if (rateFragment == null) {
                    rateFragment = new RateFragment();
                }
                switchFragment(fragment_now, rateFragment);
                break;
        }
    }

    public void switchFragment(Fragment from, Fragment to) {
        if (to == null) {
            return; // TODO Exception
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!to.isAdded()) {
            if (from == null) {
                transaction.add(R.id.activity_group_frame_layout, to).show(to).commit();
            } else {
                transaction.add(R.id.activity_group_frame_layout, to).hide(from).show(to).commit();
            }
        } else {
            transaction.hide(from).show(to).commit();
        }

        fragment_now = to;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        DBUtil.GameDBManager.initialize(getApplicationContext());

        //设置底部导航栏
        navigation = findViewById(R.id.group_bottom_navigation);
        BottomNavigationView.OnNavigationItemSelectedListener onGroupBottomNavigationMenuSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                LoginActivity.this.changePageFragment(menuItem.getItemId());
                return true;
            }
        };
        navigation.setOnNavigationItemSelectedListener(onGroupBottomNavigationMenuSelectedListener);
        //设置默认项目
        navigation.setSelectedItemId(R.id.bottom_single);
    }

    /*Forbid physic back keycode*/
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK ) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DBUtil.GameDBManager.closeDBManager();
    }
}
