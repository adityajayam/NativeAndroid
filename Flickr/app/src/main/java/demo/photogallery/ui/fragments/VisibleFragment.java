package demo.photogallery.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.fragment.app.Fragment;
import android.util.Log;

import demo.photogallery.services.PollService;

public class VisibleFragment extends Fragment {
    private static final String TAG = "VisibleFragment";

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
        getActivity().registerReceiver(broadcastReceiver, intentFilter, PollService.PERM_PRIVATE, null);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Toast.makeText(getActivity(), "Got a broadcast:" + intent.getAction(), Toast.LENGTH_LONG).show();
            // If we receive this, we're visible, so cancel
            // the notification
            Log.e(TAG, "canceling notification");
            setResultCode(Activity.RESULT_CANCELED);
        }
    };
}
