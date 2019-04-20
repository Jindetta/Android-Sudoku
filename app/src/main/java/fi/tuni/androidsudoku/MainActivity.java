package fi.tuni.androidsudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Activity displaying main menu.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Creates activity.
     *
     * @param savedInstanceState Instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView image = findViewById(R.id.image);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.scale);
        image.setAnimation(animation);
        animation.start();
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
            case R.id.scoreboard: {
                // Show scoreboard
                break;
            }
            case R.id.settings: {
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            }
        }
    }
}
