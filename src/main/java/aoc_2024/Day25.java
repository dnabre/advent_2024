package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.System.out;


public class Day25 {

    public static final String PART1_ANSWER = "3065";
    public static final String PART2_ANSWER = "-1";






    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  25");
        if (AdventOfCode2024.TESTING) {
            out.print("\t (testing)");
        }
        out.println();

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
    private static ArrayList<int[]> Keys;
    private static ArrayList<int[]> Locks;
    private static final int HEIGHT=7;
    private static final int WIDTH=5;

    static void parseInput(String filename) throws IOException {
        String[] lines = Files.readAllLines(Path.of(filename)).toArray(new String[0]);
        out.printf("read %d lines\n", lines.length);

        Keys=new ArrayList<>();
        Locks = new ArrayList<>();

        boolean key = false;
        int current_line=0;

        char[][] lock ;
        int[] heights;

        for(int i=0; i < lines.length;i++) {
            while(lines[i].equals("")) {
                continue;
            }

            lock = new char[HEIGHT][WIDTH];
            heights= new int[5];
            key = !(lines[i].equals("#####"));

            for(int h=0; h < HEIGHT; h++) {
                char[] c_line = lines[i+h].toCharArray();
                for(int w =0; w < WIDTH; w++) {
                    char ch =  c_line[w];
                    lock[h][w] = ch;
                }
            }
            int[] cols = new int[WIDTH];
            for(int w=0; w <WIDTH; w++) {
                char[] column = new char[HEIGHT];
                for(int h=0; h<HEIGHT; h++) {
                    column[h] = lock[h][w];
                }
                cols[w] = getHeight(column) -1;

            }
            if(key) {
                Keys.add(cols);
            } else {
                Locks.add(cols);
            }


            i+=HEIGHT;
        }
//        out.printf("Keys  (%d):\n", Keys.size() );
//        for(int[] k: Keys) {
//            out.printf("\t %s\n", Arrays.toString(k));
//        }
//
//        out.printf("Locks (%d):\n", Locks.size() );
//        for(int[] l: Locks) {
//            out.printf("\t %s\n", Arrays.toString(l));
//        }
//    out.println();


    }

    private static int getHeight(char[] pinColumn) {
        int count =0;
        for(char c: pinColumn) {
            if(c=='#') {
                count++;
            }
        }
        return count;
    }

    public static String getPart1() {
        int matches =0;
        for(int[] lock:Locks) {
            for(int[] key:Keys) {

                boolean match = true;
                int first_overlap = -1;
                int w=0;
                while((first_overlap< 0) && (w<WIDTH))
                {
                    match = (lock[w] + key[w]  <= WIDTH);
                    if(!match) {
                        first_overlap=w;
                    }
                    w++;
                }
                matches += match?1:0;

//                out.printf("Lock %s and key %s: ", Arrays.toString(lock), Arrays.toString(key));
//                if(match){
//                    out.print(" all columns fit!\n");
//                } else {
//                    out.printf(" overlap in the %s  column!\n",first_overlap==(WIDTH-1)?"last":AoCUtils.ORDINALS[first_overlap+1]);
//                }
            }

        }



        long answer = matches;
        return String.valueOf(answer);
    }


    public static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }
}