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
    public static final long part2_long_answer = 202366627359274L;
    private static Computer start_state;
    // work out by reversing program on paper
    int top = 20236662;
    int bottom = 7359274;
    long answer = -1;

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
        ArrayList<Long> prog_list = new ArrayList<>(Arrays.stream(prog_values.split(",")).map(Long::parseLong).toList());
        int a = Integer.parseInt(parts_a[1].trim());
        int b = Integer.parseInt(parts_b[1].trim());
        int c = Integer.parseInt(parts_c[1].trim());
        start_state = new Computer(a, b, c, prog_list);
    }

    public static String getPart1() {
        String answer = Computer.runToHalt(start_state);

        out.printf("Part 1 answer: %s\n", answer);
        if ((!AdventOfCode2024.TESTING) && (!answer.equals(PART1_ANSWER))) {
            out.print("******************   WRONG ANSWER   ******************\n\t");
            out.printf("got: %s, expected %s\n", answer, PART1_ANSWER);
        }
        return answer;
    }

    public static String getPart3() {
        long[] p = new long[16];
        for (int i = 0; i < 16; i++) {
            p[i] = AoCUtils.iPow(2L, i);
            out.printf("2^[%d] = %d \n", i, p[i]);

        }
        out.println();
        out.println(Arrays.toString(p));

        return "";
    }


    public static String getPart2() {
        Computer device = new Computer(start_state);
        ArrayList<Long> program = AoCUtils.arrayToArrayList(device.program);
        out.println(program);
        ArrayList<Long> output = new ArrayList<>();
        List<Long> matched = new ArrayList<>();
        matched.add(program.getLast());
        long init_a = AoCUtils.iPow(8L, 15L);
        long power = 14;
        output = device.runToHaltWithA2(init_a);
        long m_count = matched_digits(matched, program);


        while (!output.equals(matched)) {
            init_a += AoCUtils.iPow(8L, power-1);
            output = device.runToHaltWithA2(init_a);
            if(output.equals(program)) {
                break;
            }
            if (output.subList(output.size() - matched.size(), output.size()).equals(matched)) {
                power = Math.max(0L, power - 1L);
                m_count = matched_digits(matched,program);
                out.printf("init_a: %d, matched: %s, power: %d,m: %d, output: %s\n", init_a, matched, power,m_count, output);
                matched = program.subList(program.size() - (matched.size() + 1), program.size());

            }
        }
        long answer = init_a;
        return String.valueOf(answer);
    }


    public static int matched_digits(List<Long> part, List<Long> full) {
        if(part.size() > full.size()) {
            throw new IllegalArgumentException("part is longer than full!");
        }

        int p_end = part.size() -1;
        int f_end = full.size() -1;
        int m =0;
        long left,right;
        left = part.get(p_end);
        right=full.get(f_end);
        while(left == right) {
            m++;
            p_end++;
            f_end++;
            if((p_end >= part.size()) || (f_end >= full.size())) {
                return m;
            }
            left = part.get(p_end);
            right=full.get(f_end);
        }
        return m;

    }

}




