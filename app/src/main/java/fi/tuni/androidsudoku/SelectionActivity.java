package fi.tuni.androidsudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fi.tuni.androidsudoku.sudoku.Sudoku;

/**
 * Activity displaying difficulty options.
 *
 * @author  Joonas Lauhala @literal{<joonas.lauhala@tuni.fi>}
 * @version 20190422
 * @since   1.8
 */
public class SelectionActivity extends AppCompatActivity {

    /**
     * Creates activity.
     *
     * @param savedInstanceState Instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        setTitle(R.string.difficulty);
    }

    /**
     * Listens for click events.
     *
     * @param view Target view.
     */
    public void puzzleSelection(View view) {
        Intent activity = new Intent(this, PuzzleActivity.class);

        switch (view.getId()) {
            case R.id.easy: {
                activity.putExtra("difficulty", Sudoku.Difficulty.EASY.name());
                break;
            }
            case R.id.medium: {
                activity.putExtra("difficulty", Sudoku.Difficulty.MEDIUM.name());
                break;
            }
            case R.id.hard: {
                activity.putExtra("difficulty", Sudoku.Difficulty.HARD.name());
                break;
            }
            case R.id.very_hard: {
                activity.putExtra("difficulty", Sudoku.Difficulty.VERY_HARD.name());
                break;
            }
            default: {
                activity.putExtra("difficulty", Sudoku.Difficulty.NONE.name());
                break;
            }
        }

        startActivity(activity);
    }
}
