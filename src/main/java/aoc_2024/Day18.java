package src.main.java.aoc_2024;


import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.System.out;

public class Day18 {

    public static final String PART1_ANSWER = "438";
    public static final String PART2_ANSWER = "26,22";
    private static final Vector2d GRID_SIZE =
            AdventOfCode2024.TESTING?new Vector2d(7,7):new Vector2d(71,71);
private static final Vector2d MAP_START = new Vector2d(0,0);
private static final Vector2d MAP_END = new Vector2d(GRID_SIZE.x-1, GRID_SIZE.y-1);

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  18");
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
    private static  Vector2d[] drops;
    private static Vector2d max;

    private static final int PART1_NUMBER_OF_BYTES = AdventOfCode2024.TESTING?12:1024;
    public static void parseInput(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filename));
        out.printf("read %d lines from %s\n", lines.size(), filename);
        drops = new Vector2d[lines.size()];
        int idx=0;
        max = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
        for(String ln: lines) {
            String[] parts = ln.trim().split(",");
            int x = Integer.valueOf(parts[0]);
            int y =Integer.valueOf(parts[1]);
            drops[idx] = new Vector2d(x,y);
            max.x = Math.max(max.x, x);
            max.y = Math.max(max.y, y);
            idx++;
        }

        out.println(max);
        out.printf("grid size: %s\n", GRID_SIZE);
    }

    public static String getPart1() {
        char[][] grid = new char[GRID_SIZE.x][GRID_SIZE.y];
        for(int y=0; y < GRID_SIZE.y; y++) {
            for(int x=0; x< GRID_SIZE.x; x++) {
                grid[y][x] = '.';
            }
        }
        for(int i=0; i < PART1_NUMBER_OF_BYTES; i++) {
            Vector2d drop_point = drops[i];
            grid[drop_point.y][drop_point.x] = '#';
        }

        HashMap<Vector2d, Integer> distances_to_end = getAllDistancesStartingFrom(MAP_END, grid);
        int dist_end = distances_to_end.get(MAP_START);


        long answer = dist_end;
        return String.valueOf(answer);
    }


    public static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }

    private static HashMap<Vector2d, Integer> getAllDistancesStartingFrom(Vector2d start, char[][] grid) {
        PriorityQueue<Vector2d> work_queue = new PriorityQueue<>();
        work_queue.offer(start);
        HashMap<Vector2d, Integer> distances = new HashMap<>();
        distances.put(start, 0);
        assert(GRID_SIZE.x == GRID_SIZE.y);
        while (!work_queue.isEmpty()) {
            Vector2d current = work_queue.poll();
            int dist = distances.get(current);

            List<Vector2d> neighbors = Directions.Compass.getNeighborsClamped(current, 0, GRID_SIZE.x-1);
            for(Vector2d v: neighbors) {
                char ch = grid[v.y][v.x];
                if(ch == '.') {
                    int n_dist = dist + 1;
                    if(n_dist < distances.getOrDefault(v, Integer.MAX_VALUE)) {
                        distances.put(v,n_dist);
                        work_queue.offer(v);
                    }
                }
            }
        }
        return distances;
    }



}