package src.main.java.aoc_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class Day13 extends AoCDay {

    public static final String PART1_ANSWER = "25629";
    public static final String PART2_ANSWER = "107487112929999";
    private static final int A_TOKEN = 3;
    private static final int B_TOKEN = 1;
    private static final int PART1_LIMIT = 100;
    private static final long PART2_FACTOR = 10000000000000L;
    private static ArrayList<Problem> parsed_input;

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        ArrayList<Problem> problems = new ArrayList<>(parsed_input);
        long tokens = 0;
        for (Problem p : problems) {
            tokens += p.getTokensWithLimitedPushes(PART1_LIMIT);
        }
        long answer = tokens;
        return String.valueOf(answer);
    }

    protected String getPart2() {
        ArrayList<Problem> problems = new ArrayList<>();
        for (Problem p : parsed_input) {
            Problem new_p = new Problem(p.A, p.B, new Prize(p.P.x + PART2_FACTOR, p.P.y + PART2_FACTOR));
            problems.add(new_p);
        }
        long tokens = 0;
        for (Problem p : problems) {
            tokens += p.getTokens();
        }
        long answer = tokens;
        return String.valueOf(answer);
    }

    protected void parseInput(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filename));
        parsed_input = new ArrayList<>();
        do {
            Button a = Button.parse(lines.removeFirst());
            Button b = Button.parse(lines.removeFirst());
            Prize p = Prize.parse(lines.removeFirst());
            Problem prob = new Problem(a, b, p);
            parsed_input.add(prob);
            if (!lines.isEmpty()) {
                lines.removeFirst();
            }
        } while (!lines.isEmpty());

    }

    public Day13(int day) {
        super(day);
    }

    record Button(char letter, long x, long y) {
        public static Button parse(String s) {
            String[] parts = s.split("\\+");
            char b_char = s.charAt(7);
            long a_x = Long.parseLong(parts[1].split(",")[0]);
            long a_y = Long.parseLong(parts[2]);
            return new Button(b_char, a_x, a_y);
        }

        @Override
        public String toString() {
            return String.format("Button: %c: X%c%d, Y%c%d", letter, (x < 0) ? ' ' : '+', x, (y < 0) ? ' ' : '+', y);
        }

        public long x_press(long times) {
            return x * times;
        }

        public long y_press(long times) {
            return y * times;
        }
    }

    record Prize(long x, long y) {
        public static Prize parse(String s) {
            String[] parts = s.split("=");
            long p_x = Long.parseLong(parts[1].split(",")[0]);
            long p_y = Long.parseLong(parts[2]);
            return new Prize(p_x, p_y);
        }

        @Override
        public String toString() {
            return String.format("Prize: X=%d, Y=%d", x, y);
        }
    }

    record Problem(Button A, Button B, Prize P) {
        public long getTokens() {
            long det = A.x_press(B.y) - B.x_press(A.y);
            if (det != 0) {
                long det_a = B.y_press(P.x) - B.x_press(P.y);
                long det_b = A.x_press(P.y) - A.y_press(P.x);
                if ((det_a % det == 0) && (det_b % det == 0)) {
                    long a = det_a / det;
                    long b = det_b / det;
                    if ((a >= 0) && (b >= 0)) {
                        return A_TOKEN * a + B_TOKEN * b;
                    }
                }
            }
            return 0L;
        }

        public long getTokensWithLimitedPushes(int max_pushes) {
            for (long a_press = 0; a_press < max_pushes; a_press++) {
                for (long b_press = 0; b_press < max_pushes; b_press++) {
                    long t_x = A.x_press(a_press) + B.x_press(b_press);
                    long t_y = A.y_press(a_press) + B.y_press(b_press);
                    if ((P.x == t_x) && (P.y == t_y)) {
                        return (A_TOKEN * a_press) + (B_TOKEN * b_press);
                    }
                }
            }
            return 0L;
        }

        @Override
        public String toString() {
            return A + "\n" + B + "\n" + P;
        }
    }
}


