package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.System.out;

public class Day05 {
    public static final String PART1_ANSWER = "2297";
    public static final String PART2_ANSWER = "1745";// too low
    private static HashMap<Integer, HashSet<Integer>> after_rules;
    private static HashMap<Integer, HashSet<Integer>> before_rules;
    private static LinkedList<List<Integer>> update_list;

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.println("\tDay  5");

        String[] answers = {"", ""};
        parseInput(inputString);
        answers[0] = getPart1();
        answers[1] = getPart2();

        if (!answers[0].equals(PART1_ANSWER)) {
            out.printf("\t\tWRONG ANSWER got: %s, expected %s\n", answers[0], Day02.PART1_ANSWER);
        }

        if (!answers[1].equals(PART2_ANSWER)) {
            out.printf("\n\t\tWRONG ANSWER got: %s, expected %s\n", answers[1], Day02.PART2_ANSWER);
        }
        return answers;
    }

    private static void parseInput(String input_filename) throws IOException {
        String[] lines = Files.readAllLines(Path.of(input_filename)).toArray(new String[0]);
        boolean ordering_part = true;
        before_rules = new HashMap<>();
        after_rules = new HashMap<>();
        update_list = new LinkedList<>();

        for (String ln : lines) {
            if (ln.isEmpty()) {
                ordering_part = false;
            } else {
                if (ordering_part) {
                    List<Integer> parts = Arrays.stream(ln.split("\\|")).map(Integer::parseInt).toList();
                    int left = parts.getFirst();
                    int right = parts.getLast();

                    if (after_rules.containsKey(left)) {
                        after_rules.get(left).add(right);
                    } else {
                        HashSet<Integer> hs = new HashSet<>();
                        hs.add(right);
                        after_rules.put(left, hs);
                    }
                    if (before_rules.containsKey(right)) {
                        before_rules.get(right).add(left);
                    } else {
                        HashSet<Integer> hs = new HashSet<>();
                        hs.add(left);
                        before_rules.put(right, hs);
                    }

                } else {
                    List<Integer> parts = Arrays.stream(ln.split(",")).map(Integer::parseInt).toList();
                    LinkedList<Integer> mut_list = new LinkedList<>(parts.stream().toList());
                    update_list.add(mut_list);
                }
            }
        }
    }

    public static String getPart1() {
        out.println(update_list.getFirst());
        // int[] updates = update_list.getFirst().stream().mapToInt(i -> i).toArray();
        int list_count =0;
        for (List<Integer> ups : update_list) {
            list_count++;
            //int[] updates = update_list.getFirst().stream().mapToInt(i -> i).toArray();
            int[] updates = ups.stream().mapToInt(i-> i).toArray();
            boolean updates_good = true;
            out.printf("%-3d update list: %s\n", list_count, ups);
            for (int i = 1; i < updates.length; i++) {
                int left = updates[i - 1];
                int right = updates[i];
                boolean after_good = true;
                boolean before_good = true;
                //before
                if (before_rules.containsKey(right)) {
                    if (!before_rules.get(right).contains(left)) {
                        before_good = false;
                    }
                }
                if(list_count == 4) {
                    out.printf("\t\t pair (%d,%d) after: %b", left, right, before_good);
                }
                //after
                if (after_rules.containsKey(left)) {
                    if (!after_rules.get(left).contains(right)) {
                        after_good = false;
                    }
                }
                if(list_count == 4) {
                    out.printf("\t before: %b \n", after_good);
                }
                updates_good = before_good && after_good && updates_good;
            }
            out.printf("\t%-3d update list: %s is good: %b \n", list_count,ups, updates_good);
        }


        int answer = 1;
        return Integer.toString(answer);
    }

    public static String getPart2() {

        int answer = 2;
        return Integer.toString(answer);
    }
}