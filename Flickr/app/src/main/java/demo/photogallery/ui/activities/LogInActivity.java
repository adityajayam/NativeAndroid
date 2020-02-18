package demo.photogallery.ui.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

import demo.photogallery.R;

public class LogInActivity extends AppCompatActivity {

    private BiometricPrompt.PromptInfo promptInfo;
    private BiometricPrompt biometricPrompt;
    private static final String TAG = "LogInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        handleUIModeChanges(null);
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                startMainActivity();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();
    }

    private void handleUIModeChanges(Configuration newConfig) {
        int currentNightMode;
        if (newConfig != null) {
            currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        } else {
            currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        }
        ActionBar actionbar = getSupportActionBar();
        ImageView fingerPrintImage = findViewById(R.id.fingerPrintImageView);
        ConstraintLayout constraintLayout = findViewById(R.id.container);
        TextView subtitleText = findViewById(R.id.subtitleText);
        subtitleText.setText(R.string.login_type_hint_text);
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                fingerPrintImage.setImageResource(R.drawable.ic_action_finger_print);
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.whiteColor, null));
                subtitleText.setTextColor(getResources().getColor(R.color.blackColor, null));
                if (actionbar != null) {
                    actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.whiteColor, null)));
                    actionbar.setTitle((Html.fromHtml("<font color=\"#000000\">" + getString(R.string.app_name) + "</font>", 0)));
                }
                getWindow().setStatusBarColor(getResources().getColor(R.color.blackColor, null));
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                fingerPrintImage.setImageResource(R.drawable.ic_action_finger_print_light);
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.greyColor, null));
                subtitleText.setTextColor(getResources().getColor(R.color.whiteColor, null));
                if (actionbar != null) {
                    actionbar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.greyColor, null)));
                    actionbar.setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.app_name) + "</font>", 0)));
                }
                getWindow().setStatusBarColor(getResources().getColor(R.color.blackColor, null));
                break;
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        handleUIModeChanges(newConfig);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BiometricManager biometricManager = BiometricManager.from(this);

        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d(TAG, "App can authenticate using biometrics.");
                biometricPrompt.authenticate(promptInfo);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e(TAG, "No biometric features available on this device.");
                startMainActivity();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e(TAG, "Biometric features are currently unavailable.");
                startMainActivity();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e(TAG, "The user hasn't associated " +
                        "any biometric credentials with their account.");
                startSecuritySettings();
                break;
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void startSecuritySettings() {
        startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
    }
}
