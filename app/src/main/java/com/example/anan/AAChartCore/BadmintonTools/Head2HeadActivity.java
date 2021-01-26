//package com.example.anan.AAChartCore.BadmintonTools;
//
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.TextView;
//
//import com.example.anan.AAChartCore.BadmintonTools.data.DBUtil;
//import com.example.anan.AAChartCore.BadmintonTools.data.Game;
//import com.example.anan.AAChartCore.R;
//
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.FormulaEvaluator;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Head2HeadActivity extends AppCompatActivity {
//
//    private static final String[] COUNTRIES = new String[]{
//            "A","B","C","D"
//    };
//
//    TextView score_12, score_34;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_head2head);
//
//        DBUtil.GameDBManager.initialize(getApplicationContext());
//
//        final AutoCompleteTextView textView1 = (AutoCompleteTextView) findViewById(R.id.name_1);
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,COUNTRIES);
//        textView1.setAdapter(adapter1);
//
//        final AutoCompleteTextView textView2 = (AutoCompleteTextView) findViewById(R.id.name_2);
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,COUNTRIES);
//        textView2.setAdapter(adapter2);
//
//        final AutoCompleteTextView textView3 = (AutoCompleteTextView) findViewById(R.id.name_3);
//        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,COUNTRIES);
//        textView3.setAdapter(adapter3);
//
//        final AutoCompleteTextView textView4 = (AutoCompleteTextView) findViewById(R.id.name_4);
//        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,COUNTRIES);
//        textView4.setAdapter(adapter4);
//
//        score_12 = (TextView)findViewById(R.id.score_12);
//        score_34 = (TextView)findViewById(R.id.score_34);
//
//        Button button = (Button)findViewById(R.id.run);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                List<Game> games = new ArrayList<>();
//                games = DBUtil.GameDBManager.getInstance().getGameRecordByPlayer(textView1.getText().toString());
//
//                int aWinCount = 0;
//                int dWinCount = 0;
//
//                Log.i("xxx", textView1.getText().toString() + textView2.getText().toString() + textView3.getText().toString() + textView4.getText().toString());
//                for (Game game : games) {
//                    Log.i("xxx", game.toString());
//                    if ((game.getPlayer_1().equals(textView1.getText().toString()) || game.getPlayer_1().equals(textView1.getText().toString()))
//                        && (game.getPlayer_2().equals(textView2.getText().toString()) || game.getPlayer_2().equals(textView2.getText().toString()))
//                        && (game.getPlayer_3().equals(textView3.getText().toString()) || game.getPlayer_3().equals(textView3.getText().toString()))
//                        && (game.getPlayer_4().equals(textView4.getText().toString()) || game.getPlayer_4().equals(textView4.getText().toString()))) {
//                        Log.i("xxx", game.toString());
//                        if (game.getScore_12() > game.getScore_34()) {
//                            aWinCount++;
//                        } else {
//                            dWinCount++;
//                        }
//                    }
//                }
//
//                Log.i("xxx", "胜率: AB " + aWinCount + " : " + dWinCount + " CD");
//                score_12.setText(aWinCount+"");
//                score_34.setText(dWinCount+"");
//            }
//        });
//
//        readExcel(Environment.getExternalStorageDirectory() + File.separator + "game.xlsx");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        DBUtil.GameDBManager.getInstance().deleteAllGameRecord();
//        DBUtil.GameDBManager.closeDBManager();
//    }
//
//    private void readExcel(String fileName) {
//        try {
//            InputStream inputStream = new FileInputStream(fileName);
//            Workbook workbook;
//            if (fileName.endsWith(".xls")) {
//                workbook = new HSSFWorkbook(inputStream);
//            } else if (fileName.endsWith(".xlsx")) {
//                workbook = new XSSFWorkbook(inputStream);
//            } else {
//                return;
//            }
//            Sheet sheet = workbook.getSheetAt(0);
//            int rowsCount = sheet.getPhysicalNumberOfRows();
//            Log.i("xxx", "rows" + rowsCount);
//            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
//            for (int r = 0; r < rowsCount; r++) {
//                Row row = sheet.getRow(r);
//                int cellsCount = row.getPhysicalNumberOfCells();//每行单元格数
//                Log.i("xxx", "cells of row:" + cellsCount);
////                CellValue v0 = formulaEvaluator.evaluate(row.getCell(0));
////                CellValue v1 = formulaEvaluator.evaluate(row.getCell(1));
////                Log.i("Excel", "readExcel: " + v0.getStringValue() + "," + v1.getStringValue());
////                for (int c = 0; c < cellsCount; c++) {
////                    Log.i("Excel", "readExcel:" + row.getCell(c).getNumericCellValue());
////                }
//                Game game = new Game();
//
//                Log.i("xxx", "readExcel:" + row.getCell(0).getStringCellValue());
//                game.setPlayer_1(row.getCell(0).getStringCellValue());
//                Log.i("xxx", "readExcel:" + row.getCell(1).getStringCellValue());
//                game.setPlayer_2(row.getCell(1).getStringCellValue());
//
//                Log.i("xxx", "readExcel:" + row.getCell(2).getNumericCellValue());
//                game.setScore_12((int)row.getCell(2).getNumericCellValue());
//                Log.i("xxx", "readExcel:" + row.getCell(3).getNumericCellValue());
//                game.setScore_34((int)row.getCell(3).getNumericCellValue());
//
//                Log.i("xxx", "readExcel:" + row.getCell(4).getStringCellValue());
//                game.setPlayer_3(row.getCell(4).getStringCellValue());
//                Log.i("xxx", "readExcel:" + row.getCell(5).getStringCellValue());
//                game.setPlayer_4(row.getCell(5).getStringCellValue());
//
//                Log.i("xxx", "readExcel:" + row.getCell(6).getNumericCellValue());
//                game.setGameDate((int)row.getCell(6).getNumericCellValue());
//
//                DBUtil.GameDBManager.getInstance().addGameRecord(game, 100);
//
//                Log.i("xxx", "readExcel:" + row.getCell(4).getStringCellValue());
//                game.setPlayer_1(row.getCell(4).getStringCellValue());
//                Log.i("xxx", "readExcel:" + row.getCell(5).getStringCellValue());
//                game.setPlayer_2(row.getCell(5).getStringCellValue());
//
//                Log.i("xxx", "readExcel:" + row.getCell(3).getNumericCellValue());
//                game.setScore_12((int)row.getCell(3).getNumericCellValue());
//                Log.i("xxx", "readExcel:" + row.getCell(2).getNumericCellValue());
//                game.setScore_34((int)row.getCell(2).getNumericCellValue());
//
//                Log.i("xxx", "readExcel:" + row.getCell(0).getStringCellValue());
//                game.setPlayer_3(row.getCell(0).getStringCellValue());
//                Log.i("xxx", "readExcel:" + row.getCell(1).getStringCellValue());
//                game.setPlayer_4(row.getCell(1).getStringCellValue());
//
//                Log.i("xxx", "readExcel:" + row.getCell(6).getNumericCellValue());
//                game.setGameDate((int)row.getCell(6).getNumericCellValue());
//
//                DBUtil.GameDBManager.getInstance().addGameRecord(game, 100);
//            }
//            workbook.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
