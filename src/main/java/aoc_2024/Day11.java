package src.main.java.aoc_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;


public class Day11 extends AoCDay {

    public static final String PART1_ANSWER = "199986";
    public static final String PART2_ANSWER = "236804088748754";
    private static final int[] BLINKS = {25, 75};
    private static final long max = Long.MIN_VALUE;
    private static ArrayList<Long> parsed_input;

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        ArrayList<Long> stones = new ArrayList<>(parsed_input);
        for (int blink = 0; blink < BLINKS[0]; blink++) {
            ArrayList<Long> new_stones = new ArrayList<>(stones.size());
            for (long current : stones) {
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

    protected String getPart2() {
        ArrayList<Long> stones = new ArrayList<>(parsed_input);
        HashMap<Long, Long> counts = new HashMap<>();
        for (long s_value : stones) {
            counts.put(s_value, counts.getOrDefault(s_value, 0L) + 1L);
        }
        HashMap<Long, Long> new_counts;
        for (int blink = 0; blink < BLINKS[1]; blink++) {
            new_counts = new HashMap<>();
            for (long current : counts.keySet()) {
                if (current == 0) {
                    new_counts.put(1L, new_counts.getOrDefault(1L, 0L) + counts.get(current));
                } else if (hasEvenNumDigit(current)) {
                    String s_str = Long.toString(current);
                    int s_length = s_str.length();
                    String s_left = s_str.substring(0, s_length / 2);
                    String s_right = s_str.substring(s_length / 2, s_length);
                    long left = Long.parseLong(s_left);
                    long right = Long.parseLong(s_right);
                    new_counts.put(left, new_counts.getOrDefault(left, 0L) + counts.getOrDefault(current, 0L));
                    new_counts.put(right, new_counts.getOrDefault(right, 0L) + counts.getOrDefault(current, 0L));
                } else {
                    long new_value = 2024L * current;
                    new_counts.put(new_value, new_counts.getOrDefault(new_value, 0L) + counts.getOrDefault(current, 0L));
                }
            }

            counts = new_counts;
        }


        long answer = counts.values().stream().mapToLong(x -> x).sum();
        return String.valueOf(answer);
    }

    protected void parseInput(String filename) throws IOException {
        String raw_input = Files.readString(Path.of(filename)).trim();
        parsed_input = new ArrayList<>();
        String[] parts = raw_input.split("\\s+");
        for (String p : parts) {
            parsed_input.add(Long.valueOf(p));
        }
    }

    private static boolean hasEvenNumDigit(long d) {
        String s_str = Long.toString(d);
        int s_length = s_str.length();
        return (s_length % 2 == 0);
    }

    public Day11(int day) {
        super(day);
    }
}
