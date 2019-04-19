package fi.tuni.androidsudoku.sudoku;

/**
 * Class containing all important constants needed for puzzle generation.
 */
public final class Constants {

    /**
     * Defines grid multiplier.
     */
    public static final int MULTIPLIER = 3;

    /**
     * Defines grid group size.
     */
    public static final int GROUP_SIZE = MULTIPLIER * MULTIPLIER;

    /**
     * Defines puzzle size.
     */
    public static final int PUZZLE_SIZE = GROUP_SIZE * GROUP_SIZE;

    /**
     * Defines puzzle verification value.
     */
    public static final int PUZZLE_VERIFY_VALUE = 511;

    /**
     * Defines minimum allowed value.
     */
    public static final int MIN_CELL_VALUE = 1;

    /**
     * Defines maximum allowed value.
     */
    public static final int MAX_CELL_VALUE = 9;

    /**
     * Defines empty cell value.
     */
    public static final int EMPTY_CELL_VALUE = 0;

    /**
     * Defines all allowed values.
     */
    public static final int[] ALLOWED_VALUES = {
        1, 2, 3, 4, 5, 6, 7, 8, 9
    };

    /**
     * Overrides default constructor.
     */
    private Constants() {
        // Deny instantiation
    }
}
