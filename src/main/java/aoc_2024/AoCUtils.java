package src.main.java.aoc_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.lang.System.out;

public class AoCUtils {
    public static int[] WhitespaceDelimitedLineToIntegers(String ln) {
        String[] parts = ln.split("\\s+");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }

    public static void printGrid(char[][] grid) {
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                char ch = grid[x][y];
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
                //          grid[x][y] = ch;
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

    public static List<List<String>> breakDataByNewline(String data) {
        List<List<String>> g_string = new ArrayList<>();
        List<String> current = new ArrayList<>();
        for (String line : data.lines().toList()) {
            if (line.isBlank()) {
                g_string.add(current);
                current = new ArrayList<>();
            } else {
                current.add(line);
            }
        }
        if (!current.isEmpty()) {
            g_string.add(current);
        }
        return g_string;
    }

    public static ArrayList<Long> arrayToArrayList(long[] program) {
        ArrayList<Long> r = new ArrayList<>();
        for(long ll: program) {
            r.add(ll);
        }
        return r;
    }
}
