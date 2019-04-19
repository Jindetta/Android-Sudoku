package fi.tuni.androidsudoku;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void settingsListener(View view) {
        if (view instanceof CheckBox) {
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
            CheckBox setting = (CheckBox) view;

            switch (view.getId()) {
                case R.id.toggleHighlight: {
                    prefs.edit().putBoolean("highlight", setting.isChecked());
                }
                case R.id.toggleHints: {
                    prefs.edit().putBoolean("hints", setting.isChecked());
                }
            }

            prefs.edit().apply();
        }
    }
}
