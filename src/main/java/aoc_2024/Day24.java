package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.System.out;


public class Day24 {

    public static final String PART1_ANSWER = "-1";
    public static final String PART2_ANSWER = "-1";


    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  24");
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

    enum BIN_OP {
        AND, OR, XOR
    }

    enum trit {
        TRUE, FALSE, UNKNOWN;

        @Override
        public String toString() {
           return switch(this) {
                case TRUE -> "1";
                case FALSE -> "0";
                case UNKNOWN -> "?";
            };
        }
    }

    private sealed interface Gate {
        trit getValue();
    }
    private record ConstantGate(trit v) implements Gate{
        @Override
        public trit getValue() {
            return trit.UNKNOWN;
        }
    }
    private record AndGate(Gate left, Gate right) implements  Gate{
        @Override
        public trit getValue() {
            return trit.UNKNOWN;
        }
    }
    private record ORGate(Gate left, Gate right) implements Gate{
        @Override
        public trit getValue() {
            return trit.UNKNOWN;
        }
    }
    private record XORGate(Gate left, Gate right) implements Gate{
        @Override
        public trit getValue() {
            return trit.UNKNOWN;
        }
    }


    private static void parseInput(String filename) throws IOException {
        String[] lines = Files.readAllLines(Path.of(filename)).toArray(new String[0]);
        out.printf("read %d lines\n", lines.length);
        ArrayList<Gate> gate_list = new ArrayList<>();
        HashMap<String,Gate> name_map = new HashMap<>();
        ArrayList<ConstantGate> input_gates = new ArrayList<>();

        int break_idx=-1;
        for(int i=0; i<lines.length; i++) {
            if(lines[i].equals("")){
                break_idx= i+1;
                break;
            }
            out.printf("%3d:\t%s\n", i, lines[i]);
            String[] parts = lines[i].split(":");
            String gate_name = parts[0].trim();
            trit value = parts[1].trim().equals("1")?trit.TRUE:trit.FALSE;
            ConstantGate cg = new ConstantGate(value);


        }
        for(int i=break_idx; i < lines.length; i++) {
            out.printf("%3d:\t%s\n", i, lines[i]);
        }


        out.printf("break idx: %d\n", break_idx);


    }


    public static String getPart1() {
        long answer = -1;
        return String.valueOf(answer);
    }


    public static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }
}