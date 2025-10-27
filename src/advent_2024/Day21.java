package advent_2024;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class Day21 extends AoCDay {
    public static final String PART1_ANSWER = "157230";
    public static final String PART2_ANSWER = "195969155897936";
    private static final int PART1_NUMBER_OF_ROBOTS = 2;
    private static final int PART2_NUMBER_OF_ROBOTS = 25;
    private static final char[][] dirPad = {{' ', '^', 'A'}, {'<', 'v', '>'}};
    private static final char[][] numPad = {{'7', '8', '9'}, {'4', '5', '6'}, {'1', '2', '3'}, {' ', '0', 'A'}};
    public static String[] lines;
    private static HashMap<CodeSpan, String> dirPadMoves;
    private static HashMap<Order, Long> keyToLongCache;
    private static HashMap<CodeSpan, String> numPadMoves;

    public Day21(int day) {
        super(day);
    }

    private static String buildCode(String code, int robot) {
        if (robot == 0) {
            return code;
        }
        if (code.length() == 1) {
            return code;
        }
        StringBuilder total = new StringBuilder();
        for (String move : code.split("A")) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i <= move.length(); i++) {
                char start = 'A';
                char end = 'A';
                if (i != 0) {
                    start = move.charAt(i - 1);
                }
                if (i != move.length()) {
                    end = move.charAt(i);
                }
                CodeSpan m = new CodeSpan(start, end);

                sb.append(dirPadMoves.get(m));
                sb.append("A");
            }

            total.append(buildCode(sb.toString(), robot - 1));
        }

        return total.toString();
    }

    private static long buildCode_LengthOnly(String code, int robot) {
        if (robot == 0) {
            return code.length();
        }
        if (code.length() == 1) {
            return 1;
        }

        Order order = new Order(code, robot);
        if (keyToLongCache.containsKey(order)) {
            return keyToLongCache.get(order);
        }
        long total = 0;
        for (String move : code.split("A")) {
            //      out.printf("countChars, code: %s, depth: %d, move: %s\n", code,depth,move);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i <= move.length(); i++) {
                char start = 'A';
                char end = 'A';
                if (i != 0) {
                    start = move.charAt(i - 1);
                }
                if (i != move.length()) {
                    end = move.charAt(i);
                }
                CodeSpan m = new CodeSpan(start, end);

                sb.append(dirPadMoves.get(m));
                sb.append("A");
            }
            total += buildCode_LengthOnly(sb.toString(), robot - 1);
        }
        keyToLongCache.put(order, total);
        return total;
    }

    private static long findLengthOfShortestCommand(String code) {

        String numpad_code = getEndNumericCommand(code);
        return buildCode_LengthOnly(numpad_code, PART2_NUMBER_OF_ROBOTS);
    }

    private static String findShortestCommand(String code0) {
        String numpad_code = getEndNumericCommand(code0);
        return buildCode(numpad_code, PART1_NUMBER_OF_ROBOTS);
    }

    private static String getEndNumericCommand(String code) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < code.length(); i++) {
            char start_char;
            if (i == 0) {
                start_char = 'A';
            } else {
                start_char = code.charAt(i - 1);
            }
            CodeSpan cmd = new CodeSpan(start_char, code.charAt(i));
            sb.append(numPadMoves.get(cmd));
            sb.append("A");
        }
        return sb.toString();
    }

    private static String orderArmMovements(Vector2d start, Vector2d end, boolean numeric) {
        int dx = end.x - start.x;
        int dy = end.y - start.y;
        String v = ((dy > 0) ? "v" : "^").repeat(Math.abs(dy));
        String h = ((dx > 0) ? ">" : "<").repeat(Math.abs(dx));
        String right = v + h;
        String left = h + v;
        if (numeric) {
            if ((start.y == numPad.length - 1) && (end.x == 0)) {
                return right;

            } else if ((start.x == 0) && (end.y == numPad.length - 1)) {
                return left;
            }
        } else {
            if (start.x == 0) {
                return left;
            } else if (end.x == 0) {
                return right;
            }
        }
        if ((dy < 0 && dx < 0) || (dy > 0 && dx < 0)) {
            return left;
        }
        if ((dy < 0 && dx > 0) || (dy > 0 && dx > 0)) {
            return right;
        }
        return left;
    }

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        long total = 0;
        for (String code : lines) {
            long numeric_part_code = Long.parseLong(code.replaceAll("\\D", ""));
            String command = findShortestCommand(code);
            long complexity = numeric_part_code * command.length();
            total += complexity;
        }
        long answer = total;
        return String.valueOf(answer);
    }

    protected String getPart2() {
        // Part 2 generates really massive command sequences, just keep track of their length
        long total = 0;
        for (String code : lines) {
            long numeric_part_code = Long.parseLong(code.replaceAll("\\D", ""));
            long command_length = findLengthOfShortestCommand(code);
            total += numeric_part_code * command_length;
        }
        long answer = total;
        return String.valueOf(answer);
    }

    protected void parseInput(String filename) throws IOException {
        lines = Files.readAllLines(Path.of(filename)).toArray(new String[0]);
        HashMap<Character, Vector2d> numeric_pad_position_map = new HashMap<>();
        for (int y1 = 0; y1 < numPad.length; y1++) {
            for (int x1 = 0; x1 < numPad[0].length; x1++) {
                char ch1 = numPad[y1][x1];
                if (ch1 != ' ') {
                    numeric_pad_position_map.put(numPad[y1][x1], new Vector2d(x1, y1));
                }
            }
        }
        HashMap<Character, Vector2d> directional_pad_position_map = new HashMap<>();
        for (int y = 0; y < dirPad.length; y++) {
            for (int x = 0; x < dirPad[0].length; x++) {
                char ch = dirPad[y][x];
                if (ch != ' ') {
                    directional_pad_position_map.put(dirPad[y][x], new Vector2d(x, y));
                }
            }
        }

        numPadMoves = new HashMap<>();
        for (char left : numeric_pad_position_map.keySet()) {
            for (char right : numeric_pad_position_map.keySet()) {
                CodeSpan n_cmd = new CodeSpan(left, right);
                String n_order = orderArmMovements(numeric_pad_position_map.get(left), numeric_pad_position_map.get(right), true);
                numPadMoves.put(n_cmd, n_order);
            }
        }
        dirPadMoves = new HashMap<>();
        for (char left : directional_pad_position_map.keySet()) {
            for (char right : directional_pad_position_map.keySet()) {
                CodeSpan n_cmd = new CodeSpan(left, right);
                String n_order = orderArmMovements(directional_pad_position_map.get(left), directional_pad_position_map.get(right), false);
                dirPadMoves.put(n_cmd, n_order);
            }
        }
        keyToLongCache = new HashMap<>();
    }

    record CodeSpan(char start, char end) {
    }

    record Order(String code, int robot_number) {
    }

}