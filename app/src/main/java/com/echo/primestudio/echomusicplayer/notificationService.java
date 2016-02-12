package com.echo.primestudio.echomusicplayer;

import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import android.app.Notification;
import android.app.PendingIntent;
import android.view.View;
import android.widget.RemoteViews;

/**
 * Created by Rishabh Mishra on 2/10/2016.
 */
public class notificationService extends Service {

    public static String MAIN_ACTION = "com.echo.primestudio.echomusicplayer.main";
    public static String INIT_ACTION = "com.echo.primestudio.echomusicplayer.action.init";
    public static String PREV_ACTION = "com.echo.primestudio.echomusicplayer.action.prev";
    public static String PLAY_ACTION = "com.echo.primestudio.echomusicplayer.action.play";
    public static String NEXT_ACTION = "com.echo.primestudio.echomusicplayer.action.next";
    public static String START_FOREGROUND_ACTION = "com.echo.primestudio.echomusicplayer.action.startforeground";
    public static String STOP_FOREGROUND_ACTION = "com.echo.primestudio.echomusicplayer.action.stopforeground";
    public static String LOG_TAG = "NOTIFICATION SERVICE";
    public static int NOTIFICATION_ID = 410;

    Notification status ;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(START_FOREGROUND_ACTION)) {
            showNotification();
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

        } else if (intent.getAction().equals(PREV_ACTION)) {
            Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Previous");
        } else if (intent.getAction().equals(PLAY_ACTION)) {
            Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Play");
        } else if (intent.getAction().equals(NEXT_ACTION)) {
            Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Clicked Next");
        } else if (intent.getAction().equals(STOP_FOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
            stopForeground(true);
            stopSelf();
        }

        return START_STICKY;

    }

    private void showNotification() {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews views = new RemoteViews(getPackageName(),R.layout.status_bar);
        RemoteViews bigViews = new RemoteViews(getPackageName(),R.layout.status_bar_expanded);

        // showing default album image
        bigViews.setImageViewBitmap(R.id.album_art_notification,
                BitmapFactory.decodeResource(getResources(), R.drawable.echo_logo));

        Intent notificationIntent = new Intent(this, Main.class);
        notificationIntent.setAction(MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        Intent previousIntent = new Intent(this, notificationService.class);
        previousIntent.setAction(PREV_ACTION);
        PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
                previousIntent, 0);

        Intent playIntent = new Intent(this, notificationService.class);
        playIntent.setAction(PLAY_ACTION);
        PendingIntent pplayIntent = PendingIntent.getService(this, 0,
                playIntent, 0);

        Intent nextIntent = new Intent(this, notificationService.class);
        nextIntent.setAction(NEXT_ACTION);
        PendingIntent pnextIntent = PendingIntent.getService(this, 0,
                nextIntent, 0);

        Intent closeIntent = new Intent(this, notificationService.class);
        closeIntent.setAction(STOP_FOREGROUND_ACTION);
        PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
                closeIntent, 0);

        views.setOnClickPendingIntent(R.id.play_pause_notification, pplayIntent);
        bigViews.setOnClickPendingIntent(R.id.play_pause_notification, pplayIntent);

        views.setOnClickPendingIntent(R.id.next_notification, pnextIntent);
        bigViews.setOnClickPendingIntent(R.id.next_notification, pnextIntent);

        views.setOnClickPendingIntent(R.id.previous_notification, ppreviousIntent);
        bigViews.setOnClickPendingIntent(R.id.previous_notification, ppreviousIntent);

//        views.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
//        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);

        views.setImageViewResource(R.id.play_pause_notification,R.drawable.pause);
        bigViews.setImageViewResource(R.id.play_pause_notification,R.drawable.pause);

        views.setTextViewText(R.id.song_name_notification, "Song Title");
        bigViews.setTextViewText(R.id.song_name_notification, "Song Title");

        views.setTextViewText(R.id.song_artist_notification, "Artist Name");
        bigViews.setTextViewText(R.id.song_artist_notification, "Artist Name");

        bigViews.setTextViewText(R.id.album_art_notification, "Album Name");

        status = new Notification.Builder(this).build();
        status.contentView = views;
        status.bigContentView = bigViews;
        status.flags = Notification.FLAG_ONGOING_EVENT;
        status.icon = R.drawable.echo_logo;
        status.contentIntent = pendingIntent;
        startForeground(NOTIFICATION_ID, status);
    }

}
