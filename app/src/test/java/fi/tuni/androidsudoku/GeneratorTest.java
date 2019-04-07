package fi.tuni.androidsudoku;

import fi.tuni.androidsudoku.sudoku.Solution;
import fi.tuni.androidsudoku.sudoku.SudokuPuzzle;
import org.junit.Test;

import static org.junit.Assert.*;

public class GeneratorTest {
    @Test
    public void PuzzleTimeTest() {
        long startTime = System.currentTimeMillis();
        Solution solution = new Solution();
        float solutionTime = (System.currentTimeMillis() - startTime) / 1000f;

        System.out.printf("Generated solution in: %.3fs%nSolution: %s%n", solutionTime, solution);

        startTime = System.currentTimeMillis();
        SudokuPuzzle puzzle = new SudokuPuzzle(solution, SudokuPuzzle.Difficulty.VERY_HARD);
        float puzzleTime = (System.currentTimeMillis() - startTime) / 1000f;

        System.out.printf("Generated puzzle in: %.3fs%nPuzzle: %s%n", puzzleTime, puzzle);
    }
}