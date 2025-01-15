package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.System.out;


public class Day24 {

    public static final String PART1_ANSWER = "-1";
    public static final String PART2_ANSWER = "-1";


    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  24");
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

    protected static String getPart1() {

        long answer = -1;
        return String.valueOf(answer);
    }

    protected static String getPart2() {

        long answer = -1;
        return String.valueOf(answer);
    }

    protected static void parseInput(String filename) throws IOException {

    }




}