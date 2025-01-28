package src.main.java.aoc_2024;


import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Day02 extends AoCDay {
    public static final String PART1_ANSWER = "421";
    public static final String PART2_ANSWER = "476";
    private static String[] lines;

    public Day02(int day) {
        super(day);
    }

    public static boolean isRowSafe(int[] row) {
        boolean increasing = true;
        boolean decreasing = true;
        boolean differ_good = true;

        for (int i = 0; i < row.length - 1; i++) {

            int current = row[i];
            int next = row[i + 1];

            if (!(current < next)) {
                increasing = false;
            }
            if (!(current > next)) {
                decreasing = false;
            }

            int diff_amount = Math.abs(current - next);
            if ((diff_amount < 1) || (diff_amount > 3)) {
                differ_good = false;
            }


            if ((!increasing && !decreasing) || !differ_good) {
                return false;
            }
        }


        return true;
    }

    public static boolean isRowSafeReportsSkipping(int[] row) {
        if (isRowSafe(row)) {
            return true;
        }
        for (int r = 0; r < row.length; r++) {
            if (isRowWithSkipSafe(row, r)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRowWithSkipSafe(int[] row, int skip) {
        int[] new_arr = new int[row.length - 1];
        int idx = 0;
        for (int i = 0; i < row.length; i++) {
            if (i != skip) {
                new_arr[idx] = row[i];
                idx++;
            }
        }
        return isRowSafe(new_arr);
    }

    public static String[] runDayStatic(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.println("\tDay  2");


        String[] answers = {"", ""};
        answers[0] = getPart1();
        answers[1] = getPart2();


        if (!answers[0].equals(Day02.PART1_ANSWER)) {
            out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[0], Day02.PART1_ANSWER);
        }

        if (!answers[1].equals(Day02.PART2_ANSWER)) {
            out.printf("\n\t\tWRONG ANSWER got: %s, expected %s\n", answers[1], Day02.PART2_ANSWER);
        }
        return answers;
    }

    protected static String getPart1() {

        int safe_reports = 0;
        for (String line : lines) {
            if (isRowSafe(AoCUtils.WhitespaceDelimitedLineToIntegers(line))) {
                safe_reports++;
            }
        }
        return Integer.toString(safe_reports);
    }

   protected void parseInput(String filename) throws IOException {
        lines = Files.readAllLines(Path.of(filename)).toArray(new String[0]);
    }

    protected String getPart2() {
        int safe_reports = 0;
        for (String line : lines) {
            int[] row = AoCUtils.WhitespaceDelimitedLineToIntegers(line);
            if (isRowSafeReportsSkipping(row)) {
                safe_reports++;
            }
        }
        return Integer.toString(safe_reports);
    }


}