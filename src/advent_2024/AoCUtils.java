package advent_2024;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

import static java.lang.System.out;

public class AoCUtils {
    public static final String[] ORDINALS = {"zeroth", "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth"};
    public static final String WHITESPACE_RE = "\\s+";

    public static int[] WhitespaceDelimitedLineToIntegers(String ln) {
        String[] parts = ln.split("\\s+");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }

    public static ArrayList<Long> arrayToArrayList(long[] program) {
        ArrayList<Long> r = new ArrayList<>();
        for (long ll : program) {
            r.add(ll);
        }
        return r;
    }

    public static long iPow(long a, long b) {
        long re = 1;
        while (b > 0) {
            if ((b & 1) == 1) {
                re *= a;
            }
            b >>= 1;
            a *= a;
        }
        return re;
    }

    public static char[][] parseGrid(String filename) throws IOException {
        char[][] input_grid = Files.readAllLines(Path.of(filename)).stream().map(String::toCharArray).toList().toArray(new char[0][0]);
        int max_y = input_grid.length;
        int max_x = input_grid[0].length;

        char[][] grid = new char[max_x][max_y];

        for (int y = 0; y < input_grid.length; y++) {
            for (int x = 0; x < input_grid[y].length; x++) {
                char ch = input_grid[y][x];
                grid[y][x] = ch;
            }
        }
        return grid;
    }

    public static char[][] parseGridTurn(String filename) throws IOException {
        char[][] input_grid = Files.readAllLines(Path.of(filename)).stream().map(String::toCharArray).toList().toArray(new char[0][0]);
        int max_y = input_grid.length;
        int max_x = input_grid[0].length;

        char[][] grid = new char[max_x][max_y];

        for (int y = 0; y < input_grid.length; y++) {
            for (int x = 0; x < input_grid[y].length; x++) {
                char ch = input_grid[y][x];
                //          grid[x][y] = ch;
                grid[x][y] = ch;
            }
        }
        return grid;
    }

    public static void printGrid(char[][] grid) {
        for (char[] chars : grid) {
            for (int x = 0; x < grid[0].length; x++) {
                char ch = chars[x];
                out.print(ch);
            }
            out.println();
        }
    }

    public static void printGridT(char[][] grid) {
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                char ch = grid[y][x];
                out.print(ch);
            }
            out.println();
        }
    }

    public static void printGridWithSpecial(char[][] grid, Vector2d special_loc, char special_tile) {
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                char ch = grid[x][y];
                if (special_loc.isEqual(x, y)) {
                    out.print(special_tile);
                } else {
                    out.print(ch);
                }

            }
            out.println();
        }
    }

    static HashMap<Vector2d, Integer> findDistanceFromStartToEverywhere(char[][] grid, Vector2d start) {
        PriorityQueue<Vector2d> queue = new PriorityQueue<>();
        queue.offer(start);
        HashMap<Vector2d, Integer> distances = new HashMap<>();
        distances.put(start, 0);
        while (!queue.isEmpty()) {
            Vector2d current = queue.poll();
            int dist = distances.get(current);
            List<Vector2d> neighbors =
                    Directions.Compass.getNeighborsClamped(current, 0, grid[0].length - 1);
            for (Vector2d v : neighbors) {
                if (grid[v.y][v.x] == '.') {
                    int new_dist = dist + 1;
                    if (new_dist < distances.getOrDefault(v, Integer.MAX_VALUE)) {
                        distances.put(v, new_dist);
                        queue.add(v);
                    }
                }
            }
        }
        return distances;
    }

    static public class NullStream extends OutputStream {
        @Override
        public void write(int b) {

        }
    }

    public record Pair(int left, int right) {
    }
}
