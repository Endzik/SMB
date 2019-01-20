package pl.edu.pja.s13227.smb.maps;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MusicBroadcastReceiver extends BroadcastReceiver {

    static private String[] musicList = {"BachGavotteShort.mp3","WalloonLilliShort.mp3"};
    static private AssetFileDescriptor assetFileDescriptor;
    static private MediaPlayer player = new MediaPlayer();
    static private String current;
    static private boolean isPlaying = false;
    private int option;
    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager gm = AppWidgetManager.getInstance(context);
        ArrayList<Integer> appWidgetIds = new ArrayList<>();

        option = intent.getIntExtra("option",-1);
        Log.d("Music ", String.valueOf(intent));
        Log.d("Music int: ", String.valueOf(option));

        switch (option){
            case 0:
                Log.d("MUSIC","STOP");
                stop();
                break;
            case 1:
                Log.d("MUSIC","PLAY");
                play(context);
                break;
            case 2:
                Log.d("MUSIC","PAUSE");
                pause();
                break;
            case 3:
                Log.d("MUSIC","NEXT");
                next(context);
                break;
            case 4:
                Log.d("MUSIC","PAUSE");
                previous(context);
                break;
            default:
                Log.d("MUSIC","error");
        }

        final int n = appWidgetIds.size();
        for (int i=0; i<n; i++) {
            WidgetProvider.updateAppWidget(context, gm, appWidgetIds.get(i));
        }
    }

    private void stop() {

        player.reset();
        isPlaying = false;
    }

    private void pause() {

        player.pause();
    }

    private void next(Context context) {

        if(current == null) {
            current = musicList[0];
        }
        if (isPlaying){
            stop();
        }
        int indexLast = musicList.length-1;
        int index = Arrays.asList(musicList).indexOf(current);
        int newIndex;
        if (index == indexLast){
            newIndex = 0;
        } else {
            newIndex = index+1;
        }
        current = musicList[newIndex];

    }

    private void previous(Context context) {
        if(current == null) {
            current = musicList[0];
        }
        if (isPlaying){
            stop();
        }
            int index = Arrays.asList(musicList).indexOf(current);
            int newIndex;
            if (index == 0){
                newIndex = musicList.length-1;
            } else {
                newIndex = index-1;
            }
            current = musicList[newIndex];
    }

    private void play(Context context) {

        if (current == null){
            current = musicList[0];
        }
        Log.d("MUSIC","isPlaying?  " + String.valueOf(isPlaying));

        if (!isPlaying) {
            try {
                assetFileDescriptor = context.getAssets().openFd(current);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                player.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            isPlaying = true;
            player.start();
        } else {

            player.start();
        }
    }
}
