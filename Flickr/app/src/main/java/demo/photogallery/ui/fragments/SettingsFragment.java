package demo.photogallery.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;


import java.util.ArrayList;
import java.util.List;

import demo.photogallery.FlickrApplication;
import demo.photogallery.R;
import demo.photogallery.viewmodel.SettingsViewModel;

public class SettingsFragment extends Fragment implements Spinner.OnItemSelectedListener {

    private SettingsViewModel settingsViewModel;
    private List<String> spinnerData;
    private Spinner themeSpinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final TextView textView = root.findViewById(R.id.textView_select_theme);
        settingsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        themeSpinner = root.findViewById(R.id.spinner_theme);
        spinnerData = new ArrayList<>();
        spinnerData.add("DEFAULT");
        spinnerData.add("LIGHT");
        spinnerData.add("DARK");
        ArrayAdapter spinnerAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerData);
        themeSpinner.setAdapter(spinnerAdapter);
        themeSpinner.setOnItemSelectedListener(this);
        themeSpinner.setSelection(FlickrApplication.getAppThemeMode());
        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String selectedTheme = spinnerData.get(position);
        themeSpinner.setSelection(position);
        int THEME_VALUE = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        if (selectedTheme.equalsIgnoreCase("LIGHT")) {
            THEME_VALUE = AppCompatDelegate.MODE_NIGHT_NO;
        } else if (selectedTheme.equalsIgnoreCase("DARK")) {
            THEME_VALUE = AppCompatDelegate.MODE_NIGHT_YES;
        }
        FlickrApplication.setAppThemeMode(position);
        AppCompatDelegate.setDefaultNightMode(THEME_VALUE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}