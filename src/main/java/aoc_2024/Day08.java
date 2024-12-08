package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import static java.lang.System.out;

public class Day08 {
    public static final String PART1_ANSWER = "4814";
    public static final String PART2_ANSWER = "5448";
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
        HashMap<Character, List<Vector2d>> antennas = new HashMap<>();

        for (int y = 0; y < max.y; y++) {
            for (int x = 0; x < max.x; x++) {
                char ch = grid[x][y];
                if (Character.isLetterOrDigit(ch)) {
                    List<Vector2d> ant_list;
                    if (antennas.containsKey(ch)) {
                        ant_list = antennas.get(ch);
                    } else {
                        ant_list = new ArrayList<>();
                        antennas.put(ch, ant_list);
                    }
                    ant_list.add(new Vector2d(x, y));
                }
            }
        }
        out.printf("found %d antennas\n", antennas.size());
        for (char id : antennas.keySet()) {
            List<Vector2d> ant_list = antennas.get(id);
            out.printf("freq %c has %d stations\n", id, ant_list.size());
            List<Vector2d[]> pp = Vector2d.getAllAdjacentPairs(ant_list);

            for (Vector2d[] v : pp) {
                out.println(Arrays.toString(v));
            }
            out.println();
        }
        List<Vector2d> all_antinodes = new ArrayList<>();
        AoCUtils.printGrid(grid);     out.println("====================================");

        HashSet<Vector2d> antinodes = new HashSet<>();
        for (char id : antennas.keySet()) {

            List<Vector2d> ant_list = antennas.get(id);
            List<Vector2d[]> pp = Vector2d.getAllAdjacentPairs(ant_list);

            for (Vector2d[] v : pp) {
                Vector2d[] antinodes_pair = v[0].antinodes(v[1]);
                for(Vector2d vv: antinodes_pair) {

                    antinodes.add(vv);
                }

            }
            out.println();
            all_antinodes.addAll(antinodes);
            antinodes = new HashSet<>();
        }
        for(Vector2d v: all_antinodes) {
            try {
                grid[v.x][v.y] = '#';
            } catch (ArrayIndexOutOfBoundsException e) {
                continue;
            }
        }




        AoCUtils.printGrid(grid);
        out.println("====================================");


        int answer = all_antinodes.size();
        return Integer.toString(answer);
    }


    public static String getPart2() {


        int answer = 2;
        return Integer.toString(answer);
    }



}