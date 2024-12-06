package src.main.java.aoc_2024;

import java.awt.*;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.System.out;
import static src.main.java.aoc_2024.Directions.Compass;


public class Day06 {
    public static final String PART1_ANSWER = "4814";
    public static final String PART2_ANSWER = "5448";
    private static char[][] grid;
    private static Point maxes;
    private static Point guard_start;
    private static Compass guard_init_dir;

    private static char EMPTY='.';
    private static char BLOCK='#';


    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.println("\tDay  6");

        String[] answers = {"", ""};
        parseInput(inputString);
        answers[0] = getPart1();
        answers[1] = getPart2();

        if (!answers[0].equals(PART1_ANSWER)) {
            out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[0], PART1_ANSWER);
        }

        if (!answers[1].equals(PART2_ANSWER)) {
            out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[1], PART2_ANSWER);
        }
        return answers;
    }


    public static String getPart1() {
        final char VISITED='X';
        HashSet<Point> visited = new HashSet<>();




        int answer = 1;
        return Integer.toString(answer);
    }


    public static String getPart2() {
        int answer = 2;
        return Integer.toString(answer);
    }

    private static void parseInput(String filename) throws IOException {
        char[][] input_grid = Files.readAllLines(Path.of(filename)).stream().map(String::toCharArray).toList().toArray(new char[0][0]);
        int x_max = input_grid[0].length - 1;
        int y_max = input_grid.length - 1;
        grid = new char[x_max+1][y_max+1];
        maxes = new Point(x_max,y_max);

        for(int y =0; y <= maxes.y; y++) {
            for(int x =0; x <= maxes.x; x++){
                char ch = input_grid[y][x];

                if (ch == '^') {
                    guard_start = new Point(x,y);
                    guard_init_dir = Compass.NORTH;
                    ch = EMPTY;
                }
                grid[x][y] = ch;
                out.print(grid[x][y]);
            }
            out.println();
        }


    }
}
