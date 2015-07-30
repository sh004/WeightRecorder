package com.weightrecorder.weightrecorder;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;


public class SettingActivity extends ActionBarActivity {

    private Button buttonEnter, buttonReset;
    private EditText height, targetWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        buttonEnter = (Button) findViewById(R.id.eButton);
        buttonReset = (Button) findViewById(R.id.rButton);
        height = (EditText) findViewById(R.id.heightNum);
        targetWeight = (EditText) findViewById(R.id.weightNum);

        //登録ボタン押下
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //身長・目標体重の入力状態を判定
                String str1, str2;
                int num1, num2;
                str1 = height.getText().toString();
                str2 = targetWeight.getText().toString();
                num1 = Integer.parseInt(str1);
                num2 = Integer.parseInt(str2);
                if (str1.equals("") && str2.equals("")) {
                    //ボタンは反応しない
                } else {
                    //登録内容の更新処理
                }
            }
        });

        //初期化ボタン押下
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //確認
                //初期化処理
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
