package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static java.lang.System.out;

public class Day19 {

        public static final String PART1_ANSWER = "367";
        public static final String PART2_ANSWER = "724388733465031";


        private static HashMap<String,Boolean> cache = new HashMap<>();
        private static HashMap<String,Long> ways_cache = new HashMap<>();
        public static String[] runDay(String inputString)  {
            out.println("Advent of Code 2024");
            out.print("\tDay  19");
            if (AdventOfCode2024.TESTING) {
                out.print("\t (testing)");
            }
            out.println();
            try {
                String[] answers = {"", ""};
                String INPUT = Files.readString(Path.of(inputString));

                answers[0] = getPart1(INPUT);
                answers[1] = getPart2(INPUT);

                if (!AdventOfCode2024.TESTING) {
                    if (!answers[0].equals(PART1_ANSWER)) {
                        out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[0], PART1_ANSWER);
                    }

                    if (!answers[1].equals(PART2_ANSWER)) {
                        out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[1], PART2_ANSWER);
                    }
                }
                return answers;
            } catch (IOException e) {
                throw new RuntimeException("error opening file " + inputString + " " + e);
            }
        }




        public static String getPart1(String INPUT) {
            String[] lines = INPUT.split("\n");
            ArrayList<String> patterns = new ArrayList<>(Arrays.stream(lines[0].split(",")).map(String::trim).toList());
            ArrayList<String> designs = new ArrayList<>();

            for(int i=2; i < lines.length; i++) {
                String des = lines[i].trim();
                designs.add(des);
            }



            int count=0;
            for(int i=0; i < designs.size(); i++) {
                String design = designs.get(i);
                ArrayList<String> new_pat = filterPatterns(design, patterns);

                boolean good = canMake(design, new_pat);
                out.printf("\tdesign: %s \t doable: %b\t\t(good: %d)\n", design, good, count);

                if(good) {
                    count++;
                }
            }

            long answer = count;
            return String.valueOf(answer);
        }

    public static String getPart2(String INPUT) {
            out.println("\n part 2\n");
        String[] lines = INPUT.split("\n");
        ArrayList<String> patterns = new ArrayList<>(Arrays.stream(lines[0].split(",")).map(String::trim).toList());
        ArrayList<String> designs = new ArrayList<>();

        for(int i=2; i < lines.length; i++) {
            String des = lines[i].trim();
            designs.add(des);
        }
        long total =0;
        for(int i=0;i< designs.size(); i++){
            String design = designs.get(i);
            long count = countWays(design, filterPatterns(design,patterns));

            total += count;
        }
        long answer = total;
        return String.valueOf(answer);
    }


        private static ArrayList<String> filterPatterns(String design, ArrayList<String> patterns){
            ArrayList<String> viable = new ArrayList<>();
            for(String pat: patterns) {
                if(design.contains(pat)){
                    viable.add(pat);
                }
            }
        //    out.printf("filtered from %d to %d, eliminating %d\n", patterns.size(), viable.size(), patterns.size() - viable.size());
            return viable;
        }
        private static Boolean canMake(String design, ArrayList<String> patterns) {
            if(design.isEmpty()) return true;
            if(cache.containsKey(design)){

                return cache.get(design);
            }


            ArrayList<String> pats_to_use  = patterns;

            for(String pattern: pats_to_use){
                if(design.startsWith(pattern)){
                    boolean doable = canMake(design.substring(pattern.length()), patterns);
                    if(doable) return true;
                }
            }
            cache.put(design,false);
            return false;

        }

        private static long countWays(String design,ArrayList<String> patterns){
            if(!canMake(design,patterns)) {
                return 0;
            }
            if(design.isEmpty()) return 1;
            long ways =0;
            if(ways_cache.containsKey(design)) {
                return ways_cache.get(design);
            }

            for(String pattern: patterns) {
                if(design.startsWith(pattern)){
                    ways+= countWays(design.substring(pattern.length()), patterns);
                }
            }
            ways_cache.put(design,ways);

            return ways;
        }

}