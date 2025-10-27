package advent_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day05 extends AoCDay {
    public static final String PART1_ANSWER = "4814";
    public static final String PART2_ANSWER = "5448";
    private static HashMap<Integer, HashSet<Integer>> after_rules;
    private static HashMap<Integer, HashSet<Integer>> before_rules;
    private static LinkedList<LinkedList<Integer>> update_list;

    public Day05(int day) {
        super(day);
    }

    private static boolean checkUpdateArray(int[] updates) {
        for (int i = 1; i < updates.length; i++) {
            int left = updates[i - 1];
            int right = updates[i];
            //before rules
            if (before_rules.containsKey(right)) {
                if (!before_rules.get(right).contains(left)) {

                    return false;
                }
            }
            //after rules
            if (after_rules.containsKey(left)) {
                if (!after_rules.get(left).contains(right)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkUpdateList(List<Integer> ups) {
        int[] updates = ups.stream().mapToInt(i -> i).toArray();
        return checkUpdateArray(updates);
    }

    private static void fix_update_list(int[] updates) {
        for (int i = 1; i < updates.length; i++) {
            int left = updates[i - 1];
            int right = updates[i];
            //before rules
            if (before_rules.containsKey(right)) {
                if (!before_rules.get(right).contains(left)) {
                    updates[i - 1] = right;
                    updates[i] = left;

                }
            }
            left = updates[i - 1];
            right = updates[i];

            //after rules
            if (after_rules.containsKey(left)) {
                if (!after_rules.get(left).contains(right)) {
                    updates[i - 1] = right;
                    updates[i] = left;
                }
            }
        }
    }

    private static int get_middle_of_array(int[] ups) {
        int mid_index = ups.length / 2;
        return ups[mid_index];
    }

    private static <T> T get_middle_of_list(List<T> ups) {
        int mid_index = ups.size() / 2;
        return ups.get(mid_index);
    }

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {

        LinkedList<Integer> good_middle_pages = new LinkedList<>();

        for (List<Integer> ups : update_list) {
            boolean updates_good = checkUpdateList(ups);
            if (updates_good) {
                good_middle_pages.add(get_middle_of_list(ups));
            }
        }

        int answer = good_middle_pages.stream().mapToInt(i -> i).sum();
        return Integer.toString(answer);
    }

    protected String getPart2() {
        List<LinkedList<Integer>> bad_updates = update_list.stream().filter((ls) -> !checkUpdateList(ls)).toList();

        int mid_value_total = 0;
        for (LinkedList<Integer> b_ls : bad_updates) {

            int[] updates = b_ls.stream().mapToInt(i -> i).toArray();


            do {

                fix_update_list(updates);
            } while (!checkUpdateArray(updates));


            int m = get_middle_of_array(updates);

            mid_value_total += m;
        }


        int answer = mid_value_total;
        return Integer.toString(answer);
    }

    protected void parseInput(String input_filename) throws IOException {
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

}
