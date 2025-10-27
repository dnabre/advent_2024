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
        int[] buf = new int[19*19*19*19];
        int[] gains = new int[19*19*19*19];

        for(long seed: start_numbers) {


            computeGains(seed, buf);
            for(int i = 0; i < buf.length; i++) {
                gains[i] += buf[i];
                buf[i]=0;
            }
        }

        int max = gains[0];
        for(int g: gains) {
            max = Math.max(max, g);
        }
        int part2 = max;
        System.out.printf("part2: %s\n", part2);



        return String.valueOf(part2);
    }

    protected int seq_id(long a, long b, long c, long d) {
        long r =  (a+9L) * (19L*19L*19L) +
                        (b+9L) * (19L*19L) +
                        (c+9) * 19L +
                        (d+9);

//        System.out.printf("seq_id(a:%d, b:%d, c:%d, d:%d) = %d %n", a, b, c, d, r);
        return (int)r;
    }


    protected void computeGains(long seed, int[] price_sequence) {

        Pair[] changes = new Pair[2000];
        long prev_price = seed % 10;
        int changes_back =0;
        for (int i = 0; i < 2000; i++) {
            long number = step(seed);
            long price = number % 10;

            changes[changes_back] = new Pair((int) price - (int)prev_price, (int) price);
            changes_back++;
            prev_price = price;
            seed = number;
        }


        // Doing this backwards make a world of difference.
        // Since it elmenets a lot of code, which was the problem, it's super clear.

        for (int i = (2000 - 3 - 1) ; i >= 0; i--) {
            int idx = seq_id(changes[i].left,
                    changes[i+1].left,
                    changes[i+2].left,
                    changes[i+3].left
                    );
            int  gain = changes[i+3].right();
            price_sequence[idx] = gain;
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


    protected static record Pair(int left, int right) {}


}