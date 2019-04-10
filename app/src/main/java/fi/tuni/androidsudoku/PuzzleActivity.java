package fi.tuni.androidsudoku;

import android.view.View;
import android.content.Intent;
import android.content.Context;
import android.widget.LinearLayout;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fi.tuni.androidsudoku.sudoku.Constants;
import fi.tuni.androidsudoku.sudoku.SudokuPuzzle;

/**
 *
 */
public class PuzzleActivity extends AppCompatActivity implements View.OnFocusChangeListener,
                                                                 View.OnLongClickListener {

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
    private CellView currentSelection;

    /**
     *
     */
    private LinearLayout grid;

    /**
     *
     */
    private TimerEventReceiver timeEventListener;

    /**
     *
     */
    private static final LinearLayout.LayoutParams ROW_PARAMS = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, 0, 100f / Constants.GROUP_SIZE
    );

    /**
     *
     */
    private static final LinearLayout.LayoutParams CELL_PARAMS = new LinearLayout.LayoutParams(
        0, LinearLayout.LayoutParams.MATCH_PARENT, 100f / Constants.GROUP_SIZE
    );

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        grid = findViewById(R.id.puzzle);
        cells = new CellView[Constants.PUZZLE_SIZE];
        puzzle = new SudokuPuzzle(SudokuPuzzle.Difficulty.HARD);

        timeEventListener = new TimerEventReceiver();
        Intent timer = new Intent(this, TimerService.class);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(timeEventListener, new IntentFilter(TimerService.TIMER_EVENT));

        stopService(timer);
        startService(timer);
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        int index = 0;

        for (int i = 0; i < Constants.GROUP_SIZE; i++) {
            LinearLayout row = new LinearLayout(this);

            for (int j = 0; j < Constants.GROUP_SIZE; j++) {
                CellView cell = new CellView(this, puzzle.getCellInfo(index));

                cell.setMinWidth(CELL_PARAMS.width);
                cell.setMaxWidth(CELL_PARAMS.width);
                cell.setMinHeight(ROW_PARAMS.height);
                cell.setMaxHeight(ROW_PARAMS.height);

                cell.setOnFocusChangeListener(this);
                cell.setOnLongClickListener(this);

                row.addView(cell, CELL_PARAMS);
                cells[index++] = cell;

                if (currentSelection == null && cell.isFocusable()) {
                    currentSelection = cell;
                }
            }

            grid.addView(row, ROW_PARAMS);
        }

        currentSelection.requestFocus();
    }

    /**
     *
     */
    @Override
    protected void onDestroy() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(timeEventListener);

        super.onDestroy();
    }

    /**
     *
     * @param view
     */
    public void updateCellValue(View view) {
        if (currentSelection != null) {
            int value = Constants.EMPTY_CELL_VALUE;

            switch (view.getId()) {
                case R.id.nr_1: {
                    value = 1;
                    break;
                }
                case R.id.nr_2: {
                    value = 2;
                    break;
                }
                case R.id.nr_3: {
                    value = 3;
                    break;
                }
                case R.id.nr_4: {
                    value = 4;
                    break;
                }
                case R.id.nr_5: {
                    value = 5;
                    break;
                }
                case R.id.nr_6: {
                    value = 6;
                    break;
                }
                case R.id.nr_7: {
                    value = 7;
                    break;
                }
                case R.id.nr_8: {
                    value = 8;
                    break;
                }
                case R.id.nr_9: {
                    value = 9;
                    break;
                }
            }

            currentSelection.setCellValue(value);
            updateGridStatus();
        }
    }

    /**
     *
     */
    private void updateGridStatus() {
        for (CellView cell : cells) {
            cell.updateCellText();
        }

        if (puzzle.puzzleIsComplete()) {
            stopService(new Intent(this, TimerService.class));
        }
    }

    /**
     *
     * @param view
     * @return
     */
    @Override
    public boolean onLongClick(View view) {
        updateCellValue(view);
        return true;
    }

    /**
     *
     * @param view
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus && view instanceof CellView) {
            currentSelection = (CellView) view;

            for (CellView cell : cells) {
                if (cell.isFocusable()) {
                    cell.setActivated(currentSelection.shouldActivate(cell));
                }

                cell.setSelected(cell == currentSelection);
            }
        }
    }

    /**
     *
     */
    private class TimerEventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && TimerService.TIMER_EVENT == intent.getAction()) {
                long elapsed = intent.getLongExtra("elapsed", 0);

                setTitle(
                    getString(
                        R.string.timer,
                        elapsed / 1000 / 60,
                        elapsed / 1000 % 60,
                        elapsed % 1000 / 10
                    )
                );
            }
        }
    }
}
