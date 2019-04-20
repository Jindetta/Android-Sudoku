package fi.tuni.androidsudoku;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.os.Bundle;
import android.view.View;

/**
 * Activity displaying settings.
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * Defines settings key.
     */
    public static final String KEY = "AndroidSudokuPreferences";

    /**
     * Creates activity.
     *
     * @param savedInstanceState Instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences(KEY, MODE_PRIVATE);

        CheckBox highlightCells = findViewById(R.id.toggleHighlight);
        highlightCells.setChecked(prefs.getBoolean("highlight", true));

        CheckBox displayHints = findViewById(R.id.toggleHints);
        displayHints.setChecked(prefs.getBoolean("hints", true));
    }

    /**
     * Listens for click events.
     *
     * @param view Target view.
     */
    public void settingsListener(View view) {
        if (view instanceof CheckBox) {
            SharedPreferences.Editor prefs = getSharedPreferences(KEY, MODE_PRIVATE).edit();
            CheckBox setting = (CheckBox) view;

            switch (view.getId()) {
                case R.id.toggleHighlight: {
                    prefs.putBoolean("highlight", setting.isChecked());
                }
                case R.id.toggleHints: {
                    prefs.putBoolean("hints", setting.isChecked());
                }
            }

            prefs.apply();
        }
    }
}
