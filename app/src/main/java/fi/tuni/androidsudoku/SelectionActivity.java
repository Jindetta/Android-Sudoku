package fi.tuni.androidsudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fi.tuni.androidsudoku.sudoku.Sudoku;

public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
    }

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
