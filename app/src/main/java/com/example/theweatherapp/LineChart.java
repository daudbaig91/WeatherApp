package com.example.theweatherapp;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class LineChart {



    LineChart(View view,ArrayList<Entry> yValues){
        com.github.mikephil.charting.charts.LineChart mChart;
        mChart = view.findViewById(R.id.lineChart);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        LineDataSet set1 = new LineDataSet(yValues , "Weather Today");

        set1.setLineWidth(3f);
        set1.setValueTextSize(14);
        set1.setFillAlpha(110);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);


        LineData data = new LineData(dataSets);
        mChart.setData(data);
        mChart.getXAxis().setLabelCount(5, true);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        //making all background lines invisible
        mChart.setVisibleXRangeMaximum(4);
        mChart.setDrawGridBackground(false);
        mChart.getLegend().setEnabled(false);

        mChart.getDescription().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setEnabled(false);


        mChart.getXAxis().setDrawGridLines(false);
        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.setDrawGridBackground(false);
        mChart.getXAxis().setCenterAxisLabels(false);
        //refreshing chart
        mChart.invalidate();

    }
}
