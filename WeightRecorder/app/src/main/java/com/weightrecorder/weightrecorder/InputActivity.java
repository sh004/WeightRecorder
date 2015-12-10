package com.weightrecorder.weightrecorder;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.database.Cursor;
import android.view.KeyEvent;
import java.util.Calendar;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE2;
import static com.weightrecorder.weightrecorder.DBHelper.BSID;
import static com.weightrecorder.weightrecorder.DBHelper.YEAR_DATE;
import static com.weightrecorder.weightrecorder.DBHelper.DATE;
import static com.weightrecorder.weightrecorder.DBHelper.WEEK;
import static com.weightrecorder.weightrecorder.DBHelper.TIME;
import static com.weightrecorder.weightrecorder.DBHelper.WEIGHT;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE1;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE1_ID;


public class InputActivity extends ActionBarActivity  {

    private DBHelper helper;
    private TextView dateAndTime;
    private EditText presentWeight;
    private Button buttonEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        helper = new DBHelper(this);                                    //DBセット
        dateAndTime = (TextView) findViewById(R.id.DateAndTime);        //現在日時
        presentWeight = (EditText) findViewById(R.id.iWeight);          //現在体重
        buttonEnter = (Button) findViewById(R.id.eButton);              //登録ボタン

        //現在日時を反映
        dateAndTime.setText(getDateAndTime());

        //登録ボタン押下処理
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            int num = 0;    //BSid用
            String str;
            double weight;

            //入力状態判定
            @Override
            public void onClick(View v) {
                str = presentWeight.getText().toString();

                if (str.equals("")) {
                    Toast.makeText(InputActivity.this, "値を入力してください。", Toast.LENGTH_SHORT).show();
                } else {
                    weight = Double.parseDouble(str);

                    if (weight > 0) {
                        addData(weight);    //DBに書き込み
                    } else {
                        Toast.makeText(InputActivity.this, "正しく入力してください。", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            //DBに書き込み
            private void addData(double weight) {
                getBSid();

                if (num == 0) {
                    Toast.makeText(InputActivity.this, "設定を行ってください。", Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();

                    values.put(BSID, num);
                    values.put(YEAR_DATE, getYearDate());
                    values.put(DATE, getDate());
                    values.put(WEEK, getWeek());
                    values.put(TIME, getTime());
                    values.put(WEIGHT, weight);

                    db.insert(TABLE2, null, values);
                    db.close();

                    Toast.makeText(InputActivity.this, "登録しました。", Toast.LENGTH_SHORT).show();

                    //「最新」へ移動
                    Intent intent = new Intent(InputActivity.this, LatestActivity.class);
                    startActivity(intent);
                }
            }

            //BSIDを取得
            private void getBSid() {
                SQLiteDatabase db = helper.getReadableDatabase();

                String sql = "SELECT " + TABLE1_ID +
                            " FROM " + TABLE1 +
                            " ORDER BY " + TABLE1_ID + " DESC LIMIT 1;";

                Cursor cursor = db.rawQuery(sql, null);
                startManagingCursor(cursor);

                while (cursor.moveToNext()) {
                    num = cursor.getInt(cursor.getColumnIndex(TABLE1_ID));
                }
                db.close();
            }
        });
    }

    //現在日時を取得
    private String getDateAndTime() {
       String date, time;

       date = getYearDate() + getWeek();    //日付
       time = getTime();                //時間

       return date + " " + time;
    }

    //西暦, 月日
    private String getYearDate() {
        Calendar cal = Calendar.getInstance();
        int num, month, day;
        String year_date;

        num = cal.get(Calendar.YEAR);                           //西暦
        month = cal.get(Calendar.MONTH) + 1;                    //月
        day = cal.get(Calendar.DAY_OF_MONTH);                   //日
        year_date = String.format("%1$04d.%2$02d.%3$02d", num, month, day);

        return year_date;
    }

    //月日
    private String getDate() {
        Calendar cal = Calendar.getInstance();
        int month, day;
        String date;

        month = cal.get(Calendar.MONTH) + 1;                    //月
        day = cal.get(Calendar.DAY_OF_MONTH);                   //日

        date = String.format("%1$02d.%2$02d", month, day);      //月日

        return date;
    }

    //曜日
    private String getWeek() {
        Calendar cal = Calendar.getInstance();
        int weekNum = cal.get(Calendar.DAY_OF_WEEK) - 1;
        String[] weekName = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

        String week = String.format("(" + weekName[weekNum] + ")");

        return week;
    }

    //時間
    private String getTime() {
        Calendar cal = Calendar.getInstance();
        int hour, minute;
        String time;

        hour = cal.get(Calendar.HOUR_OF_DAY);                       //時
        minute = cal.get(Calendar.MINUTE);                          //分

        time =  String.format("%1$02d:%2$02d", hour, minute);       //時間

        return time;
    }

    //MainActivityへ戻る
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(InputActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
