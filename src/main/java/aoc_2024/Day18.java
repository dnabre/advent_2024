package src.main.java.aoc_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day18 extends AoCDay {

    public static final String PART1_ANSWER = "438";
    public static final String PART2_ANSWER = "26,22";
    private static final Vector2d GRID_SIZE = AdventOfCode2024.TESTING ? new Vector2d(7, 7) : new Vector2d(71, 71);
    private static final Vector2d MAP_END = new Vector2d(GRID_SIZE.x - 1, GRID_SIZE.y - 1);
    private static final Vector2d MAP_START = new Vector2d(0, 0);
    private static final int PART1_NUMBER_OF_BYTES = AdventOfCode2024.TESTING ? 12 : 1024;
    private static Vector2d[] drops;

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        HashMap<Vector2d, Integer> distances_to_end = getAllDistancesStartingDropList(PART1_NUMBER_OF_BYTES);
        long answer = distances_to_end.get(MAP_START);
        return String.valueOf(answer);
    }

    protected String getPart2() {
        // binary search for highest m that has a path
        int low = 0;
        int high = drops.length - 1;
        int mid = -1;
        while (low <= high) {
            mid = low + (high - low) / 2;
            HashMap<Vector2d, Integer> distances_to_end = getAllDistancesStartingDropList(mid);
            if (!distances_to_end.containsKey(MAP_START)) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        Vector2d last_drop_point = drops[mid - 1];
        return String.format("%d,%d", last_drop_point.x, last_drop_point.y);
    }

    protected void parseInput(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filename));
        drops = new Vector2d[lines.size()];
        int idx = 0;
        Vector2d max = new Vector2d(Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (String ln : lines) {
            String[] parts = ln.trim().split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            drops[idx] = new Vector2d(x, y);
            max.x = Math.max(max.x, x);
            max.y = Math.max(max.y, y);
            idx++;
        }
    }

    private static HashMap<Vector2d, Integer> getAllDistancesStartingDropList(int drop_count) {
        assert (drop_count < drops.length);
        HashSet<Vector2d> bad_spots = new HashSet<>(Arrays.asList(drops).subList(0, drop_count));
        PriorityQueue<Vector2d> work_queue = new PriorityQueue<>();
        work_queue.offer(MAP_END);
        HashMap<Vector2d, Integer> distances = new HashMap<>();
        distances.put(MAP_END, 0);
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

    public Day18(int day) {
        super(day);
    }

}