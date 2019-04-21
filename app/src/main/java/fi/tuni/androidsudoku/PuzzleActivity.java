package fi.tuni.androidsudoku;

import android.content.*;
import android.view.View;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fi.tuni.androidsudoku.sudoku.Constants;
import fi.tuni.androidsudoku.sudoku.Sudoku;

/**
 * Activity displaying Sudoku puzzle.
 */
public class PuzzleActivity extends AppCompatActivity implements View.OnFocusChangeListener,
                                                                 View.OnLongClickListener {

    /**
     * Stores all cells.
     */
    private CellView[] cells;

    /**
     * Stores puzzle instance.
     */
    private Sudoku puzzle;

    /**
     * Stores cell highlight state.
     */
    private boolean highlightCells;

    /**
     * Stores note display state.
     */
    private boolean displayNotes;

    /**
     * Stores current cell selection.
     */
    private CellView currentSelection;

    /**
     * Stores layout.
     */
    private LinearLayout grid;

    /**
     * Stores timer broadcast listener.
     */
    private TimerEventReceiver timeEventListener;

    /**
     * Defines layout parameters for rows.
     */
    private static final LinearLayout.LayoutParams ROW_PARAMS = new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, 0, 100f / Constants.GROUP_SIZE
    );

    /**
     * Defines layout parameters for cells.
     */
    private static final LinearLayout.LayoutParams CELL_PARAMS = new LinearLayout.LayoutParams(
        0, LinearLayout.LayoutParams.MATCH_PARENT, 100f / Constants.GROUP_SIZE
    );

    /**
     * Overrides default constructor.
     */
    public PuzzleActivity() {
        puzzle = new Sudoku();
    }

    /**
     * Creates activity.
     *
     * @param savedInstanceState    Instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        grid = findViewById(R.id.puzzle);
        cells = new CellView[Constants.PUZZLE_SIZE];
        setupPuzzle(getIntent());

        SharedPreferences prefs = getSharedPreferences(SettingsActivity.KEY, MODE_PRIVATE);
        highlightCells = prefs.getBoolean("highlight", true);
        displayNotes = prefs.getBoolean("hints", true);

        timeEventListener = new TimerEventReceiver();
        Intent timer = new Intent(this, TimerService.class);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(timeEventListener, new IntentFilter(TimerService.TIMER_EVENT));

        stopService(timer);
        startService(timer);
    }

    /**
     * Post-processes activity.
     *
     * @param savedInstanceState    Instance state.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        int index = 0;

        for (int i = 0; i < Constants.GROUP_SIZE; i++) {
            LinearLayout row = new LinearLayout(this);

            for (int j = 0; j < Constants.GROUP_SIZE; j++) {
                CellView cell = new CellView(this, puzzle.getCellInfo(index), displayNotes);

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
     * Destroys activity.
     */
    @Override
    protected void onDestroy() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(timeEventListener);

        super.onDestroy();
    }

    /**
     * Updates given cell value.
     *
     * @param view  Target view.
     */
    public void updateCellValue(View view) {
        if (currentSelection != null && currentSelection.isFocusable()) {
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
     * Updates grid cell state.
     */
    private void updateGridStatus() {
        if (puzzle.puzzleIsComplete()) {
            for (CellView cell : cells) {
                cell.setActivated(false);
                cell.setFocusable(false);
                cell.setSelected(false);

                cell.updateCellText();
            }

            stopService(new Intent(this, TimerService.class));
            Toast.makeText(this, R.string.completed, Toast.LENGTH_LONG).show();
        } else {
            for (CellView cell : cells) {
                cell.updateCellText();
            }
        }
    }

    /**
     * Initializes puzzle with intended difficulty.
     *
     * @param intent    Intent data.
     */
    private void setupPuzzle(Intent intent) {
        puzzle.generateSolution();

        for (Sudoku.Difficulty difficulty : Sudoku.Difficulty.values()) {
            if (difficulty.name().equals(intent.getStringExtra("difficulty"))) {
                puzzle.generatePuzzle(difficulty);
                break;
            }
        }
    }

    /**
     * Listens for long click events.
     *
     * @param view  Target view.
     * @return true if event is processed, false otherwise.
     */
    @Override
    public boolean onLongClick(View view) {
        updateCellValue(view);
        return true;
    }

    /**
     * Listens for focus change events.
     *
     * @param view      Target view.
     * @param hasFocus  Focus state.
     */
    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus && view instanceof CellView) {
            currentSelection = (CellView) view;

            for (CellView cell : cells) {
                if (highlightCells && cell.isFocusable()) {
                    cell.setActivated(currentSelection.shouldActivate(cell));
                }

                cell.setSelected(cell == currentSelection);
            }
        }
    }

    /**
     * Private class for receiving broadcast events.
     */
    private class TimerEventReceiver extends BroadcastReceiver {

        /**
         * Receives broadcast events.
         *
         * @param context   Application context.
         * @param intent    Sender intent.
         */
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
