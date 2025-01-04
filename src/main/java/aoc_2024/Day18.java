package src.main.java.aoc_2024;


import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.lang.System.out;

public class Day18 {

    public static final String PART1_ANSWER = "438";
    public static final String PART2_ANSWER = "26,22";


    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  18");
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
    private static final Vector2d[] drops;
    public static void parseInput(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filename));
        out.printf("read %d lines from %s\n", lines.size(), filename);
        drops = new Vector2d[lines.size()];
        int idx=0;
        for(String ln: lines) {
            String[] ln = ln.

            idx++;
        }


    }

    public static String getPart1() {
        long answer = -1;
        return String.valueOf(answer);
    }


    public static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }
}