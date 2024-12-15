package src.main.java.aoc_2024;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import static java.lang.System.out;
import static src.main.java.aoc_2024.Directions.Compass;

public class Day15 {

    public static final String PART1_ANSWER = "1471826";
    public static final String PART2_ANSWER = "1457703";

    private static char[][] initial_grid;
    private static Vector2d grid_sizes;
   // private static int grid_size;

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




    public static void parseInput(String INPUT) throws IOException {

        List<List<String>> blocks  = AoCUtils.breakDataByNewline(INPUT);
        String text = String.join("\n", blocks.getFirst());
        String move = String.join("", blocks.getLast());

        List<String> lines = text.lines().toList();
        int height = lines.size();
        int width = lines.getFirst().length();
        //grid_sizes = new Vector2d(width,height);
        grid_sizes = new Vector2d(height,width);


        initial_grid = new char[width][height];
        for(int y = 0; y < height; y++) {
            String line = lines.get(y);
            for(int x = 0; x < width; x++) {
                char ch = line.charAt(x);
                initial_grid[x][y] = ch;
                if(ch == '@') {
                    robot_start = new Vector2d(x,y);
                    initial_grid[x][y] = '.';

                }

            }
        }
        move_list = new Compass[move.length()];
        int idx =0;
        for(char ch: move.toCharArray()) {
            Compass dir = Compass.fromChar(ch);
            move_list[idx] = dir;
            idx++;
        }

    }

    public static String getPart1() {
        int grid_size =-1;
        if(grid_sizes.x == grid_sizes.y) {
            grid_size = grid_sizes.x;
        }
        char[][] grid = new char[grid_size][grid_size];
        for(int y=0; y < grid_size;y++) {
            for(int x=0; x < grid_size; x++) {
                grid[x][y] = initial_grid[x][y];
            }
        }
        Vector2d robot_loc = new Vector2d(robot_start);

        for(int i=0; i < move_list.length; i++) {
            Vector2d move_direction = move_list[i].coordDelta();
            Vector2d step_target = robot_loc.plus(move_direction);
            char ch = grid[step_target.x][step_target.y];

            switch (ch) {
                case '.' -> {
                    robot_loc.add(move_direction);
                }
                case '#' -> {
                    // robot hits wall
                }
                case 'O' -> {
                    Vector2d start_box_push = robot_loc.plus(move_direction);
                    char u_box = grid[start_box_push.x][start_box_push.y];
                    while(u_box == 'O') {
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

        AoCUtils.printGridWithSpecial(grid,robot_loc,'@');
        out.printf("\n----------------------------------------------------------------------\n");
        long gps_sum=0L;
        for(int y=0; y < grid_size; y++) {
            for(int x=0; x < grid_size; x++) {
                char ch = grid[x][y];
                if (ch == 'O') {
                    gps_sum += getGPS(x,y);
                }
            }
        }


        long answer = gps_sum;
        return String.valueOf(answer);
    }


    public static String getPart2() throws IOException {
        String part2_input = embiggenGrid(initial_grid);
        parseInput(part2_input);
        ///  everything here will be grid[y][x]   we don't question this.

        for(int x=0; x< grid_sizes.x; x++) {
            for (int y=0; y < grid_sizes.y; y++) {
                if(robot_start.isEqual(y,x)) {
                    out.print('@');
                } else {
                    out.print(initial_grid[y][x]);
                }
            }
            out.println();
        }




        long answer = -1;
        return String.valueOf(answer);
    }

    private static String embiggenGrid(char[][] initialGrid) {
        int grid_size = -1;
        if(grid_sizes.x == grid_sizes.y) {
            grid_size = grid_sizes.x;
        }
        int grid_height = grid_size * 2;
        int grid_width = grid_size ;
        char[][] grid = new char[grid_width][grid_height];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        grid[robot_start.x][robot_start.y] = '@';
        for(int y=0; y < grid_size; y++) {
            for(int x=0; x < grid_size; x++) {
                char ch = initialGrid[x][y];
                if (robot_start.isEqual(x,y)) {
                    ps.print('@');
                    ps.print('.');
                } else if(ch=='O') {
                    ps.print('[');
                    ps.print(']');
                } else {
                        ps.print(ch);
                        ps.print(ch);
                }
            }
            ps.println();
        }
        ps.println();
        for(Compass c : move_list) {
            ps.print(c.toChar());
        }
        ps.println();
        ps.close();
        return baos.toString();
    }

    private static long getGPS(int x, int y) {
        return (100 * y) +x;
    }
}


