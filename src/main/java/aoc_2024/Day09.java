package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.System.out;
import static src.main.java.aoc_2024.AoCUtils.LABELS;


public class Day09 {

    public static final String PART1_ANSWER = "4814";
    public static final String PART2_ANSWER = "5448";
    private static int VIEW_WIDTH = 32;
    private static char[] initial_array;
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
        initial_array = raw_input.toCharArray();
    }

    public static String getPart1() {
        out.println(Arrays.toString(initial_array));

        HashMap<Character, FileBlock> labelToFile = new HashMap<>();
        ArrayList<DiskBlock> all_blocks = new ArrayList<>();
        ArrayList<FileBlock> file_blocks = new ArrayList<>();
        ArrayList<FreeSpace> free_blocks = new ArrayList<>();
        boolean isCurrentFile = true;
        int label_idx = 0;
        int offset = 0;
        int file_id = 0;
        for (int i = 0; i < initial_array.length; i++) {
            char ch = initial_array[i];
            int block_size = Character.getNumericValue(ch);
            if (block_size==0) {
                isCurrentFile = !isCurrentFile;
                continue;
            }
            DiskBlock db = null;
            if (isCurrentFile) {
                char label = LABELS[label_idx];
                FileBlock fb = new FileBlock(file_id, label, offset, block_size);
                labelToFile.put(label, fb);
                file_blocks.add(fb);
                label_idx++;
                file_id++;
                db = fb;
            } else {
                FreeSpace fs = new FreeSpace(offset, block_size);
                free_blocks.add(fs);
                db = fs;
            }

            all_blocks.add(db);
            offset += block_size;
            isCurrentFile = !isCurrentFile;
        }
        file_id--;
        out.printf("file_id: %d\n", file_id);
        out.printf("number of files: %d \n", file_blocks.size());
        out.printf("number of free blocks: %d\n", free_blocks.size());
        out.printf("total number of blocks: %d\n", all_blocks.size());


        char[] disk = getDiskView(initial_array);
        for(int i=0; i< free_blocks.size(); i++) {
            FreeSpace fs = free_blocks.get(i);
            out.printf("Checking FreeSpace block %d, %s \n \t", i, fs);

            int start = fs.orig_offset;
            for(int sp=0; sp < fs.size; sp++) {
                char ch = disk[start+sp];
                out.print(ch);
            }
            out.println();
        }



        initial_array = null;
        out.printf("initializing disk from input array: \n\t\t");
        printDiskView(disk);
        out.println();
        out.println("---------------------------------------------------------------------");
        out.println("\t\t starting file move");
        out.println("---------------------------------------------------------------------");

        for (int file_no = file_id; file_no >= 0; file_no--) {

            FileBlock file_b = file_blocks.get(file_no);
            FreeSpace free_b = null;

            out.printf("\nprocessing file number %d, %s \n", file_no, file_b);
            out.printf("\t disk before move: \n");
            printDiskView(disk);


            int file_b_left = file_b.size;
            int file_idx = 0; // index into file
            while (!free_blocks.isEmpty()) {
                assert free_b == null;
                free_b = free_blocks.removeFirst();
                int free_b_left = free_b.size;
                int free_idx = 0; // index into current free space block
                out.printf("top of loop, free_b: %s\n", free_b);

                // move blocks until we run out of free space or file-blocks to move
                while ((free_b_left > 0) && (file_b_left > 0)) {
                    int free_target_i = free_b.orig_offset + free_idx;
                    int file_target_i = file_b.orig_offset + file_b.size - 1 - file_idx;
                    //chSwap(disk,free_target_i,file_target_i  );
                    disk[free_target_i] = disk[file_target_i];
                    disk[file_target_i] = '.';
                    free_b_left--;
                    file_b_left--;
                    free_idx++;
                    file_idx++;
                    printDiskView(disk);



                }
                out.printf("copyloop done, free_b_left: %d, file_b_left: %d, free_idx: %d, file_idx: %d ", free_b_left, file_b_left, free_idx, file_idx);

                if (free_b_left == 0 && file_b_left == 0) {
                    //blocks matched perfectly
                    //discard free block so we know to get a new one
                    out.println("\t\t file_b and free_b match, file move done");
                    free_b = null;
                    break;
                }

                if (free_b_left == 0) {
                    //ran out of space in free block
                    out.println("\t\t free_block exhausted, acquiring new one");


                    if (free_blocks.isEmpty()) {
                        out.println("incomplete file move, no free space available");
                        break;
                    } else {
                        free_b = free_blocks.removeFirst();

                        continue;
                    }
                }
                if (file_b_left == 0) {
                    out.println("done moving file, put leftover free block back");
                    // done moving file, free block has space left;

                    int used_blocks = free_b.size - free_b_left;
                    int new_offset = free_b.orig_offset + used_blocks;
                    int new_size = free_b.size - used_blocks;

                    FreeSpace leftover = new FreeSpace(new_offset, new_size);
                    free_blocks.addFirst(leftover);
                    free_b = null;
                    break;
                }
            }
            out.print(" move finished: \t");
            printDiskView(disk);

        }


        int checksum = getChecksum(disk);
        int answer = checksum;
        return String.valueOf(answer);
    }

    private static void printDiskView(char[] disk_stringifying) {
        StringBuilder sb = new StringBuilder();
        int len = disk_stringifying.length;
        if (len <= 64) {
            for (char ch : disk_stringifying) {
                sb.append(ch);
            }
            sb.append(String.format("\t size: %d \n", len));
        } else {
            int idx = 0;
            while (idx < len) {
                for (int w = 0; (w < VIEW_WIDTH) && (idx < len); w++) {
                    sb.append(disk_stringifying[idx]);
                    idx++;
                }
                sb.append(String.format("\n size: %d\n", len));
            }
        }
        System.out.println(sb.toString());

    }

    private static char[] getDiskView(char[] block_array) {
        boolean isCurrentFile = true;
        int label_idx = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < block_array.length; i++) {
            int block_size = Character.getNumericValue(block_array[i]);
            if (isCurrentFile) {
                char label = LABELS[label_idx];
                label_idx++;
                sb.append(String.valueOf(label).repeat(block_size));

            } else {
                char ch = '.';
                sb.append(String.valueOf(ch).repeat(block_size));
            }
            isCurrentFile = !isCurrentFile;
        }
        String disk_view = sb.toString();
        return disk_view.toCharArray();
    }


    private static int getChecksum(char[] disk_for_checksum) {
        int checksum = 0;
        for (int i = 0; i < disk_for_checksum.length; i++) {
            char ch = disk_for_checksum[i];
            if (ch == '.') {
                return checksum;
            }
            int v_ch = Character.getNumericValue(ch);
            checksum += (v_ch * i);
        }
        return checksum;
    }


    public static String getPart2() {
        out.print(Arrays.toString(initial_array));
        out.printf(" , size: %d\n", initial_array.length);
        int total_number_blocks = 0;
        for (int i = 0; i < initial_array.length; i++) {
            total_number_blocks += Character.getNumericValue(initial_array[i]);
        }
        out.printf("total number of blocks: %d\n", total_number_blocks);


        boolean isCurrentFile = true;
        int file_idx = 0;
        int label_idx = 0;
        StringBuilder sb = new StringBuilder(total_number_blocks);
        for (int i = 0; i < initial_array.length; i++) {
            int block_size = Character.getNumericValue(initial_array[i]);
            if (isCurrentFile) {
                char label = LABELS[label_idx];
                label_idx++;
                sb.append(String.valueOf(label).repeat(block_size));
                file_idx++;
            } else {
                char ch = '.';
                sb.append(String.valueOf(ch).repeat(block_size));
            }
            isCurrentFile = !isCurrentFile;
        }
        String disk_view = sb.toString();
        char[] disk_view_chars = disk_view.toCharArray();
        out.printf("disk_view size: %d\n", disk_view.length());
        out.println(disk_view);


        int answer = 2;
        return Integer.toString(answer);
    }


    sealed interface DiskBlock {
    }

    record FreeSpace(int orig_offset, int size) implements DiskBlock {
    }

    record FileBlock(int number, char label, int orig_offset, int size) implements DiskBlock {
    }


}