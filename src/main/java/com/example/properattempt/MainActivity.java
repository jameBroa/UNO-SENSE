package com.example.properattempt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button buttonTemp, buttonHumidity, buttonCO2, buttonTVOC, buttonPM10, buttonPM25;
    private TextView textViewTime;
    private String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        callAsynchronousTask("temperature");
//        callAsynchronousTask("humidity");
//        callAsynchronousTask("co2");
//        callAsynchronousTask2("tvoc");
//        callAsynchronousTask2("pm10");
//        callAsynchronousTask2("pm2.5");




        //***************ALLOWS ACCESS OBJECTS IN UI*****************************

        buttonTemp = findViewById(R.id.buttonTemp);
        buttonHumidity = findViewById(R.id.buttonHumidity);
        buttonCO2 = findViewById(R.id.buttonCO2);
        buttonTVOC = findViewById(R.id.buttonTVOC);
        buttonPM10 = findViewById(R.id.buttonPM10);
        buttonPM25 = findViewById(R.id.buttonPM25);

        textViewTime = findViewById(R.id.textViewTime);

        //********************* ADDS LISTENERS FOR BUTTONS *****************************

        buttonTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Going to temperature activity");
                goTemperature();
            }
        });

        buttonHumidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("This button works");
                goHumidity();

            }
        });
        buttonCO2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("This button works");
                goCO2();

            }
        });

        buttonTVOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("This button works");
            }
        });
        buttonPM10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("This button works");

            }
        });

        buttonPM25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("This button works");

            }
        });
    }

    //**************MULTITHREADING TO GET DATA*************************

    private class getUNOData extends AsyncTask<String, Void, Void>  {

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
                value = result.toUpperCase();



            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (temp.equals("co2")) {
                buttonCO2.setText(value.substring(0,4) + "\n" + value.substring(5,value.length()) + "ppm");
            }
            else if(temp.equals("temperature")) {
                buttonTemp.setText(value + "°C");
            }
            else if(temp.equals("humidity")) {
                buttonHumidity.setText(value.substring(0,9) + "\n" + value.substring(10, value.length()) + "%");
            }

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
                            new getUNOData().execute(stat);
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

    private class getUNOData2 extends AsyncTask<String, Void, Void>  {

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
                value = result.toUpperCase();



            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if(temp.equals("tvoc")) {
                buttonTVOC.setText(value.substring(0,5) + "\n" + value.substring(6, value.length()) + "ppm");
            }
            else if(temp.equals("pm10")) {
                buttonPM10.setText(value.substring(0,5) + "\n" + value.substring(6,value.length()) + "μg/m³");
            }
            else if(temp.equals("pm2.5")) {
                buttonPM25.setText(value.substring(0,6) + "\n" + value.substring(7,value.length()) + "μg/m³");
            }
        }


    }

    public void callAsynchronousTask2(final String stat) {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            new getUNOData2().execute(stat);
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

    public void goTemperature() {
        Intent intent = new Intent(this, TemperatureActivity.class);
        startActivity(intent);

    }

    public void goHumidity() {
        Intent intent = new Intent(this, HumidityActivity.class);
        startActivity(intent);
    }

    public void goCO2() {
        Intent intent = new Intent(this, CO2Activity.class);
        startActivity(intent);

    }

}