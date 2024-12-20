package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.System.out;


public class Day07 {
    public static final String PART1_ANSWER = "267566105056";
    public static final String PART2_ANSWER = "1619";
    private static ArrayList<Equation> equations = new ArrayList<>();
    enum Operators {Add, Multiply}
    enum Operators2 {Add, Multiply, Concat}

    record State(List<Operators> ops) {
    }

    record State2(List<Operators2> ops) {
    }

    record Equation(long value, long[] terms) {
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Equation[value=");
            sb.append(value);
            sb.append(", terms=");
            sb.append(Arrays.toString(terms));
            sb.append("]");
            return sb.toString();
        }

    }





    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.println("\tDay  10");

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
        String[] lines = Files.readAllLines(Path.of(filename)).toArray(new String[0]);
        for (String ln : lines) {
            String[] parts = ln.split(":");
            long value = Long.parseLong(parts[0].trim());
            String[] s_terms = parts[1].trim().split(AoCUtils.WHITESPACE_RE);
            long[] terms = new long[s_terms.length];
            for (int i = 0; i < s_terms.length; i++) {
                terms[i] = Long.parseLong(s_terms[i].trim());
            }
            Equation eq = new Equation(value, terms);
            equations.add(eq);

        }

    }

    public static String getPart1() {
        long total = 0L;
        for (Equation eq : equations) {
            boolean sat = canEquationBeSatisfied(eq.value, eq.terms);
            if (sat) {
                total += eq.value;
            }
        }


        long answer = total;
        return String.valueOf(answer);
    }

    public static String getPart2() {
        long total = 0L;
        for (Equation eq : equations) {
            boolean sat = canEquationBeSatisfied2(eq.value, eq.terms);
            if (sat) {
                total += eq.value;
            }
        }


        long answer = total;
        return String.valueOf(answer);
    }

    private static boolean canEquationBeSatisfied(long goal, long[] terms) {
        State start = new State(new ArrayList<>());
        Deque<State> work_queue = new ArrayDeque<>();
        work_queue.add(start);
        while (!work_queue.isEmpty()) {
            State current = work_queue.poll();
            if (current.ops.size() == terms.length - 1) {
                long total = terms[0];
                for (int i = 1; i < terms.length; i++) {
                    Operators op = current.ops.removeFirst();
                    long next_term = terms[i];
                    switch (op) {
                        case Add -> {
                            total += next_term;
                        }
                        case Multiply -> {
                            total *= next_term;
                        }
                    }
                }
                if (total == goal) {
                    return true;
                }
            } else {
                List<Operators> a_list = new ArrayList<>(current.ops);
                a_list.add(Operators.Add);
                State new_a = new State(a_list);
                work_queue.add(new_a);
                List<Operators> m_list = new ArrayList<>(current.ops);
                m_list.add(Operators.Multiply);
                State new_m = new State(m_list);
                work_queue.add(new_m);
            }


        }
        return false;
    }

    private static boolean canEquationBeSatisfied2(long goal, long[] terms) {
        State2 start = new State2(new ArrayList<>());
        Deque<State2> work_queue = new ArrayDeque<>();
        work_queue.add(start);
        while (!work_queue.isEmpty()) {
            State2 current = work_queue.poll();
            if (current.ops.size() == terms.length - 1) {
                long total = terms[0];
                for (int i = 1; i < terms.length; i++) {
                    Operators2 op = current.ops.removeFirst();
                    long next_term = terms[i];
                    switch (op) {
                        case Add -> {
                            total += next_term;
                        }
                        case Multiply -> {
                            total *= next_term;
                        }
                        case Concat -> {
                            total = Long.parseLong(Long.toString(total) + Long.toString(next_term));

                        }
                    }
                }

                if (total == goal) {
                    return true;
                }
            } else {
                List<Operators2> a_list = new ArrayList<>(current.ops);
                a_list.add(Operators2.Add);
                State2 new_a = new State2(a_list);
                work_queue.add(new_a);

                List<Operators2> m_list = new ArrayList<>(current.ops);
                m_list.add(Operators2.Multiply);
                State2 new_m = new State2(m_list);
                work_queue.add(new_m);

                List<Operators2> c_list =new ArrayList<>(current.ops);
                c_list.add(Operators2.Concat);
                State2 new_c = new State2(c_list);
                work_queue.add(new_c);
            }
        }
        return false;
    }
}