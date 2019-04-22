package fi.tuni.androidsudoku.activities;

import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import fi.tuni.androidsudoku.R;

/**
 * Activity displaying main menu.
 *
 * @author  Joonas Lauhala @literal{<joonas.lauhala@tuni.fi>}
 * @version 20190422
 * @since   1.8
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Defines key for settings.
     */
    public static String SETTINGS_KEY = "AndroidSudokuPreferences";

    /**
     * Defines all slides.
     */
    private static int[] IMAGES = {
        R.mipmap.puzzle_1,
        R.mipmap.puzzle_2,
        R.mipmap.puzzle_3,
        R.mipmap.puzzle_4
    };

    /**
     * Stores current slide index.
     */
    private int slideIndex;

    /**
     * Stores slideshow controller.
     */
    private ImageSwitcher switcher;

    /**
     * Stores application preferences.
     */
    private SharedPreferences preferences;

    /**
     * Stores slideshow handler.
     */
    private Handler handler;

    /**
     * Stores upcoming slideshow event.
     */
    private Runnable event;

    /**
     * Creates activity.
     *
     * @param savedInstanceState Instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slideIndex = 0;

        switcher = findViewById(R.id.slideshow);
        switcher.setFactory(() -> new ImageView(this));
        switcher.setImageResource(IMAGES[slideIndex]);

        switcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in));
        switcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out));

        preferences = getSharedPreferences(SETTINGS_KEY, MODE_PRIVATE);
    }

    /**
     * Resumes activity.
     */
    @Override
    protected void onResume() {
        super.onResume();

        handler = new Handler();
        event = this::updateSlide;

        event.run();
    }

    /**
     * Pauses activity.
     */
    @Override
    protected void onPause() {
        super.onPause();

        handler.removeCallbacks(event);
    }

    /**
     * Listens for click events.
     *
     * @param view  Target view.
     */
    public void clickHandler(View view) {
        switch (view.getId()) {
            case R.id.start: {
                startActivity(new Intent(this, SelectionActivity.class));
                break;
            }
            case R.id.settings: {
                DialogFragment dialog = new SettingsDialog();
                dialog.show(getFragmentManager(), "SettingsDialog");
                break;
            }
        }
    }

    /**
     * Listens for settings dialog click events.
     *
     * @param view Target view.
     */
    public void settingsListener(View view) {
        if (view instanceof CheckBox) {
            SharedPreferences.Editor editor = preferences.edit();
            CheckBox setting = (CheckBox) view;

            switch (view.getId()) {
                case R.id.toggleHighlight: {
                    editor.putBoolean("highlight", setting.isChecked());
                    break;
                }
                case R.id.toggleHints: {
                    editor.putBoolean("hints", setting.isChecked());
                    break;
                }
            }

            editor.apply();
        }
    }

    /**
     * Updates current slide.
     */
    private void updateSlide() {
        handler.postDelayed(event, 2000);
        slideIndex = (slideIndex + 1) % IMAGES.length;
        switcher.setImageResource(IMAGES[slideIndex]);
    }

    /**
     * Class for creating settings menu.
     */
    public static class SettingsDialog extends DialogFragment {

        /**
         * Creates dialog.
         *
         * @param savedInstanceState    Instance state.
         * @return                      Newly created Dialog.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Activity activity = getActivity();

            LayoutInflater inflater = activity.getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            View layout = inflater.inflate(R.layout.dialog_settings, null);

            SharedPreferences preferences = activity.getSharedPreferences(SETTINGS_KEY, MODE_PRIVATE);
            CheckBox toggleHighlight = layout.findViewById(R.id.toggleHighlight);
            toggleHighlight.setChecked(preferences.getBoolean("highlight", true));
            CheckBox toggleHints = layout.findViewById(R.id.toggleHints);
            toggleHints.setChecked(preferences.getBoolean("hints", true));

            return builder.setPositiveButton(R.string.save, (dialog, id) -> dialog.dismiss())
                          .setTitle(R.string.settings)
                          .setView(layout)
                          .create();
        }
    }
}
