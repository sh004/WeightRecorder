package com.weightrecorder.weightrecorder;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import  static com.weightrecorder.weightrecorder.DBHelper.USER;
import  static com.weightrecorder.weightrecorder.DBHelper.ME;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE1;
import static com.weightrecorder.weightrecorder.DBHelper.HEIGHT;
import static com.weightrecorder.weightrecorder.DBHelper.TARGET_WEIGHT;


public class SettingActivity extends ActionBarActivity {

    private DBHelper helper;
    private Button buttonEnter, buttonReset;
    private EditText height, targetWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        helper = new DBHelper(this);                                    //DBセット
        buttonEnter = (Button) findViewById(R.id.eButton);              //登録ボタン
        buttonReset = (Button) findViewById(R.id.rButton);              //初期化ボタン
        height = (EditText) findViewById(R.id.heightNum);               //身長
        targetWeight = (EditText) findViewById(R.id.targetWeightNum);   //目標体重

        //登録ボタン押下
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            String str1, str2;
            double num1, num2;

            //身長・目標体重の入力状態を判定
            @Override
            public void onClick(View v) {
                str1 = height.getText().toString();
                str2 = targetWeight.getText().toString();

                if (str1.equals("") || str2.equals("")) {
                    Toast.makeText(SettingActivity.this, "値を入力してください。", Toast.LENGTH_SHORT).show();
                } else {
                    num1 = Double.parseDouble(str1);
                    num2 = Double.parseDouble(str2);

                    if (num1 > 0 && num2 > 0) {
                        addData(num1, num2);    //DBに書き込み
                    } else {
                        Toast.makeText(SettingActivity.this, "正しく入力してください。", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            //DBに書き込み
            private void addData(double height, double targetWeight) {
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(HEIGHT, height);
                values.put(TARGET_WEIGHT, targetWeight);

                db.update(TABLE1, values, USER + "= '" + ME + "'", null);
                db.close();

                Toast.makeText(SettingActivity.this, "登録しました。", Toast.LENGTH_SHORT).show();
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
}
