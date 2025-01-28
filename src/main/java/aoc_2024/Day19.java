package src.main.java.aoc_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static java.lang.System.out;


public class Day19 extends AoCDay {

    public static final String PART1_ANSWER = "367";
    public static final String PART2_ANSWER = "724388733465031";
    private static final HashMap<String, Boolean> cache = new HashMap<>();
    private static ArrayList<String> designs;
    private static ArrayList<String> patterns;
    private static final HashMap<String, Long> ways_cache = new HashMap<>();

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        int count = 0;
        for (String design : designs) {
            if (canMake(design, filterPatterns(design, patterns))) {
                count++;
            }
        }
        long answer = count;
        return String.valueOf(answer);
    }

    protected String getPart2() {
        long total = 0;
        for (String design : designs) {
            total += countWays(design, filterPatterns(design, patterns));
        }
        long answer = total;
        return String.valueOf(answer);
    }

    protected void parseInput(String input_filename) throws IOException {
        String[] lines = Files.readAllLines(Path.of(input_filename)).toArray(new String[0]);
        patterns = new ArrayList<>(Arrays.stream(lines[0].split(",")).map(String::trim).toList());
        designs = new ArrayList<>();

        for (int i = 2; i < lines.length; i++) {
            String des = lines[i].trim();
            designs.add(des);
        }
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

    private static ArrayList<String> filterPatterns(String design, ArrayList<String> patterns) {
        ArrayList<String> viable = new ArrayList<>();
        for (String pat : patterns) {
            if (design.contains(pat)) {
                viable.add(pat);
            }
        }
        return viable;
    }

    public Day19(int day) {
        super(day);
    }

}