package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static java.lang.System.out;
import static src.main.java.aoc_2024.Directions.Compass;

public class Day15 {

    public static final String PART1_ANSWER = "1471826";
    public static final String PART2_ANSWER = "1457703";

    private static char[][] grid;
    private static int grid_width;
    private static int grid_height;
    private static Vector2d robot_start;
    private static Compass[] move_list;
    private static HashSet<Vector2d> walls;
    private static HashSet<Vector2d> inital_blocks;

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  15");
        if (AdventOfCode2024.TESTING) {
            out.print("\t (testing)");
        }
        out.println();

        String[] answers = {"", ""};
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




    public static void parseInput(String filename) throws IOException {
        String INPUT = Files.readString(Path.of(filename));

        List<List<String>> blocks  = AoCUtils.breakDataByNewline(INPUT);
        String text = String.join("\n", blocks.getFirst());
        String move = String.join("", blocks.getLast());

        List<String> lines = text.lines().toList();
        int height = lines.size();
        int width = lines.getFirst().length();
        char[][] grid = new char[height][width];
        for(int y = 0; y < height; y++) {
            String line = lines.get(y);
            grid[y] = new char[width];
            for(int x = 0; x < width; x++) {
                char ch = line.charAt(x);
                grid[y][x] = ch;
                if(ch=='@') {
                    robot_start = new Vector2d(x,y);
                }

            }
        }
        grid_width = width;
        grid_height = height;
        move_list = new Compass[move.length()];
        int idx =0;
        for(char ch: move.toCharArray()) {
            Compass dir = Compass.fromChar(ch);
            move_list[idx] = dir;
            idx++;
        }
    }

    public static String getPart1() {
        Vector2d robot_loc = new Vector2d(robot_start);
        for(int i=0; i < move_list.length; i++) {



        }
        // get GPS sum
        long answer = -1;
        return String.valueOf(answer);
    }


    public static String getPart2() {
        
        long answer = -1;
        return String.valueOf(answer);
    }
    
}


