package src.main.java.aoc_2024;


import java.io.PrintStream;

public class AdventOfCode2024 {
    static final String[][] test_files = {
            {"inputs/2024/day_01_test_01.txt"},
            {"inputs/2024/day_02_test_01.txt"},
            {"inputs/2024/day_03_test_01.txt"},
            {"inputs/2024/day_04_test_01.txt"},
            {"inputs/2024/day_05_test_01.txt"},
            {"inputs/2024/day_06_test_01.txt"},
            {"inputs/2024/day_07_test_01.txt"},
            {"inputs/2024/day_08_test_01.txt","inputs/2024/day_08_test_02.txt" },
            {"inputs/2024/day_09_test_01.txt","inputs/2024/day_09_test_02.txt"},
            {"inputs/2024/day_10_test_01.txt","inputs/2024/day_10_test_02.txt"},
            {"inputs/2024/day_11_test_01.txt"},
            {"inputs/2024/day_12_test_01.txt", "inputs/2024/day_12_test_02.txt","inputs/2024/day_12_test_03.txt"},
            {"inputs/2024/day_13_test_01.txt"},
            {"inputs/2024/day_14_test_01.txt"},
            {"inputs/2024/day_15_test_01.txt","inputs/2024/day_15_test_02.txt","inputs/2024/day_15_test_03.txt"},
            {"inputs/2024/day_16_test_01.txt","inputs/2024/day_16_test_02.txt"},
            {"inputs/2024/day_17_test_01.txt", "inputs/2024/day_17_test_02.txt"},
            {"inputs/2024/day_18_test_01.txt"},
            {"inputs/2024/day_19_test_01.txt"},
            {"inputs/2024/day_20_test_01.txt"},
            {"inputs/2024/day_21_test_01.txt", "inputs/2024/day_21_test_02.txt", "inputs/2024/day_21_test_03.txt"},
            {"inputs/2024/day_22_test_01.txt","inputs/2024/day_22_test_02.txt"},
            {"inputs/2024/day_23_test_01.txt","inputs/2024/day_23_test_02.txt"},
            {"inputs/2024/day_24_test_01.txt","inputs/2024/day_24_test_02.txt",
                        "inputs/2024/day_24_test_03.txt","inputs/2024/day_24_test_04.txt"},
            {"inputs/2024/day_25_test_01.txt"}
    };

    static final String[] input_files= {
            "inputs/2024/day_01_input_01.txt",
            "inputs/2024/day_02_input_01.txt",
            "inputs/2024/day_03_input_01.txt",
            "inputs/2024/day_04_input_01.txt",
            "inputs/2024/day_05_input_01.txt",
            "inputs/2024/day_06_input_01.txt",
            "inputs/2024/day_07_input_01.txt",
            "inputs/2024/day_08_input_01.txt",
            "inputs/2024/day_09_input_01.txt",
            "inputs/2024/day_10_input_01.txt",
            "inputs/2024/day_11_input_01.txt",
            "inputs/2024/day_12_input_01.txt",
            "inputs/2024/day_13_input_01.txt",
            "inputs/2024/day_14_input_01.txt",
            "inputs/2024/day_15_input_01.txt",
            "inputs/2024/day_16_input_01.txt",
            "inputs/2024/day_17_input_01.txt",
            "inputs/2024/day_18_input_01.txt",
            "inputs/2024/day_19_input_01.txt",
            "inputs/2024/day_20_input_01.txt",
            "inputs/2024/day_21_input_01.txt",
            "inputs/2024/day_22_input_01.txt",
            "inputs/2024/day_23_input_01.txt",
            "inputs/2024/day_24_input_01.txt",
            "inputs/2024/day_25_input_01.txt",
    };

    public static final int DAY = 22;
    public static final boolean TESTING = false;
    public static final int TEST_IDX =2;
    public static final boolean TIMING = true;

    public static void main(String[] args) throws Exception {

        int day_number = DAY;
        String input_string;
        if (TESTING) {
            input_string = test_files[day_number - 1][TEST_IDX - 1];
        } else {
            input_string = input_files[day_number - 1];
        }

            long start, end;
            if (TIMING) {
                start = System.nanoTime();
            }


           String[] results = Day22.runDay( System.out,input_string);

            System.out.printf("\t\tpart1:\t\t%s\n", results[0]);
            System.out.printf("\t\tpart2:\t\t%s\n", results[1]);
            if (TIMING) {
                end = System.nanoTime();
                System.out.printf("\n total time: %.1f ms\n", (end - start) / 1000000f);
            }

    }
    private static String[] runDay(int day, PrintStream out, String input_string) {
        String[] results = new String[2];
        switch(day){

        }

        return results;
    }
}

