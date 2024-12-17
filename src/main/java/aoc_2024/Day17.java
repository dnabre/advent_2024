package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.System.out;

public class Day17 {

    public static final String PART1_ANSWER = "4,6,1,4,2,1,3,1,6";
    public static final String PART2_ANSWER = "-1";
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
        List<Integer> prog_list = Arrays.stream(prog_values.split(",")).map(Integer::parseInt).toList();
        int a = Integer.parseInt(parts_a[1].trim());
        int b = Integer.parseInt(parts_b[1].trim());
        int c = Integer.parseInt(parts_c[1].trim());
        start_state = new Computer(a, b, c, prog_list);
    }


    public static String getPart1() {
        Computer comp = new Computer(start_state);
        out.println(comp);
        comp.silence_output = true;
        boolean running = true;
        while(running) {
            running = comp.step();
        }
        out.print("computer halted\noutput: ");
        out.println(comp.output);

        out.println(comp.getFormatedOutput());
        long answer = -1;
        return String.valueOf(answer);
    }


    public static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }

    public static class Computer {

         static OpCode[] decode = {
                                            OpCode.ADV, OpCode.BXL, OpCode.BST,
                                            OpCode.JNZ, OpCode.BXC, OpCode.OUT,
                                            OpCode.BDV, OpCode.CDV};
        public int reg_a;
        public int reg_b;
        public int reg_c;
        public List<Integer> output;
        public int[] program;
        public int pc =0;
        public boolean halted= false;
        public boolean silence_output = false;
        public Computer(Computer other) {
            this.reg_a = other.reg_a;
            this.reg_b = other.reg_b;
            this.reg_c = other.reg_c;

            this.output = new ArrayList<>(other.output);
            this.program = Arrays.copyOf(other.program, other.program.length);
            this.pc = other.pc;
            this.halted = other.halted;
            this.silence_output = other.silence_output;
        }

        public Computer(int ra, int rb, int rc, List<Integer> prog) {
            reg_a = ra;
            reg_b = rb;
            reg_c = rc;
            program = prog.stream().mapToInt(i -> i).toArray();
            output = new ArrayList<>();
        }

        public Computer(int ra, int rb, int rc, int[] prog) {
            reg_a = ra;
            reg_b = rb;
            reg_c = rc;
            program = prog.clone();
            output = new ArrayList<>();
        }

        public boolean step() {
            if(this.halted || pc >=  program.length){
                this.halted = true;
                return false;
            }
            OpCode ins = asOpCode(program[pc]);
            final int operand = program[pc + 1];
            pc +=2;
            switch(ins) {
                case ADV -> {
                    int num = this.reg_a;
                    int denom = 2 << (getCombo(operand) -1);
                    this.reg_a = num / denom;
                }
                case BXL -> {
                    this.reg_b = this.reg_b  ^ operand;
                }
                case BST -> {
                    this.reg_b = getCombo(operand) % 8;
                }
                case JNZ -> {
                    if(reg_a != 0) {
                        pc = operand;
                    }
                }
                case BXC -> {
                    this.reg_b = this.reg_b ^ this.reg_c;
                }
                case OUT -> {
                    int combo = getCombo(operand) % 8;
                    output(combo);
                }
                case BDV -> {
                    int num = this.reg_a;
                    int denom = 2 << (getCombo(operand) -1);
                    this.reg_b = num / denom;
                }
                case CDV -> {
                    int num = this.reg_a;
                    int denom = 2 << (getCombo(operand) -1);
                    this.reg_c = num / denom;
                }
            }
            return true;
        }
        public int getCombo(int literal) {
            return switch (literal) {
                case 0, 1, 2, 3 -> literal;
                case 4 -> this.reg_a;
                case 5 -> this.reg_b;
                case 6 -> this.reg_c;
                default -> throw new IllegalArgumentException();
            };
        }
         static OpCode asOpCode(int o) {
            return decode[o];
        }

        public void output(int o) {
            if(!this.silence_output) {
                out.printf("output: %d, result in total output: %s\n",
                        o,this.output);
            }
            output.addLast(o);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Computer \t [pc=%d]\n", this.pc));
            sb.append("\tRegister A: ");
            sb.append(this.reg_a);
            sb.append("\n");
            sb.append("\tRegister B: ");
            sb.append(this.reg_b);
            sb.append("\n");
            sb.append("\tRegister C: ");
            sb.append(this.reg_c);
            sb.append("\n");
            sb.append("\nProgram: ");
            sb.append(Arrays.toString(this.program));
            if (!output.isEmpty()) {
                sb.append("\nOutput: ");
                sb.append(this.output);
            }
            return sb.toString();
        }

        public String getFormatedOutput() {
            if(this.output.isEmpty()) {
                return "no output";
            }
            StringBuilder sb = new StringBuilder();
            for(int i=0; i < output.size()-1; i++) {
                sb.append(output.get(i));
                sb.append(',');
            }
            sb.append(output.getLast());
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Computer computer = (Computer) o;
            return reg_a == computer.reg_a && reg_b == computer.reg_b && reg_c == computer.reg_c && pc == computer.pc && Objects.equals(output, computer.output) && Objects.deepEquals(program, computer.program);
        }

        @Override
        public int hashCode() {
            return Objects.hash(reg_a, reg_b, reg_c, output, Arrays.hashCode(program), pc);
        }

        enum OpCode {ADV, BXL, BST, JNZ, BXC, OUT, BDV, CDV}
    }

}