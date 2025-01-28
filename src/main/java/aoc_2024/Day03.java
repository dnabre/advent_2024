package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Day03 extends AoCDay {
    public static final String PART1_ANSWER = "161085926";
    public static final String PART2_ANSWER = "82045421";

    public static final String PART1_REGEX = "mul\\(\\d{1,3},\\d{1,3}\\)";
    public static final String PART2_REGEX = "mul\\(\\d{1,3},\\d{1,3}\\)|don't\\(\\)|do\\(\\)";
    private static String input;


    public Day03(int day) {
        super(day);
    }

    public static String[] runDayStatic(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.println("\tDay  3");

        parseInput(inputString);
        String[] answers = {"", ""};
        answers[0] = getPart1();
        answers[1] = getPart2();


        if (!answers[0].equals(PART1_ANSWER)) {
            out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[0], Day02.PART1_ANSWER);
        }

        if (!answers[1].equals(PART2_ANSWER)) {
            out.printf("\n\t\tWRONG ANSWER got: %s, expected %s\n", answers[1], Day02.PART2_ANSWER);
        }
        return answers;
    }

    protected static String getPart1() {


        Pattern pat = Pattern.compile(PART1_REGEX);
        Matcher matcher = pat.matcher(input);
        int total = 0;

        while (matcher.find()) {
            String mg = matcher.group();
            String numbers = mg.substring(4, mg.length() - 1);
            String[] parts = numbers.split(",");
            int left = Integer.parseInt(parts[0]);
            int right = Integer.parseInt(parts[1]);
            total += left * right;
        }
        int answer = total;
        return Integer.toString(answer);
    }

    protected static String getPart2() {


        Pattern pat = Pattern.compile(PART2_REGEX);
        Matcher matcher = pat.matcher(input);
        int total = 0;
        boolean mul_enabled = true;

        while (matcher.find()) {
            String mg = matcher.group();
            if (mg.startsWith("mul")) {
                if (mul_enabled) {
                    String numbers = mg.substring(4, mg.length() - 1);
                    String[] parts = numbers.split(",");
                    int left = Integer.parseInt(parts[0]);
                    int right = Integer.parseInt(parts[1]);
                    total += left * right;
                }
            } else mul_enabled = !mg.equals("don't()");
        }
        int answer = total;
        return Integer.toString(answer);
    }

   protected void parseInput(String filename) throws IOException {
        input = Files.readString(Path.of(filename));
    }
}