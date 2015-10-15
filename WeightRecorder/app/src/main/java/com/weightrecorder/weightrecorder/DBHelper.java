package com.weightrecorder.weightrecorder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    //データベース
    private static final String DATABASE_NAME = "WeightRecorder";    //体重レコーダー
    private static final int DATABASE_VERSION = 1;                 //バージョン
    //テーブル1：基本設定
    public static final String TABLE1 = "BasicSetting";
    public static final String USER = "user";                      //検索用
    public static final String HEIGHT = "height";                  //身長
    public static final String TARGET_WEIGHT = "target_weight";    //目標体重
    public static final String ME = "me";
    //テーブル2：記録
    public static final String TABLE2 = "Record";
    public static final String DATE = "date";                      //入力日
    public static final String TIME = "time";                      //入力時間
    public static final String WEIGHT = "weight";                  //体重
    public static final String COMPARE_TARGET= "compare_target";   //目標±


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //テーブル1：基本設定を作成
        db.execSQL(
                "CREATE TABLE " + TABLE1 + " ("
                + USER + " STRING, "
                + HEIGHT + " DOUBLE, "
                + TARGET_WEIGHT + " DOUBLE);"
        );
        db.execSQL(
                "INSERT INTO " + TABLE1 + " (" + USER + ", " + HEIGHT + ", " + TARGET_WEIGHT + ") "
                + "VALUES ('" + ME + "', 0, 0);"
        );

        //テーブル2：記録を作成
        db.execSQL("CREATE TABLE " + TABLE2 + " ("
                + DATE + " STRING, "
                + TIME + " STRING, "
                + WEIGHT + " DOUBLE, "
                + COMPARE_TARGET + "DOUBLE);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // データベースの変更が生じた場合は、ここに処理を記述する。
    }

}
