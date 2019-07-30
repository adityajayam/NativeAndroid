package demo.photogallery.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

public class CommonNotification {

    private Context mContext;

    public CommonNotification(Context context) {
        mContext = context;
    }

    public void createBasicNotification(String channelId, String title, String contentText, int icon, boolean ifBigText, PendingIntent pendingIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (ifBigText) {
            builder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(contentText));
        } else {
            builder.setContentText(title);
        }
        builder.setContentIntent(pendingIntent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createAndRegisterNotificationChannel(String channelName, String channelDescription, String channelId, int priority) {
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, priority);
        notificationChannel.setDescription(channelDescription);
        NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

}
