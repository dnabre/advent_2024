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
        String answer = Computer.runToHalt(start_state);

        out.printf("Part 1 answer: %s\n", answer);
        if((!AdventOfCode2024.TESTING) &&  (!answer.equals(PART1_ANSWER))){
            out.print("******************   WRONG ANSWER   ******************\n\t");
            out.printf("got: %s, expected %s\n", answer, PART1_ANSWER);
        }
        return answer;
    }
    public static String getPart3() {
        long[] p = new long[16];
        for(int i=0; i < 16; i++) {
            p[i] = AoCUtils.iPow(2L, i);
            out.printf("2^[%d] = %d \n", i, p[i]);

        }
        out.println();
        out.println(Arrays.toString(p));

        return "";
    }
    public static String getPart2() {
        ArrayList<Long> last_a_for_digit = new ArrayList<>();

        // work out by reversing program on paper
        int top = 20236662;
        int bottom = 7359274;
        long substitute_a = -1;
        Computer device = new Computer(start_state);
        out.println("starting device\n");
        out.println(device);
       // long a = AoCUtils.iPow(8L,15L) ;
        long a= 0;

        ArrayList<String> targetOutput = new ArrayList<>();
        for(long num: device.program) {
            targetOutput.add(Long.toString(num));
        }
        for( int i=targetOutput.size(); i >=0; i--) {

            String subTargetOutput = String.join(",", targetOutput.subList(i,targetOutput.size()));
            System.out.printf("i: %d, a: %d, reg_a: %d, targetOutput: %s, subTarget: %s\n", i, a, device.reg_a,tightFormat(targetOutput) , subTargetOutput );
            String output= "";
            while(!subTargetOutput.equals(output)){

                output = device.runToHaltWithA(a);
                a++;

            }
            last_a_for_digit.add(a-1);
            out.println(last_a_for_digit);

            a >>= 3;
        }

        out.printf("\n\tfound required a: %d, or reg_a: %d\n", a, device.reg_a);

        //long answer = Long.parseLong(top + Integer.toString(bottom));
        long answer = a;
        return String.valueOf(answer);
    }
    public static String tightFormat(ArrayList<String> ls) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i< ls.size() -1; i++) {
            sb.append(ls.get(i));
            sb.append(',');
        }
        sb.append(ls.getLast());
        return sb.toString();
    }


 }

