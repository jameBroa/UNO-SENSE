package com.example.properattempt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class CO2Activity extends AppCompatActivity {

    GraphView plot;
    private double xValue, yValue;
    private double counter = -1;

    private ArrayList<Double> xValues = new ArrayList<>();
    private ArrayList<Double> yValues = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_o2);

        plot = findViewById(R.id.GraphViewPlot);

        plot.getViewport().setScalable(true);
        plot.getViewport().setScalableY(true);
        plot.getViewport().setScrollable(false);
        plot.getViewport().setScrollableY(false);


//        plot.getViewport().setYAxisBoundsManual(true);
//        plot.getViewport().setMaxY(1000);
//        plot.getViewport().setMinY(0);
//
        plot.getViewport().setXAxisBoundsManual(true);
        plot.getViewport().setMaxX(100);
        plot.getViewport().setMinX(0);


            callAsynchronousTask("co2");

//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    DataPoint[] dataPoints = new DataPoint[xValues.size()];
//
//                    for(int i=0; i < xValues.size(); i++) {
//                        dataPoints[i] = new DataPoint(xValues.indexOf(i), yValues.indexOf(i));
//                    }
//
//                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
//                    plot.addSeries(series);
//                }
//            }, 1000);





//        if(xValue == 0 && yValue == 0) {
//
//        } else {
//            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[] {
//                    new DataPoint(xValue, yValue)
//
//            });
//            plot.addSeries(series);
//            series.setShape(PointsGraphSeries.Shape.POINT);
//            series.setSize(10f);
//        }

    }



    private class getRealTimeData extends AsyncTask<String, Void, Void> {

        String temp;

        @Override
        protected Void doInBackground(String... strings) {
            temp = strings[0];
            System.out.println(strings[0]);
            try {

                String url = "http://192.168.0.101:8888/getDashboardData";


                String textDocument = Jsoup.connect(url).get().html();

                //String textDocument = document.html();
                textDocument = textDocument.substring(372, textDocument.length());
                //System.out.println(textDocument);
                int index = textDocument.indexOf(strings[0]);
                String temp = textDocument.substring(index, textDocument.length());
                //System.out.println(temp);

                int counter = 0;
                String nextChar = "";
                boolean check = false;
                do {
                    nextChar = temp.substring(counter, counter + 1);
                    if (nextChar.equals(",")) {
                        break;
                    }
                    counter++;
                } while (!check);

                int lastIndex = counter + index;

                String result = textDocument.substring(index, lastIndex);
                result = result.replace("\"", "");
                if(strings[0].equals("tvoc")) {
                    result = result.substring(0,5) + " 0." + result.substring(6,result.length());
                }

                int beginningIndex = result.indexOf(" ");
                beginningIndex++;

                String stringAns = result.substring(beginningIndex, result.length());
                //System.out.println(stringAns);
                if(strings[0] == "tvoc") {
                    yValue = Double.parseDouble(stringAns);

                }
                yValue = Double.parseDouble(stringAns);
                yValues.add(yValue);



            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.println(yValue);
        }


    }


    public void callAsynchronousTask(final String stat) {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            xValues.add(counter);
                            counter++;

                            if(counter >= 100) {
                                counter = 0;
                                plot.removeAllSeries();
                            }

                            new getRealTimeData().execute(stat);
                            if(xValue != 0 && yValue != 0) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                       new DataPoint(xValue,yValue)
                                });


                                series.setDrawDataPoints(true);
                                series.setDataPointsRadius(10);
                                series.setColor(Color.BLUE);
                                series.setTitle("CO2 Levels");
                                series.setThickness(8);

                                Paint paint = new Paint();
                                paint.setStyle(Paint.Style.STROKE);
                                paint.setStrokeWidth(20);
                                paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 1));
                                series.setCustomPaint(paint);
                                plot.addSeries(series);


                            }



                            // PerformBackgroundTask this class is the class that extends AsynchTask

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 1000); //execute in every 50000 ms
    }



}




