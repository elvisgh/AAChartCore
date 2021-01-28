package com.example.anan.AAChartCore.BadmintonTools.tool;

import android.content.Context;
import android.util.Log;

import com.example.anan.AAChartCore.BadmintonTools.data.DBUtil;
import com.example.anan.AAChartCore.BadmintonTools.data.Game;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ReadExcelUtil {
    private static Context context;
    private static ReadExcelUtil util = null;

    private ReadExcelUtil(Context context) {
        this.context = context;
    }

    public static ReadExcelUtil getInstance(Context context) {
        if (util == null) {
            util = new ReadExcelUtil(context);
        }
        return util;
    }


    public void readExcel(String fileName) {
        try {
            InputStream inputStream = new FileInputStream(fileName);
            Workbook workbook;
            if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(inputStream);
            } else if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                Log.i("xxx", "文件格式不对，非.xls或.xlsx");
                return;
            }
            Sheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            Log.i("xxx", "rows of sheet: " + rowsCount);
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();

            if (rowsCount > 0) {
                DBUtil.GameDBManager.getInstance().deleteAllGameRecord();
            }

            List<String> players = new ArrayList<>();

            for (int r = 0; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();//每行单元格数
                Log.i("xxx", "cells of row: " + cellsCount);

                Game game = new Game();

                game.setPlayer_1(row.getCell(0).getStringCellValue());
                game.setPlayer_2(row.getCell(1).getStringCellValue());

                game.setScore_12((int)row.getCell(2).getNumericCellValue());
                game.setScore_34((int)row.getCell(3).getNumericCellValue());

                game.setPlayer_3(row.getCell(4).getStringCellValue());
                game.setPlayer_4(row.getCell(5).getStringCellValue());

                game.setGameDate((int)row.getCell(6).getNumericCellValue());

                DBUtil.GameDBManager.getInstance().addGameRecord(game, 100);
                Log.i("xxx", "content of row: " + game.toString());

                //在这里录入运动员数据 TODO
                if (!players.contains(game.getPlayer_1())) players.add(game.getPlayer_1());
                if (!players.contains(game.getPlayer_2())) players.add(game.getPlayer_2());
                if (!players.contains(game.getPlayer_3())) players.add(game.getPlayer_3());
                if (!players.contains(game.getPlayer_4())) players.add(game.getPlayer_4());
            }

            SharedPreferenceUtil.setList("players", players);

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
