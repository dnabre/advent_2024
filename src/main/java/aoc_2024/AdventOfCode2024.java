package src.main.java.aoc_2024;


import java.io.IOException;

public class AdventOfCode2024 {
    public static void main(String[] args) {

      //  System.out.println("Working Directory = " + System.getProperty("user.dir"));

        //String input_string = "input/2024/day_02_test_01.txt";
       String input_string = "input/2024/day_02_input_01.txt";
        try {
            String[] results = Day02.runDay(System.out,input_string );


            System.out.printf("\t\tpart1:\t\t%s\n", results[0]);
            System.out.printf("\t\tpart2:\t\t%s\n", results[1]);
        } catch (IOException e) {
            throw new RuntimeException("error opening file " + input_string + " " + e.toString());
        }

    }
}

