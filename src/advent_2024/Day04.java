package advent_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day04 extends AoCDay {
    public static final String PART1_ANSWER = "2297";
    public static final String PART2_ANSWER = "1745";
    public static int[][] corner_direction = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    public static int[][] octo_directions = {{1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}};
    private static char[][] grid = null;
    private static int x_max = -1;
    private static int x_min = -1;
    private static int y_max = -1;
    private static int y_min = -1;

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        int total = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                char ch = grid[y][x];
                if (ch == 'X') {
                    total += XMasCountStartingFrom(x, y);
                }
            }
        }
        int answer = total;
        return Integer.toString(answer);
    }

    protected String getPart2() {
        int total = 0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                char ch = grid[y][x];
                if (ch == 'A') {
                    total += MasXCountStartingFrom(x, y);
                }
            }
        }
        int answer = total;
        return Integer.toString(answer);
    }

    protected void parseInput(String input_filename) throws IOException {
        grid = Files.readAllLines(Path.of(input_filename)).stream().map(String::toCharArray).toList().toArray(new char[0][0]);
        x_min = 0;
        y_min = 0;
        x_max = grid[0].length - 1;
        y_max = grid.length - 1;
    }

    private static int MasXCountStartingFrom(int x, int y) {
        int diag_count = 0;
        for (int[] ds : corner_direction) {
            String s = readString(x - ds[0], y - ds[1], ds[0], ds[1], 3);
            if (s.equals("MAS")) {
                diag_count++;
            }
        }
        return (diag_count == 2 ? 1 : 0);
    }

    private static int XMasCountStartingFrom(int x, int y) {
        int count = 0;
        for (int[] ds : octo_directions) {
            String s = readString(x, y, ds[0], ds[1], 4);
            if (s.equals("XMAS") || s.equals("SMAX")) {
                count++;
            }
        }
        return count;
    }

    private static String readString(int x, int y, int dx, int dy, int length) {
        StringBuilder sb = new StringBuilder();
        while (y >= y_min && y <= y_max && x >= x_min && x <= x_max) {
            sb.append(grid[y][x]);
            x += dx;
            y += dy;
            if (sb.length() == length) return sb.toString();
        }
        return sb.toString();
    }

    public Day04(int day) {
        super(day);
    }

}