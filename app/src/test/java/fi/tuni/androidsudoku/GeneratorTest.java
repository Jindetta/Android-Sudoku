package fi.tuni.androidsudoku;

import fi.tuni.androidsudoku.sudoku.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GeneratorTest {

    /**
     *
     */
    private Sudoku sudoku;

    @Before
    public void SolutionTimeTest() {
        sudoku = new Sudoku();

        long startTime = System.currentTimeMillis();
        sudoku.generateSolution();
        float completeTime = (System.currentTimeMillis() - startTime) / 1000f;

        int[] solutionValues = sudoku.getSolutionValues();

        assertTrue(completeTime < 0.025f);
        assertEquals(Constants.PUZZLE_SIZE, solutionValues.length);
        assertTrue(isValidSudokuPuzzle(solutionValues));

        System.out.printf("Generated sudoku\t(%.3fs): %s%n", completeTime, sudoku.getSolutionString());
    }

    @Test
    public void PuzzleTimeTest() {
        long startTime = System.currentTimeMillis();
        sudoku.generatePuzzle(Sudoku.Difficulty.VERY_HARD);
        float completeTime = (System.currentTimeMillis() - startTime) / 1000f;

        System.out.printf("Generated puzzle\t(%.3fs): %s%n", completeTime, sudoku.getPuzzleString(null));
    }

    /**
     *
     * @param values
     * @return
     */
    private static boolean isValidSudokuPuzzle(int[] values) {
        for (int i = 0, rowValue = 0, colValue = 0; i < Constants.GROUP_SIZE; i += 9) {
            for (int j = 0; j < Constants.GROUP_SIZE; j++) {
                rowValue += Math.pow(2, values[i * Constants.GROUP_SIZE + j] - 1);
                colValue += Math.pow(2, values[j * Constants.GROUP_SIZE + i] - 1);
            }

            if (rowValue != Constants.PUZZLE_VERIFY_VALUE || colValue != Constants.PUZZLE_VERIFY_VALUE) {
                return false;
            }
        }

        return true;
    }
}