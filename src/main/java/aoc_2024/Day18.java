package src.main.java.aoc_2024;


import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day18 {

    public static final String PART1_ANSWER = "438";
    public static final String PART2_ANSWER = "26,22";
    private static Vector2d[] drops;
    private static Vector2d max;
    private static final Vector2d GRID_SIZE = AdventOfCode2024.TESTING ? new Vector2d(7, 7) : new Vector2d(71, 71);
    private static final Vector2d MAP_START = new Vector2d(0, 0);
    private static final Vector2d MAP_END = new Vector2d(GRID_SIZE.x - 1, GRID_SIZE.y - 1);
    private static final int PART1_NUMBER_OF_BYTES = AdventOfCode2024.TESTING ? 12 : 1024;

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

    protected static String getPart1() {


        HashSet<Vector2d> corrupted_points = new HashSet<>();
        corrupted_points.addAll(Arrays.asList(drops).subList(0, PART1_NUMBER_OF_BYTES));
        HashMap<Vector2d, Integer> distances_to_end = getAllDistancesStartingFromWithBadSet(MAP_END, corrupted_points);

        long answer = distances_to_end.get(MAP_START);
        return String.valueOf(answer);
    }

    protected static String getPart2() {
        // binary search for highest m that has a path
        int low = 0;
        int high = drops.length - 1;
        int mid = -1;
        while (low <= high) {
            mid = low + (high - low) / 2;
            HashMap<Vector2d, Integer> distances_to_end = getAllDistancesStartingDropList(MAP_END, mid);
            if (!distances_to_end.containsKey(MAP_START)) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        Vector2d last_drop_point = drops[mid - 1];
        String answer = String.format("%d,%d", last_drop_point.x, last_drop_point.y);
        return answer;
    }

    protected static void parseInput(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filename));
        drops = new Vector2d[lines.size()];
        int idx = 0;
        max = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (String ln : lines) {
            String[] parts = ln.trim().split(",");
            int x = Integer.valueOf(parts[0]);
            int y = Integer.valueOf(parts[1]);
            drops[idx] = new Vector2d(x, y);
            max.x = Math.max(max.x, x);
            max.y = Math.max(max.y, y);
            idx++;
        }
    }

    private static HashMap<Vector2d, Integer> getAllDistancesStartingDropList(Vector2d start, int drop_count) {
        HashSet<Vector2d> bad_spots = new HashSet<>();
        if (drop_count >= drops.length) {
            throw new IllegalArgumentException(String.format("told to drops %d from array of length %d", drop_count, drops.length));
        }
        for (int i = 0; i < drop_count; i++) {
            bad_spots.add(drops[i]);
        }


        PriorityQueue<Vector2d> work_queue = new PriorityQueue<>();
        work_queue.offer(start);
        HashMap<Vector2d, Integer> distances = new HashMap<>();
        distances.put(start, 0);
        assert (GRID_SIZE.x == GRID_SIZE.y);
        while (!work_queue.isEmpty()) {
            Vector2d current = work_queue.poll();
            int dist = distances.get(current);
            List<Vector2d> neighbors = Directions.Compass.getNeighborsClamped(current, 0, GRID_SIZE.x - 1);
            for (Vector2d v : neighbors) {

                if (!bad_spots.contains(v)) {
                    int n_dist = dist + 1;
                    if (n_dist < distances.getOrDefault(v, Integer.MAX_VALUE)) {
                        distances.put(v, n_dist);
                        work_queue.offer(v);
                    }
                }
            }
        }
        return distances;
    }

    private static HashMap<Vector2d, Integer> getAllDistancesStartingFrom(Vector2d start, char[][] grid) {
        PriorityQueue<Vector2d> work_queue = new PriorityQueue<>();
        work_queue.offer(start);
        HashMap<Vector2d, Integer> distances = new HashMap<>();
        distances.put(start, 0);
        assert (GRID_SIZE.x == GRID_SIZE.y);
        while (!work_queue.isEmpty()) {
            Vector2d current = work_queue.poll();
            int dist = distances.get(current);
            List<Vector2d> neighbors = Directions.Compass.getNeighborsClamped(current, 0, GRID_SIZE.x - 1);
            for (Vector2d v : neighbors) {
                char ch = grid[v.y][v.x];
                if (ch == '.') {
                    int n_dist = dist + 1;
                    if (n_dist < distances.getOrDefault(v, Integer.MAX_VALUE)) {
                        distances.put(v, n_dist);
                        work_queue.offer(v);
                    }
                }
            }
        }
        return distances;
    }

    private static HashMap<Vector2d, Integer> getAllDistancesStartingFromWithBadSet(Vector2d start, HashSet<Vector2d> bad_points) {
        PriorityQueue<Vector2d> work_queue = new PriorityQueue<>();
        work_queue.offer(start);
        HashMap<Vector2d, Integer> distances = new HashMap<>();
        distances.put(start, 0);
        assert (GRID_SIZE.x == GRID_SIZE.y);
        while (!work_queue.isEmpty()) {
            Vector2d current = work_queue.poll();
            int dist = distances.get(current);
            List<Vector2d> neighbors = Directions.Compass.getNeighborsClamped(current, 0, GRID_SIZE.x - 1);
            for (Vector2d v : neighbors) {

                if (!bad_points.contains(v)) {
                    int n_dist = dist + 1;
                    if (n_dist < distances.getOrDefault(v, Integer.MAX_VALUE)) {
                        distances.put(v, n_dist);
                        work_queue.offer(v);
                    }
                }
            }
        }
        return distances;
    }

    private static HashSet<Vector2d> getPathSet(Vector2d current, Vector2d goal, HashMap<Vector2d, Integer> distance_to_end) {
        HashSet<Vector2d> path_set = new HashSet<>();
        path_set.add(current);
        int last_step_distance = distance_to_end.get(current);
        while (!current.equals(goal)) {
            for (Vector2d v : Directions.Compass.getNeighborsClamped(current, 0, GRID_SIZE.x - 1)) {
                if (distance_to_end.getOrDefault(v, Integer.MAX_VALUE) == last_step_distance - 1) {
                    path_set.add(current);
                    current = v;
                    break;
                }
            }
            last_step_distance = distance_to_end.get(current);
        }
        return path_set;
    }


}