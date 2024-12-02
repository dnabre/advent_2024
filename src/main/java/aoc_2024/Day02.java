package src.main.java.aoc_2024;


import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static java.lang.System.in;
import static java.lang.System.out;

public class Day02 {
    public static final String PART1_ANSWER = "412";
    public static final String PART2_ANSWER = "24643097";

    public static String getPart1(String s) throws IOException {
        String[] lines = Files.readAllLines(Path.of(s.trim())).toArray(new String[0]);
        int values = lines.length;
        out.printf("read %d lines", values);

        int safe_reports = 0;
        for (int row_number = 0; row_number < values; row_number++) {
            boolean increasing = true;
            boolean decreasing = true;
            boolean differ_good = true;

            int[] row = AoCUtils.WhitespaceDelimitedLineToInts(lines[row_number]);

            for (int i = 0; i < row.length -1; i++) {

                int current = row[i];
                int next = row[i + 1];

                if (!(current < next)) {
                    increasing = false;
                }
                if (!(current > next)) {
                    decreasing = false;
                }

                int diff_amount = Math.abs(current - next);
                if ((diff_amount<1) || (diff_amount>3)){
                    differ_good = false;
                }


                if ((!increasing && !decreasing) || !differ_good) {
                    break;
                }
            }
            if (differ_good && (increasing || decreasing)) {
                safe_reports++;
            }
        }
        return Integer.toString(safe_reports);
    }


    public static String getPart2(String s) throws IOException {
        String[] lines = Files.readAllLines(Path.of(s.trim())).toArray(new String[0]);
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


    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.println("\tDay  2");

        //String[] answers = new String[2];
        String[] answers = {"",""};
        answers[0] = getPart1(inputString);
        // answers[1] = getPart2( inputString);


        if (!answers[0].equals(Day02.PART1_ANSWER)) {
            out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[0], Day02.PART1_ANSWER);
        }

        if (!answers[1].equals(Day02.PART2_ANSWER)) {
            out.printf("\n\t\tWRONG ANSWER got: %s, expected %s\n", answers[1], Day02.PART2_ANSWER);
        }
        return answers;
    }
}
