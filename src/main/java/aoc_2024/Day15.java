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
    private static Vector2d maxes;
    private static char[][] grid;
    private static HashSet<Box> box_list;
    private static HashSet<Vector2d> walls;
    private static HashMap<Vector2d, Box> box_map;


    private sealed interface CheckReturn {
        public boolean isMoveGood();
        public CheckReturn and( CheckReturn other);
        public Vector2d getBlocker();
    }
    record Wall(Vector2d pos) implements CheckReturn{
        @Override
        public CheckReturn and(CheckReturn other) {
            return this;
        }

        @Override
        public Vector2d getBlocker() {
            return pos;
        }

        @Override
        public boolean isMoveGood() {
            return false;
        }
    }
    record Ok() implements CheckReturn{
        @Override
        public CheckReturn and(CheckReturn other) {
            return other;
        }

        @Override
        public Vector2d getBlocker() {
            return null;
        }

        @Override
        public boolean isMoveGood() {
            return true;
        }
    }



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
        grid = initial_grid;
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

    public static String getPart2(String INPUT) {
        List<List<String>> blocks = AoCUtils.breakDataByNewline(INPUT);
        ArrayList<Compass> moves = getMoves(blocks.get(1));

        List<String> grid_lines = blocks.get(0);
        char[][] o_expanded_grid = getExpandedGrid(grid_lines);
        char[][] grid = o_expanded_grid;
        out.println("expanded grid:");
        AoCUtils.printGrid(grid);
        out.println("----------------------------------");
        int box_count = getBoxCount(grid);


        // -----------------------------------------------------------------------------------------


        Vector2d robot_current = robot_start;
        for(int i=0; i < moves.size(); i++ ){
            Compass dir = moves.get(i);


            new_box_locations = new ArrayList<>();
            out.printf("\n move[%d]\t pre-move robot: %s, next_move: %s",i, robot_current, dir);
            printSpecialGrid(grid, walls, box_map, robot_current);
            boolean robot_move_good = true;
            Vector2d robot_target = robot_current.plus(dir.coordDelta());

            if (walls.contains(robot_target)) {

                out.println(" wall");

                continue;
            }

            if (box_map.containsKey(robot_target)) {
                // push box
                Box b = box_map.get(robot_target);
                CheckReturn r = null;
                out.printf("checking if box @ %s can move to free %s for move from %s on box %s\n",
                        robot_target,  robot_target, robot_current, b);
                r = b.canMove(dir);
                if (r.isMoveGood()) {
                    out.printf("moving of box: %s in %s, approved\n", b, dir);
                    b.doMove(dir);
                    assert(box_count==box_list.size());
                    assert(box_count == box_map.size()/2);
                } else {
                    out.printf(" box can't move, blocked by wall at %s\n",r.getBlocker() );

                    continue;

                }
            }


            robot_current = robot_target;
            out.printf(" -> %sgood move\n", robot_current);


        }
        out.println("\n all moves done \n");
        printSpecialGrid(grid, walls, box_map, robot_current);

        // scan through boxes until we hit wall or empty space
        // if wall, we can't move -- done
        // if free, update our position and put correct box half in empty space
        // remember location of empty space
        // do a sweep through boxes on row moved happened (2 if cols) fixing any box whose
        //      halves don't line up.


        long answer = -1;
        return String.valueOf(answer);
    }

    private static int getBoxCount(char[][] grid) {
        maxes.x = grid.length;
        maxes.y = grid[0].length;
        out.printf("maxes: %s\n", maxes);

        box_list = new HashSet<>();
        walls = new HashSet<>();
        box_map = new HashMap<>();

        int box_count =0;
        for (int y = 0; y < maxes.y; y++) {
            for (int x = 0; x < maxes.x; x++) {
                char ch = grid[x][y];
                if (ch == '#') {
                    walls.add(new Vector2d(x, y));
                }
                if (ch == '[') {
                    Box b = new Box(new Vector2d(x, y), new Vector2d(x + 1, y));
                    box_list.add(b);
                    box_count++;
                    box_map.put(b.left, b);
                    box_map.put(b.right, b);
                }
            //    out.print(grid[x][y]);
            }
       //     out.println();
        }
        assert(box_count==box_list.size());
        assert(box_count == box_map.size() /2);
        return box_count;
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
//        AoCUtils.printGrid(grid);
//        AoCUtils.printGridWithSpecial(grid, robot_start, '@');
        return grid;
    }

    private static ArrayList<Compass> getMoves(List<String> other) {
        ArrayList<Compass> moves = new ArrayList<>();
        for (String ll : other) {
            char[] cc = ll.toCharArray();
            for (int i = 0; i < cc.length; i++) {
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

    private static void printSpecialGrid(char[][] grid, HashSet<Vector2d> walls, HashMap<Vector2d, Box> boxMap,
                                         Vector2d robotCurrent) {
        out.println();
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                char ch = grid[x][y];
                Vector2d current = new Vector2d(x, y);
                if (robotCurrent.equals(current)) {
                    out.print('@');
                } else if (walls.contains(current)) {
                    out.print('#');
                } else if (boxMap.containsKey(current)) {
                    Box b = box_map.get(current);
                    if (b.left.equals(current)) {
                        out.print('[');
                    }
                    if (b.right.equals(current)) {
                        out.print(']');
                    }
                } else {
                    out.print('.');
                }
            }
            out.println();
        }

    }
    private static ArrayList<Box> new_box_locations =null;

    record Box(Vector2d left, Vector2d right) {
        Box{
            assert(!left.equals(right));
        }

        public static void debug(int boxCount, HashSet<Box> boxList, HashMap<Vector2d, Box> boxMap) {
            out.printf("[BOX.DEBUG] box_count: %d, box_list size: %d, box_map size: %d\n",
                    boxCount, boxList.size(), boxMap.size());

            ArrayList<Vector2d> box_spots = new ArrayList<>();
            ArrayList<Box> test_box_list =new ArrayList<>();
            for(Box b:boxList) {
                out.printf("[BOX.DEBUG]    Box: %s\n", b);
                Box left_box = boxMap.get(b.left);
                box_spots.add(b.left);
                test_box_list.add(left_box);
                Box right_box = boxMap.get(b.right);
                box_spots.add(b.right);
                test_box_list.add(right_box);
               out.printf("[BOX.DEBUG]\t\t map_left: %s, map_right: %s\n", left_box, right_box);

            }



        }

        CheckReturn canMove(Compass dir) {
            Vector2d target_loc_left = left.plus(dir.coordDelta());
            Vector2d target_loc_right = right.plus(dir.coordDelta());
            Box new_box_loc = new Box(target_loc_left, target_loc_right);
            out.printf("Box.canMove(%s) on (%s) to %s \n", dir, this, new_box_loc);


            if (walls.contains(target_loc_left))
            {
                out.printf("\t fail on WALL HIT (left) @ %s\n", target_loc_left);
                return new Wall(target_loc_left);
            }
            if(walls.contains(target_loc_right)) {
                // hit wall
                out.printf("\t fail on WALL HIT (right) @ %s\n", target_loc_right);
                return new Wall(target_loc_right);
            }


            Box left_hit = null;
            Box right_hit = null;
            CheckReturn left_ok = null;
            CheckReturn right_ok = null;
            switch (dir) {
                case NORTH, SOUTH -> {
                    if(box_map.containsKey(target_loc_left) || box_map.containsKey(target_loc_right)){
                        out.printf("box %s on %s-> %s\n", dir==Compass.NORTH?"above":"below",dir,
                                box_map.getOrDefault(target_loc_left, box_map.get(target_loc_right)));
                    }
                    if (box_map.containsKey(target_loc_left)) {
                        left_hit = box_map.get(target_loc_left);
                        left_ok = left_hit.canMove(dir);
                        if(!left_ok.isMoveGood()) {
                            out.printf("\t fail can't move Box(%s) %s \n",left_hit, dir);
                        }
                    }
                    if (box_map.containsKey(target_loc_right)) {
                        right_hit = box_map.get(target_loc_right);
                        if (right_hit.equals(left_hit)) {
                            // right and left are the same boxes, only check one
                            right_ok = left_ok;
                        } else {
                            right_ok = right_hit.canMove(dir);
                            if(!right_ok.isMoveGood()) {
                                out.printf("\t fail can't move Box(%s) %s \n",left_hit, dir);
                            }
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
            out.printf("\n2can move OK, fall through to final return (box: %s, dir %s) \n\t\t\t*left_hit: %s, right_hit: %s\n", this,dir, left_hit, right_hit);

            out.printf("\t\t2 left_ok: %s, right_ok %s", left_ok,right_ok);
            if((left_ok==null) && (right_ok==null) ){
                out.printf("\n\t\t\t3 nothing %s of us, return Ok\n",dir );
                return new Ok();
            }
            if(left_ok==null) {
                out.printf("\n\t\t\t3 nothing left-%s of us, right-%s: %s\n",dir, dir, right_ok );
                return right_ok;
            }
            if(right_ok==null) {
                out.printf("\n\t\t\t nothing right-%s of us, left-%s: %s\n",dir, dir, left_ok );
                return left_ok;
            }

            return left_ok.and(right_ok);





        }


        public void doMove(Compass dir) {


            Vector2d target_loc_left = left.plus(dir.coordDelta());
            Vector2d target_loc_right = right.plus(dir.coordDelta());
            Box new_box = new Box(target_loc_left, target_loc_right);
            out.printf("Box.canMove(%s) on (%s) to %s \n", dir, this, new_box);

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
            // Recursively moved any boxes in the way, just need to move ourselves
            // target_loc_left, target_loc_right are our new locations.
            new_box_locations.add(new_box);
            box_list.remove(this);
            box_map.remove(this.left);
            box_map.remove(this.right);
            box_list.add(new_box);
            box_map.put(new_box.left, new_box);
            box_map.put(new_box.right, new_box);
            out.printf("Box.doMove(%s) on (%s) -> %s\n", dir, this, new_box);

        }
    }
}





