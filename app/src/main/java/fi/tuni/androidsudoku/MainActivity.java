package fi.tuni.androidsudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickHandler(View view) {
        switch (view.getId()) {
            case R.id.start: {
                startActivity(new Intent(this, PuzzleActivity.class));
                break;
            }
            case R.id.scoreboard: {
                // Show scoreboard
                break;
            }
            case R.id.settings: {
                // Open settings
                break;
            }
        }
    }
}
