package demo.photogallery.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import demo.photogallery.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleUIModeChanges();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    private void handleUIModeChanges() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        ActionBar actionbar = getSupportActionBar();
        ConstraintLayout constraintLayout = findViewById(R.id.container);
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.whiteColor, null));
                if (actionbar != null) {
                    actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.whiteColor, null)));
                    actionbar.setTitle((Html.fromHtml("<font color=\"#000000\">" + getString(R.string.app_name) + "</font>", 0)));
                }
                getWindow().setStatusBarColor(getResources().getColor(R.color.blackColor, null));
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.greyColor, null));
                if (actionbar != null) {
                    actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.greyColor, null)));
                    actionbar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.app_name) + "</font>", 0)));
                }
                getWindow().setStatusBarColor(getResources().getColor(R.color.blackColor, null));
                break;
        }
    }
}
