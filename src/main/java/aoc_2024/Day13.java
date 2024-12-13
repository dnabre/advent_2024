package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;




public class Day13 {

    public static final String PART1_ANSWER = "25629";
    public static final String PART2_ANSWER = "107487112929999";

    private static final int A_TOKEN = 3;
    private static final int B_TOKEN = 1;
    private static final long PART2_FACTOR = 10000000000000L;
    private static ArrayList<Problem> parsed_input;

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  13");
        if (AdventOfCode2024.TESTING) {
            out.print("\t (testing)");
        }
        out.println();

        String[] answers = {"", ""};
        parseInput(inputString);
        answers[0] = getPart1();
        answers[1] = getPart2();

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
        List<String> lines = Files.readAllLines(Path.of(filename));
        parsed_input = new ArrayList<>();
        do {
            Button a = Button.parse(lines.removeFirst());
            Button b = Button.parse(lines.removeFirst());
            Prize p = Prize.parse(lines.removeFirst());
            Problem prob = new Problem(a, b, p);
            parsed_input.add(prob);
            if(!lines.isEmpty()) {
                lines.removeFirst();
            }
        } while(!lines.isEmpty());

    }
    public static String getPart1() {
        ArrayList<Problem> problems = new ArrayList<>(parsed_input);
        long tokens =0;
        for(Problem p: problems) {
            Button A = p.a;
            Button B = p.b;
            Prize P = p.p;

            for(long a_press =0; a_press < 101; a_press++) {
                for(long b_press =0; b_press < 101; b_press++) {
                    long t_x = A.x_press(a_press) + B.x_press(b_press);
                    long t_y = A.y_press(a_press) + B.y_press(b_press);
                    if((P.x == t_x) && (P.y == t_y)) {
                        tokens += (A_TOKEN * a_press) + (B_TOKEN * b_press);
                    }
                }
            }
        }

        long answer = tokens;
        return String.valueOf(answer);
    }


    public static String getPart2() {

        ArrayList<Problem> problems = new ArrayList<>();
        for(Problem p :parsed_input) {
            Problem new_p = new Problem(p.a,p.b,new Prize(p.p.x + PART2_FACTOR, p.p.y + PART2_FACTOR));
            problems.add(new_p);
        }
        long tokens =0;


        //

//        public static long clc(long px, long py, long ax, long ay, long bx, long by)

        for(Problem p: problems) {
            Button A = p.a;
            Button B = p.b;
            Prize P = p.p;
            out.println(p);
            out.println();
            long r = clc(P.x, P.y, A.x, A.y, B.x, B.y);

            tokens += r;


            out.println("--------------------------------------------");
        }

        long answer = tokens;
        return String.valueOf(answer);
    }

    record Problem(Button a, Button b, Prize p) {
        @Override
        public String toString() {
            return a + "\n" + b + "\n" + p;
        }
    }
    record Button(char letter, long x, long y) {
        @Override
        public String toString() {
            return String.format("Button: %c: X%c%d, Y%c%d",
                    letter, (x<0)?' ':'+', x, (y<0)?' ':'+' , y);
        }
        public static Button parse(String s) {
            String[] parts = s.split("\\+");
            char b_char = s.charAt(7);
            long a_x = Long.parseLong(parts[1].split(",")[0]);
            long a_y = Long.parseLong(parts[2]);
            return new Button(b_char, a_x, a_y);
        }
        public long x_press(long times) {
            return x * times;
        }
        public long y_press(long times) {
            return y * times;
        }
    }
    record Prize(long x, long y){
        @Override
        public String toString() {
            return String.format("Prize: X=%d, Y=%d", x, y);
        }
        public static Prize parse(String s) {
            String[] parts = s.split("=");
            long p_x = Long.parseLong(parts[1].split(",")[0]);
            long p_y = Long.parseLong(parts[2]);
            return new Prize(p_x, p_y);
        }
    }

    public static long clc(long px, long py, long ax, long ay, long bx, long by) {
        long det = ax * by - ay*bx;
        if (det ==0) {
            return 0;
        }
        long numa = px * by - py * bx;
        long numb = py * ax - px * ay;
        if ((numa % det !=0) || (numb % det != 0)) {
            return 0;
        }
        long a = numa / det;
        long b = numb /det;
        if ((a>=0) && (b>=0)) {
            return A_TOKEN * a + B_TOKEN * b;
        } else {
            return 0L;
        }
    }
}


