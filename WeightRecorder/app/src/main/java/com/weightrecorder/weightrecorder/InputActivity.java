package com.weightrecorder.weightrecorder;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;
import java.util.Calendar;


public class InputActivity extends ActionBarActivity  {

    private TextView dateAndTime;   //現在日時
    private EditText weight;        //体重
    private Button buttonEnter;     //登録ボタン

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        dateAndTime = (TextView) findViewById(R.id.DateAndTime);
        weight = (EditText) findViewById(R.id.iWeight);
        buttonEnter = (Button) findViewById(R.id.eButton);

        //現在日時を反映
        dateAndTime.setText(getDateAndTime());

        //登録ボタン押下
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //入力内容を取得
            double num = Double.parseDouble(weight.getText().toString());

                //入力状態を判定
                if (num > 0) {
                    //入力内容を「履歴」に反映

                    //「最新」へ移動
                    Intent intent = new Intent(InputActivity.this, LatestActivity.class);
                    startActivity(intent);
                } else if (weight == null | weight.length() == 0) {
                    Toast.makeText(InputActivity.this, "値を入力してください。", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InputActivity.this, "正しく入力してください。", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //現在日時を取得
    public static String getDateAndTime() {
        String date, time;

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);                                  //西暦
        int month = cal.get(Calendar.MONTH) + 1;                            //月
        int day = cal.get(Calendar.DAY_OF_MONTH);                           //日
        String[] week = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};  //曜日
        int weekNum = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int hour = cal.get(Calendar.HOUR_OF_DAY);                           //時
        int minute = cal.get(Calendar.MINUTE);                              //分

        date = String.format("%1$04d.%2$02d.%3$02d", year, month, day) + "(" + week[weekNum] + ")";
        time =  String.format("%1$02d:%2$02d", hour, minute);

        return date + " " + time;
    }
}
