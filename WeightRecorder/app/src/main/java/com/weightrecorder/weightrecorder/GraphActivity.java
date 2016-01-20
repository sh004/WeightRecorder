package com.weightrecorder.weightrecorder;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

import static com.weightrecorder.weightrecorder.DBHelper.TABLE2;
import static com.weightrecorder.weightrecorder.DBHelper.TABLE2_ID;
import static com.weightrecorder.weightrecorder.DBHelper.WEIGHT;


public class GraphActivity extends ActionBarActivity {
    private LineChartView lineChart;
    private LineChartData chartData;
    private List<PointValue> values;
    private Line line;
    private List<Line> lines;
    private DBHelper helper;
    private int total;
    private float weight;

    private class ValueTouchListener implements LineChartOnValueSelectListener {
        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(GraphActivity.this, values + "kg", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        lineChart = (LineChartView) findViewById(R.id.lineChart);
        chartData = new LineChartData();
        values = new ArrayList<PointValue>();
        line = new Line(values).setColor(Color.WHITE).setCubic(true);
        lines = new ArrayList<Line>();
        lineChart.setOnValueTouchListener(new ValueTouchListener());
        helper = new DBHelper(this);                            //DBセット

        getTotal();

        //データ追加 point(x, y)
        float x = 0, y = 0;
        for (x = 1; x <= total; x++) {
            road(x);

            y = weight;
            values.add(new PointValue(x, y));
        }

//        values.add(new PointValue(1, 550));
//        values.add(new PointValue(2, 551));
//        values.add(new PointValue(3, 550));
//        values.add(new PointValue(4, 548));
//        values.add(new PointValue(5, 550));
//        values.add(new PointValue(6, 549));
//        values.add(new PointValue(7, 551));
//        values.add(new PointValue(8, 552));
//        values.add(new PointValue(9, 548));
//        values.add(new PointValue(10, 548));
//        values.add(new PointValue(11, 549));

        lines.add(line);
        chartData.setLines(lines);
        lineChart.setLineChartData(chartData);
    }

    //データの総数
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

    private void road(float x) {
        SQLiteDatabase db = helper.getReadableDatabase();

        //TABLE2 x 番目に古いデータを取得
        String sql = "SELECT " + TABLE2 + "." + WEIGHT +
                " FROM " + TABLE2 + " ORDER BY " + TABLE2_ID + " ASC LIMIT " + x + ";";

        Cursor cursor = db.rawQuery(sql, null);
        startManagingCursor(cursor);

        while (cursor.moveToNext()) {
            weight = cursor.getFloat(cursor.getColumnIndex(WEIGHT));
        }

        db.close();
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
