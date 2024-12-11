package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.IntStream;

import static java.lang.System.out;


public class Day10 {

    public static final String PART1_ANSWER = "6340197768906";
    public static final String PART2_ANSWER = "6363913128533";
    private static char[] packed_disk;
    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.println("\tDay  10");

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
        String raw_input = Files.readString(Path.of(filename));
        packed_disk = raw_input.toCharArray();

    }

    public static String getPart1() {
        long answer = 1;
        return String.valueOf(answer);
    }

    public static String getPart2()  {

        long answer = 2;
        return String.valueOf(answer);
    }


}