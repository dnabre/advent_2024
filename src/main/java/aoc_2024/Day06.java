package src.main.java.aoc_2024;


import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.System.out;
import static src.main.java.aoc_2024.Directions.Compass;


public class Day06 {
    public static final String PART1_ANSWER = "5067";
    public static final String PART2_ANSWER = "5448";
    private static char[][] grid;
    private static char[][] original_grid;
    private static HashSet<Vector2d> blockers;

    private static Vector2d maxes;
    private static Vector2d guard_start;
    private static Compass guard_init_dir;

    private static final char EMPTY='.';
    private static final char BLOCK='#';
    private static int x_max;
    private static int y_max;

    public static class  Guard {
        public Vector2d loc;
        public Compass heading;

        public Guard(Vector2d init_loc, Compass init_heading) {
            loc = init_loc;
            heading = init_heading;
        }

        @Override
        public String toString() {
            return String.format("Guard @ %s facing %s", loc.toString(), heading.toString());
        }
    }


    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.println("\tDay  6");

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


    public static String getPart1() {
        HashSet<Vector2d> visited = new HashSet<>();
        Guard guy = new Guard(guard_start, guard_init_dir);
        while(guard_on_map(guy)) {
            grid = original_grid.clone();
            visited.add(guy.loc);
            Vector2d next_step = step_forward_location(guy);
            if (next_step == null) {
                break;
            }
            char next_tile = next_step.fromGrid(grid);
            if (next_tile == BLOCK) {
                guy.heading = guy.heading.turnRight();
            } else {
                guy.loc = next_step;
            }
        }
        int answer = visited.size();
        return Integer.toString(answer);
    }

    private static Vector2d step_forward_location(Guard g) {
        Vector2d delta = g.heading.coordDelta();
        Vector2d new_loc = new Vector2d(g.loc);
        new_loc.add(delta);
        if (!new_loc.inside(x_max,y_max)) {
        //    out.println("step would take guard outside grid");
            return null;
        }
        return new_loc;
    }

    private static boolean guard_on_map(Guard g) {
        return g.loc.inside(x_max, y_max);

    }


    public static String getPart2() {
        HashSet<Vector2d> visited = new HashSet<>();
        HashSet<Guard> visited_states = new HashSet<>();
        Guard guy = new Guard(guard_start, guard_init_dir);
        grid = original_grid.clone();
        while (true) {
            visited.add(guy.loc);
            Vector2d next_step = step_forward_location(guy);
            if (next_step == null) {
                break;
            }
            if(blockers.contains(next_step)) {
                guy.heading = guy.heading.turnRight();
            } else {
                guy.loc = next_step;
            }
        }





        int answer = visited.size();
        return Integer.toString(answer);
    }

    private static void parseInput(String filename) throws IOException {
        char[][] input_grid = Files.readAllLines(Path.of(filename)).stream().map(String::toCharArray).toList().toArray(new char[0][0]);
        int input_x_max = input_grid[0].length - 1;
        int input_y_max = input_grid.length - 1;

        Day06.x_max = input_y_max;
        Day06.y_max = input_x_max;

        blockers = new HashSet<>();
        grid = new char[input_x_max+1][input_y_max+1];
        maxes = new Vector2d(input_x_max,input_y_max);

        for(int y =0; y <= maxes.y; y++) {
            for(int x =0; x <= maxes.x; x++){
                char ch = input_grid[y][x];
                if (ch == '^') {
                    guard_start = new Vector2d(x,y);
                    guard_init_dir = Compass.NORTH;
                    ch = EMPTY;
                } else if (ch == BLOCK) {
                    blockers.add(new Vector2d(x,y));
                }
                grid[x][y] = ch;
            }
        }
        original_grid = grid.clone();
    }

    public static void printGrid(char[][] grid) {
       out.println("--------------------------------------------------");
        for(int y =0; y <= maxes.y; y++) {
            for(int x =0; x <= maxes.x; x++){
                out.print(grid[x][y]);
            }
            out.println();
        }
        out.println("--------------------------------------------------");
    }
}
