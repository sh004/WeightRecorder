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

        //現在の日時を表示
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);        // 0=1月 … 11=12月
        int day = cal.get(Calendar.DAY_OF_MONTH);
        //int week = cal.get(Calendar.DAY_OF_WEEK);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        String date = year + "." + (month+1) + "." + day;   //+ "(" + week + ")";
        String time = hour + ":" + minute;
        dateAndTime.setText(date + " " + time);

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
