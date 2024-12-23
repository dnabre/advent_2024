package src.main.java.aoc_2024;

import jdk.jshell.spi.SPIResolutionException;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import static java.lang.System.out;

public class Day20 {

    public static final String PART1_ANSWER = "1441";
    public static final String PART2_ANSWER = "1021490";
    private static final long STEP_COST = 1;
    private static final long PART1_TIME_LIMIT = 100;
    private static final char WALL = '#';
    private static char[][] grid;
    private static Vector2d MAP_START;
    private static Vector2d MAP_END;
    private static Vector2d max;
    private static long NO_CHEAT_TIME;



    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  20");
        if (AdventOfCode2024.TESTING) {
            out.print("\t (testing)");
        }
        out.println();

        String[] answers = {"", ""};

        parseInput(inputString);

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

    public static void parseInput(String filename) throws IOException {
        grid = AoCUtils.parseGrid(filename);


        for(int y=0; y < grid.length; y++) {
            for(int x=0; x < grid[0].length; x++) {
                char ch = grid[y][x];
                if( ch == 'S') {
                    MAP_START = new Vector2d(x,y);
                    grid[y][x] = '.';
                }
                if (ch == 'E') {
                    MAP_END = new Vector2d(x,y);
                    grid[y][x] = '.';
                }
            }out.println();
        }
        max = new Vector2d(grid[0].length, grid.length);


    }

    public static String getPart1() {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                out.print(grid[y][x]);
            }
            out.println();
        }
        out.printf("start: %s\n", MAP_START);
        out.printf("end  : %s\n", MAP_END);

        int[][] d_grid = new int[grid.length][grid[0].length];
        for (int y = 0; y < d_grid.length; y++) {
            for (int x = 0; x < d_grid[0].length; x++) {
                char ch = grid[y][x];
                if(ch=='.') {
                    d_grid[y][x] = 0;
                }
                if(ch==WALL) {
                    d_grid[y][x] = -1;
                }
            }
        }
        //do full distanst from all points to end. then fill the paths with their distance from end.


        // do path search from start->end, label distance to end on path grid. the look at every point on path
        // see if jumping 2-grid distance would land on a distance to end that is better than current + 2;




        long answer = -1;
        return String.valueOf(answer);
    }

    private static HashSet<Cheat> findPossibleCheats(HashSet<Vector2d> openSpots) {
        out.printf("searching for cheats from %d possible starts\n", openSpots.size());
        HashSet<Cheat> possible_cheats = new HashSet<>();
        for (Vector2d open_spot : openSpots) {
            List<Vector2d> adj_tiles = Directions.Compass.getNeighbors(open_spot);
            for (Vector2d adj : adj_tiles) {
                if (grid[adj.y][adj.x] == WALL) {
                    //MAP_START = new Vector2d(adj.x, adj.y); // Wall space next to open space
                    Vector2d start_wall = new Vector2d(adj.x, adj.y);
                    for (Vector2d possible_end : Directions.Compass.getNeighbors(MAP_START)) {
                        // open_spot (.) -> start wall (#) -> end (.)
                        // Cheat open_spot -> end
                        if(grid[possible_end.y][possible_end.x] == WALL){ continue;}

                        //The three parts should be distinct, not all may be possible at this point,
                        if ((!open_spot.equals(start_wall)) &&
                                (!start_wall.equals(possible_end)) &&
                                (!possible_end.equals(open_spot))) {
                            Cheat n_cheat = new Cheat(open_spot, possible_end);
                            possible_cheats.add(n_cheat);
                        }

                    }
                }
            }
        }
        return possible_cheats;
    }

    private static HashSet<Vector2d> getAllOpenSpots() {
        HashSet<Vector2d> spots = new HashSet<>();
        for (int y = 0; y < max.y; y++) {
            for (int x = 0; x < max.x; x++) {
                if (grid[y][x] != '#') {
                    spots.add(new Vector2d(x, y));

                }
            }
        }


        return spots;
    }



 //   public record State(Vector2d pos, long time, HashSet<Vector2d> path) { }
    public record State(Vector2d pos, long time, State previous) { }

    private static State findFastestBetween(Vector2d start_pos, Vector2d end_pos) {


        State start = new State(start_pos, 0, null);
        ArrayDeque<State> work_queue = new ArrayDeque<>();
        work_queue.add(start);
        while (!work_queue.isEmpty()) {
            State current = work_queue.removeFirst();
            if ((NO_CHEAT_TIME > 0) && (current.time > NO_CHEAT_TIME)) {
                return null;
            }
            if (current.pos.equals(end_pos)) {
                return current;
            }

            List<Vector2d> next_to = Directions.Compass.getNeighbors(current.pos);
            for (Vector2d step : next_to) {
                char ch = grid[step.y][step.x];
                if (ch == '#') {
                    continue;
                }


                // check if were there
                if(current.previous != null ) {
                    boolean found_on_path = false;
                    Vector2d looking_for =current.pos;
                    State ptr = current.previous;
                    while (ptr != null) {
                        Vector2d loc = ptr.pos;
                        if(loc.equals(looking_for)) {
                            //found loop
                            found_on_path = true;
                            break;
                        }
                        ptr = ptr.previous;
                    }
                    if (found_on_path) {
                    //    out.printf("\t discarding %s because it hits our previous path\n", step);
                        continue;
                    }
                }
                State new_state = new State(step, current.time + STEP_COST, current);

                work_queue.addFirst(new_state);
            }
        }

        //out.println("search failed. work queue empty");
        return null;
    }

    public static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }


    public record Cheat(Vector2d start, Vector2d end) {
        // open spots on either end of one wall cheat
    }
}