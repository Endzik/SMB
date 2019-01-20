package pl.edu.pja.s13227.smb.maps;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

    private static final int PLAYER_NEXT = 3;
    private static final int PLAYER_PREVIOUS = 4;
    private static final int PLAYER_START = 1;
    private static final int PLAYER_STOP = 0;
    private static final int PLAYER_PAUSE = 2;

    private static int[] imageResources = {R.drawable.png_download_free_1, R.drawable.png_example_5};
    private static int currentImg = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.pja.edu.pl"));
        PendingIntent pendingIntentBrowser = PendingIntent.getActivity(context, 0, browser, 0);
        views.setOnClickPendingIntent(R.id.browser, pendingIntentBrowser);

        Intent play = new Intent(context,MusicBroadcastReceiver.class);
        play.putExtra("option",PLAYER_START);
        PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context, 1, play, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.buttonPlay, pendingIntentPlay);

        Intent stop = new Intent(context,MusicBroadcastReceiver.class);
        stop.putExtra("option",PLAYER_STOP);
        PendingIntent pendingIntentStop = PendingIntent.getBroadcast(context, 0, stop, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.buttonStop, pendingIntentStop);

        Intent pause = new Intent(context,MusicBroadcastReceiver.class);
        pause.putExtra("option",PLAYER_PAUSE);
        PendingIntent pendingIntentPause = PendingIntent.getBroadcast(context, 2, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.buttonPause, pendingIntentPause);

        Intent next = new Intent(context,MusicBroadcastReceiver.class);
        next.putExtra("option",PLAYER_NEXT);
        PendingIntent pendingIntentNext = PendingIntent.getBroadcast(context, 3, next, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.buttonNext, pendingIntentNext);

        Intent previous = new Intent(context,MusicBroadcastReceiver.class);
        previous.putExtra("option",PLAYER_PREVIOUS);
        PendingIntent pendingIntentPrevious = PendingIntent.getBroadcast(context, 4, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.buttonPrevious, pendingIntentPrevious);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

