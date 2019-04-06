package fi.tuni.androidsudoku.sudoku;

import java.util.List;

/**
 *
 */
public class Solution {

    /**
     *
     */
    private Cell[] solution;

    /**
     *
     */
    private void generateSolution() {
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
    }

    /**
     *
     */
    public Solution() {
        solution = new Cell[Constants.PUZZLE_SIZE];

        for (int i = 0; i < solution.length; i++) {
            solution[i] = new Cell(i, Constants.EMPTY_CELL_VALUE, true);
        }

        setupNeighbourCells(solution);
        generateSolution();
    }

    /**
     *
     */
    public Cell[] copySolution() {
        Cell[] result = new Cell[solution.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = solution[i].clone();
        }

        setupNeighbourCells(result);
        return result;
    }

    /**
     *
     * @param index
     * @return
     */
    public Cell get(int index) {
        return solution[index];
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (Cell cell : solution) {
            builder.append(cell.getValue());
        }

        return builder.toString();
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
