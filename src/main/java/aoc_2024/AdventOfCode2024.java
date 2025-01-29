package src.main.java.aoc_2024;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.OptionalDouble;

import static java.lang.System.out;

public class AdventOfCode2024 {
    public static final int DAY = 16;
    public static final boolean TESTING = false;
    public static final int TEST_IDX = 1;
    public static final boolean TIMING = true;
    protected  static final double ADJUST_TIME_TO_MS = 1000000f;
    protected  static final int NUMBER_OF_DAYS = 25;
    protected static final boolean RUN_ALL = false;
    protected static final double SLOW_THRESHOLD = 400.0;
    protected static final String[][] all_answers = new String[NUMBER_OF_DAYS][];
    protected static final double[] all_times = new double[NUMBER_OF_DAYS];
    protected static AoCDay[] days;

    public static void main(String[] args) throws Exception {
        if (RUN_ALL) {
            runAll();
            return;
        }

        String s_day = AdventOfCode2024.class.getPackageName() +"." + "Day" + (DAY<10?"0":"") + String.valueOf(DAY);
        AoCDay test_day = null;

        try {

            Class<?> day_class = Class.forName(s_day);
            Constructor<?> ctor = day_class.getConstructors()[0];
            test_day = (AoCDay) ctor.newInstance(new Object[]{DAY});
            out.printf("Testing %s\n", test_day);
        } catch (Exception e) {
            out.printf("Error when trying to construct class: %s. Exception Type: %s\\n\"", s_day, e.getClass().getName());
            out.println(e.toString());
        }

        int day_number = DAY;
        String input_string;
        if (TESTING) {
            input_string = test_files[day_number - 1][TEST_IDX - 1];
        } else {
            input_string = input_files[day_number - 1];
        }
        assert test_day != null;
   //     String[] results = test_day.runDay(out, input_string);
        double run_time = test_day.doDay(input_string) /ADJUST_TIME_TO_MS;
        String[] results = test_day.answers;
        out.printf("\t\tpart1:\t\t%s\n", results[0]);
        out.printf("\t\tpart2:\t\t%s\n", results[1]);
        if (TIMING) {

            out.printf("\n total time: %.1f ms\n", run_time);
        }

    }

    static public void printDayDetail(int d) {
        String[] results = all_answers[d];
        boolean[] correct = days[d].checkAnswers(results);
        if (!correct[0]) {
            out.printf("\t ERROR \t Part 1 answer %s is WRONG\n", results[0]);
        }
        if (!correct[1]) {
            out.printf("\t ERROR \t Part 1 answer %s is WRONG\n", results[1]);
        }
    }

    public static void runAll() {
        ArrayList<AoCDay> slow_ones = new ArrayList<>();
        days = getAllDays();
        for (int d = 0; d < NUMBER_OF_DAYS; d++) {
            AoCDay today = days[d];
            String filename = input_files[d];
            out.printf("Running day: %d ... ", today.getDay());
            all_times[d] = today.doDay(filename) / ADJUST_TIME_TO_MS;
            all_answers[d] = today.answers;
            out.printf(" done. Time:  %.1f ms\n", all_times[d]);
            if (all_times[d] > SLOW_THRESHOLD) {
                slow_ones.add(today);
            }
            printDayDetail(d);
        }
        double total_time = Arrays.stream(all_times).sum();
        OptionalDouble avg_time = Arrays.stream(all_times).average();
        out.printf("\ntotal run time: %.1f ms, avg: %.1f\n", total_time, avg_time.getAsDouble());

        out.println("--------------------------------------------------------------------------------------\n");
        out.println("Days with slow run times\n");
        for (AoCDay dd : slow_ones) {
            out.printf("\t\t%s , time: %.1f\n", dd, dd.time / ADJUST_TIME_TO_MS);
        }
    }

