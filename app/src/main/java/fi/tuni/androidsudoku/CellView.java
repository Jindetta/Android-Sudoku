package fi.tuni.androidsudoku;

import android.graphics.*;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.ContextThemeWrapper;
import android.view.View;

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
    private boolean notes;

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
        this.notes = true;
        updateCell();
    }

    /**
     *
     */
    private void updateCell() {
        if (cell != null) {
            updateCellText();

            final boolean enabled = !cell.isLocked();
            final int colorId = enabled ? R.color.cellDefaultFont : R.color.cellLockedFont;

            setTextColor(getResources().getColor(colorId, null));
            setFocusable(enabled);
            setEnabled(enabled);
        }
    }

    /**
     *
     * @param view
     * @return
     */
    public boolean shouldActivate(CellView view) {
        return cell != null && cell.isNeighbourCell(view.cell);
    }

    /**
     *
     * @return
     */
    public void setCellValue(int value) {
        if (value != Constants.EMPTY_CELL_VALUE) {
            cell.forceValue(value);
        } else {
            cell.setEmpty();
        }

        updateCellText();
    }

    /**
     *
     */
    public void updateCellText() {
        if (!cell.isEmpty()) {
            setTextSize(getResources().getDimension(R.dimen.cellTextSize));
            setText(String.valueOf(cell.getValue()));
        } else if (notes) {
            setTextSize(getResources().getDimension(R.dimen.cellNoteTextSize));
            setText(cell.getNotes());
        } else {
            setText("");
        }
    }

    /**
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        final float x = getTranslationX();
        final float y = getTranslationY();
        final int   h = getHeight();
        final int   w = getWidth();

        final int   columnIndex = cell.getColumn() + 1;
        final int   rowIndex    = cell.getRow() + 1;

        if (columnIndex % Constants.MULTIPLIER == 0 && columnIndex < Constants.GROUP_SIZE) {
            canvas.drawLine(x + w, y, x + w, y + h, paint);
        } else if (columnIndex % Constants.MULTIPLIER == 1 && columnIndex >= Constants.MULTIPLIER) {
            canvas.drawLine(x, y, x, y + h, paint);
        }

        if (rowIndex % Constants.MULTIPLIER == 0 && rowIndex < Constants.GROUP_SIZE) {
            canvas.drawLine(x, y + h, x + w, y + h, paint);
        } else if (rowIndex % Constants.MULTIPLIER == 1 && rowIndex >= Constants.MULTIPLIER) {
            canvas.drawLine(x, y, x + w, y, paint);
        }

        super.onDraw(canvas);
    }
}
