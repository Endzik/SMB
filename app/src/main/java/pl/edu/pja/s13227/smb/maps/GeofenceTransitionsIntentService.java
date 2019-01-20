package pl.edu.pja.s13227.smb.maps;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceTransitionsIntentService extends IntentService {

    private NotificationManager notificationManager;

    public GeofenceTransitionsIntentService() {
        super("my-geo-service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = geofencingEvent.getErrorCode()+"";
            Log.e("GEOFENCING", errorMessage);
            return;
        }

        NotificationManager notificationMng = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("GEOFENCING", "Geofencing", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Geofencing alerts");
        notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            Notification notification = buildNotification("Entering");
            notificationMng.notify(1, notification);
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Notification notification = buildNotification("Leaving");
            notificationMng.notify(2, notification);        }
    }

    private Notification buildNotification(String action) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "GEOFENCING");
        return notificationBuilder
                .setContentTitle("Geofence alert!")
                .setContentText(action + " a favourite shop!")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_LIGHTS
                        | Notification.DEFAULT_VIBRATE
                        | Notification.DEFAULT_SOUND
                        | Notification.FLAG_AUTO_CANCEL)
                .build();
    }
}
