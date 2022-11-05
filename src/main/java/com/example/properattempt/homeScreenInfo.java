package com.example.properattempt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;





/**
 * Implementation of App Widget functionality.
 */
public class homeScreenInfo extends AppWidgetProvider {

    private static int counter = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        counter++;
        String temp = Integer.toString(counter);
        Log.v("testing", temp);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_screen_info);
        views.setTextViewText(R.id.currentText, String.valueOf(appWidgetId));

        //Retrieve and display the time//

        views.setTextViewText(R.id.currentText, temp);

        Intent alarm = new Intent(context, homeScreenInfo.class);
        
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        alarm.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE); // Set appwidget update action
        alarm.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId); // Set appwidget ids to be updated
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarm,
                PendingIntent.FLAG_CANCEL_CURRENT); // get the broadcast pending intent
        alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(),
                1000, pendingIntent); // set repeat alarm







    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        //If multiple widgets are active, then update them all//

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}



