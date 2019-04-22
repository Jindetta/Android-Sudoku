package fi.tuni.androidsudoku.sudoku;

import java.util.ArrayList;
import java.util.List;

/**
 * Class containing cell information.
 *
 * @author  Joonas Lauhala @literal{<joonas.lauhala@tuni.fi>}
 * @version 20190422
 * @since   1.8
 */
public class Cell implements Cloneable {

    /**
     * Stores cell index.
     */
    private int index;

    /**
     * Stores cell value.
     */
    private int value;

    /**
     * Stores cell lock state.
     */
    private boolean locked;

    /**
     * Stores neighbour cells.
     */
    private List<Cell> neighbours;

    /**
     * Overrides default constructor.
     *
     * @param index     Cell index.
     * @param value     Cell value.
     * @param locked    Cell lock state.
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
     * Sets up neighbour cells.
     *
     * @param puzzle    Puzzle information.
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
     * Checks if cell is neighbour.
     *
     * @param cell  Cell to check.
     * @return      True if cell is neighbour, false otherwise.
     */
    public boolean isNeighbourCell(Cell cell) {
        if (neighbours != null && cell != null) {
            int index = cell.getIndex();

            for (Cell neighbour : neighbours) {
                if (neighbour.getIndex() == index) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks if cell is neighbour.
     *
     * @param cell  Cell to check.
     * @return      True if cell is neighbour, false otherwise.
     */
    private boolean isNeighbour(Cell cell) {
        return neighbours != null && neighbours.contains(cell);
    }

    /**
     * Checks if value is valid.
     *
     * @param value Value to check.
     * @return      True if value is valid, false otherwise.
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
     * Gets all valid values for this cell.
     *
     * @return  List of integer values.
     */
    public List<Integer> getCandidates() {
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
     * Checks if cell has single valid value.
     *
     * @return True if cell has single valid value, false otherwise.
     */
    public boolean hasSingleCandidate() {
        int validValues = 0;

        for (int value : Constants.ALLOWED_VALUES) {
            if (isValidCellValue(value) && ++validValues != 1) {
                break;
            }
        }

        return validValues == 1;
    }

    /**
     * Gets cell index.
     *
     * @return Cell index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets cell index.
     *
     * @param index Index to set.
     */
    private void setIndex(int index) {
        if (index < 0 || index >= Constants.PUZZLE_SIZE) {
            throw new IllegalArgumentException("Cell index cannot be out-of-bounds.");
        }

        this.index = index;
    }

    /**
     * Gets cell value.
     *
     * @return Cell value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets cell value to next valid value.
     *
     * @return True if set successfully, false otherwise.
     */
    public boolean setNextValue() {
        List<Integer> values = getCandidates();
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
     * Sets cell value.
     *
     * @param value Value to set.
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
     * Sets random cell value.
     *
     * @param values    List of random values.
     * @return          True if value was set, false otherwise.
     */
    public boolean setRandomValue(List<Integer> values) {
        int randomIndex = (int) (Math.random() * values.size());

        return setValue(values.get(randomIndex));
    }

    /**
     * Forces value to cell.
     *
     * @param value Value to set.
     */
    public void forceValue(int value) {
        this.value = value;
    }

    /**
     * Checks if cell is locked.
     *
     * @return True if cell is locked, false otherwise.
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Sets cell lock state.
     *
     * @param locked Lock state.
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Checks if cell has no value.
     *
     * @return True if cell is empty, false otherwise.
     */
    public boolean isEmpty() {
        return value == Constants.EMPTY_CELL_VALUE;
    }

    /**
     * Clears cell value.
     */
    public void setEmpty() {
        value = Constants.EMPTY_CELL_VALUE;
    }

    /**
     * Gets block index.
     *
     * @return Block index.
     */
    public int getBlock() {
        int row = index / (Constants.GROUP_SIZE * Constants.MULTIPLIER);
        return row * Constants.MULTIPLIER + index / Constants.MULTIPLIER % Constants.MULTIPLIER;
    }

    /**
     * Gets column index.
     *
     * @return Column index.
     */
    public int getColumn() {
        return index % Constants.GROUP_SIZE;
    }

    /**
     * Gets row index.
     *
     * @return Row index.
     */
    public int getRow() {
        return index / Constants.GROUP_SIZE;
    }

    /**
     * Returns notes assigned to this cell.
     *
     * @return String of notes.
     */
    public String getNotes() {
        StringBuilder notes = new StringBuilder();
        final List<Integer> values = getCandidates();

        for (Integer number : Constants.ALLOWED_VALUES) {
            notes.append(values.contains(number) ? number : "\u2000");

            if (number < Constants.GROUP_SIZE) {
                notes.append(number % Constants.MULTIPLIER == 0 ? "\n" : "\u2000");
            }
        }

        return notes.toString();
    }

    /**
     * Clones this cell.
     *
     * @return Clones copy of this cell.
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
     * Checks if cell is equal to given object.
     *
     * @param object    Object to check.
     * @return          True if equal, false otherwise.
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
     * Returns string representation of this cell.
     *
     * @return Cell data as String value.
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
     * Checks if value is within allowed range.
     *
     * @param value Value to check.
     * @return      True if within range, false otherwise.
     */
    private static boolean allowedValue(int value) {
        return value >= Constants.MIN_CELL_VALUE &&
               value <= Constants.MAX_CELL_VALUE;
    }
}