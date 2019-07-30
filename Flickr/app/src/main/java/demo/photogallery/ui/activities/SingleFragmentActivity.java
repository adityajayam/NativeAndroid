package demo.photogallery.ui.activities;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import demo.photogallery.R;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    private static final String TAG = "SingleFragmentActivity";

    public abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle onSavedInstance) {
        super.onCreate(onSavedInstance);
        Log.e(TAG, "onCreate()..");
        setContentView(getResourceIdForLayout());
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @LayoutRes
    public int getResourceIdForLayout() {
        return R.layout.activity_fragment;
    }
}
