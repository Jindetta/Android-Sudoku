package fi.tuni.androidsudoku;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import fi.tuni.androidsudoku.sudoku.Constants;
import fi.tuni.androidsudoku.sudoku.SudokuPuzzle;

/**
 *
 */
public class PuzzleActivity extends AppCompatActivity {

    /**
     *
     */
    private CellView[] cells;

    /**
     *
     */
    private SudokuPuzzle puzzle;

    /**
     *
     */
    private static final TableLayout.LayoutParams ROW_PARAMS = new TableLayout.LayoutParams(
        TableLayout.LayoutParams.MATCH_PARENT,
        TableLayout.LayoutParams.WRAP_CONTENT,
        1f / Constants.GRID_SET
    );

    /**
     *
     */
    private static final TableRow.LayoutParams CELL_PARAMS = new TableRow.LayoutParams(
        TableRow.LayoutParams.WRAP_CONTENT,
        TableRow.LayoutParams.MATCH_PARENT,
        1f / Constants.GRID_SET
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        cells = new CellView[Constants.GRID_SIZE];
        puzzle = new SudokuPuzzle(SudokuPuzzle.Difficulty.EASY);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        TableLayout layout = findViewById(R.id.puzzle);

        final int MAX_WIDTH = layout.getWidth() / Constants.GRID_SET;
        final int MAX_HEIGHT = layout.getHeight() / Constants.GRID_SET;

        int index = 0;

        for (int i = 0; i < Constants.GRID_SET; i++) {
            TableRow row = new TableRow(this);

            for (int j = 0; j < Constants.GRID_SET; j++) {
                CellView cell = new CellView(this, puzzle.getCellInfo(index));

                cell.setMinWidth(MAX_WIDTH);
                cell.setMaxWidth(MAX_WIDTH);
                cell.setMinHeight(MAX_HEIGHT);
                cell.setMaxHeight(MAX_HEIGHT);

                row.addView(cell, CELL_PARAMS);
                cells[index++] = cell;
            }

            layout.addView(row, ROW_PARAMS);
        }
    }

    /**
     *
     * @param view
     */
    public void cellClicked(View view) {
        CellView cellView = (CellView) view;

        int randomValue = (int) (Math.random() * 9) + 1;
        cellView.setText(String.valueOf(randomValue));
    }
}
