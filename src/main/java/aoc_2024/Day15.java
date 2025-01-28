package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static src.main.java.aoc_2024.Directions.Compass;

public class Day15 extends AoCDay {

    public static final String PART1_ANSWER = "1471826";
    public static final String PART2_ANSWER = "1457703";
    private static HashSet<Box> box_list;
    private static HashMap<Vector2d, Box> box_map;
    private static Vector2d grid_sizes;
    private static char[][] initial_grid;
    private static Vector2d maxes;
    private static Compass[] move_list;
    private static ArrayList<Box> new_box_locations = null;
    private static Vector2d robot_start;
    private static HashSet<Vector2d> walls;

    private sealed interface CheckReturn {
        CheckReturn and(CheckReturn other);

        boolean isMoveGood();
    }

    public Day15(int day) {
        super(day);
    }

    private record Box(Vector2d left, Vector2d right) {
        Box {
            if (left.equals(right)) {
                throw new IllegalArgumentException("Parameters left and right cannot be equal");
            }
        }

        CheckReturn canMove(Compass dir) {
            Vector2d target_loc_left = left.plus(dir.coordDelta());
            Vector2d target_loc_right = right.plus(dir.coordDelta());
            if (walls.contains(target_loc_left)) {
                return new Wall(target_loc_left);
            }
            if (walls.contains(target_loc_right)) {
                return new Wall(target_loc_right);
            }
            Box left_hit = null;
            Box right_hit;
            CheckReturn left_ok = null;
            CheckReturn right_ok = null;
            switch (dir) {
                case NORTH, SOUTH -> {
                    if (box_map.containsKey(target_loc_left)) {
                        left_hit = box_map.get(target_loc_left);
                        left_ok = left_hit.canMove(dir);
                    }
                    if (box_map.containsKey(target_loc_right)) {
                        right_hit = box_map.get(target_loc_right);
                        if (right_hit.equals(left_hit)) {
                            right_ok = left_ok;
                        } else {
                            right_ok = right_hit.canMove(dir);
                        }
                    }
                }
                case EAST -> {
                    if (box_map.containsKey(target_loc_right)) {
                        right_hit = box_map.get(target_loc_right);
                        return right_hit.canMove(dir);
                    }
                }
                case WEST -> {
                    if (box_map.containsKey(target_loc_left)) {
                        left_hit = box_map.get(target_loc_left);
                        return left_hit.canMove(dir);
                    }
                }
            }
            if ((left_ok == null) && (right_ok == null)) {
                return new Ok();
            }
            if (left_ok == null) {
                return right_ok;
            }
            if (right_ok == null) {
                return left_ok;
            }

            return left_ok.and(right_ok);
        }

        private void doMove(Compass dir) {
            Vector2d target_loc_left = left.plus(dir.coordDelta());
            Vector2d target_loc_right = right.plus(dir.coordDelta());
            Box new_box = new Box(target_loc_left, target_loc_right);
            Box left_hit = null;
            Box right_hit;
            switch (dir) {
                case NORTH, SOUTH -> {
                    if (box_map.containsKey(target_loc_left)) {
                        left_hit = box_map.get(target_loc_left);
                        left_hit.doMove(dir);
                    }
                    if (box_map.containsKey(target_loc_right)) {
                        right_hit = box_map.get(target_loc_right);
                        if (!right_hit.equals(left_hit)) {
                            right_hit.doMove(dir);
                        }
                    }
                }
                case EAST -> {
                    if (box_map.containsKey(target_loc_right)) {
                        right_hit = box_map.get(target_loc_right);
                        right_hit.doMove(dir);
                    }
                }
                case WEST -> {
                    if (box_map.containsKey(target_loc_left)) {
                        left_hit = box_map.get(target_loc_left);
                        left_hit.doMove(dir);
                    }
                }
            }
            new_box_locations.add(new_box);
            box_list.remove(this);
            box_map.remove(this.left);
            box_map.remove(this.right);
            box_list.add(new_box);
            box_map.put(new_box.left, new_box);
            box_map.put(new_box.right, new_box);

        }

        public int getGPS() {
            int x = left.x;
            int y = left.y;
            return (100 * y) + x;
        }
    }

    private record Ok() implements CheckReturn {
        @Override
        public CheckReturn and(CheckReturn other) {
            return other;
        }


        @Override
        public boolean isMoveGood() {
            return true;
        }
    }

    private record Wall(Vector2d pos) implements CheckReturn {
        @Override
        public CheckReturn and(CheckReturn other) {
            return this;
        }


        @Override
        public boolean isMoveGood() {
            return false;
        }
    }

    public static String[] runDayStatic(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  15");
        if (AdventOfCode2024.TESTING) {
            out.print("\t (testing)");
        }
        out.println();

        String[] answers = {"", ""};
        String INPUT = Files.readString(Path.of(inputString));

        parseInput(INPUT);
        answers[0] = getPart1();
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
    }

    protected static String getPart2(String INPUT) {
        List<List<String>> blocks = AoCUtils.breakDataByNewline(INPUT);
        ArrayList<Compass> moves = getMoves(blocks.get(1));
        List<String> grid_lines = blocks.get(0);
        char[][] grid = getExpandedGrid(grid_lines);
        setupPart2(grid);

        Vector2d robot_current = robot_start;
        for (Compass dir : moves) {
            new_box_locations = new ArrayList<>();
            Vector2d robot_target = robot_current.plus(dir.coordDelta());
            if (walls.contains(robot_target)) {
                continue;
            }
            if (box_map.containsKey(robot_target)) {
                Box b = box_map.get(robot_target);
                CheckReturn r;
                r = b.canMove(dir);
                if (r.isMoveGood()) {
                    b.doMove(dir);
                } else {
                    continue;
                }
            }
            robot_current = robot_target;
        }
        long total = 0;
        for (Box b : box_list) {
            total += b.getGPS();
        }

        long answer = total;
        return String.valueOf(answer);
    }

