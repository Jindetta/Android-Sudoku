package fi.tuni.androidsudoku.sudoku;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Cell implements Cloneable {

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
     */
    private List<Cell> neighbours;

    /**
     *
     * @param index
     * @param value
     * @param locked
     */
    public Cell(int index, int value, boolean locked) {
        neighbours = null;

        setLocked(locked);
        setIndex(index);

        if (value != Constants.EMPTY_CELL_VALUE) {
            setValue(value);
        } else {
            setEmpty();
        }
    }

    /**
     *
     * @param puzzle
     */
    public void setupNeighbours(Cell[] puzzle) {
        neighbours = new ArrayList<>(20);

        for (Cell cell : puzzle) {
            if (cell.getIndex() != getIndex()) {
                if (cell.isNeighbour(this) || cell.getColumn() == getColumn()
                    || cell.getBlock() == getBlock() || cell.getRow() == getRow()) {
                    neighbours.add(cell);
                }
            }
        }
    }

    /**
     *
     * @param index
     * @return
     */
    public boolean isNeighbour(int index) {
        if (neighbours != null) {
            for (Cell cell : neighbours) {
                if (cell.getIndex() == index) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     *
     * @param cell
     * @return
     */
    private boolean isNeighbour(Cell cell) {
        return neighbours != null && neighbours.contains(cell);
    }

    /**
     *
     * @param value
     * @return
     */
    private boolean isValidCellValue(int value) {
        if (allowedValue(value)) {
            if (neighbours != null) {
                for (Cell cell : neighbours) {
                    if (value == cell.getValue()) {
                        return false;
                    }
                }
            }

            return true;
        }

        return false;
    }

    /**
     *
     * @return
     */
    public List<Integer> getValidValues() {
        final int[] values = Constants.ALLOWED_VALUES;
        List<Integer> results = new ArrayList<>(values.length);

        for (int value : values) {
            if (isValidCellValue(value)) {
                results.add(value);
            }
        }

        return results;
    }

    /**
     *
     * @return
     */
    public int getValidValuesCount() {
        List<Integer> values = getValidValues();

        return values.size();
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
    private void setIndex(int index) {
        if (index < 0 || index >= Constants.PUZZLE_SIZE) {
            throw new IllegalArgumentException("Cell index cannot be out-of-bounds.");
        }

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
     * @return
     */
    public boolean setNextValue() {
        List<Integer> values = getValidValues();
        int index = values.indexOf(getValue());

        if (index != -1) {
            if (++index < values.size()) {
                return setValue(values.get(index));
            }
        } else if (!values.isEmpty()) {
            return setValue(values.get(0));
        }

        return false;
    }

    /**
     *
     *
     * @param value
     * @return
     */
    public boolean setValue(int value) {
        if (value != getValue() && isValidCellValue(value)) {
            this.value = value;

            return true;
        }

        return false;
    }

    /**
     *
     * @param value
     */
    public void forceValue(int value) {
        this.value = value;
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
     * @return
     */
    public boolean isEmpty() {
        return value == Constants.EMPTY_CELL_VALUE;
    }

    /**
     *
     */
    public void setEmpty() {
        value = Constants.EMPTY_CELL_VALUE;
    }

    /**
     *
     * @return
     */
    public int getBlock() {
        int row = index / (Constants.GROUP_SIZE * Constants.MULTIPLIER);
        return row * Constants.MULTIPLIER + index / Constants.MULTIPLIER % Constants.MULTIPLIER;
    }

    /**
     *
     * @return
     */
    public int getColumn() {
        return index % Constants.GROUP_SIZE;
    }

    /**
     *
     * @return
     */
    public int getRow() {
        return index / Constants.GROUP_SIZE;
    }

    /**
     *
     * @return
     */
    @Override
    public Cell clone() {
        try {
            return (Cell) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }

    /**
     *
     * @param object
     * @return
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (object != null && object.getClass() == getClass()) {
            Cell cell = (Cell) object;

            return cell.isLocked() == isLocked() &&
                   cell.getValue() == getValue() &&
                   cell.getIndex() == getIndex();
        }

        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format(
            java.util.Locale.ENGLISH,
            "[value: %d, index: %d, column: %d, row: %d, block: %d]",
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
    private static boolean allowedValue(int value) {
        return value >= Constants.MIN_CELL_VALUE &&
               value <= Constants.MAX_CELL_VALUE;
    }
}