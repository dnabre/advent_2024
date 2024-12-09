package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.IntStream;

import static java.lang.System.out;


public class Day09 {

    public static final String PART1_ANSWER = "6340197768906";
    public static final String PART2_ANSWER = "6363913128533";
    private static char[] packed_disk;


    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.println("\tDay  9");

        String[] answers = {"", ""};
        parseInput(inputString);
        answers[0] = getPart1();
        answers[1] = getPart2();

        if (!AdventOfCode2024.TESTING) {
            if (!answers[0].equals(PART1_ANSWER)) {
                out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[0], PART1_ANSWER);
            }

            if (!answers[1].equals(PART2_ANSWER)) {
                out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[1], PART2_ANSWER);
            }
        }
        return answers;
    }

    public static void parseInput(String filename) throws IOException {
        String raw_input = Files.readString(Path.of(filename));
        packed_disk = raw_input.toCharArray();

    }

    public static String getPart1() {
        int[] disk = getUnpackedDisk(packed_disk);
        System.out.printf("checksum: %d\n", getChecksum(disk));
        prettyPrintDisk(disk);
        int used_space = 0;
        for (int v : disk) {
            if (v != -1) {
                used_space++;
            }
        }
        int right_idx = disk.length - 1;
        int left_idx = 0;
        while (right_idx > used_space) {
            while (disk[left_idx] != -1) {
                left_idx++;
            }
            while (disk[right_idx] == -1) {
                right_idx--;
            }
            disk[left_idx] = disk[right_idx];
            disk[right_idx] = -1;
        }
        long answer = getChecksum(disk);
        return String.valueOf(answer);
    }

    public static String getPart2() throws IOException{
        ArrayList<FileBlock> file_list = new ArrayList<>();
        ArrayList<EmptyBlock> empty_list = new ArrayList<>();

        char[] packed = packed_disk;
        ArrayList<Integer> disk_list = new ArrayList<>();
        int file_no = 0;
        boolean isFile = true;

        for (char ch : packed) {
            int chick = Character.getNumericValue(ch);
            int to_write = -1;
            if (isFile) {
                to_write = file_no;
                FileBlock fb = new FileBlock(file_no, disk_list.size(), chick);
                file_list.add(fb);
                file_no++;
            } else {
                if (chick != 0) {
                    EmptyBlock eb = new EmptyBlock(disk_list.size(), chick);
                    empty_list.add(eb);
                }
            }

            for (int count = 0; count < chick; count++) {
                disk_list.add(to_write);
            }
            isFile = !isFile;
        }

        int[] disk = disk_list.stream().flatMapToInt(IntStream::of).toArray();

        prettyPrintDisk(disk);
        file_list.sort(Comparator.comparingInt(f -> -f.number));
        TreeSet<EmptyBlock> empty_blocks = new TreeSet<>(empty_list);

        for (FileBlock f : file_list) {
            out.println(f);
            int need_size = f.size;
            EmptyBlock go_here = null;
            for (EmptyBlock eb : empty_blocks) {
                if (eb.size >= need_size) {
                    go_here = eb;
                    out.println("\tfound block" + go_here);
                    break;
                }
            }

            if (go_here == null) {
                out.println("could not place file: " + f);
            } else {
                empty_blocks.remove(go_here);

                for (int i = 0; i < f.size; i++) {
                    out.print("\t");
                    prettyPrintDisk(disk);
                    disk[go_here.offset + i] = disk[f.offset + i];
                }
                EmptyBlock freed_space = new EmptyBlock(f.offset, f.size);
                for (int i = 0; i < f.size; i++) {
                    disk[f.offset + i] = -1;
                }
                if (go_here.size > f.size) {
                    // left over space to save
                    EmptyBlock left_over = null;
                    int offset = go_here.offset() + f.size;
                    int size = go_here.size - f.size;
                    if (offset + size == freed_space.offset) {
                        //merge two freed blocks
                        left_over = new EmptyBlock(offset, size + freed_space.size);
                        empty_blocks.add(left_over);
                        freed_space = null;
                    } else {
                        left_over = new EmptyBlock(go_here.offset + f.size, go_here.size() - f.size);
                    }
                }
                if (freed_space != null) {
                    empty_blocks.add(freed_space);
                }

            }
            prettyPrintDisk(disk);
        }
        out.println("defrag done");
        long cs = getChecksum(disk);
        out.printf("checksum: %d \n", cs);
        prettyPrintDisk(disk);
        long answer = getChecksum(disk);
        return String.valueOf(answer);
    }


    private static long getChecksum(int[] disk_for_checksum) {
        long checksum = 0;
        for (int i = 0; i < disk_for_checksum.length; i++) {
            if(disk_for_checksum[i] == -1) continue;

            checksum += (i * disk_for_checksum[i]);
        }
        return checksum;
    }

    private static int[] getUnpackedDisk(char[] packed) {
        ArrayList<Integer> disk_list = new ArrayList<>();
        int file_no = 0;
        boolean isFile = true;

        for (char ch : packed) {
            int chick = Character.getNumericValue(ch);
            int to_write = -1;
            if (isFile) {
                to_write = file_no;
                file_no++;

            }
            for (int count = 0; count < chick; count++) {
                disk_list.add(to_write);
            }
            isFile = !isFile;
        }

        return disk_list.stream().flatMapToInt(IntStream::of).toArray();

    }

    private static void prettyPrintDisk(int[] p_disk) {
        for (int v : p_disk) {
            if (v == -1) {
                out.print('.');
            } else {
                out.print(v);
            }
        }
        out.printf("\t size: %d \n", p_disk.length);
    }

    sealed interface BlockSpan {
    }

    record FileBlock(int number, int offset, int size) implements BlockSpan {
    }

    record EmptyBlock(int offset, int size) implements BlockSpan, Comparable<EmptyBlock> {

        @Override
        public int compareTo(EmptyBlock other) {
            return Integer.compare(this.offset, other.offset);
        }
    }

}