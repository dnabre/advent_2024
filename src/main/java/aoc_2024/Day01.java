package src.main.java.aoc_2024;


import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;

public class Day01 extends AoCDay {

    public static final String PART1_ANSWER = "2769675";
    public static final String PART2_ANSWER = "24643097";
    static private String[] lines;

    public Day01(int day) {
        super(day);
    }

    public static String[] runDayStatic(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.println("\tDay  1");
        parseInput(inputString);
        String[] answers = new String[2];
        answers[0] = getPart1();
        answers[1] = getPart2();


        if (!answers[0].equals(Day01.PART1_ANSWER)) {
            out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[0], Day01.PART1_ANSWER);
        }

        if (!answers[1].equals(Day01.PART2_ANSWER)) {
            out.printf("\n\t\tWRONG ANSWER got: %s, expected %s\n", answers[1], Day01.PART2_ANSWER);
        }
        return answers;
    }

    protected void parseInput(String filename) throws IOException {
        lines = Files.readAllLines(Path.of(filename)).toArray(new String[0]);
    }

    protected String getPart1() {

        int values = lines.length;

        int[] left = new int[values];
        int[] right = new int[values];
        for (int i = 0; i < values; i++) {
            String ln = lines[i];
            String[] parts = ln.split("\\s+");
            left[i] = Integer.parseInt(parts[0]);
            right[i] = Integer.parseInt(parts[1]);
        }
        Arrays.sort(left);
        Arrays.sort(right);

        int total = 0;
        for (int i = 0; i < values; i++) {
            int diff = Math.abs(left[i] - right[i]);
            total += diff;
        }
        return Integer.toString(total);
    }

    protected String getPart2() {
        int values = lines.length;

        int[] left = new int[values];
        HashMap<Integer, Integer> right = new HashMap<>();

        for (int i = 0; i < values; i++) {
            String ln = lines[i];
            String[] parts = ln.split("\\s+");
            left[i] = Integer.parseInt(parts[0]);
            int r = Integer.parseInt(parts[1]);
            if (right.containsKey(r)) {
                right.computeIfPresent(r, (_, count) -> count + 1);
            } else {
                right.put(r, 1);
            }
        }

        int total = 0;
        for (int i = 0; i < values; i++) {
            int l = left[i];
            if (right.containsKey(l)) {
                int count = right.get(l);
                total += l * count;
            }
        }
        return Integer.toString(total);
    }
}
