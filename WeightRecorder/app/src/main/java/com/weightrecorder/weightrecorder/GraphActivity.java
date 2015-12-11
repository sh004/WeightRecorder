package com.weightrecorder.weightrecorder;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;


public class GraphActivity extends ActionBarActivity {

    private Line line;
    private LinePoint point;
    private LineGraph graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        line = new Line();
        point = new LinePoint();
        graph = (LineGraph) findViewById(R.id.graph);

        for(int i = 0; i > total; i++) {
            point.setX(i);
            point.setY(weight);
            line.addPoint(point);                           //ポイント追加
        }

        line.setColor(Color.parseColor("#9acd32"));     //線の色
        graph.addLine(line);                            //グラフセット
        graph.setRangeY(0, 10);                         //最大値,最小値
        graph.setLineToFill(0);
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
