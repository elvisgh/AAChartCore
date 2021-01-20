package com.example.anan.AAChartCore.ChartsDemo.MainContent.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Administrator
 * @date 2020/5/14
 */
public class DBUtil {
    public static class GameDBHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "game.db";
        private static int DATABASE_VERSION = 3;

        public GameDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createGameRecordTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //数据库升级
            Log.d("xxx", "upgrade database ...");
            dropGameRecordTable(db);

            onCreate(db);
        }

        private void createGameRecordTable(SQLiteDatabase db) {
            String sql = "create table IF NOT EXISTS game_record(" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "player_1 VARCHAR," +
                    "player_2 VARCHAR," +
                    "player_3 VARCHAR," +
                    "player_4 VARCHAR," +
                    "score_12 smallint," +
                    "score_34 smallint," +
                    "game_date smallint)";

            Log.d("xxx", "create database ...");
            db.execSQL(sql);//创建数据库，存储PTT消息
        }

        private void dropGameRecordTable(SQLiteDatabase db) {
            db.execSQL("drop table if exists game_record");
        }
    }

    public static class GameDBManager {
        private static GameDBManager instance;
        private static GameDBHelper mChatDBHelper;
        private AtomicInteger mOpenCounter = new AtomicInteger();
        private SQLiteDatabase mDatabase;

        public static synchronized void initialize(Context context) {
            if (instance == null) {
                instance = new GameDBManager(context);
            }
        }

        public static synchronized GameDBManager getInstance() {
            if (instance == null) {
                throw new IllegalMonitorStateException(GameDBManager.class.getName() + " is not initialized, call initialize(..) method first.");
            }

            return instance;
        }

        private synchronized SQLiteDatabase openDatabase() {
            if (mOpenCounter.incrementAndGet() == 1) {
                // Opening new database
                try {
                    mDatabase = mChatDBHelper.getWritableDatabase();
                } catch (Exception e) {
                    mDatabase = mChatDBHelper.getReadableDatabase();
                }
            }

            return mDatabase;
        }


        private synchronized void closeDatabase() {
            if (mOpenCounter.decrementAndGet() == 0) {
                // Closing database
                mDatabase.close();
            }
        }

        public GameDBManager(Context context) {
            mChatDBHelper = new GameDBHelper(context);
        }

        public static void closeDBManager() {
            if (mChatDBHelper != null) {
                mChatDBHelper.close();
                mChatDBHelper = null;
            }

            if (instance != null) {
                instance.closeDatabase();
                instance = null;
            }
        }

        /**
         * 增加对局数据到数据库
         *
         * @param game
         * @return
         */
        public synchronized long addGameRecord(Game game, int limit) {
            SQLiteDatabase db = openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("player_1", game.getPlayer_1());
            cv.put("player_2", game.getPlayer_2());
            cv.put("player_3", game.getPlayer_3());
            cv.put("player_4", game.getPlayer_4());

            cv.put("score_12", game.getScore_12());
            cv.put("score_34", game.getScore_34());

            cv.put("game_date", game.getGameDate());
            long result = db.insert("game_record", null, cv);
            Log.d("xxx", "game record addRecord result = " + result);

            closeDatabase();
            return result;
        }

        public void deleteAllGameRecord() {
            Log.d("xxx", "game record truncate");
            SQLiteDatabase db = openDatabase();
            db.execSQL("Delete from game_record");
            closeDatabase();
        }

        /**
         * 查询历史对局数据
         *
         * @param player 参赛队员
         * @return
         */
        public List<Game> getGameRecordByPlayer(String player) {
            SQLiteDatabase db = openDatabase();
            ArrayList<Game> games = new ArrayList<>();
            Cursor cursor = db.query("game_record", null, "player_1=? or player_2=?", new String[]{player, player}, null, null, "game_date");
            while (cursor.moveToNext()) {
                Game game = new Game();
                game.setPlayer_1(cursor.getString(cursor.getColumnIndex("player_1")));
                game.setPlayer_2(cursor.getString(cursor.getColumnIndex("player_2")));
                game.setPlayer_3(cursor.getString(cursor.getColumnIndex("player_3")));
                game.setPlayer_4(cursor.getString(cursor.getColumnIndex("player_4")));

                game.setScore_12(cursor.getInt(cursor.getColumnIndex("score_12")));
                game.setScore_34(cursor.getInt(cursor.getColumnIndex("score_34")));

                game.setGameDate(cursor.getInt(cursor.getColumnIndex("game_date")));

                games.add(game);
            }
            cursor.close();
            closeDatabase();
            return games;
        }
    }
}
