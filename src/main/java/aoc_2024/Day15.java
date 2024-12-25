package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static java.lang.System.out;
import static src.main.java.aoc_2024.Directions.Compass;

public class Day15 {

    public static final String PART1_ANSWER = "1471826";
    public static final String PART2_ANSWER = "1457703";

    private static char[][] initial_grid;
    private static Vector2d grid_sizes;


    private static Vector2d robot_start;
    private static Compass[] move_list;


    public static String[] runDay(PrintStream out, String inputString) throws IOException {
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


    public static void parseInput(String INPUT) throws IOException {

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

    public static String getPart1() {
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
                    // any other tile blocking the scan (wall, monkey, robot, elephant), we do nothing.
                }
                default -> {
                    out.printf("robot is trying to step into unknown ('%c')\n", ch);
                    System.exit(-1);
                }

            }
        }


        long gps_sum = 0L;
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

    private static Vector2d maxes;
    record Box(Vector2d left, Vector2d right){
        boolean doesBoxHitBox(Box box, Compass dir) {
            Vector2d b_left = box.left.plus(dir.coordDelta());
            Vector2d b_right = box.right.plus(dir.coordDelta());
            return (b_left.equals(left) || b_left.equals(right)) || ((b_right.equals(left) || b_right.equals(right)));
        }
        boolean canMove(Compass dir) {
            out.printf("Box.canMove on (%s)\n", this);

            Vector2d target_loc_left = left.plus(dir.coordDelta());
            Vector2d target_loc_right = right.plus(dir.coordDelta());

            if(walls.contains(target_loc_left) || walls.contains(target_loc_right)) {
                // hit wall
                return false;
            }


            Box left_hit = null;
            Box right_hit = null;
            switch (dir) {
                case NORTH -> {
                    if(box_map.containsKey(target_loc_left)) {
                      left_hit = box_map.get(target_loc_left);
                    }
                    if(box_map.containsKey(target_loc_right)) {
                        right_hit = box_map.get(target_loc_right);
                    }
                    assert(left_hit != this);
                    assert(right_hit != this);
                    if((left_hit == null) && (right_hit==null)){
                        return true;
                    }
                    if(left_hit==null) {
                        return right_hit.canMove(dir);
                    }
                    if(right_hit ==null){
                        return left_hit.canMove(dir);
                    }

                    // both aren't null
                    if(left_hit.equals(right_hit)) {
                        return left_hit.canMove(dir);
                    } else {
                        return left_hit.canMove(dir) && right_hit.canMove(dir);
                    }



                }
                case EAST -> {
                }
                case SOUTH -> {
                }
                case WEST -> {
                }
            }




            return true;

        }


        public void doMove(Compass dir) {
        }
    }
    private static char[][] grid;
    private static ArrayList<Box> box_list;
    private static HashSet<Vector2d> walls;
    private static HashMap<Vector2d,Box> box_map;

    public static String getPart2(String INPUT) {
        List<List<String>> blocks = AoCUtils.breakDataByNewline(INPUT);
        ArrayList<Compass> moves = getMoves(blocks.get(1));

        List<String> grid_lines = blocks.get(0);

        char[][] grid = getExpandedGrid(grid_lines);

        maxes.x = grid.length;
        maxes.y = grid[0].length;
        out.printf("maxes: %s\n", maxes);

        box_list =new ArrayList<>();
        walls = new HashSet<>();
        box_map= new HashMap<>();



        for(int y=0; y < maxes.y; y++) {
            for(int x=0; x < maxes.x; x++) {
                char ch = grid[x][y];
                if (ch == '#') {
                    walls.add(new Vector2d(x,y));
                }
                if(ch =='[') {
                    Box b = new Box(new Vector2d(x,y), new Vector2d(x+1,y));
                    box_list.add(b);
                    box_map.put(b.left,b);
                    box_map.put(b.right,b);
                }
                out.print(grid[x][y]);
            }
            out.println();
        }
// -----------------------------------------------------------------------------------------


        Vector2d robot_current = robot_start;
        for(Compass dir: moves) {
            boolean robot_move_good = true;
            Vector2d robot_target = robot_start.plus(dir.coordDelta());


            if(walls.contains(robot_target)){
                robot_move_good = false;
            }

            if(box_map.containsKey(robot_target)) {
               // push box
               Box b = box_map.get(robot_target);
               if(b.canMove(dir)){
                   b.doMove(dir);
               } else {
                   robot_move_good = false;
               }
            }
            if(robot_move_good) {
                robot_current = robot_target;
            }

            break;
        }






        // scan through boxes until we hit wall or empty space
        // if wall, we can't move -- done
        // if free, update our position and put correct box half in empty space
        // remember location of empty space
        // do a sweep through boxes on row moved happened (2 if cols) fixing any box whose
        //      halves don't line up.



        long answer = -1;
        return String.valueOf(answer);
    }

    private static char[][] getExpandedGrid(List<String> grid_lines) {
        int line_count = 0;
        List<String> expanded_lines = new ArrayList<>();
        int max_line_length = Integer.MIN_VALUE;
        for (String ln : grid_lines) {
            String e_line = expand(ln);
            if(max_line_length < e_line.length()) {
                max_line_length = e_line.length();
            }
            expanded_lines.add(e_line);
            line_count++;
        }

        maxes = new Vector2d(max_line_length, line_count);
        char[][] grid = new char[maxes.x][maxes.y];
        for(int y=0; y < maxes.y; y++) {
            for(int x=0; x < maxes.x; x++) {
                char[] c_a = expanded_lines.get(y).toCharArray();

                 if(c_a[x] == '@') {
                     robot_start = new Vector2d(x,y);
                     grid[x][y] = '.';
                 } else {
                     grid[x][y] = c_a[x];
                 }
            }
        }
//        AoCUtils.printGrid(grid);
//        AoCUtils.printGridWithSpecial(grid, robot_start, '@');
        return grid;
    }

    private static ArrayList<Compass> getMoves(List<String> other) {
        ArrayList<Compass> moves = new ArrayList<>();
        for(String ll: other) {
            char[] cc = ll.toCharArray();
            for(int i=0; i< cc.length; i++ ) {
                moves.add(Compass.fromChar(cc[i]));
            }
        }
        return moves;
    }


    private static long getGPS(int x, int y) {
        return (100L * y) + x;
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

}





