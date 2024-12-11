package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import static src.main.java.aoc_2024.AoCUtils.LongPair;


public class Day11 {

    public static final String PART1_ANSWER = "199986";
    public static final String PART2_ANSWER = "236804088748754";
    private static char[] packed_disk;

    private static ArrayList<Long> parsed_input;
    private static final int[] BLINKS = {25, 75};

    private static HashMap<Long, LongPair> full_cache = new HashMap<>();
    private static HashMap<Long, Boolean> even = new HashMap<>();
    private static HashMap<Long, Case> case_cache = new HashMap<>();
    private static HashMap<Long, LongPair> second_case_cache = new HashMap<>();

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  11");
        if(AdventOfCode2024.TESTING){
            out.print("\t (testing)");
        }
        out.println();

        String[] answers = {"", ""};
        parseInput(inputString);


        if(AdventOfCode2024.TIMING) {
            long start = System.currentTimeMillis();
            answers[0] = getPart1();
            long finish = System.currentTimeMillis();
            long part1_time = finish - start;
            out.printf("\tpart 1:\t %s\t\t %d ms\n" , answers[0], part1_time);

            start = System.currentTimeMillis();
            answers[1] = getPart2();
            finish = System.currentTimeMillis();
            long part2_time = finish - start;
            out.printf("\tpart 2:\t %s\t\t %d ns\n", answers[1], part2_time);
        } else {
            answers[0] = getPart1();
            answers[0] = getPart2();
        }

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
        String raw_input = Files.readString(Path.of(filename)).trim();
        parsed_input = new ArrayList<>();
        String[] parts = raw_input.split("\\s+");
        for (String p : parts) {
            parsed_input.add(Long.valueOf(p));
        }
    }

    public static String getPart1() {
        ArrayList<Long> stones = new ArrayList<>(parsed_input);
        for (int blink = 0; blink <BLINKS[0]; blink++) {
            ArrayList<Long> new_stones = new ArrayList<>(stones.size());
            for (int i = 0; i < stones.size(); i++) {
                long current = stones.get(i);
                if (current == 0) {
                    new_stones.add(1L);
                } else {
                        String s_str = Long.toString(current);
                        int s_length = s_str.length();
                        if (s_length % 2 == 0) {
                            String s_left = s_str.substring(0, s_length / 2);
                            String s_right = s_str.substring(s_length / 2, s_length);
                            new_stones.add(Long.parseLong(s_left));
                            new_stones.add(Long.parseLong(s_right));
                        } else {
                            new_stones.add(2024L * current);
                        }
                    }
            }
            stones = new_stones;
        }
        long answer = stones.size();
        return String.valueOf(answer);
    }

    public static String getPart2() {
        ArrayList<Long> stones = new ArrayList<>(parsed_input);
        for (int blink = 0; blink < BLINKS[1]; blink++) {
            ArrayList<Long> new_stones = new ArrayList<>(stones.size());
            for (int i = 0; i < stones.size(); i++) {
                long current = stones.get(i);
                if (current == 0) {
                    new_stones.add(1L);
                } else {

                    if (even.containsKey(current)) {
                        if (even.get(current)) {
                            String s_str = Long.toString(current);
                            int s_length = s_str.length();
                            String s_left = s_str.substring(0, s_length / 2);
                            String s_right = s_str.substring(s_length / 2, s_length);
                            new_stones.add(Long.parseLong(s_left));
                            new_stones.add(Long.parseLong(s_right));
                        } else {
                            new_stones.add(2024L * current);
                        }
                    } else {
                        String s_str = Long.toString(current);
                        int s_length = s_str.length();
                        if (s_length % 2 == 0) {
                            even.put(current,true);
                            String s_left = s_str.substring(0, s_length / 2);
                            String s_right = s_str.substring(s_length / 2, s_length);
                            new_stones.add(Long.parseLong(s_left));
                            new_stones.add(Long.parseLong(s_right));
                        } else {
                            even.put(current,false);
                            new_stones.add(2024L * current);
                        }
                    }
                }
            }
            stones = new_stones;
        }
        long answer = 2;
        return String.valueOf(answer);
    }

    private static enum Case {One, Even, Default}

}
