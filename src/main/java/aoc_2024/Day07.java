package src.main.java.aoc_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;


public class Day07 extends AoCDay {
    public static final String PART1_ANSWER = "267566105056";
    public static final String PART2_ANSWER = "116094961956019";
    private static final ArrayList<Equation> equations = new ArrayList<>();

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        long total = 0L;
        for (Equation eq : equations) {
            boolean sat = canEquationBeSatisfied(eq.value, eq.terms, false);
            if (sat) {
                total += eq.value;
            }
        }
        long answer = total;
        return String.valueOf(answer);
    }

    protected String getPart2() {
        long total = 0L;
        for (Equation eq : equations) {
            boolean sat = canEquationBeSatisfied(eq.value, eq.terms, true);
            if (sat) {
                total += eq.value;
            }
        }
        long answer = total;
        return String.valueOf(answer);
    }

    protected void parseInput(String filename) throws IOException {
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

    public static long concat(long total, long next_term) {
        return Long.parseLong(total + Long.toString(next_term));
    }

    private static boolean canEquationBeSatisfied(long goal, long[] terms, boolean use_concat) {
        State start = new State(terms[0], 1);
        Deque<State> work_queue = new ArrayDeque<>();
        work_queue.add(start);
        while (!work_queue.isEmpty()) {
            State current = work_queue.poll();
            if (current.to_term == terms.length) {
                if (current.running == goal) {
                    return true;
                }
            } else {
                long a_run = current.running + terms[current.to_term];
                if (a_run <= goal) {
                    State new_a = new State(a_run, current.to_term + 1);
                    work_queue.add(new_a);
                }

                long m_run = current.running * terms[current.to_term];
                if (m_run <= goal) {
                    State new_m = new State(m_run, current.to_term + 1);
                    work_queue.add(new_m);
                }
                if (use_concat) {
                    long c_run = concat(current.running, terms[current.to_term]);
                    if (c_run <= goal) {
                        State new_c = new State(c_run, current.to_term + 1);
                        work_queue.add(new_c);
                    }
                }
            }
        }
        return false;
    }

    public Day07(int day) {
        super(day);
    }

    record Equation(long value, long[] terms) {
    }

    record State(long running, int to_term) {
    }
}