package com.weightrecorder.weightrecorder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import static com.weightrecorder.weightrecorder.DBHelper.BSID;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE1;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE1_ID;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE2;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE2_ID;
import static com.weightrecorder.weightrecorder.DBHelper.TARGET_WEIGHT;
import static com.weightrecorder.weightrecorder.DBHelper.YEAR_DATE;
import static com.weightrecorder.weightrecorder.DBHelper.WEEK;
import static com.weightrecorder.weightrecorder.DBHelper.TIME;
import static com.weightrecorder.weightrecorder.DBHelper.WEIGHT;


public class MainActivity extends ActionBarActivity {

    private DBHelper helper;
    private TextView dateAndTime, weight, compare_target;
    private boolean FinishFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new DBHelper(this);                                    //DBセット
        dateAndTime = (TextView) findViewById(R.id.DateAndTime);        //最新データ日時
        weight = (TextView) findViewById(R.id.lWeight);                 //最新体重
        compare_target = (TextView) findViewById(R.id.cTarget);         //目標体重±

        road();     //最新データを読み込む
    }

    //最新データを読み込む
    public void road() {
        int BSid = 0;
        String year_date, week, time;
        double latest = 0, target_weight = 0, a;
        BigDecimal bd;

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "SELECT " + TABLE2 + "." + BSID + ", " + TABLE2 + "." + YEAR_DATE + ", " + TABLE2 + "." + WEEK + ", " + TABLE2 + "." + TIME + ", " + TABLE2 + "." + WEIGHT +
                    " FROM " + TABLE2 + " ORDER BY " + TABLE2_ID + " DESC LIMIT 1;";

        Cursor cursor = db.rawQuery(sql, null);
        startManagingCursor(cursor);

        //TABLE2 日付, 最新体重
        while (cursor.moveToNext()) {
            BSid = cursor.getInt(cursor.getColumnIndex(BSID));
            year_date = cursor.getString(cursor.getColumnIndex(YEAR_DATE));
            week = cursor.getString(cursor.getColumnIndex(WEEK));
            time = cursor.getString(cursor.getColumnIndex(TIME));
            latest = cursor.getDouble(cursor.getColumnIndex(WEIGHT));

            if (year_date.equals("") || week.equals("") || time.equals("")) {
                dateAndTime.setText("0000.00.00(SUN) 00:00");
                weight.setText("00.0");
            } else {
                dateAndTime.setText(year_date + week + " " + time);     //最新データ日時
                weight.setText(String.valueOf(latest));                 //最新体重
            }
        }

        //TABLE1 最新データ比較用
        sql =  "SELECT " + TABLE1 + "." + TARGET_WEIGHT +
                " FROM " + TABLE1 + " WHERE " + TABLE1_ID + " = " + BSid + ";";

        cursor = db.rawQuery(sql, null);
        startManagingCursor(cursor);

        while (cursor.moveToNext()) {
            target_weight = cursor.getDouble(cursor.getColumnIndex(TARGET_WEIGHT));
        }
        db.close();

        //目標±
        a = latest - target_weight;
        bd = new BigDecimal(a).setScale(1, BigDecimal.ROUND_HALF_UP);
        if (a > 0) {
            compare_target.setText(" +" + String.valueOf(bd));
        } else if (a < 0) {
            compare_target.setText(String.valueOf(bd));
        } else {
            compare_target.setText("±00.0");
        }
    }


    //ページ移動
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.Latest:    //最新へ
                intent = new Intent(this, LatestActivity.class);
                startActivity(intent);
                break;
            case R.id.Input:    //入力へ
                intent = new Intent(this, InputActivity.class);
                startActivity(intent);
                break;
            case R.id.History:  //履歴へ
                intent = new Intent(this,HistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.Graph:     //グラフへ
                intent = new Intent(this, GraphActivity.class);
                startActivity(intent);
                break;
            case R.id.Setting:   //設定へ
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    //終了
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (FinishFlag) {
                finish();
            } else {
                Toast.makeText(this, "もう一度押すと終了する", Toast.LENGTH_SHORT).show();
                FinishFlag = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FinishFlag = false;
                    }
                }, 2000);
            }
        }
        return false;
    }

}
