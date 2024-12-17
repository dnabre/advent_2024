package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;

public class Day17 {

    public static final String PART1_ANSWER = "4,6,1,4,2,1,3,1,6";
    public static final String PART2_ANSWER = "202366627359274";
    private static Computer start_state;

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  17");
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

    private static void parseInput(String inputString) throws IOException {
        String[] input_lines = Files.readAllLines(Path.of(inputString)).toArray(new String[0]);

        String[] parts_a = input_lines[0].split(":");
        String[] parts_b = input_lines[1].split(":");
        String[] parts_c = input_lines[2].split(":");
        // lines[3] blank
        String prog_values = input_lines[4].split(":")[1].trim();
        List<Long> prog_list = Arrays.stream(prog_values.split(",")).map(Long::parseLong).toList();
        int a = Integer.parseInt(parts_a[1].trim());
        int b = Integer.parseInt(parts_b[1].trim());
        int c = Integer.parseInt(parts_c[1].trim());
        start_state = new Computer(a, b, c, prog_list);
    }


    public static String getPart1() {
        Computer comp = new Computer(start_state);
        out.println(comp);
        boolean running = true;
        while (running) {
            running = comp.step();
        }
//        out.print("computer halted\noutput: ");
//        out.println(comp.output);


        return comp.getFormatedOutput();
    }


    public static String getPart2() {
        Computer comp = new Computer(start_state);

        // Those mod 8's should give us a narrow search range if I can't figure out where to put it in.

        ArrayList<Long> prog = new ArrayList<Long>(Arrays.stream(comp.program).boxed().toList());
        out.println(comp.disssembleProgram());

        final long REPLACE_A = 202366627359274L;


        boolean running = true;
        comp.reg_a = REPLACE_A;
        while (running) {
            running = comp.step();
        }
        boolean result = prog.equals(comp.output);
        if (result) {
            out.println("output matches initial program");
        } else {
            out.println("output is not program:\n");
        }

        {
            out.printf("\tprogram: %s\n", prog);
            out.printf("\toutput : %s\n", comp.output);
        }

        out.print("computer halted\n");


        long answer = -1;
        return String.valueOf(answer);
    }

}

