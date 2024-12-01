package src.main.java.aoc_2024;


public class AdventOfCode2024 {
    public static void main(String[] args) {
        System.out.println("Time for Advent 2024");
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

//        String input_string = "input/2024/day_01_test_01.txt";
        String input_string = "input/2024/day_01_input_01.txt";
        try {
            String[] results = Day01.runDay(System.out,input_string );
            System.out.printf("part1:\t\t%s\n", results[0]);
            System.out.printf("part2:\t\t%s\n", results[1]);
        } catch (Exception e) {
            throw new RuntimeException("error opening file " + input_string + " " + e.toString());
        }

    }
}

