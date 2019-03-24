package fi.tuni.androidsudoku;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
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
    private TextView timer;

    /**
     *
     */
    private TimerEventReceiver timeEventListener;

    /**
     *
     */
    private static final TableLayout.LayoutParams ROW_PARAMS = new TableLayout.LayoutParams(
        TableLayout.LayoutParams.MATCH_PARENT,
        TableLayout.LayoutParams.WRAP_CONTENT,
        1f / Constants.GROUP_SIZE
    );

    /**
     *
     */
    private static final TableRow.LayoutParams CELL_PARAMS = new TableRow.LayoutParams(
        TableRow.LayoutParams.WRAP_CONTENT,
        TableRow.LayoutParams.MATCH_PARENT,
        1f / Constants.GROUP_SIZE
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle);

        cells = new CellView[Constants.PUZZLE_SIZE];
        puzzle = new SudokuPuzzle(SudokuPuzzle.Difficulty.VERY_HARD);
        timer = findViewById(R.id.timer);

        timeEventListener = new TimerEventReceiver();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(timeEventListener, new IntentFilter(TimerService.TIMER_EVENT));

        Intent timerService = new Intent(this, TimerService.class);
        timerService.putExtra("started", System.currentTimeMillis());
        startService(timerService);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        TableLayout layout = findViewById(R.id.puzzle);

        final int MAX_WIDTH = layout.getWidth() / Constants.GROUP_SIZE;
        final int MAX_HEIGHT = layout.getHeight() / Constants.GROUP_SIZE;

        int index = 0;

        for (int i = 0; i < Constants.GROUP_SIZE; i++) {
            TableRow row = new TableRow(this);

            for (int j = 0; j < Constants.GROUP_SIZE; j++) {
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

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, TimerService.class));
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(timeEventListener);

        super.onDestroy();
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

    /**
     *
     */
    private class TimerEventReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && TimerService.TIMER_EVENT == intent.getAction()) {
                long minutes = intent.getLongExtra("minutes", 0);
                long seconds = intent.getLongExtra("seconds", 0);
                long millis = intent.getLongExtra("millis", 0);

                timer.setText(getString(R.string.timer, minutes, seconds, millis));
            }
        }
    }
}
