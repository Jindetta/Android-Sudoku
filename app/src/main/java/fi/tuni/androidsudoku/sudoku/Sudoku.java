package fi.tuni.androidsudoku.sudoku;

import java.util.*;

/**
 *
 */
public class Sudoku {

    /**
     *
     */
    private Cell[] puzzle;

    /**
     *
     */
    private Cell[] solution;

    /**
     *
     */
    public Sudoku() {
        solution = new Cell[Constants.PUZZLE_SIZE];

        for (int i = 0; i < solution.length; i++) {
            solution[i] = new Cell(i, Constants.EMPTY_CELL_VALUE, true);
        }

        setupNeighbourCells(solution);
    }

    /**
     *
     */
    public void generateSolution() {
        int currentIndex = 0;

        while (currentIndex < solution.length) {
            Cell currentCell = solution[currentIndex];
            List<Integer> values = currentCell.getValidValues();

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
     *
     * @param difficulty
     */
    public void generatePuzzle(Difficulty difficulty) {
        List<Cell> filledCells = getListOfFilledCells(puzzle);
        Random random = new Random();

        while (filledCells.size() > difficulty.getCluesCount()) {
            int index = random.nextInt(filledCells.size());
            Cell randomCell = filledCells.get(index);

            int value = randomCell.getValue();
            randomCell.setEmpty();

            if (randomCell.hasSingleValidValue() || isUniquePuzzle()) {
                randomCell.setLocked(false);
                filledCells.remove(index);
            } else {
                randomCell.setValue(value);
            }
        }
    }

    /**
     *
     * @param puzzle
     * @return
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
     *
     * @param puzzle
     * @return
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
     *
     * @return
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
     *
     * @param puzzle
     * @return
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
     *
     * @return
     */
    public boolean puzzleIsComplete() {
        for (Cell cell : puzzle) {
            if (cell.isEmpty() || !cell.hasSingleValidValue()) {
                return false;
            }
        }

        return true;
    }

    /**
     *
     * @param index
     * @return
     */
    public Cell getCellInfo(int index) {
        if (index >= 0 && index < puzzle.length) {
            return puzzle[index];
        }

        return null;
    }

    /**
     *
     * @return
     */
    public int[] getSolutionValues() {
        int[] result = new int[solution.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = solution[i].getValue();
        }

        return result;
    }

    /**
     *
     * @param format
     * @return
     */
    public String getPuzzleString(Format format) {
        return getStringValue(puzzle, format);
    }

    /**
     *
     * @return
     */
    public String getSolutionString() {
        return getStringValue(solution, null);
    }

    /**
     *
     * @return
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
     *
     */
    public enum Difficulty {
        NONE        (81),
        EASY        (44),
        MEDIUM      (39),
        HARD        (36),
        VERY_HARD   (28);

        /**
         *
         */
        private int cluesCount;

        /**
         *
         * @param cluesCount
         */
        Difficulty(int cluesCount) {
            this.cluesCount = cluesCount;
        }

        /**
         *
         * @return
         */
        public int getCluesCount() {
            return cluesCount;
        }
    }

    /**
     *
     */
    public enum Format {
        USE_DOT_FILL            ('.'),
        USE_ZERO_FILL           ('0'),
        USE_DASH_FILL           ('-'),
        USE_HASH_TAG_FILL       ('#'),
        USE_QUESTION_MARK_FILL  ('?');

        /**
         *
         */
        private char fillCharacter;

        /**
         *
         * @param fillCharacter
         */
        Format(char fillCharacter) {
            this.fillCharacter = fillCharacter;
        }

        /**
         *
         * @return
         */
        public char getFillCharacter() {
            return fillCharacter;
        }
    }

    /**
     *
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
     *
     * @param cells
     * @param format
     * @return
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
     *
     */
    private static void setupNeighbourCells(Cell[] cells) {
        for (Cell cell : cells) {
            cell.setupNeighbours(cells);
        }
    }
}