package advent_2024;

import java.io.IOException;
import java.io.PrintStream;

abstract public class AoCDay {

    public final int day;
    protected String[] answers;
    protected long time = -1L;

    public AoCDay(int day) {
        this.day = day;
    }

    abstract public boolean[] checkAnswers(String[] answers);

    public long doDay(String filename, boolean print_output) {
        long start, end;
        try {
            start = System.nanoTime();
            if (print_output) {
                answers = runDay(System.out, filename);
            } else {
                answers = runDay(new PrintStream(new AoCUtils.NullStream()), filename);
            }


            end = System.nanoTime();
            time = end - start;
        } catch (IOException e) {
            System.err.printf("\t I/O error when running Day %d on file: %s \t (%s)\n", this.day, filename, e);
        }

        return time;
    }

    public int getDay() {
        return day;
    }

    public String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.printf("\tDay %d", this.day);
        out.println();

        String[] problem_answers = new String[]{"empty", "empty"};

        parseInput(inputString);

        long startPart1 = System.nanoTime();
        problem_answers[0] = getPart1();
        long endPart1 = System.nanoTime();
        long nano_count_1 = (endPart1 - startPart1);
        long durationPart1Ms = nano_count_1 / 1_000_000L;
        out.printf("Part 1 completed in %d ms (%d ns)%n", durationPart1Ms, nano_count_1);


        long startPart2 = System.nanoTime();
        problem_answers[1] = getPart2();
        long endPart2 = System.nanoTime();
        long nano_count_2 = (endPart2 - startPart2);
        long durationPart2Ms = nano_count_2 / 1_000_000L;
        out.printf("Part 2 completed in %d ms, (%d ns)%n", durationPart2Ms, nano_count_2);


        return problem_answers;
    }

    @Override
    public String toString() {
        int day_num = this.getDay();
        return String.format("Advent of Code, Day %s%d", day_num < 10 ? "0" : "", day_num);

    }

    abstract protected String getPart1();

    abstract protected String getPart2();

    abstract protected void parseInput(String input_filename) throws IOException;
}

