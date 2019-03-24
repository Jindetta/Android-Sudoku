package fi.tuni.androidsudoku.sudoku;

import java.util.List;
import java.util.Random;

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
        Random randomNumber = new Random();
        int currentIndex = 0;

        while (currentIndex < solution.length) {
            Cell currentCell = solution[currentIndex];
            List<Integer> values = currentCell.getValidValues();

            if (values.isEmpty()) {
                currentIndex--;
            } else {
                int value = getRandomValue(values, randomNumber);

                if (currentCell.setValue(value)) {
                    currentIndex++;
                } else {
                    currentCell.setEmpty();
                    currentIndex--;
                }
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
     * @param values
     * @param randomGenerator
     * @return
     */
    private static int getRandomValue(List<Integer> values, Random randomGenerator) {
        return values.get(randomGenerator.nextInt(values.size()));
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
