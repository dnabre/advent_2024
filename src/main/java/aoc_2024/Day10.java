package src.main.java.aoc_2024;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Day10 extends AoCDay {
    public static final String PART1_ANSWER = "737";
    public static final String PART2_ANSWER = "1619";
    private static final int MAX_HEIGHT = 9;
    private static char[][] grid;

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        List<Vector2d> starts = getStartingPositions();
        List<Vector2d> targets = getTargetPositions();
        int path_total = 0;
        for (Vector2d start : starts) {
            int found_paths = FindPathStartToPeaks(start, targets);
            path_total += found_paths;
        }
        long answer = path_total;
        return String.valueOf(answer);
    }

    protected String getPart2() {
        List<Vector2d> starts = getStartingPositions();
        List<Vector2d> targets = getTargetPositions();
        int path_total = 0;
        for (Vector2d start : starts) {
            int found_paths = FindDistinctPathStartToPeaks(start, targets);
            path_total += found_paths;
        }
        long answer = path_total;
        return String.valueOf(answer);
    }

    protected void parseInput(String filename) throws IOException {
        grid = AoCUtils.parseGrid(filename);
    }

    private static int FindDistinctPathStartToPeaks(Vector2d start_point, List<Vector2d> targets) {
        HashSet<ArrayList<Vector2d>> unique_paths = new HashSet<>();
        int min_idx = 0;
        int max_idx = grid.length - 1;
        HashSet<State2> seen = new HashSet<>();
        State2 start = new State2(start_point, new ArrayList<Vector2d>(10));
        start.path.add(start_point);
        ArrayDeque<State2> work_queue = new ArrayDeque<>();
        work_queue.addLast(start);
        while (!work_queue.isEmpty()) {
            State2 c = work_queue.removeFirst();
            if (targets.contains(c.current)) {
                unique_paths.add(c.path);
            }
            List<Vector2d> neighs = Directions.Compass.getNeighborsClamped(c.current, min_idx, max_idx);
            for (Vector2d vv : neighs) {
                if (Character.getNumericValue(grid[vv.x][vv.y]) == c.height() + 1) {
                    ArrayList<Vector2d> path = new ArrayList<>(c.path);
                    path.add(vv);
                    State2 new_state = new State2(vv, path);
                    if (!seen.contains(new_state)) {
                        seen.add(new_state);
                        work_queue.add(new_state);
                    }
                }
            }
        }
        return unique_paths.size();
    }

    private static int FindPathStartToPeaks(Vector2d start_point, List<Vector2d> targets) {
        HashSet<Vector2d> peaks_pathed_to = new HashSet<>();
        int min_idx = 0;
        int max_idx = grid.length - 1;
        HashSet<State> seen = new HashSet<>();
        State start = new State(start_point);
        ArrayDeque<State> work_queue = new ArrayDeque<>();
        work_queue.addLast(start);
        while (!work_queue.isEmpty()) {
            State c = work_queue.removeFirst();
            if (targets.contains(c.current)) {
                peaks_pathed_to.add(c.current);
            }
            List<Vector2d> neighs = Directions.Compass.getNeighborsClamped(c.current, min_idx, max_idx);
            for (Vector2d vv : neighs) {
                if (Character.getNumericValue(grid[vv.x][vv.y]) == c.height() + 1) {
                    State new_state = new State(vv);
                    if (!seen.contains(new_state)) {
                        seen.add(new_state);
                        work_queue.add(new_state);
                    }
                }
            }
        }
        return peaks_pathed_to.size();
    }

    private static List<Vector2d> getStartingPositions() {
        List<Vector2d> pos = new ArrayList<>();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                char ch = grid[x][y];
                if (ch == '0') {
                    pos.add(new Vector2d(x, y));
                }
            }
        }
        return pos;
    }

    private static List<Vector2d> getTargetPositions() {
        List<Vector2d> pos = new ArrayList<>();
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                char ch = grid[x][y];
                if (ch == '9') {
                    pos.add(new Vector2d(x, y));
                }
            }
        }
        return pos;
    }

    public Day10(int day) {
        super(day);
    }

    record State(Vector2d current) {
        public int height() {
            return Character.getNumericValue(grid[current.x][current().y]);
        }

        @Override
        public String toString() {
            return String.format("@%s, height: %d", this.current, this.height());
        }
    }

    record State2(Vector2d current, ArrayList<Vector2d> path) {
        public int height() {
            return Character.getNumericValue(grid[current.x][current().y]);
        }

        @Override
        public String toString() {
            return String.format("@%s, height: %d (S2)", this.current, this.height());
        }
    }
}