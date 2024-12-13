package src.main.java.aoc_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                System.out.print(grid[x][y]);
            }
            System.out.println();
        }
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

}
