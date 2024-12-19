package src.main.java.aoc_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.System.out;

public class Day19 {

    public static final String PART1_ANSWER = "367";
    public static final String PART2_ANSWER = "724388733465031";


    private static final HashMap<String, Boolean> cache = new HashMap<>();
    private static final HashMap<String, Long> ways_cache = new HashMap<>();
    private static ArrayList<String> patterns;
    private static ArrayList<String> designs;

    public static String[] runDay(String inputString) {
        out.println("Advent of Code 2024");
        out.print("\tDay  19");
        if (AdventOfCode2024.TESTING) {
            out.print("\t (testing)");
        }
        out.println();
        try {
            String[] answers = {"", ""};
            String INPUT = Files.readString(Path.of(inputString));

            parseInput(INPUT);
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
        } catch (IOException e) {
            throw new RuntimeException("error opening file " + inputString + " " + e);
        }
    }

    private static void parseInput(String INPUT) throws IOException {
        String[] lines = INPUT.split("\n");
        patterns = new ArrayList<>(Arrays.stream(lines[0].split(",")).map(String::trim).toList());
        designs = new ArrayList<>();

        for (int i = 2; i < lines.length; i++) {
            String des = lines[i].trim();
            designs.add(des);
        }
    }

    public static String getPart1() {
        int count = 0;
        for (String design : designs) {
            if (canMake(design, filterPatterns(design, patterns))) {
                count++;
            }
        }
        long answer = count;
        return String.valueOf(answer);
    }

    public static String getPart2() {
        long total = 0;
        for (String design : designs) {
            total += countWays(design, filterPatterns(design, patterns));
        }
        long answer = total;
        return String.valueOf(answer);
    }

    private static ArrayList<String> filterPatterns(String design, ArrayList<String> patterns) {
        ArrayList<String> viable = new ArrayList<>();
        for (String pat : patterns) {
            if (design.contains(pat)) {
                viable.add(pat);
            }
        }
        return viable;
    }

    private static Boolean canMake(String design, ArrayList<String> patterns) {
        if (design.isEmpty()) return true;
        if (cache.containsKey(design)) {
            return cache.get(design);
        }

        for (String pattern : patterns) {
            if (design.startsWith(pattern)) {
                boolean doable = canMake(design.substring(pattern.length()), patterns);
                if (doable) return true;
            }
        }
        cache.put(design, false);
        return false;

    }

    private static long countWays(String design, ArrayList<String> patterns) {
        if (!canMake(design, patterns)) {
            return 0;
        }
        if (design.isEmpty()) return 1;
        if (ways_cache.containsKey(design)) {
            return ways_cache.get(design);
        }

        long ways = 0;
        for (String pattern : patterns) {
            if (design.startsWith(pattern)) {
                ways += countWays(design.substring(pattern.length()), patterns);
            }
        }
        ways_cache.put(design, ways);
        return ways;
    }

}