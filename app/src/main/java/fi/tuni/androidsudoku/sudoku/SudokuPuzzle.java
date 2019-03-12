package fi.tuni.androidsudoku.sudoku;

/**
 *
 */
public class SudokuPuzzle {

    /**
     *
     */
    private Cell[] puzzle;

    /**
     *
     */
    public SudokuPuzzle() {
        puzzle = new Cell[Constants.GRID_SIZE];

        for (int i = 0; i < puzzle.length; i++) {
            puzzle[i] = new Cell(i);
        }
    }

    /**
     *
     * @param index
     * @return
     */
    private boolean isInRange(int index) {
        return index >= 0 && index < puzzle.length;
    }

    /**
     *
     * @param index
     * @param value
     * @return
     */
    private boolean setCell(int index, int value) {
        if (isInRange(index)) {
            Cell cell = puzzle[index];

            if (!cell.isLocked()) {
                return cell.setValue(value);
            }
        }

        return false;
    }

    /**
     *
     * @param x
     * @param y
     * @param value
     * @return
     */
    private boolean setCell(int x, int y, int value) {
        return setCell(x * Constants.GRID_SET + y, value);
    }
}