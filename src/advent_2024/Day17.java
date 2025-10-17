package advent_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Day17 extends AoCDay {

    public static final String PART1_ANSWER = "4,6,1,4,2,1,3,1,6";
    public static final String PART2_ANSWER = "202366627359274";
    private static Computer start_state;

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        return Computer.runToHalt(start_state);
    }

    protected String getPart2() {
        Computer device = new Computer(start_state);
        ArrayList<Long> program = AoCUtils.arrayToArrayList(device.program);
        ArrayList<Long> output;
        List<Long> matched = new ArrayList<>();
        matched.add(program.getLast());
        long init_a = AoCUtils.iPow(8L, 15L);
        long power = 14;
        output = device.resetAndRunWithA(init_a);

        while (!output.equals(matched)) {
            init_a += AoCUtils.iPow(8L, power - 1);
            output = device.resetAndRunWithA(init_a);
            if (output.equals(program)) {
                break;
            }
            if (output.subList(output.size() - matched.size(), output.size()).equals(matched)) {
                power = Math.max(0L, power - 1L);
                matched = program.subList(program.size() - (matched.size() + 1), program.size());
            }
        }
        long answer = init_a;
        return String.valueOf(answer);
    }

    protected void parseInput(String inputString) throws IOException {
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


    public Day17(int day) {
        super(day);
    }
}




