package demo.photogallery.services;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import demo.photogallery.FlickrFetcher;
import demo.photogallery.R;
import demo.photogallery.model.GalleryItem;
import demo.photogallery.ui.activities.PhotoGalleryActivity;
import demo.photogallery.util.QueryPreferences;

public class PollService extends IntentService {
    private static final String TAG = "PollService";
    // Set interval to 1 minute
    private static final long POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(1);
    private static final String CHANNEL_ID = "0";
    public static final String ACTION_SHOW_NOTIFICATION =
            "demo.photogallery.SHOW_NOTIFICATION";
    public static final String PERM_PRIVATE = "demo.photogallery.PRIVATE";
    public static final String REQUEST_CODE = "REQUEST_CODE";
    public static final String NOTIFICATION = "NOTIFICATION";

    public static Intent newIntent(Context context) {
        return new Intent(context, PollService.class);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     **/
    public PollService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "Received an intent: " + intent);
        if (!isNetworkAvailableAndConnected()) {
            return;
        }
        String query = QueryPreferences.getStoredQuery(this);
        String lastResultId = QueryPreferences.getLastResultId(this);
        List<GalleryItem> items;
        if (query == null) {
            items = new FlickrFetcher().fetchRecentPhotos("0");
        } else {
            items = new FlickrFetcher().searchPhotos(query, "0");
        }
        if (items.size() == 0) {
            return;
        }
        String resultId = items.get(0).getmId();
        if (resultId.equals(lastResultId)) {
            Log.i(TAG, "Got an old result: " + resultId);
        } else {
            Log.i(TAG, "Got a new result: " + resultId);

            //notification
            createNotificationChannel();
            Resources resources = getResources();
            Intent i = PhotoGalleryActivity.newIntent(this);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setTicker(resources.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.new_pictures_title))
                    .setContentText(resources.getString(R.string.new_pictures_text))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
/*
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(0, notification.build());
            sendBroadcast(new Intent(ACTION_SHOW_NOTIFICATION), PERM_PRIVATE);
*/
            sendBackGroundNotification(0, notification.build());
        }
        QueryPreferences.setLastResultId(this, resultId);
    }

    private void sendBackGroundNotification(int requestCode, Notification notification) {
        Intent intent = new Intent(ACTION_SHOW_NOTIFICATION);
        intent.putExtra(REQUEST_CODE, requestCode);
        intent.putExtra(NOTIFICATION, notification);
        sendOrderedBroadcast(intent, PERM_PRIVATE, null, null, Activity.RESULT_OK, null, null);
    }

    public boolean isNetworkAvailableAndConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        boolean isNetworkAvailable = connectivityManager.getActiveNetworkInfo() != null;
        return isNetworkAvailable &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent serviceIntent = PollService.newIntent(context);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, serviceIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            return;
        }
        if (isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
        QueryPreferences.setAlarmOn(context, isOn);
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent serviceIntent = PollService.newIntent(context);
        PendingIntent pendingIntent = PendingIntent
                .getService(context, 0, serviceIntent, PendingIntent.FLAG_NO_CREATE);
        return pendingIntent != null;
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
