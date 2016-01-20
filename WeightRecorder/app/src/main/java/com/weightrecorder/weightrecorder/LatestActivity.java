package com.weightrecorder.weightrecorder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import java.math.BigDecimal;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE1;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE1_ID;
import static com.weightrecorder.weightrecorder.DBHelper.HEIGHT;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE2;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE2_ID;
import static com.weightrecorder.weightrecorder.DBHelper.BSID;
import static com.weightrecorder.weightrecorder.DBHelper.TARGET_WEIGHT;
import static com.weightrecorder.weightrecorder.DBHelper.YEAR_DATE;
import static com.weightrecorder.weightrecorder.DBHelper.WEEK;
import static com.weightrecorder.weightrecorder.DBHelper.TIME;
import static com.weightrecorder.weightrecorder.DBHelper.WEIGHT;


public class LatestActivity extends ActionBarActivity {

    private DBHelper helper;
    private TextView dateAndTime, weight, compare_target, previous_weight, BMI, DOO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest);

        helper = new DBHelper(this);                                    //DBセット
        dateAndTime = (TextView) findViewById(R.id.DateAndTime);        //最新データ日時
        weight = (TextView) findViewById(R.id.lWeight);                 //最新体重
        compare_target = (TextView) findViewById(R.id.cTarget);         //目標体重±
        previous_weight = (TextView) findViewById(R.id.pWeight);        //目標体重±
        BMI = (TextView) findViewById(R.id.BMInum);                     //BMI
        DOO = (TextView) findViewById(R.id.DOOjudge);                   //肥満度

        latestDateAndTime();    //最新日時
        latestCompareWeight();  //最新体重,目標±,前回比
        latestBMIandDOO();      //BMI,肥満度
    }

    //最新日時
    private void latestDateAndTime() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "SELECT " + TABLE2 + "." + YEAR_DATE + ", " + TABLE2 + "." + WEEK + ", " + TABLE2 + "." + TIME +
                " FROM " + TABLE2 + " ORDER BY " + TABLE2_ID + " DESC LIMIT 1;";
        String year, week, time;

        Cursor cursor = db.rawQuery(sql, null);
        startManagingCursor(cursor);

        while (cursor.moveToNext()) {
            year = cursor.getString(cursor.getColumnIndex(YEAR_DATE));
            week = cursor.getString(cursor.getColumnIndex(WEEK));
            time = cursor.getString(cursor.getColumnIndex(TIME));

            if (year.equals("") || week.equals("") || time.equals("")) {
                dateAndTime.setText("0000.00.00(SUN) 00:00");
            } else {
                dateAndTime.setText(year + week + " " + time);   //最新データ日時
            }
        }
        db.close();
    }

    //目標±,前回比
    private void latestCompareWeight() {
        int BSid = 0;
        double weight1 = 0, target_weight = 0, weight2 = 0, a;
        BigDecimal bd;

        SQLiteDatabase db = helper.getReadableDatabase();

        //TABLE2 最新データ
        String sql =  "SELECT " + TABLE2 + "." + BSID + ", " + TABLE2 + "." + WEIGHT +
                " FROM " + TABLE2 + " ORDER BY " + TABLE2_ID + " DESC LIMIT 1;";

        Cursor cursor = db.rawQuery(sql, null);
        startManagingCursor(cursor);

        while (cursor.moveToNext()) {
            BSid = cursor.getInt(cursor.getColumnIndex(BSID));
            weight1 = cursor.getDouble(cursor.getColumnIndex(WEIGHT));
        }

        //TABLE1 最新データ比較用
        sql =  "SELECT " + TABLE1 + "." + TARGET_WEIGHT +
                " FROM " + TABLE1 + " WHERE " + TABLE1_ID + " = " + BSid + ";";

        cursor = db.rawQuery(sql, null);
        startManagingCursor(cursor);

        while (cursor.moveToNext()) {
            target_weight = cursor.getDouble(cursor.getColumnIndex(TARGET_WEIGHT));
        }

        //TABLE2 2番目に新しいデータ
        sql =  "SELECT " + TABLE2 + "." + WEIGHT +
                " FROM " + TABLE2 + " ORDER BY " + TABLE2_ID + " DESC LIMIT 2;";

        cursor = db.rawQuery(sql, null);
        startManagingCursor(cursor);

        while (cursor.moveToNext()) {
            weight2 = cursor.getDouble(cursor.getColumnIndex(WEIGHT));
        }

        db.close();

        //最新体重
        weight.setText(String.valueOf(weight1));

        //目標±
        a = weight1 - target_weight;
        bd = new BigDecimal(a).setScale(1, BigDecimal.ROUND_HALF_UP);
        if (a > 0) {
            compare_target.setText(" +" + String.valueOf(bd));
        } else if (a < 0) {
            compare_target.setText(String.valueOf(bd));
        } else {
            compare_target.setText("±00.0");
        }

        //前回比
        a = weight1 - weight2;
        bd = new BigDecimal(a).setScale(1, BigDecimal.ROUND_HALF_UP);
        if (a > 0) {
            previous_weight.setText("+" + String.valueOf(bd));
        } else if (a < 0) {
            previous_weight.setText(String.valueOf(bd));
        } else {
            previous_weight.setText("±00.0");
        }
    }

    //BMI,肥満度計算
    private void latestBMIandDOO() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "SELECT " + TABLE1 + "." + HEIGHT + ", " + TABLE2 + "." + WEIGHT +
                " FROM " + TABLE1 + ", " + TABLE2 +
                " ORDER BY " + TABLE2 + "." + TABLE2_ID + " DESC LIMIT 1;";
        String str;
        Double height, weight, bmi;
        BigDecimal bd;

        Cursor cursor = db.rawQuery(sql, null);
        startManagingCursor(cursor);

        while (cursor.moveToNext()) {
            height = cursor.getDouble(cursor.getColumnIndex(HEIGHT));
            weight = cursor.getDouble(cursor.getColumnIndex(WEIGHT));

            str = String.valueOf(height);

            if (str.equals("")) {
                BMI.setText("00.0");
                DOO.setText("");
            } else {
                if(height > 0 && weight > 0) {
                    height = height / 100;
                    bmi = weight / (height * height);
                    bd = new BigDecimal(bmi).setScale(1, BigDecimal.ROUND_DOWN);
                    bmi = bd.doubleValue();

                    if(bmi < 18.5) {
                        str = "低体重";
                    } else if(bmi < 25 && bmi > 18.4) {
                        str = "普通体重";
                    } else if(bmi < 30 && bmi > 24) {
                        str = "肥満(1度)";
                    } else if(bmi < 35 && bmi > 29) {
                        str = "肥満(2度)";
                    } else if(bmi < 40 && bmi > 34) {
                        str = "肥満(3度)";
                    } else if(bmi > 39) {
                        str = "肥満(4度)";
                    } else {
                        str = "";
                    }
                    BMI.setText(String.valueOf(bmi));
                    DOO.setText(str);
                } else {
                    BMI.setText("00.0");
                    DOO.setText("");
                }
            }
        }
        db.close();
    }

    //MainActivityへ戻る
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(LatestActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
