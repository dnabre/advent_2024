package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class Day08 {

    public static final String PART1_ANSWER = "261"; // 340 is too high
    public static final String PART2_ANSWER = "898";
    private static char[][] grid;
    private static Vector2d max;

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.println("\tDay  8");

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

    private static void parseInput(String file_name) throws IOException {
        grid = AoCUtils.parseGrid(file_name);
        int max_x = grid.length;
        int max_y = grid[0].length;
        max = new Vector2d(max_x, max_y);
    }

    public static String getPart1() {

        HashSet<Vector2d> antinodes = new HashSet<>();
        HashMap<Character, ArrayList<Vector2d>> antennas = getAntennaList(grid);
        for (ArrayList<Vector2d> ant_list : antennas.values()) {

            for (int i = 0; i < ant_list.size(); i++) {
                int x = ant_list.get(i).x;
                int y = ant_list.get(i).y;

                for (int j = i + 1; j < ant_list.size(); j++) {
                    int compare_x = ant_list.get(j).x;
                    int compare_y = ant_list.get(j).y;

                    int xDiff = x - compare_x;
                    int yDiff = y - compare_y;

                    int antinode_1x = x + xDiff;
                    int antinode_1y = y + yDiff;

                    int antinode_2x = compare_x - xDiff;
                    int antinode_2y = compare_y - yDiff;

                    if (antinode_1x < max.x && antinode_1x >= 0 && antinode_1y < max.y && antinode_1y >= 0) {
                        antinodes.add(new Vector2d(antinode_1x,antinode_1y));
                    }

                    if (antinode_2x < max.x && antinode_2x >= 0 && antinode_2y < max.y && antinode_2y >= 0) {
                        antinodes.add(new Vector2d(antinode_2x, antinode_2y));
                    }
                }
            }
        }

        int answer = antinodes.size();
        return Integer.toString(answer);
    }
    public static String getPart2() {

        HashSet<Vector2d> antinodes = new HashSet<>();
        HashMap<Character, ArrayList<Vector2d>> antennas = getAntennaList(grid);
        for (ArrayList<Vector2d> ant_list : antennas.values()) {

            for (int i = 0; i < ant_list.size(); i++) {
                int x = ant_list.get(i).x;
                int y = ant_list.get(i).y;

                for (int j = i + 1; j < ant_list.size(); j++) {
                    int compareX = ant_list.get(j).x;
                    int compareY = ant_list.get(j).y;

                    int xDiff = x - compareX;
                    int yDiff = y - compareY;

                    int antinode_1X = x + xDiff;
                    int antinode_1Y = y + yDiff;

                    int antinode_2X = compareX - xDiff;
                    int antinode_2Y = compareY - yDiff;

                    while (antinode_1X < max.x && antinode_1X >= 0 && antinode_1Y < max.y && antinode_1Y >= 0) {
                        antinodes.add(new Vector2d(antinode_1X,antinode_1Y));
                        antinode_1Y += yDiff;
                        antinode_1X += xDiff;
                    }

                    while (antinode_2X < max.x && antinode_2X >= 0 && antinode_2Y < max.y && antinode_2Y >= 0) {
                        antinodes.add(new Vector2d(antinode_2X, antinode_2Y));
                        antinode_2Y -= yDiff;
                        antinode_2X -= xDiff;
                    }
                    antinodes.add(new Vector2d(x,y));
                    antinodes.add(new Vector2d(compareX, compareY));
                }
            }
        }

        int answer = antinodes.size();
        return Integer.toString(answer);
    }

    private static HashMap<Character, ArrayList<Vector2d>> getAntennaList(char[][] grid) {
        HashMap<Character, ArrayList<Vector2d>> antennas = new HashMap<>();
        for (int y = 0; y < max.y; y++) {
            for (int x = 0; x < max.x; x++) {
                char ch = grid[x][y];
                if (ch != '.') {
                    ArrayList<Vector2d> ant_list = antennas.getOrDefault(ch, new ArrayList<>());
                    ant_list.add(new Vector2d(x, y));
                    antennas.put(ch, ant_list);
                }
            }
        }
        return antennas;
    }


}


