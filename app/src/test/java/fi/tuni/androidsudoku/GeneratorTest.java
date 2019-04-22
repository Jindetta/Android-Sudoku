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

        //assertTrue(completeTime < 0.025f);
        assertTrue(sudoku.puzzleIsComplete());

        System.out.printf("Generated sudoku\t(%.3fs): %s%n", completeTime, sudoku.getSolutionString());
    }

    @Test
    public void PuzzleTimeTest() {
        long startTime = System.currentTimeMillis();
        sudoku.generatePuzzle(Sudoku.Difficulty.VERY_HARD);
        float completeTime = (System.currentTimeMillis() - startTime) / 1000f;

        //assertTrue(completeTime < 0.500f);

        System.out.printf("Generated puzzle\t(%.3fs): %s%n", completeTime, sudoku.getPuzzleString(null));
    }
}