   protected void parseInput(String INPUT) throws IOException {
        List<List<String>> blocks = AoCUtils.breakDataByNewline(INPUT);
        String text = String.join("\n", blocks.getFirst());
        String move = String.join("", blocks.getLast());
        List<String> lines = text.lines().toList();
        int height = lines.size();
        int width = lines.getFirst().length();

        grid_sizes = new Vector2d(height, width);
        initial_grid = new char[width][height];
        for (int y = 0; y < height; y++) {
            String line = lines.get(y);
            for (int x = 0; x < width; x++) {
                char ch = line.charAt(x);
                initial_grid[x][y] = ch;
                if (ch == '@') {
                    robot_start = new Vector2d(x, y);
                    initial_grid[x][y] = '.';
                }
            }
        }

        move_list = new Compass[move.length()];
        int idx = 0;
        for (char ch : move.toCharArray()) {
            Compass dir = Compass.fromChar(ch);
            move_list[idx] = dir;
            idx++;
        }
    }

    private static String expand(String line) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '#') {
                sb.append("##");
            } else if (c == 'O') {
                sb.append("[]");
            } else if (c == '.') {
                sb.append("..");
            } else if (c == '@') {
                sb.append("@.");
            } else {
                throw new IllegalArgumentException("invalid character: " + c);
            }
        }
        return sb.toString();
    }

    private static char[][] getExpandedGrid(List<String> grid_lines) {
        int line_count = 0;
        List<String> expanded_lines = new ArrayList<>();
        int max_line_length = Integer.MIN_VALUE;
        for (String ln : grid_lines) {
            String e_line = expand(ln);
            if (max_line_length < e_line.length()) {
                max_line_length = e_line.length();
            }
            expanded_lines.add(e_line);
            line_count++;
        }
        maxes = new Vector2d(max_line_length, line_count);
        char[][] grid = new char[maxes.x][maxes.y];
        for (int y = 0; y < maxes.y; y++) {
            for (int x = 0; x < maxes.x; x++) {
                char[] c_a = expanded_lines.get(y).toCharArray();
                if (c_a[x] == '@') {
                    robot_start = new Vector2d(x, y);
                    grid[x][y] = '.';
                } else {
                    grid[x][y] = c_a[x];
                }
            }
        }
        return grid;
    }

    private static int getGPS(int x, int y) {
        return (100 * y) + x;
    }

    private static ArrayList<Compass> getMoves(List<String> other) {
        ArrayList<Compass> moves = new ArrayList<>();
        for (String ll : other) {
            char[] cc = ll.toCharArray();
            for (char c : cc) {
                moves.add(Compass.fromChar(c));
            }
        }
        return moves;
    }

    private static void setupPart2(char[][] grid) {
        maxes.x = grid.length;
        maxes.y = grid[0].length;

        box_list = new HashSet<>();
        walls = new HashSet<>();
        box_map = new HashMap<>();

        for (int y = 0; y < maxes.y; y++) {
            for (int x = 0; x < maxes.x; x++) {
                char ch = grid[x][y];
                if (ch == '#') {
                    walls.add(new Vector2d(x, y));
                }
                if (ch == '[') {
                    Box b = new Box(new Vector2d(x, y), new Vector2d(x + 1, y));
                    box_list.add(b);
                    box_map.put(b.left, b);
                    box_map.put(b.right, b);
                }
            }
        }
    }

    protected String getPart1() {
        int grid_size = -1;
        if (grid_sizes.x == grid_sizes.y) {
            grid_size = grid_sizes.x;
        }
        char[][] grid = new char[grid_size][grid_size];
        for (int y = 0; y < grid_size; y++) {
            for (int x = 0; x < grid_size; x++) {
                grid[x][y] = initial_grid[x][y];
            }
        }
        Vector2d robot_loc = new Vector2d(robot_start);

        for (Compass compass : move_list) {
            Vector2d move_direction = compass.coordDelta();
            Vector2d step_target = robot_loc.plus(move_direction);
            char ch = grid[step_target.x][step_target.y];
            switch (ch) {
                case '.' -> robot_loc.add(move_direction);
                case '#' -> {
                }
                case 'O' -> {
                    Vector2d start_box_push = robot_loc.plus(move_direction);
                    char u_box = grid[start_box_push.x][start_box_push.y];
                    while (u_box == 'O') {
                        start_box_push.add(move_direction);
                        u_box = grid[start_box_push.x][start_box_push.y];
                    }
                    if (u_box == '.') {
                        grid[start_box_push.x][start_box_push.y] = 'O';
                        grid[step_target.x][step_target.y] = '.';
                        robot_loc = step_target;
                    }
                }
                default ->
                        throw new IllegalArgumentException(String.format("robot is trying to step into unknown ('%c')\n", ch));
            }
        }
        int gps_sum = 0;
        for (int y = 0; y < grid_size; y++) {
            for (int x = 0; x < grid_size; x++) {
                char ch = grid[x][y];
                if (ch == 'O') {
                    gps_sum += getGPS(x, y);
                }
            }
        }
        long answer = gps_sum;
        return String.valueOf(answer);
    }
}





