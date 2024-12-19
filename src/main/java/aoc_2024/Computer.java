package src.main.java.aoc_2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.System.out;

public class Computer {

        static OpCode[] decode = {OpCode.ADV, OpCode.BXL, OpCode.BST, OpCode.JNZ, OpCode.BXC, OpCode.OUT, OpCode.BDV, OpCode.CDV};
        public long reg_a;
        public long reg_b;
        public long reg_c;
        public ArrayList<Long> output;
        public long[] program;
        public int pc = 0;
        public boolean halted = false;
        public boolean verbose_outputs = false;

        public Computer(src.main.java.aoc_2024.Computer other) {
            this.reg_a = other.reg_a;
            this.reg_b = other.reg_b;
            this.reg_c = other.reg_c;

            this.output = new ArrayList<>(other.output);
            this.program = Arrays.copyOf(other.program, other.program.length);
            this.pc = other.pc;
            this.halted = other.halted;
            this.verbose_outputs = other.verbose_outputs;
        }

        public Computer(long ra, long rb, long rc, List<Long> prog) {
            reg_a = ra;
            reg_b = rb;
            reg_c = rc;
            program = prog.stream().mapToLong(i -> i).toArray();
            output = new ArrayList<>();
        }

        public Computer(long ra, long rb, long rc, long[] prog) {
            reg_a = ra;
            reg_b = rb;
            reg_c = rc;
            program = prog.clone();
            output = new ArrayList<>();
        }

        static OpCode asOpCode(long o) {
            return decode[(int) o];
        }

        public boolean step() {
            if (this.halted || pc >= program.length) {
                this.halted = true;
                return false;
            }
            OpCode ins = asOpCode(program[pc]);
            final long operand = program[pc + 1];
            pc += 2;
            switch (ins) {
                case ADV -> {
                    long num = this.reg_a;
                    long denom = AoCUtils.iPow(2, getCombo(operand));
                    this.reg_a = num / denom;
                }
                case BXL -> {
                    this.reg_b = this.reg_b ^ operand;
                }
                case BST -> {
                    this.reg_b = getCombo(operand) % 8;
                }
                case JNZ -> {
                    if (reg_a != 0L) {
                        pc = (int) operand;
                    }
                }
                case BXC -> {
                    this.reg_b = this.reg_b ^ this.reg_c;
                }
                case OUT -> {
                    long combo = getCombo(operand) % 8;
                    output(combo);
                }
                case BDV -> {
                    long num = this.reg_a;
                    long denom = AoCUtils.iPow(2, getCombo(operand));
                    this.reg_b = num / denom;
                }
                case CDV -> {
                    long num = this.reg_a;
                    long denom = AoCUtils.iPow(2, getCombo(operand));
                    this.reg_c = num / denom;
                }
            }
            return true;
        }

        public long getCombo(long literal) {
            Long mm = literal;
            switch (mm) {
                case 0L, 1L, 2L, 3L -> {
                    return literal;
                }
                case 4L -> {
                    return this.reg_a;
                }
                case 5L -> {
                    return this.reg_b;
                }
                case 6L -> {
                    return this.reg_c;
                }
                default -> throw new IllegalArgumentException();
            }
        }

        public void output(long o) {
            if (verbose_outputs) {
                out.printf("output: %d, result in total output: %s\n", o, this.output);
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
            if (this.output.isEmpty()) {
                return "no output";
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < output.size() - 1; i++) {
                sb.append(output.get(i));
                sb.append(',');
            }
            sb.append(output.getLast());
            return sb.toString();
        }

        public static String runToHalt(Computer other) {
            Computer fresh = new Computer(other);
            boolean running = true;
            while (running) {
                running = fresh.step();
            }
            return fresh.getFormatedOutput();
        }
        public String runToHaltWithA(long sub_a) {
            long old_a = this.reg_a;
            this.reg_a = sub_a;
            boolean running = true;
            while (running) {
                running = step();
            }
            this.reg_a = sub_a;
            return getFormatedOutput();
        }




        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            src.main.java.aoc_2024.Computer computer = (src.main.java.aoc_2024.Computer) o;
            return reg_a == computer.reg_a && reg_b == computer.reg_b && reg_c == computer.reg_c && pc == computer.pc && Objects.equals(output, computer.output) && Objects.deepEquals(program, computer.program);
        }

        @Override
        public int hashCode() {
            return Objects.hash(reg_a, reg_b, reg_c, output, Arrays.hashCode(program), pc);
        }

        enum OpCode {ADV, BXL, BST, JNZ, BXC, OUT, BDV, CDV}
    }

