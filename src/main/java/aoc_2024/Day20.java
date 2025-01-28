package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Day20 extends AoCDay {

    public static final String PART1_ANSWER = "1441";
    public static final String PART2_ANSWER = "1021490";
    private static Vector2d MAP_END;
    private static Vector2d MAP_START;
    private static HashMap<Vector2d, Integer> ToEnd;
    private static char[][] grid;
    private static Vector2d[] path;
    private static HashSet<Vector2d> path_set;
    private static final char WALL = '#';
    private static final int PART1_THRESHOLD = 100;
    private static final int PART2_MAX_DISTANCE = 20;
    private static final int PART1_JUMP_SIZE = 2;

    public Day20(int day) {
        super(day);
    }

    public static String[] runDayStatic(PrintStream out, String inputString) throws IOException {
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

   protected void parseInput(String filename) throws IOException {
        grid = AoCUtils.parseGrid(filename);


        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                char ch = grid[y][x];
                if (ch == 'S') {
                    MAP_START = new Vector2d(x, y);
                    grid[y][x] = '.';
                }
                if (ch == 'E') {
                    MAP_END = new Vector2d(x, y);
                    grid[y][x] = '.';
                }

            }
        }

        grid[MAP_START.y][MAP_START.x] = '.';
        grid[MAP_END.y][MAP_END.x] = '.';

        ToEnd = AoCUtils.findDistanceFromStartToEverywhere(grid, MAP_END);
        build_path();

    }

    private static Vector2d bestNextStep(Vector2d current, HashMap<Vector2d, Integer> toEnd) {
        int current_dist = toEnd.get(current);

        for (Vector2d n : Directions.Compass.getNeighbors(current)) {
            if (toEnd.getOrDefault(n, -1) == current_dist - 1) {
                return n;
            }
        }
        return null;
    }

    private static void build_path() {
        ArrayList<Vector2d> path_list = new ArrayList<>();

        Vector2d current = MAP_START;
        do {
            path_list.add(current);
            current = bestNextStep(current, ToEnd);
        } while ((current != null) && (!current.equals(MAP_END)));
        path_list.add(MAP_END);
        path = path_list.toArray(new Vector2d[0]);
        path_set = new HashSet<>(path_list);

    }

    private static int getAllCheatWithMax() {
        int good_cheats = 0;
        for (int i = 0; i < path.length; i++) {
            Vector2d start = path[i];
            int no_cheat_to_end = ToEnd.get(start);
            for (int j = i + 1; j < path.length; j++) {
                Vector2d landing = path[j];
                int l1 = start.manhattan(landing);
                if ((0 < l1) && (l1 <= PART2_MAX_DISTANCE)) {
                    int cheat_distance = l1 + ToEnd.get(landing);
                    int saved = no_cheat_to_end - cheat_distance;
                    if (saved >= PART1_THRESHOLD) {
                        good_cheats++;
                    }
                }
            }
        }
        return good_cheats;
    }

    private static int getAllCheatsFromPath() {
        int good_cheat_count = 0;
        for (Vector2d p : path) {
            int no_cheat_distance = ToEnd.get(p);
            if (no_cheat_distance > PART1_THRESHOLD) {
                for (Vector2d jump_through : Directions.Compass.getNeighbors(p)) {
                    if (grid[jump_through.y][jump_through.x] == WALL) {
                        for (Vector2d landing : Directions.Compass.getNeighbors(jump_through)) {
                            if (path_set.contains(landing) && !landing.equals(p)) {
                                int cheat_distance = PART1_JUMP_SIZE + ToEnd.get(landing);
                                int saved = no_cheat_distance - cheat_distance;
                                if (saved >= PART1_THRESHOLD) {
                                    good_cheat_count++;
                                }
                            }
                        }
                    }
                }
            }
        }
        return good_cheat_count;
    }

    protected String getPart1() {
        long answer = getAllCheatsFromPath();
        return String.valueOf(answer);
    }

    protected String getPart2() {
        int answer = getAllCheatWithMax();
        return String.valueOf(answer);
    }
}