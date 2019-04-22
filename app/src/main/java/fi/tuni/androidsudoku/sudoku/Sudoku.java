package fi.tuni.androidsudoku.sudoku;

import java.util.*;

/**
 * Class for generating sudoku puzzles.
 *
 * @author  Joonas Lauhala @literal{<joonas.lauhala@tuni.fi>}
 * @version 20190422
 * @since   1.8
 */
public class Sudoku {

    /**
     * Stores puzzle.
     */
    private Cell[] puzzle;

    /**
     * Stores puzzle solution.
     */
    private Cell[] solution;

    /**
     * Overrides default constructor.
     */
    public Sudoku() {
        solution = new Cell[Constants.PUZZLE_SIZE];

        for (int i = 0; i < solution.length; i++) {
            solution[i] = new Cell(i, Constants.EMPTY_CELL_VALUE, true);
        }

        setupNeighbourCells(solution);
    }

    /**
     * Produces puzzle solution.
     */
    public void generateSolution() {
        int currentIndex = 0;

        while (currentIndex < solution.length) {
            Cell currentCell = solution[currentIndex];
            List<Integer> values = currentCell.getCandidates();

            if (values.isEmpty() || !currentCell.setRandomValue(values)) {
                currentCell.setEmpty();
                currentIndex--;
            } else {
                currentIndex++;
            }
        }

        puzzle = copy(solution);
    }

    /**
     * Generates puzzle based on difficulty.
     *
     * @param difficulty    Difficulty setting.
     */
    public void generatePuzzle(Difficulty difficulty) {
        List<Cell> filledCells = getListOfFilledCells(puzzle);
        Random random = new Random();

        while (filledCells.size() > difficulty.getClueCount()) {
            int index = random.nextInt(filledCells.size());
            Cell randomCell = filledCells.get(index);

            int value = randomCell.getValue();
            randomCell.setEmpty();

            if (randomCell.hasSingleCandidate() || isUniquePuzzle()) {
                randomCell.setLocked(false);
                filledCells.remove(index);
            } else {
                randomCell.setValue(value);
            }
        }
    }

    /**
     * Gets list of empty cells.
     *
     * @param puzzle    Puzzle to read.
     * @return          List of empty cells.
     */
    private List<Cell> getListOfEmptyCells(Cell[] puzzle) {
        List<Cell> results = new ArrayList<>();

        for (Cell cell : puzzle) {
            if (cell.isEmpty()) {
                results.add(cell);
            }
        }

        return results;
    }

    /**
     * Gets list of filled cells.
     *
     * @param puzzle    Puzzle to read.
     * @return          List of filled cells.
     */
    private List<Cell> getListOfFilledCells(Cell[] puzzle) {
        List<Cell> results = new ArrayList<>(puzzle.length);

        for (Cell cell : puzzle) {
            if (!cell.isEmpty()) {
                results.add(cell);
            }
        }

        return results;
    }

    /**
     * Checks if puzzle is unique.
     *
     * @return  True if puzzle is unique, false otherwise.
     */
    private boolean isUniquePuzzle() {
        Cell[] puzzle = copy(this.puzzle);
        List<Cell> cells = getListOfEmptyCells(puzzle);
        int currentIndex = 0;

        while (currentIndex < cells.size()) {
            Cell cell = cells.get(currentIndex);

            if (!cell.setNextValue()) {
                cell.setEmpty();
                currentIndex--;
            } else {
                currentIndex++;
            }
        }

        return verifyPuzzle(puzzle);
    }

    /**
     * Verifies validity of puzzle.
     *
     * @param puzzle    Puzzle to verify.
     * @return          True if puzzle is valid, false otherwise.
     */
    private boolean verifyPuzzle(Cell[] puzzle) {
        for (int i = 0; i < puzzle.length; i++) {
            if (puzzle[i].getValue() != solution[i].getValue()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if puzzle is complete.
     *
     * @return  True if puzzle is complete, false otherwise.
     */
    public boolean puzzleIsComplete() {
        for (int i = 0; i < Constants.GROUP_SIZE; i++) {
            int verificationValue = 0;

            for (int j = 0; j < Constants.GROUP_SIZE; j++) {
                verificationValue += Math.pow(2, puzzle[i * Constants.GROUP_SIZE + j].getValue() - 1)
                                   + Math.pow(2, puzzle[j * Constants.GROUP_SIZE + i].getValue() - 1);
            }

            if (verificationValue != Constants.PUZZLE_VERIFY_VALUE) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gets cell information.
     *
     * @param index Index of cell.
     * @return      Cell data.
     */
    public Cell getCellInfo(int index) {
        if (index >= 0 && index < puzzle.length) {
            return puzzle[index];
        }

        return null;
    }

    /**
     * Gets puzzle as string.
     *
     * @param format    Puzzle format.
     * @return          String representation of the puzzle.
     */
    public String getPuzzleString(Format format) {
        return getStringValue(puzzle, format);
    }

    /**
     * Gets solution as string.
     *
     * @return          String representation of the solution.
     */
    public String getSolutionString() {
        return getStringValue(solution, null);
    }

    /**
     * Outputs string with puzzle and solution.
     *
     * @return String representation of this class.
     */
    @Override
    public String toString() {
        return String.format(
            Locale.ENGLISH,
            "Solution: %s%nPuzzle: %s",
            getSolutionString(),
            getPuzzleString(null)
        );
    }

    /**
     * Enumeration for difficulties.
     */
    public enum Difficulty {
        NONE        (81),
        EASY        (40),
        MEDIUM      (36),
        HARD        (32),
        VERY_HARD   (28);

        /**
         * Stores clue count.
         */
        private int clueCount;

        /**
         * Overrides default constructor.
         *
         * @param clueCount    Clues to show.
         */
        Difficulty(int clueCount) {
            this.clueCount = clueCount;
        }

        /**
         * Gets clue count.
         *
         * @return Clue count as integer.
         */
        public int getClueCount() {
            return clueCount;
        }
    }

    /**
     * Enumeration for string formats.
     */
    public enum Format {
        USE_DOT_FILL            ('.'),
        USE_ZERO_FILL           ('0'),
        USE_DASH_FILL           ('-'),
        USE_HASH_TAG_FILL       ('#'),
        USE_QUESTION_MARK_FILL  ('?');

        /**
         * Stores fill character.
         */
        private char fillCharacter;

        /**
         * Overrides default constructor.
         *
         * @param fillCharacter Fill character to use.
         */
        Format(char fillCharacter) {
            this.fillCharacter = fillCharacter;
        }

        /**
         * Gets fill character.
         *
         * @return Fill character.
         */
        public char getFillCharacter() {
            return fillCharacter;
        }
    }

    /**
     * Copies all cells.
     */
    private static Cell[] copy(Cell[] cells) {
        Cell[] result = new Cell[cells.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = cells[i].clone();
        }

        setupNeighbourCells(result);
        return result;
    }

    /**
     * Gets string value from array of cells.
     *
     * @param cells     Array of cells.
     * @param format    String format.
     * @return          String representation of cells.
     */
    private String getStringValue(Cell[] cells, Format format) {
        StringBuilder builder = new StringBuilder();

        for (Cell cell : cells) {
            if (format != null && cell.isEmpty()) {
                builder.append(format.getFillCharacter());
            } else {
                builder.append(cell.getValue());
            }
        }

        return builder.toString();
    }

    /**
     * Sets up neighbour cells.
     */
    private static void setupNeighbourCells(Cell[] cells) {
        for (Cell cell : cells) {
            cell.setupNeighbours(cells);
        }
    }
}