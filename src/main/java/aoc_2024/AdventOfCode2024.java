package src.main.java.aoc_2024;


import java.io.IOException;

public class AdventOfCode2024 {
    static final String[] test_files = {
            "input/2024/day_01_test_01.txt",
            "input/2024/day_02_test_01.txt",
            "input/2024/day_03_test_01.txt",
            "input/2024/day_04_test_01.txt",
            "input/2024/day_05_test_01.txt",
    };

    static final String[] input_files= {
            "input/2024/day_01_input_01.txt",
            "input/2024/day_02_input_01.txt",
            "input/2024/day_03_input_01.txt",
            "input/2024/day_04_input_01.txt",
            "input/2024/day_05_input_01.txt",
    };


    public static final boolean TESTING = false;

    public static void main(String[] args){

        int day_number=5;


        String input_string;
        if (TESTING) {
            input_string = test_files[day_number-1];
        } else {
            input_string = input_files[day_number-1];
        }

        try {
            String[] results = Day05.runDay(System.out, input_string);
            System.out.printf("\t\tpart1:\t\t%s\n", results[0]);
            System.out.printf("\t\tpart2:\t\t%s\n", results[1]);
        } catch (IOException e) {
            throw new RuntimeException("error opening file " + input_string + " " + e);
        }

    }
}

