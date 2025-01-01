package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.lang.System.out;
public class Day16 {

    public static final String PART1_ANSWER = "-1";
    public static final String PART2_ANSWER = "-1";
    private static Vector2d MAP_END;
    private static Vector2d MAP_START;

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  16");
        if (AdventOfCode2024.TESTING) {
            out.print("\t (testing)");
        }
        out.println();

        String[] answers = {"", ""};
        String INPUT = Files.readString(Path.of(inputString));

        parseInput(inputString);
        answers[0] = getPart1();
        answers[1] = getPart2();

        if (!AdventOfCode2024.TESTING) {
            if (!answers[0].equals(PART1_ANSWER)) {
                out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[0], PART1_ANSWER);
            }

            if (!answers[1].equals(PART2_ANSWER)) {
                out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[1], PART2_ANSWER);
            }
        }
        return answers;
    }
    private static char[][] grid;
    private static Vector2d max;
    private static void parseInput(String filename) throws IOException {
        char[][] input_grid = AoCUtils.parseGrid(filename);

        max = new Vector2d(input_grid[0].length,input_grid.length);
        grid = new char[max.y][max.x];

        for (int y = 0; y < max.y; y++) {
            for (int x = 0; x < max.x; x++) {
                char ch = input_grid[y][x];
                out.print(ch);
                if(ch=='S') {
                    MAP_START = new Vector2d(x,y);
                    ch = '.';
                } else if (ch =='E') {
                    MAP_END = new Vector2d(x,y);
                    ch = '.';
                }
                grid[y][x] = ch;
            }
            out.println();
        }
    }

    public static String getPart1() {






        long answer = -1;
        return String.valueOf(answer);
    }


    public static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }
}