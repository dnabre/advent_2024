package src.main.java.aoc_2024;


import static java.lang.System.out;

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

    public static void runAll() {
        AoCDay[] days = getAllDays();
        for(int d=0; d < 25; d++) {
            AoCDay today = days[d];
            String filename =input_files[d];
            out.printf("Running day: %d ... \n", today.getDay());
            long time = today.doDay(filename);
            String[] results = today.answers;
            out.printf("\t\tpart1:\t\t%s\n", results[0]);

            out.printf("\t\tpart2:\t\t%s\n", results[1]);
            out.printf("\t\t\t total time: %.1f ms\n", time / 1000000f);

        }
    }


    public static void main(String[] args) throws Exception {
        runAll();


        int day_number = DAY;
        String input_string;
        if (TESTING) {
            input_string = test_files[day_number - 1][TEST_IDX - 1];
        } else {
            input_string = input_files[day_number - 1];
        }

//            long start, end;
//            if (TIMING) {
//                start = System.nanoTime();
//            }
//
//
//           String[] results = Day22.runDay( out,input_string);
//
//            out.printf("\t\tpart1:\t\t%s\n", results[0]);
//            out.printf("\t\tpart2:\t\t%s\n", results[1]);
//            if (TIMING) {
//                end = System.nanoTime();
//                out.printf("\n total time: %.1f ms\n", (end - start) / 1000000f);
//            }

    }

    public static AoCDay[] getAllDays() {
        AoCDay[] days = new AoCDay[25];

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


}

