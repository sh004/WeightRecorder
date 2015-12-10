package com.weightrecorder.weightrecorder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper {

    //データベース
    private static final String DATABASE_NAME = "WeightRecorder";   //体重レコーダー
    private static final int DATABASE_VERSION = 1;                  //バージョン
    //テーブル1：基本設定
    public static final String TABLE1 = "BasicSetting";
    public static final String TABLE1_ID = "BasicSetting_id";       //通し番号
    public static final String HEIGHT = "height";                   //身長
    public static final String TARGET_WEIGHT = "target_weight";     //目標体重
    //テーブル2：記録
    public static final String TABLE2 = "Records";
    public static final String TABLE2_ID = "Records_id";            //通し番号
    public static final String BSID = "BSid";                       //基本設定の通し番号
    public static final String YEAR_DATE = "year_date";             //西暦,月日
    public static final String DATE = "date";                       //月日
    public static final String WEEK = "week";                       //曜日
    public static final String TIME = "time";                       //時間
    public static final String WEIGHT = "weight";                   //体重


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //テーブル1：基本設定を作成
        db.execSQL(
                "CREATE TABLE " + TABLE1 + " ("
                + TABLE1_ID + " INTEGER PRIMARY KEY, "
                + HEIGHT + " DOUBLE, "
                + TARGET_WEIGHT + " DOUBLE);"
        );

        //テーブル2：記録を作成
        db.execSQL("CREATE TABLE " + TABLE2 + " ("
                + TABLE2_ID + " INTEGER PRIMARY KEY, "
                + BSID + " INTEGER, "
                + YEAR_DATE + " STRING, "
                + DATE + " STRING, "
                + WEEK + " STRING, "
                + TIME + " STRING, "
                + WEIGHT + " DOUBLE);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //DBのアップグレード
    }

}
