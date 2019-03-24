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
        solution = new Solution();
        puzzle = solution.copySolution();

        generatePuzzle(difficulty);
    }

    /**
     *
     * @return
     */
    private List<Integer> getAvailableCells(int candidates) {
        List<Integer> results = new ArrayList<>();

        for (Cell cell : puzzle) {
            if (!cell.isEmpty() && cell.getValidValuesCount() <= candidates) {
                results.add(cell.getIndex());
            }
        }

        return results;
    }

    private int getValueCount(int value) {
        int totalIndexes = 0;

        for (Cell cell : puzzle) {
            if (cell.getValue() == value) {
                totalIndexes++;
            }
        }

        return totalIndexes;
    }

    private Cell getNextEmptyCell(Cell[] puzzle) {
        for (Cell cell : puzzle) {
            if (cell.isEmpty()) {
                return cell;
            }
        }

        return null;
    }

    private void generatePuzzle(Difficulty difficulty) {
        int cellsLeft = puzzle.length - difficulty.getCluesCount();
        Random random = new Random();

        while (cellsLeft > 0) {
            int index = random.nextInt(puzzle.length);
            Cell randomCell = puzzle[index];

            if (!randomCell.isEmpty()) {
                int value = randomCell.getValue();
                randomCell.setEmpty();

                if (isUniquePuzzle()) {
                    randomCell.setLocked(false);
                    cellsLeft--;
                } else {
                    randomCell.setValue(value);
                }
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
        Cell cell = getNextEmptyCell(puzzle);
        Stack<Cell> stack = new Stack<>();

        while (cell != null) {
            if (cell.setNextValue()) {
                stack.push(cell);

                cell = getNextEmptyCell(puzzle);
            } else if (!stack.isEmpty()) {
                cell.setEmpty();

                cell = stack.pop();
            }
        }

        return verifyPuzzle(puzzle);
    }

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

    @Override
    public String toString() {
        return getPuzzleString(null);
    }

    public enum Difficulty {
        NONE        (81),
        EASY        (46),
        MEDIUM      (42),
        HARD        (38),
        VERY_HARD   (32);

        private int cluesCount;

        Difficulty(int cluesCount) {
            this.cluesCount = cluesCount;
        }

        public int getCluesCount() {
            return cluesCount;
        }
    }

    public enum Format {
        USE_DOT_FILL            ('.'),
        USE_ZERO_FILL           ('0'),
        USE_DASH_FILL           ('-'),
        USE_HASH_TAG_FILL       ('#'),
        USE_QUESTION_MARK_FILL  ('?');

        private char fillCharacter;

        Format(char fillCharacter) {
            this.fillCharacter = fillCharacter;
        }

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