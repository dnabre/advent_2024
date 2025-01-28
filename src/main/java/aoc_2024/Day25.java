package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;


public class Day25 extends AoCDay {

    public static final String PART1_ANSWER = "3065";
    public static final String PART2_ANSWER = "-1";
    private static ArrayList<int[]> Keys;
    private static ArrayList<int[]> Locks;
    private static final int HEIGHT = 7;
    private static final int WIDTH = 5;

    public Day25(int day) {
        super(day);
    }

    public static String[] runDayStatic(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  25");
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

   protected void parseInput(String filename) throws IOException {
        String[] lines = Files.readAllLines(Path.of(filename)).toArray(new String[0]);
        Keys = new ArrayList<>();
        Locks = new ArrayList<>();

        for (int i = 0; i < lines.length; i += HEIGHT + 1) {
            char[][] lock = new char[HEIGHT][];
            boolean key = !(lines[i].equals("#####"));
            int[] cols = new int[WIDTH];
            Arrays.fill(cols, -1);
            for (int h = 0; h < HEIGHT; h++) {
                lock[h] = lines[i + h].toCharArray();
                for (int w = 0; w < WIDTH; w++) {
                    cols[w] += (lock[h][w] == '#' ? 1 : 0);
                }
            }
            if (key) {
                Keys.add(cols);
            } else {
                Locks.add(cols);
            }
        }
    }

    protected String getPart1() {
        int matches = 0;
        for (int[] lock : Locks) {
            for (int[] key : Keys) {
                boolean match = true;
                for (int w = 0; (w < WIDTH) && match; w++) {
                    match = (lock[w] + key[w] <= WIDTH);
                }
                matches += match ? 1 : 0;
            }
        }
        long answer = matches;
        return String.valueOf(answer);
    }


    protected String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }
}