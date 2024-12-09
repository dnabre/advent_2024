package src.main.java.aoc_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
    public static void printGrid(char[][] grid){
        for(int y=0; y < grid.length; y++) {
            for(int x=0; x < grid[0].length; x++) {
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

    public static  List<Vector2d[]> getAllPairingsVector2d(List<Vector2d> antList) {
        List<Vector2d[]> ls = new ArrayList<>();
        Vector2d[] ant_array =  antList.toArray(new Vector2d[0]);
        for(int a=0; a < ant_array.length; a++) {
            for(int b = a+1; b< ant_array.length; b++) {
              Vector2d[] new_pair = new Vector2d[2];
              new_pair[0] = ant_array[a];
              new_pair[1] = ant_array[b];
              ls.add(new_pair);
            }
        }
           return ls;

    }
}




