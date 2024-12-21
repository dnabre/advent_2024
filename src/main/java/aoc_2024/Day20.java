package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.List;

import static java.lang.System.out;

public class Day20 {

    public static final String PART1_ANSWER = "-1";
    public static final String PART2_ANSWER = "-1";
    private static char[][] grid;
    private static Vector2d start;
    private static Vector2d end;
    private static final long STEP_COST = 1;

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  20");
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
        grid = AoCUtils.parseGrid(filename);
        char[][] new_grid = new char[grid.length+2][grid[0].length+2];
        for(int y=0; y< new_grid.length; y++) {
            for (int x = 0; x < new_grid[0].length; x++) {
                new_grid[y][x] = '#';
            }
        }
        for(int y=0; y< grid.length; y++) {
            for(int x=0; x < grid[0].length; x++) {
                new_grid[y+1][x+1] = grid[y][x];
            }
        }
        grid= new_grid;
        for(int y=0; y< grid.length; y++) {
            for(int x=0; x < grid[0].length; x++) {
                char ch = grid[y][x];
                if (ch == 'S') {
                    start = new Vector2d(x,y);
                }
                if(ch == 'E') {
                    end = new Vector2d(x,y);
                }
            }
        }
    }

    public record State(Vector2d pos, long time, HashSet<Vector2d> path) {}

    public static String getPart1() {
        for(int y=0; y< grid.length; y++) {
            for(int x=0; x < grid[0].length; x++) {
                out.print(grid[y][x]);
            }
            out.println();
        }
        out.printf("start: %s\n", start);
        out.printf("end  : %s\n", end);

        long picos = findFastestBetween(start,end);

        out.printf("Shortest path from %s to %s too %d\n", start,end,picos);


        long answer = -1;
        return String.valueOf(answer);
    }

    private static long findFastestBetween(Vector2d start_pos, Vector2d end_pos) {
        State start = new State(start_pos, 0, new HashSet<>());
        start.path.add(start_pos);
        ArrayDeque<State> work_queue = new ArrayDeque<>();
        work_queue.add(start);
        while(!work_queue.isEmpty()) {
            State current = work_queue.removeFirst();

            if(current.pos.equals(end_pos)) {
                return current.time;
            }

            List<Vector2d> next_to = Directions.Compass.getNeighbors(current.pos);
            for(Vector2d step: next_to) {
                char ch = grid[step.y][step.x];
                if(ch == '#') { continue; }
                if(current.path.contains(step)) { continue; }
                State new_state = new State(step,current.time + STEP_COST,new HashSet<>(current.path) );
                new_state.path.add(step);
                work_queue.addLast(new_state);
            }
        }
        return -1L;
    }


    public static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }
}