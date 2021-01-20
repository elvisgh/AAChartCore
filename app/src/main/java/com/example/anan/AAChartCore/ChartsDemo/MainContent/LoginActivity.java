package com.example.anan.AAChartCore.ChartsDemo.MainContent;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.anan.AAChartCore.ChartsDemo.MainContent.data.DBUtil;
import com.example.anan.AAChartCore.ChartsDemo.MainContent.data.Game;
import com.example.anan.AAChartCore.ChartsDemo.MainContent.fragment.H2HCoupleFragment;
import com.example.anan.AAChartCore.ChartsDemo.MainContent.fragment.H2HSingleFragment;
import com.example.anan.AAChartCore.ChartsDemo.MainContent.fragment.RateFragment;
import com.example.anan.AAChartCore.ChartsDemo.MainContent.fragment.TotalPointsFragment;
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

//        LogUtil.d("群组切换Fragment");

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

//        if (fragmentGroupTalk != null) {
//            fragmentGroupTalk.setVisible(false);
//        }
//
//        if (fragmentGroupMember != null) {
//            fragmentGroupMember.setVisible(false);
//        }
//
//        if (fragmentGroupTalkHistory != null) {
//            fragmentGroupTalkHistory.setVisible(false);
//        }

        fragment_now = to;

//        if (fragment_now == fragmentGroupTalk) {
//            fragmentGroupTalk.setVisible(true);
//        } else if (fragment_now == fragmentGroupMember) {
//            fragmentGroupMember.setVisible(true);
//        } else if (fragment_now == fragmentGroupTalkHistory) {
//            fragmentGroupTalkHistory.setVisible(true);
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

//        LogUtil.d("群组界面创建，设置底部导航栏 默认界面为对讲界面");

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

        readExcel(Environment.getExternalStorageDirectory() + File.separator + "game.xlsx");
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

        DBUtil.GameDBManager.getInstance().deleteAllGameRecord();
        DBUtil.GameDBManager.closeDBManager();
    }

    private void readExcel(String fileName) {
        try {
            InputStream inputStream = new FileInputStream(fileName);
            Workbook workbook;
            if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                return;
            }
            Sheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            Log.i("xxx", "rows" + rowsCount);
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for (int r = 0; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();//每行单元格数
                Log.i("xxx", "cells of row:" + cellsCount);
//                CellValue v0 = formulaEvaluator.evaluate(row.getCell(0));
//                CellValue v1 = formulaEvaluator.evaluate(row.getCell(1));
//                Log.i("Excel", "readExcel: " + v0.getStringValue() + "," + v1.getStringValue());
//                for (int c = 0; c < cellsCount; c++) {
//                    Log.i("Excel", "readExcel:" + row.getCell(c).getNumericCellValue());
//                }
                Game game = new Game();

                Log.i("xxx", "readExcel:" + row.getCell(0).getStringCellValue());
                game.setPlayer_1(row.getCell(0).getStringCellValue());
                Log.i("xxx", "readExcel:" + row.getCell(1).getStringCellValue());
                game.setPlayer_2(row.getCell(1).getStringCellValue());

                Log.i("xxx", "readExcel:" + row.getCell(2).getNumericCellValue());
                game.setScore_12((int)row.getCell(2).getNumericCellValue());
                Log.i("xxx", "readExcel:" + row.getCell(3).getNumericCellValue());
                game.setScore_34((int)row.getCell(3).getNumericCellValue());

                Log.i("xxx", "readExcel:" + row.getCell(4).getStringCellValue());
                game.setPlayer_3(row.getCell(4).getStringCellValue());
                Log.i("xxx", "readExcel:" + row.getCell(5).getStringCellValue());
                game.setPlayer_4(row.getCell(5).getStringCellValue());

                Log.i("xxx", "readExcel:" + row.getCell(6).getNumericCellValue());
                game.setGameDate((int)row.getCell(6).getNumericCellValue());

                DBUtil.GameDBManager.getInstance().addGameRecord(game, 100);

                Log.i("xxx", "readExcel:" + row.getCell(4).getStringCellValue());
                game.setPlayer_1(row.getCell(4).getStringCellValue());
                Log.i("xxx", "readExcel:" + row.getCell(5).getStringCellValue());
                game.setPlayer_2(row.getCell(5).getStringCellValue());

                Log.i("xxx", "readExcel:" + row.getCell(3).getNumericCellValue());
                game.setScore_12((int)row.getCell(3).getNumericCellValue());
                Log.i("xxx", "readExcel:" + row.getCell(2).getNumericCellValue());
                game.setScore_34((int)row.getCell(2).getNumericCellValue());

                Log.i("xxx", "readExcel:" + row.getCell(0).getStringCellValue());
                game.setPlayer_3(row.getCell(0).getStringCellValue());
                Log.i("xxx", "readExcel:" + row.getCell(1).getStringCellValue());
                game.setPlayer_4(row.getCell(1).getStringCellValue());

                Log.i("xxx", "readExcel:" + row.getCell(6).getNumericCellValue());
                game.setGameDate((int)row.getCell(6).getNumericCellValue());

                DBUtil.GameDBManager.getInstance().addGameRecord(game, 100);
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        LogUtil.d("群组界面恢复可见");
//
//        if (fragment_now != fragmentGroupTalk) {
//            switchFragment(fragment_now, fragmentGroupTalk);
//            navigation.setSelectedItemId(R.id.bottom_talk);
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("xxx", "onBackPressed");
    }
}
