package fi.tuni.androidsudoku;

import android.graphics.*;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.ContextThemeWrapper;

import fi.tuni.androidsudoku.sudoku.Cell;
import fi.tuni.androidsudoku.sudoku.Constants;

/**
 *
 */
public class CellView extends AppCompatTextView {

    /**
     *
     */
    private Cell cell;

    /**
     *
     */
    private static Paint paint;

    /**
     *
     * @param context
     */
    private CellView(Context context) {
        super(new ContextThemeWrapper(context, R.style.SudokuCell));
    }

    /**
     *
     * @param context
     */
    public CellView(Context context, Cell cell) {
        this(context);

        if (paint == null) {
            paint = new Paint();
            paint.setStrokeWidth(getResources().getDimension(R.dimen.cellBorderThick));
            paint.setColor(getResources().getColor(R.color.cellBorderHighlight, null));
        }

        this.cell = cell;
        updateCell();
    }

    /**
     *
     */
    public void updateCell() {
        if (cell != null) {
            setText(getCellValue(cell));

            final boolean enabled = !cell.isLocked();
            final int colorId = enabled ? R.color.cellDefaultFont : R.color.cellLockedFont;

            setTextColor(getResources().getColor(colorId, null));
            setFocusable(enabled);
            setEnabled(enabled);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final float x = getTranslationX();
        final float y = getTranslationY();
        final int   h = getHeight();
        final int   w = getWidth();

        final int   columnIndex = cell.getColumn() + 1;
        final int   rowIndex    = cell.getRow() + 1;

        if (columnIndex % Constants.GRID == 0 && columnIndex < Constants.GRID_SET) {
            canvas.drawLine(x + w, y, x + w, y + h, paint);
        }

        if (rowIndex % Constants.GRID == 0 && rowIndex < Constants.GRID_SET) {
            canvas.drawLine(x, y + h, x + w, y + h, paint);
        }

        super.onDraw(canvas);
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
