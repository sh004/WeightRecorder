package com.weightrecorder.weightrecorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;



public class GraphActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

    }

    //MainActivityへ戻る
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(GraphActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