    protected static AoCDay[] getAllDays() {
        AoCDay[] days = new AoCDay[NUMBER_OF_DAYS];

        days[0] = new Day01(1);
        days[1] = new Day02(2);
        days[2] = new Day03(3);
        days[3] = new Day04(4);
        days[4] = new Day05(5);
        days[5] = new Day06(6);
        days[6] = new Day07(7);
        days[7] = new Day08(8);
        days[8] = new Day09(9);
        days[9] = new Day10(10);
        days[10] = new Day11(11);
        days[11] = new Day12(12);
        days[12] = new Day13(13);
        days[13] = new Day14(14);
        days[14] = new Day15(15);
        days[15] = new Day16(16);
        days[16] = new Day17(17);
        days[17] = new Day18(18);
        days[18] = new Day19(19);
        days[19] = new Day20(20);
        days[20] = new Day21(21);
        days[21] = new Day22(22);
        days[22] = new Day23(23);
        days[23] = new Day24(24);
        days[24] = new Day25(25);

        return days;
    }

    static final String[][] test_files = {{"inputs/2024/day_01_test_01.txt"}, {"inputs/2024/day_02_test_01.txt"}, {"inputs/2024/day_03_test_01.txt"}, {"inputs/2024/day_04_test_01.txt"}, {"inputs/2024/day_05_test_01.txt"}, {"inputs/2024/day_06_test_01.txt"}, {"inputs/2024/day_07_test_01.txt"}, {"inputs/2024/day_08_test_01.txt", "inputs/2024/day_08_test_02.txt"}, {"inputs/2024/day_09_test_01.txt", "inputs/2024/day_09_test_02.txt"}, {"inputs/2024/day_10_test_01.txt", "inputs/2024/day_10_test_02.txt"}, {"inputs/2024/day_11_test_01.txt"}, {"inputs/2024/day_12_test_01.txt", "inputs/2024/day_12_test_02.txt", "inputs/2024/day_12_test_03.txt"}, {"inputs/2024/day_13_test_01.txt"}, {"inputs/2024/day_14_test_01.txt"}, {"inputs/2024/day_15_test_01.txt", "inputs/2024/day_15_test_02.txt", "inputs/2024/day_15_test_03.txt"}, {"inputs/2024/day_16_test_01.txt", "inputs/2024/day_16_test_02.txt"}, {"inputs/2024/day_17_test_01.txt", "inputs/2024/day_17_test_02.txt"}, {"inputs/2024/day_18_test_01.txt"}, {"inputs/2024/day_19_test_01.txt"}, {"inputs/2024/day_20_test_01.txt"}, {"inputs/2024/day_21_test_01.txt", "inputs/2024/day_21_test_02.txt", "inputs/2024/day_21_test_03.txt"}, {"inputs/2024/day_22_test_01.txt", "inputs/2024/day_22_test_02.txt"}, {"inputs/2024/day_23_test_01.txt", "inputs/2024/day_23_test_02.txt"}, {"inputs/2024/day_24_test_01.txt", "inputs/2024/day_24_test_02.txt", "inputs/2024/day_24_test_03.txt", "inputs/2024/day_24_test_04.txt"}, {"inputs/2024/day_25_test_01.txt"}};
    static final String[] input_files = {"inputs/2024/day_01_input_01.txt", "inputs/2024/day_02_input_01.txt", "inputs/2024/day_03_input_01.txt", "inputs/2024/day_04_input_01.txt", "inputs/2024/day_05_input_01.txt", "inputs/2024/day_06_input_01.txt", "inputs/2024/day_07_input_01.txt", "inputs/2024/day_08_input_01.txt", "inputs/2024/day_09_input_01.txt", "inputs/2024/day_10_input_01.txt", "inputs/2024/day_11_input_01.txt", "inputs/2024/day_12_input_01.txt", "inputs/2024/day_13_input_01.txt", "inputs/2024/day_14_input_01.txt", "inputs/2024/day_15_input_01.txt", "inputs/2024/day_16_input_01.txt", "inputs/2024/day_17_input_01.txt", "inputs/2024/day_18_input_01.txt", "inputs/2024/day_19_input_01.txt", "inputs/2024/day_20_input_01.txt", "inputs/2024/day_21_input_01.txt", "inputs/2024/day_22_input_01.txt", "inputs/2024/day_23_input_01.txt", "inputs/2024/day_24_input_01.txt", "inputs/2024/day_25_input_01.txt",};


}

