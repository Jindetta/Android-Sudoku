package fi.tuni.androidsudoku.sudoku;

/**
 *
 */
public final class Constants {

    /**
     *
     */
    public static final int GRID = 3;

    /**
     *
     */
    public static final int GRID_SET = GRID * GRID;

    /**
     *
     */
    public static final int GRID_SIZE = GRID_SET * GRID_SET;

    /**
     *
     */
    public static final int MIN_CELL_VALUE = 1;

    /**
     *
     */
    public static final int MAX_CELL_VALUE = 9;

    /**
     *
     */
    public static final int EMPTY_CELL_VALUE = 0;

    /**
     *
     */
    public static final int[] ALLOWED_VALUES = {
        1, 2, 3, 4, 5, 6, 7, 8, 9
    };

    /**
     *
     */
    private Constants() {
        // Deny instantiation
    }
}
