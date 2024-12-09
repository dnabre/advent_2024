package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;

import static java.lang.System.out;
import static src.main.java.aoc_2024.AoCUtils.LABELS;


public class Day09 {

    public static final String PART1_ANSWER = "4814"; //439102322 is too low
    //1941764320114 is too low
    //6340197768906
    public static final String PART2_ANSWER = "5448";
    private static char[] packed_disk;
    private static String test_final = "0099811188827773336446555566..............";

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.println("\tDay  9");

        String[] answers = {"", ""};
        parseInput(inputString);
        answers[0] = getPart1();
        //   answers[1] = getPart2();

        if (!answers[0].equals(PART1_ANSWER)) {
            out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[0], PART1_ANSWER);
        }

        if (!answers[1].equals(PART2_ANSWER)) {
            out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[1], PART2_ANSWER);
        }
        return answers;
    }

    public static void parseInput(String filename) throws IOException {
        String raw_input = Files.readString(Path.of(filename));
        packed_disk = raw_input.toCharArray();
    }

    public static String getPart1() {
       out.println(Arrays.toString(packed_disk));
       ArrayList<Integer> disk_list = new ArrayList<>();
       int file_no=0;
       boolean isFile = true;
       int count_free=0;
       for(int i=0; i < packed_disk.length; i++) {
           char ch = packed_disk[i];
           int chick = Character.getNumericValue(ch);
           int to_write=-1;
           if (isFile) {
               to_write = file_no;
               file_no++;
           }
           for(int count=0; count < chick; count++) {
               disk_list.add(to_write);
               if(!isFile) {
                   count_free++;
               }
           }
           isFile = !isFile;
       }
       out.printf("count_free: %d\n", count_free);
        int[] disk= disk_list.stream().flatMapToInt(IntStream::of).toArray();
       // out.println(Arrays.toString(disk));
        prettyPrintDisk(disk);
        int right_idx = disk.length-1;
        int left_idx = 0;
        while(count_free >2) {
            while(disk[left_idx] != -1) {
                left_idx++;
            }
            while(disk[right_idx] == -1) {
                right_idx--;
            }
            disk[left_idx] = disk[right_idx];
            disk[right_idx] = -1;
            count_free--;
            prettyPrintDisk(disk);
        }


        out.println();
        prettyPrintDisk(disk);




        long answer = getChecksum(disk);
        return String.valueOf(answer);
    }

    private static void prettyPrintDisk(int[] p_disk) {
        for(int i=0; i < p_disk.length; i++) {
            int v = p_disk[i];
            if (v == -1) {
                out.print('.');
            } else {
                out.print(v);
            }
        }
        out.printf("\t size: %d \n", p_disk.length);
    }


    private static long getChecksum(int[] disk_for_checksum) {
        long checksum = 0;
        for (int i = 0; i < disk_for_checksum.length; i++) {
            int ch = disk_for_checksum[i];
            if (ch == -1) {
                return checksum;
            }
            checksum += ch * i;
        }
        return checksum;
    }


    public static String getPart2() {
        int answer = 2;
        return Integer.toString(answer);
    }
}