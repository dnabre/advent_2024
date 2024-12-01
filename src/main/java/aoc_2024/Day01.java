package src.main.java.aoc_2024;


import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.*;

public class Day01 {

    public static final String PART1_ANSWER = "2769675";
    public static final String PART2_ANSWER = "24643097";


    
    
    public static String getPart1(PrintStream out, String s) throws IOException {


        String[] lines = Files.readAllLines(Path.of(s.trim())).toArray(new String[0]);
        int values = lines.length;

        int[] left = new int[values];
        int[] right = new int[values];
        for (int i = 0; i < values; i++) {
            String ln = lines[i];
            String[] parts = ln.split("\\s+");
            left[i] = Integer.valueOf(parts[0]);
            right[i] = Integer.valueOf(parts[1]);
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


    public static String getPart2(PrintStream out, String s) throws IOException {
        out.printf("reading input file %s\n", s);

        List<String> in_lines = Files.readAllLines(Path.of(s.trim()));
        String[] lines = in_lines.toArray(new String[0]);
        out.println(lines);
        int values = lines.length;
        out.printf("read %d lines\n", values);
        int[] left = new int[values];
        HashMap<Integer,Integer> right = new HashMap<Integer,Integer>();


        for (int i = 0; i < values; i++) {
            String ln = lines[i];
            String[] parts = ln.split("\\s+");
            left[i] = Integer.valueOf(parts[0]);
            int r = Integer.valueOf(parts[1]);
            if (right.containsKey(r)) {
                int old_count = right.get(r);
                right.put(r,old_count+1);
            } else {
                right.put(r,1);
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

        out.println(total);
        return Integer.toString(total);

    }


    public static  String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.println("\tDay  1\n");

        String[] answers = new String[2];
        answers[0] = getPart1(out, inputString);
        answers[1] = getPart2(out,inputString);



        if ( ! answers[0].equals( Day01.PART1_ANSWER)) {
            out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[0], Day01.PART1_ANSWER);
        }

        if (! answers[1].equals( Day01.PART2_ANSWER)) {
            out.printf("\n\t\tWRONG ANSWER got: %s, expected %s\n", answers[1], Day01.PART2_ANSWER);
        }


        return answers;
    }
}
