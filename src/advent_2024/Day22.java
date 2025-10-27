package advent_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
// --- Day 22: Monkey Market ---

/* originally prototyped in python, then converted to java, then multiple
   rewrites for speed. the original problem has long been forgotten
 */

public class Day22 extends AoCDay {
    public static final String PART1_ANSWER = "13753970725";
    public static final String PART2_ANSWER = "1570";
    private static final int STEPS = 2000;
    private static final int POSSIBLE_PRICE_VALUES = 19;  // [-9...0...9]
    private static long[] start_numbers;


    public Day22(int day) {
        super(day);
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

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        long total = 0L;
        for (long secret : start_numbers) {
            for (int i = 0; i < STEPS; i++) {
                secret = step(secret);
            }
            total += secret;
        }

        long answer = total;
        return String.valueOf(answer);
    }

    protected String getPart2() {
        int range_of_values = POSSIBLE_PRICE_VALUES * POSSIBLE_PRICE_VALUES * POSSIBLE_PRICE_VALUES * POSSIBLE_PRICE_VALUES; // POSSIBLE_PRICE_VALUES^4

        int[] buffer_for_gains = new int[range_of_values];
        int[] gains = new int[range_of_values];

        for (long seed : start_numbers) {
            computeGains(seed, buffer_for_gains);
            for (int i = 0; i < buffer_for_gains.length; i++) {
                gains[i] += buffer_for_gains[i];
                buffer_for_gains[i] = 0;
            }
        }
        int max = gains[0];
        for (int g : gains) {
            max = Math.max(max, g);
        }
        long anwser = max;
        return String.valueOf(anwser);
    }

    protected int digitsToVigesimal(long a, long b, long c, long d) {
        long r = (a + 9) * (POSSIBLE_PRICE_VALUES * POSSIBLE_PRICE_VALUES * POSSIBLE_PRICE_VALUES)
                + (b + 9) * (POSSIBLE_PRICE_VALUES * POSSIBLE_PRICE_VALUES)
                + (c + 9) * POSSIBLE_PRICE_VALUES
                + (d + 9);
        return (int) r;
    }

    protected void computeGains(long seed, int[] gains_buffer) {
        AoCUtils.Pair[] changes = new AoCUtils.Pair[STEPS];
        long prev_price = seed % 10;
        int changes_back = 0;
        for (int i = 0; i < STEPS; i++) {
            long number = step(seed);
            long price = number % 10;

            changes[changes_back] = new AoCUtils.Pair((int) price - (int) prev_price, (int) price);
            changes_back++;
            prev_price = price;
            seed = number;
        }

        // Doing this backwards makes a world of difference (remove lots of code too)
        for (int i = (STEPS - 3 - 1); i >= 0; i--) {
            int idx = digitsToVigesimal(changes[i].left(), changes[i + 1].left(), changes[i + 2].left(), changes[i + 3].left());
            int gain = changes[i + 3].right();
            gains_buffer[idx] = gain;
        }
    }

    @Override
    protected void parseInput(String input_filename) throws IOException {
        String[] lines = Files.readAllLines(Path.of(input_filename)).toArray(new String[0]);
        start_numbers = new long[lines.length];
        for (int i = 0; i < lines.length; i++) {
            start_numbers[i] = Long.parseLong(lines[i]);
        }
    }


}