package com.weightrecorder.weightrecorder;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;


public class MainActivity extends ActionBarActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
