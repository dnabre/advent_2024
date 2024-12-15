package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;


public class Day14 {

    public static final String PART1_ANSWER = "214109808";
    public static final String PART2_ANSWER = "7687";

    public static final int PART1_TICKS = 100;
    record Robot(Vector2d pos, Vector2d velocity) {
        @Override
        public String toString() {
            return String.format("Robot: p=%s, v=%s", pos,velocity);
        }
        public Robot elapsed(int time, Vector2d map_size) {
            int pos_x = (pos.x + velocity.x * time) % map_size.x;
            int pos_y = (pos.y + velocity.y * time) % map_size.y;
            if (pos_x < 0) {
                pos_x = pos_x + map_size.x;
            }
            if (pos_y <0) {
                pos_y = pos_y + map_size.y;
            }

            return new Robot(new Vector2d(pos_x, pos_y),velocity);
        }
    }
    private static ArrayList<Robot> robot_initial;
    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  14");
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

    public static void parseInput(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filename));
        robot_initial = new ArrayList<>();
        for(String s: lines) {
            String[] parts = s.split("\\s+");
            parts[0] = parts[0].substring(2);
            parts[1] = parts[1].substring(2);
            Vector2d p, v;
            String[] p_parts = parts[0].split(",");
            String[] v_parts = parts[1].split(",");
            p = new Vector2d(Integer.parseInt(p_parts[0]),Integer.parseInt(p_parts[1]));
            v = new Vector2d(Integer.parseInt(v_parts[0]),Integer.parseInt(v_parts[1]));
            robot_initial.add(new Robot(p,v));
        }
    }

    public static String getPart1() {
        Vector2d map_size = AdventOfCode2024.TESTING? new Vector2d(11,7):new Vector2d(101,103);
        ArrayList<Robot> new_positions = new ArrayList<>();
        for(Robot r: robot_initial) {
            Robot n_r = r.elapsed(PART1_TICKS, map_size);
            new_positions.add(n_r);
        }
/*
getCountIn(0 until mapSize.x / 2, 0 until mapSize.y / 2),
        getCountIn(mapSize.x / 2 + 1 until mapSize.x, 0 until mapSize.y / 2),
        getCountIn(0 until mapSize.x / 2, mapSize.y / 2 + 1 until mapSize.y),
        getCountIn(mapSize.x / 2 + 1 until mapSize.x, mapSize.y / 2 + 1 until mapSize.y)
    )
 */

      //  int nw_quad = countInQuad(new_positions, )


        long answer = -1;
        return String.valueOf(answer);
    }


    public static String getPart2() {

        long answer = -1;
        return String.valueOf(answer);
    }

}


