package fi.tuni.androidsudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.widget.*;
import fi.tuni.androidsudoku.sudoku.Constants;

/**
 *
 */
public class PuzzleActivity extends AppCompatActivity {

    /**
     *
     */
    private TextView[] cells;

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

        cells = new TextView[Constants.GRID_SIZE];
        TableLayout layout = findViewById(R.id.puzzle);
        int index = 0;

        for (int i = 0; i < Constants.GRID_SET; i++) {
            TableRow row = new TableRow(this);

            for (int j = 0; j < Constants.GRID_SET; j++) {
                TextView cell = new TextView(new ContextThemeWrapper(this, R.style.SudokuCell));
                cell.setText("X");

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
        if (view.isEnabled()) {
            CellView cellView = (CellView) view;

            int randomValue = (int) (Math.random() * 8) + 1;
            cellView.setText(String.valueOf(randomValue));
        }
    }
}
