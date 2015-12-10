package com.weightrecorder.weightrecorder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.math.BigDecimal;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE1;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE1_ID;
import static com.weightrecorder.weightrecorder.DBHelper.TARGET_WEIGHT;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE2;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE2_ID;
import static com.weightrecorder.weightrecorder.DBHelper.BSID;
import static com.weightrecorder.weightrecorder.DBHelper.DATE;
import static com.weightrecorder.weightrecorder.DBHelper.TIME;
import static com.weightrecorder.weightrecorder.DBHelper.WEIGHT;


public class HistoryActivity extends ActionBarActivity {

    private ArrayList<HashMap<String, String>> data;
    private SimpleAdapter simpleadapter;
    private ListView listview;
    private DBHelper helper;
    private int total;
    private int ID;
    private String n_date, n_time, n_weight, n_target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        helper = new DBHelper(this);                        //DBセット
        data = new ArrayList<HashMap<String, String>>();    //データ追加用
        listview = (ListView) findViewById(R.id.listview);  //リストビュー
        simpleadapter = new SimpleAdapter(this, data, R.layout.activity_history_row,
                new String[]{"date", "time", "weight", "target"},
                new int[]{R.id.date, R.id.time, R.id.weight, R.id.target}
        );

        getTotal();

        for (int n = 0; n < total; n++) {
            HashMap<String, String> map = new HashMap<String, String>();

            road(n);

            //データを反映
            map.put("date", n_date);
            map.put("time", n_time);
            map.put("weight", n_weight);
            map.put("target", n_target);
            data.add(map);
        }

        listview.setAdapter(simpleadapter);     //リストビュー(history)にデータ(history_row)をセット
    }


    //配列のサイズ
    private void getTotal() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String sql =  "SELECT " + TABLE2 + "." + TABLE2_ID +
                     " FROM " + TABLE2 + " ORDER BY " + TABLE2_ID + " DESC LIMIT " + 1 + ";";

        Cursor cursor = db.rawQuery(sql, null);
        startManagingCursor(cursor);

        while (cursor.moveToNext()) {
            total = cursor.getInt(cursor.getColumnIndex(TABLE2_ID));
        }
        db.close();
    }

    private void road(int n) {
        BigDecimal bd;
        int BSid = 0;
        double weight = 0, target_weight = 0, a;

        SQLiteDatabase db = helper.getReadableDatabase();

        //TABLE2 n + 1 番目に新しいデータを取得
        String sql = "SELECT " + TABLE2 + "." + TABLE2_ID + ", " + TABLE2 + "." + BSID + ", " +
                                 TABLE2 + "." + DATE + ", " + TABLE2 + "." + TIME + ", " + TABLE2 + "." + WEIGHT +
                    " FROM " + TABLE2 + " ORDER BY " + TABLE2_ID + " DESC LIMIT " + (n + 1) + ";";

        Cursor cursor = db.rawQuery(sql, null);
        startManagingCursor(cursor);

        while (cursor.moveToNext()) {
            ID = cursor.getInt(cursor.getColumnIndex(TABLE2_ID));
            BSid = cursor.getInt(cursor.getColumnIndex(BSID));
            n_date = cursor.getString(cursor.getColumnIndex(DATE));
            n_time = cursor.getString(cursor.getColumnIndex(TIME));
            weight = cursor.getDouble(cursor.getColumnIndex(WEIGHT));
        }

        //TABLE1 目標比較用
        sql = "SELECT " + TABLE1 + "." + TARGET_WEIGHT +
             " FROM " + TABLE1 + " WHERE " + TABLE1_ID + " = " + BSid + ";";

        cursor = db.rawQuery(sql, null);
        startManagingCursor(cursor);

        while (cursor.moveToNext()) {
            target_weight = cursor.getDouble(cursor.getColumnIndex(TARGET_WEIGHT));
        }

        //体重,目標±
        a = weight - target_weight;
        n_weight = String.valueOf(weight);
        bd = new BigDecimal(a).setScale(1, BigDecimal.ROUND_HALF_UP);
        if (a > 0) {
            n_target = " +" + String.valueOf(bd);
        } else if (a < 0) {
            n_target = String.valueOf(bd);
        } else {
            n_target = String.valueOf("±00.0");
        }

        db.close();
    }

    //MainActivityへ戻る
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
