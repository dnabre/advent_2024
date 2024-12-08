package src.main.java.aoc_2024;


import java.io.IOException;

public class AdventOfCode2024 {
    static final String[][] test_files = {
            {"input/2024/day_01_test_01.txt"},
            {"input/2024/day_02_test_01.txt"},
            {"input/2024/day_03_test_01.txt"},
            {"input/2024/day_04_test_01.txt"},
            {"input/2024/day_05_test_01.txt"},
            {"input/2024/day_06_test_01.txt"},
            {"input/2024/day_07_test_01.txt"},
            {"input/2024/day_08_test_01.txt","input/2024/day_08_test_02.txt" },
            {"input/2024/day_09_test_01.txt"},
            {"input/2024/day_10_test_01.txt"},


    };

    static final String[] input_files= {
            "input/2024/day_01_input_01.txt",
            "input/2024/day_02_input_01.txt",
            "input/2024/day_03_input_01.txt",
            "input/2024/day_04_input_01.txt",
            "input/2024/day_05_input_01.txt",
            "input/2024/day_06_input_01.txt",
            "input/2024/day_07_input_01.txt",
            "input/2024/day_08_input_01.txt",
            "input/2024/day_09_input_01.txt",
            "input/2024/day_10_input_01.txt",
    };


    public static final boolean TESTING = true;
    public static final int TEST_IDX=0;

    public static void main(String[] args){

        int day_number=8;


        String input_string;
        if (TESTING) {
            input_string = test_files[day_number-1][TEST_IDX];
        } else {
            input_string = input_files[day_number-1];
        }

        try {
            String[] results = Day08.runDay(System.out, input_string);
            System.out.printf("\t\tpart1:\t\t%s\n", results[0]);
            System.out.printf("\t\tpart2:\t\t%s\n", results[1]);
        } catch (IOException e) {
            throw new RuntimeException("error opening file " + input_string + " " + e);
        }

    }
}

