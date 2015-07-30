package com.weightrecorder.weightrecorder;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;
import java.util.Calendar;


public class InputActivity extends ActionBarActivity  {

    private TextView dateAndTime;   //現在の日時
    private EditText weight;        //体重
    private Button buttonEnter;     //登録ボタン

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        dateAndTime = (TextView) findViewById(R.id.DateAndTime);
        weight = (EditText) findViewById(R.id.iWeight);
        buttonEnter = (Button) findViewById(R.id.eButton);

        dateAndTime.setText(getDateAndTime());

        //登録ボタン押下
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //体重の入力状態を判定
                String str = weight.getText().toString();
                int num = Integer.parseInt(str);
                if (str.equals("")) {
                    //ボタンは反応しない
                } else {
                    //入力内容を「履歴」に反映

                    //「最新」へ移動
                    Intent intent = new Intent(InputActivity.this, LatestActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    public static String getDateAndTime() {
        String date, time;

        //現在の日時を取得
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);                                  //西暦
        int month = cal.get(Calendar.MONTH) + 1;                            //月
        int day = cal.get(Calendar.DAY_OF_MONTH);                           //日
        String[] week = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};  //曜日
        int weekNum = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int hour = cal.get(Calendar.HOUR_OF_DAY);                           //時
        int minute = cal.get(Calendar.MINUTE);                              //分

        date = String.format("%1$04d.%2$02d.%3$02d", year, month, day) +  "(" + week[weekNum] + ")";
        time =  String.format("%1$02d:%2$02d", hour, minute);
        //time = hour + ":" + minute;

        return date + " " + time;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.setting) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
