package fi.tuni.androidsudoku.sudoku;

/**
 *
 */
public class Cell {

    /**
     *
     */
    private int index;

    /**
     *
     */
    private int value;

    /**
     *
     */
    private boolean locked;

    /**
     *
     * @param index
     */
    public Cell(int index) {
        setLocked(false);
        setIndex(index);
        setEmpty();
    }

    /**
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     *
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     *
     * @return
     */
    public int getValue() {
        return value;
    }

    /**
     *
     *
     * @param value
     * @return
     */
    public boolean setValue(int value) {
        if (allowedValue(value)) {
            this.value = value;
            return true;
        }

        return false;
    }

    /**
     *
     * @return
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     *
     * @param locked
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     *
     */
    public void setEmpty() {
        value = 0;
    }

    /**
     *
     * @return
     */
    public int getBlock() {
        int row = index / (Constants.GRID_SET * Constants.GRID);
        return row * Constants.GRID + index / Constants.GRID % Constants.GRID;
    }

    /**
     *
     * @return
     */
    public int getColumn() {
        return index % Constants.GRID_SET;
    }

    /**
     *
     * @return
     */
    public int getRow() {
        return index / Constants.GRID_SET;
    }

    /**
     *
     * @param cell
     * @return
     */
    public boolean equals(Cell cell) {
        return cell != null &&
               cell.isLocked() == isLocked() &&
               cell.getValue() == getValue() &&
               cell.getIndex() == getIndex();
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format(
            java.util.Locale.ENGLISH,
            "[value = %d, index = %d, column = %d, row = %d, block = %d]",
            getValue(),
            getIndex(),
            getColumn(),
            getRow(),
            getBlock()
        );
    }

    /**
     *
     * @param value
     * @return
     */
    public static boolean allowedValue(int value) {
        return value >= Constants.MIN_CELL_VALUE &&
               value <= Constants.MAX_CELL_VALUE;
    }
}