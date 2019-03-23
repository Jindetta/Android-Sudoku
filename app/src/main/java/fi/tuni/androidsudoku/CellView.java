package fi.tuni.androidsudoku;

import android.content.Context;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatTextView;
import android.view.ContextThemeWrapper;

import fi.tuni.androidsudoku.sudoku.Cell;

/**
 *
 */
public class CellView extends AppCompatTextView {

    /**
     *
     */
    private int index;

    /**
     *
     * @param context
     */
    public CellView(Context context, int index) {
        super(new ContextThemeWrapper(context, R.style.SudokuCell));

        this.index = index;
    }

    /**
     *
     * @param cell
     */
    public void setText(Cell cell) {
        setText(getCellValue(cell));

        if (cell.isLocked()) {
            setTextColor(AppCompatResources.getColorStateList(getContext(), R.color.cellClue));
            setEnabled(false);
        }
    }

    /**
     *
     * @param cell
     * @return
     */
    private static String getCellValue(Cell cell) {
        return !cell.isEmpty() ? String.valueOf(cell.getValue()) : "";
    }
}
