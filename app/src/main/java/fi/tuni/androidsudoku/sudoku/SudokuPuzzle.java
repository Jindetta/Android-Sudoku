package fi.tuni.androidsudoku.sudoku;

import java.util.*;

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
    private Solution solution;

    /**
     *
     */
    public SudokuPuzzle(Difficulty difficulty) {
        this(new Solution(), difficulty);
    }

    /**
     *
     * @param solution
     * @param difficulty
     */
    public SudokuPuzzle(Solution solution, Difficulty difficulty) {
        this.solution = solution;

        puzzle = solution.copySolution();
        generatePuzzle(difficulty);
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
     * @param difficulty
     */
    private void generatePuzzle(Difficulty difficulty) {
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
     */
    private Cell[] copyPuzzle() {
        Cell[] result = new Cell[puzzle.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = puzzle[i].clone();
        }

        setupNeighbourCells(result);
        return result;
    }

    /**
     *
     * @return
     */
    private boolean isUniquePuzzle() {
        Cell[] puzzle = copyPuzzle();
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
            if (puzzle[i].getValue() != solution.get(i).getValue()) {
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
     * @param format
     * @return
     */
    public String getPuzzleString(Format format) {
        StringBuilder builder = new StringBuilder();

        for (Cell cell : puzzle) {
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
     * @return
     */
    @Override
    public String toString() {
        return getPuzzleString(null);
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
    private static void setupNeighbourCells(Cell[] puzzle) {
        for (Cell cell : puzzle) {
            cell.setupNeighbours(puzzle);
        }
    }
}