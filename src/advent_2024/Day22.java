package advent_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
// --- Day 22: Monkey Market ---

/***
 * Note: prototyped in python, some oddities in a direct translation
 *
 */

public class Day22 extends AoCDay {
    public static final String PART1_ANSWER = "13753970725";
    public static final String PART2_ANSWER = "1570";
    private static final int ADDED_SET_SIZE = 2048;
    private static final int QUAD_MAP_SIZE = 65536;
    private static final int STEPS = 2000;
    private static long[] diff;
    private static long[] diff2;
    private static long[] start_numbers;

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        long total = 0L;
        for (long secret : start_numbers) {
            for (int i = 0; i < 2000; i++) {
                secret = step(secret);
            }
            total += secret;
        }

        long answer = total;
        return String.valueOf(answer);
    }

    protected String getPart2() {

        HashMap<Quad, Long> daily = new HashMap<>(QUAD_MAP_SIZE);
        for (long init : start_numbers) {
            getDiffs(init);
            HashSet<Quad> added = new HashSet<>(ADDED_SET_SIZE);
            for (int j = 0; j < STEPS - 4; j++) {

                Quad each_day = new Quad(diff[j], diff[j + 1], diff[j + 2], diff[j + 3]);

                if (!added.contains(each_day)) {
                    long offset = daily.getOrDefault(each_day, 0L);
                    offset += diff2[j + 3];
                    daily.put(each_day, offset);
                    added.add(each_day);
                }
            }
        }


        long max = Long.MIN_VALUE;
        for (long t : daily.values()) {
            max = Math.max(max, t);
        }

        long answer = max;
        return String.valueOf(answer);
    }

    @Override
    protected void parseInput(String input_filename) throws IOException {
        String[] lines = Files.readAllLines(Path.of(input_filename)).toArray(new String[0]);
        start_numbers = new long[lines.length];
        for (int i = 0; i < lines.length; i++) {
            start_numbers[i] = Long.parseLong(lines[i]);
        }
    }

    private static void getDiffs(long x) {
        //Pair[] diffs = new Pair[STEPS];
        diff = new long[STEPS];
        diff2 = new long[STEPS];
        long last = x;
        long current_n;
        long b;
        long not_b;

        for (int i = 0; i < 2000; i++) {
            current_n = step(last);
            b = last % 10;
            not_b = current_n % 10;
            last = current_n;
            diff[i] = not_b - b;
            diff2[i] = not_b;
        }
    }

    private static long step(long secret) {
        long result = secret << 6;      // times 64
        secret = result ^ secret;
        secret = secret % 16777216;
        result = secret >> 5;           // div 32
        secret = result ^ secret;
        secret = secret % 16777216;
        result = secret << 11;          // times 2048
        secret = result ^ secret;
        secret = secret % 16777216;
        return secret;

    }

    public Day22(int day) {
        super(day);
    }

    public record Quad(long one, long two, long three, long four) {
    }
}