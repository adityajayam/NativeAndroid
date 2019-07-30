package demo.photogallery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import demo.photogallery.services.PollService;
import demo.photogallery.util.QueryPreferences;

public class StartupReceiver extends BroadcastReceiver {
    private static final String TAG = "StartupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received broadcast intent: " + intent.getAction());
        boolean isOn = QueryPreferences.isAlarmOn(context);
        PollService.setServiceAlarm(context,isOn);
    }
}
