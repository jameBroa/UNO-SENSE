package com.example.properattempt;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
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
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class TemperatureActivity extends AppCompatActivity {

    GraphView plot;
    private double xValue, yValue;
    private int counter = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        plot = findViewById(R.id.GraphViewPlot);

        plot.getViewport().setScalable(true);
        plot.getViewport().setScalableY(true);
        plot.getViewport().setScrollable(false);
        plot.getViewport().setScrollableY(false);


        plot.getViewport().setYAxisBoundsManual(true);
        plot.getViewport().setMaxY(100);
        plot.getViewport().setMinY(0);

        plot.getViewport().setXAxisBoundsManual(true);
        plot.getViewport().setMaxX(200);
        plot.getViewport().setMinX(0);

        callAsynchronousTask("temperature");

        if(xValue == 0 && yValue == 0) {

        } else {
            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[] {
                    new DataPoint(xValue, yValue)

            });
            plot.addSeries(series);
            series.setShape(PointsGraphSeries.Shape.POINT);
            series.setSize(10f);
        }

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
                            xValue = counter;
                            counter++;

                            new getRealTimeData().execute(stat);

                            PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(new DataPoint[] {
                                    new DataPoint(xValue, yValue)
                            });

                            plot.addSeries(series);
                            series.setShape(PointsGraphSeries.Shape.POINT);
                            series.setSize(10f);

                            // PerformBackgroundTask this class is the class that extends AsynchTask

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 500); //execute in every 50000 ms
    }




}




