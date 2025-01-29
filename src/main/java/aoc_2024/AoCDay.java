package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;

abstract public class AoCDay {

    protected String[] answers;
    protected int day;
    protected long time = -1L;
    private static final String PART1_ANSWER = "WRONG";
    private static final String PART2_ANSWER = "WRONG";


    abstract public boolean[] checkAnswers(String[] answers);


    public long doDay(String filename) {
        long start, end;
        try {
            start = System.nanoTime();
            answers = runDay(new PrintStream(new AoCUtils.NullStream()), filename);
            end = System.nanoTime();
            time = end - start;
        } catch (IOException e) {
            System.err.printf("I/O error when running Day %d on file: %s \t (%s)\n", this.day, filename, e);
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
        problem_answers[0] = getPart1();
        problem_answers[1] = getPart2();

        return problem_answers;
    }

    abstract protected String getPart1();

    abstract protected String getPart2();

    abstract protected void parseInput(String input_filename) throws IOException;

    public AoCDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        int day_num = this.getDay();
        return String.format("Advent of Code, Day %s%d",
        day_num <10 ? "0":"", day_num);

    }
}

