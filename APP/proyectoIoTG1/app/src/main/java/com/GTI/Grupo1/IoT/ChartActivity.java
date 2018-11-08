package com.GTI.Grupo1.IoT;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class ChartActivity extends Activity {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        PieChartView pieChartView = findViewById(R.id.chart);
        List<SliceValue> pieData = new ArrayList<>();
        pieData.add(new SliceValue(10, Color.LTGRAY).setLabel("Grasa corporal: 19.4"));
        pieData.add(new SliceValue(23, Color.DKGRAY).setLabel("Masa corporal: 45.6"));
        pieData.add(new SliceValue(17, Color.WHITE).setLabel(""));
        pieData.add(new SliceValue(50, 100).setLabel(""));
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChartData.setHasCenterCircle(true);
        pieChartView.setPieChartData(pieChartData);
        pieChartView.setChartRotation(180, true);
        pieChartView.setChartRotationEnabled(false);
        pieChartData.setCenterCircleScale(100);
    }
}
