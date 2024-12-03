package src.main.java.aoc_2024;


import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;


public class Day03 {
    public static final String PART1_ANSWER = "161085926";
    public static final String PART2_ANSWER = "476";// too low

    public static final String PART1_REGEX = "mul\\(\\d{1,3},\\d{1,3}\\)";
    public static String[] runDay(PrintStream out, String inputString) throws IOException {
//        out.println("Advent of Code 2024");
//        out.println("\tDay  3");


        String[] answers = {"", ""};
        answers[0] = getPart1(inputString);
        answers[1] = getPart2(inputString);


        if (!answers[0].equals(Day02.PART1_ANSWER)) {
            out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[0], Day02.PART1_ANSWER);
        }

        if (!answers[1].equals(Day02.PART2_ANSWER)) {
            out.printf("\n\t\tWRONG ANSWER got: %s, expected %s\n", answers[1], Day02.PART2_ANSWER);
        }
        return answers;
    }

    public static String getPart1(String s) throws IOException {
        String input = Files.readString(Path.of(s.trim()));
        out.printf("read %d characters\n", input.length());


        Pattern pat = Pattern.compile(PART1_REGEX);
        Matcher matcher = pat.matcher(input);
        int match_count =0;
        int total =0;

        while (matcher.find()) {
            String mg = matcher.group();
            String nums = mg.substring(4, mg.length()-1);
            String[] parts = nums.split(",");
            int left = Integer.parseInt(parts[0]);
            int right = Integer.parseInt(parts[1]);
            total += left*right;
            match_count++;
        }


        out.printf("match_count: %d\n", match_count);


        int answer = total;
        return Integer.toString(answer);
    }

    public static String getPart2(String s) throws IOException {
        String[] lines = Files.readAllLines(Path.of(s.trim())).toArray(new String[0]);
        int answer = 2;
        return Integer.toString(answer);
    }


}