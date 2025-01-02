package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

import static java.lang.System.out;
import static src.main.java.aoc_2024.Directions.Compass;

public class Day16 {

    public static final String PART1_ANSWER = "102460";
    public static final String PART2_ANSWER = "527";
    private static Vector2d MAP_END;
    private static Vector2d MAP_START;
    private static char[][] grid;
    private static Vector2d max;
    private static ArrayList<Position> positions;

    public static String getPart1() {
        Position start = new Position(MAP_START, Compass.EAST);
        HashMap<Position, Integer> distance_from_start = getAllDistancesStartingFrom(start);
        int cost = Integer.MAX_VALUE;
        for (Compass d : Compass.values()) {
            int dist = distance_from_start.get(new Position(MAP_END, d));
            cost = Math.min(cost, dist);
        }
        long answer = cost;
        return String.valueOf(answer);
    }

    public static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  16");
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

    static private final int PART1_TURN_PRICE = 1000;
    static private final int PART1_STEP_PRICE = 1;

    public record Position(Vector2d pos, Compass direction) implements Comparable<Position> {
        @Override
        public int compareTo(final Position other) {
            if (this.pos.y != other.pos.y) {
                return Integer.compare(this.pos.y, other.pos.y);
            } else {
                return Integer.compare(this.pos.x, other.pos.x);
            }
        }


    }


    private static HashMap<Position, Integer> getAllDistancesStartingFrom(Position start) {
        PriorityQueue<Position> work_queue = new PriorityQueue<>();
        work_queue.offer(start);
        HashMap<Position, Integer> distances = new HashMap<>();
        distances.put(start, 0);
        while (!work_queue.isEmpty()) {
            Position current = work_queue.poll();
            int dist = distances.get(current);

            Vector2d step_forward = current.pos.locationAfterStep(current.direction);
            if (grid[step_forward.y][step_forward.x] == '.') {
                Position in_front_of = new Position(step_forward, current.direction);
                int n_dist = dist + PART1_STEP_PRICE;
                if (n_dist < distances.getOrDefault(in_front_of, Integer.MAX_VALUE)) {
                    distances.put(in_front_of, n_dist);
                    work_queue.offer(in_front_of);
                }
            }
            Position left_turn = new Position(current.pos, current.direction.turnLeft());
            Position right_turn = new Position(current.pos, current.direction.turnRight());

            int turn_dist = dist + PART1_TURN_PRICE;
            if( turn_dist < distances.getOrDefault(left_turn, Integer.MAX_VALUE)) {
                distances.put(left_turn, turn_dist);
                work_queue.offer(left_turn);
            }
            if( turn_dist < distances.getOrDefault(right_turn, Integer.MAX_VALUE)) {
                distances.put(right_turn, turn_dist);
                work_queue.offer(right_turn);
            }
        }
        return distances;
    }


    private static void parseInput(String filename) throws IOException {
        char[][] input_grid = AoCUtils.parseGrid(filename);
        max = new Vector2d(input_grid[0].length, input_grid.length);
        grid = new char[max.y][max.x];
        positions = new ArrayList<>();
        for (int y = 0; y < max.y; y++) {
            for (int x = 0; x < max.x; x++) {
                Vector2d pos = new Vector2d(x, y);
                for (Compass d : Compass.values()) {
                    positions.add(new Position(pos, d));
                }
                char ch = input_grid[y][x];
                if (ch == 'S') {
                    MAP_START = pos;
                    ch = '.';
                } else if (ch == 'E') {
                    MAP_END = pos;
                    ch = '.';
                }
                grid[y][x] = ch;
            }
        }
    }
}