package advent_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import static advent_2024.Directions.Compass;

public class Day06 extends AoCDay {
    public static final String PART1_ANSWER = "5067";
    public static final String PART2_ANSWER = "1793";
    private static final char BLOCK = '#';
    private static final char EMPTY = '.';
    private static HashSet<Vector2d> blockers;
    private static char[][] grid;
    private static Compass guard_init_dir;
    private static Vector2d guard_start;
    private static int x_max;
    private static int y_max;

    public Day06(int day) {
        super(day);
    }

    private static boolean guard_on_map(Guard g) {
        return g.loc.inside(x_max, y_max);

    }

    private static Vector2d step_forward_location(Guard g) {
        Vector2d delta = g.heading.coordDelta();
        Vector2d new_loc = new Vector2d(g.loc);
        new_loc.add(delta);
        if (!new_loc.inside(x_max, y_max)) {
            return null;
        }
        return new_loc;
    }

    private static Run_Result testBlockerForward(Guard guy, Vector2d new_block) {
        HashSet<Guard> newly_visited_states = new HashSet<>();

        while (true) {
            Vector2d next_step = step_forward_location(guy);
            if (next_step == null) {
                return Run_Result.ESCAPE;
            }

            if (blockers.contains(next_step) || (next_step.equals(new_block))) {
                guy = new Guard(guy.loc, guy.heading.turnRight());
            } else {
                guy = new Guard(next_step, guy.heading);
            }
            if (newly_visited_states.contains(guy)) {
                return Run_Result.LOOP;
            } else {
                newly_visited_states.add(guy);
            }
        }

    }

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        HashSet<Vector2d> visited = new HashSet<>();
        Guard guy = new Guard(guard_start, guard_init_dir);
        while (guard_on_map(guy)) {
            visited.add(guy.loc);
            Vector2d next_step = step_forward_location(guy);
            if (next_step == null) {
                break;
            }
            //char next_tile = next_step.fromGrid(grid);
            char next_tile = grid[next_step.x][next_step.y];
            if (next_tile == BLOCK) {
                guy = new Guard(guy.loc, guy.heading.turnRight());
            } else {
                guy = new Guard(next_step, guy.heading);
            }
        }
        int answer = visited.size();
        return Integer.toString(answer);
    }

    protected String getPart2() {
        Guard guy = new Guard(guard_start, guard_init_dir);
        ArrayList<Guard> path_states = new ArrayList<>();
        Stack<Guard> path_state_stack = new Stack<>();

        while (true) {
            path_states.add(guy);
            Vector2d next_step = step_forward_location(guy);
            if (next_step == null) {
                break;
            }
            if (blockers.contains(next_step)) {
                guy = new Guard(guy.loc, guy.heading.turnRight());
            } else {
                guy = new Guard(next_step, guy.heading);
            }
            path_state_stack.push(guy);
        }
        HashSet<Vector2d> loop_makers = new HashSet<>();
        for (Guard g_state : path_states.reversed()) {
            if (path_state_stack.empty()) {
                continue;
            }
            Vector2d new_blocker = g_state.loc;
            if (!path_state_stack.empty()) {
                Run_Result r = testBlockerForward(new Guard(guard_start, guard_init_dir), new_blocker);
                if (r == Run_Result.LOOP) {
                    loop_makers.add(new_blocker);
                }
            }
        }
        int answer = loop_makers.size();
        return Integer.toString(answer);
    }

    protected void parseInput(String filename) throws IOException {
        char[][] input_grid = Files.readAllLines(Path.of(filename)).stream().map(String::toCharArray).toList().toArray(new char[0][0]);
        int input_x_max = input_grid[0].length - 1;
        int input_y_max = input_grid.length - 1;

        Day06.x_max = input_y_max;
        Day06.y_max = input_x_max;

        blockers = new HashSet<>();
        grid = new char[input_x_max + 1][input_y_max + 1];
        Vector2d maxes = new Vector2d(input_x_max, input_y_max);

        for (int y = 0; y <= maxes.y; y++) {
            for (int x = 0; x <= maxes.x; x++) {
                char ch = input_grid[y][x];
                if (ch == '^') {
                    guard_start = new Vector2d(x, y);
                    guard_init_dir = Compass.NORTH;
                    ch = EMPTY;
                } else if (ch == BLOCK) {
                    blockers.add(new Vector2d(x, y));
                }
                grid[x][y] = ch;
            }
        }
    }

    private enum Run_Result {
        ESCAPE, LOOP
    }

    public record Guard(Vector2d loc, Compass heading) {
        @Override
        public String toString() {
            return String.format("Guard @ %s facing %s", loc.toString(), heading.toString());
        }
    }


}


