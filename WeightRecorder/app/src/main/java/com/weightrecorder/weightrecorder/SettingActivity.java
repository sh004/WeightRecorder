package com.weightrecorder.weightrecorder;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE1;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE1_ID;
import static com.weightrecorder.weightrecorder.DBHelper.HEIGHT;
import static com.weightrecorder.weightrecorder.DBHelper.TARGET_WEIGHT;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE2;


public class SettingActivity extends ActionBarActivity {

    private DBHelper helper;
    private EditText height, targetWeight;
    private TextView DOOjudge;
    private Button buttonEnter, buttonReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        helper = new DBHelper(this);                                    //DBセット
        height = (EditText) findViewById(R.id.heightNum);               //身長
        targetWeight = (EditText) findViewById(R.id.targetWeightNum);   //目標体重
        DOOjudge = (TextView)findViewById(R.id.DOOjudge);               //肥満度
        buttonEnter = (Button) findViewById(R.id.eButton);              //登録ボタン
        buttonReset = (Button) findViewById(R.id.rButton);              //初期化ボタン


        road();     //身長・目標体重・肥満度を表示

        //肥満度の即時判定
        height.addTextChangedListener(watchHandler);
        targetWeight.addTextChangedListener(watchHandler);

        //登録ボタン押下
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            String str1, str2;
            double num1, num2;

            //入力状態判定 書き込み
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

                db.insert(TABLE1, null, values);
                db.close();

                Toast.makeText(SettingActivity.this, "登録しました。", Toast.LENGTH_SHORT).show();
            }
        });

        //初期化ボタン押下
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDlg = new AlertDialog.Builder(SettingActivity.this);
                alertDlg.setMessage("初期化しますか？");
                //OKボタン押下
                alertDlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = helper.getWritableDatabase();
                        db.delete(TABLE1, null, null);
                        db.delete(TABLE2, null, null);
                        db.close();

                        height.setText("00.0");
                        targetWeight.setText("00.0");
                        DOOjudge.setText("");

                        Toast.makeText(SettingActivity.this, "初期化しました。", Toast.LENGTH_SHORT).show();
                    }
                });
                //Cancelボタン押下
                alertDlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SettingActivity.this, "キャンセルしました。", Toast.LENGTH_SHORT).show();
                    }
                });

                alertDlg.create().show();
            }
        });

    }

    //肥満度の即時反映
    private TextWatcher watchHandler = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //変更前
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //変更直前
        }

        @Override
        public void afterTextChanged(Editable s) {
            //変更後
            String str1, str2;

            str1 = height.getText().toString();
            str2 = targetWeight.getText().toString();

            if (str1.equals("") || str2.equals("")) {
                DOOjudge.setText("");
            } else {
                setDOOjudge();
            }
        }
    };

    //身長・目標体重を表示
    private void road() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "SELECT " + HEIGHT + "," + TARGET_WEIGHT +  " FROM " + TABLE1 + " ORDER BY " + TABLE1_ID + " DESC LIMIT 1;";
        String str1, str2;
        Double num1, num2;    //身長, 目標体重

        Cursor cursor = db.rawQuery(sql, null);
        startManagingCursor(cursor);

        while (cursor.moveToNext()) {
            num1 = cursor.getDouble(cursor.getColumnIndex(HEIGHT));
            num2 = cursor.getDouble(cursor.getColumnIndex(TARGET_WEIGHT));

            str1 = String.valueOf(num1);
            str2 = String.valueOf(num2);

            if (str1.equals("") || str2.equals("")) {
                height.setText("00.0");
                targetWeight.setText("00.0");
                DOOjudge.setText("");
            } else {
                height.setText(str1);
                targetWeight.setText(str2);
                setDOOjudge();      //肥満度判定
            }
        }

        db.close();
    }

    //肥満度判定
    private void setDOOjudge() {
        String str;
        Double num1, num2, BMI;

        num1 = Double.parseDouble(height.getText().toString());
        num2 = Double.parseDouble(targetWeight.getText().toString());

        if(num1 > 0 && num2 > 0) {
            num1 = num1 / 100;
            BMI = num2 / (num1 * num1);

            if(BMI < 18.5) {
                str = "低体重";
            } else if(BMI < 25 && BMI > 18.4) {
                str = "普通体重";
            } else if(BMI < 30 && BMI > 24) {
                str = "肥満(1度)";
            } else if(BMI < 35 && BMI > 29) {
                str = "肥満(2度)";
            } else if(BMI < 40 && BMI > 34) {
                str = "肥満(3度)";
            } else if(BMI > 39) {
                str = "肥満(4度)";
            } else {
                str = "";
            }
            DOOjudge.setText(str);
        } else {
            DOOjudge.setText("");
        }
    }

    //MainActivityへ戻る
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(SettingActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
