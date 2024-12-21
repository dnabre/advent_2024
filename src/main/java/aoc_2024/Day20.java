package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import static java.lang.System.out;

public class Day20 {

    public static final String PART1_ANSWER = "-1";
    public static final String PART2_ANSWER = "-1";
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

        char[][] new_grid = new char[grid.length + 2][grid[0].length + 2];
        for (int y = 0; y < new_grid.length; y++) {
            for (int x = 0; x < new_grid[0].length; x++) {
                new_grid[y][x] = '#';
            }
        }
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                new_grid[y + 1][x + 1] = grid[y][x];
            }
        }
        grid = new_grid;
        max = new Vector2d(grid[0].length, grid.length);
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                char ch = grid[y][x];
                if (ch == 'S') {
                    MAP_START = new Vector2d(x, y);
                }
                if (ch == 'E') {
                    MAP_END = new Vector2d(x, y);
                }
            }
        }
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
        long picos = findFastestBetween(MAP_START, MAP_END).time;
        NO_CHEAT_TIME = picos;

        out.printf("Shortest path from %s to %s takes %d\n", MAP_START, MAP_END, picos);

        HashSet<Vector2d> open_spots = getAllOpenSpots();
        out.printf("open spots: %d\n", open_spots.size());
        HashSet<Cheat> possible_cheats = findPossibleCheats(open_spots);

        out.printf("found %d distinct possible cheats\n", possible_cheats.size());

        HashMap<Long, List<Cheat>> time_to_cheats = new HashMap<>();


        for (Cheat cheat : possible_cheats) {

            long pre_cheat_time =  findFastestBetween(MAP_START, cheat.start).time;

            if (pre_cheat_time < 0) {
                continue;
            }
            long post_cheat_time =  findFastestBetween(cheat.end, MAP_END).time;
            if (post_cheat_time < 0) {
                continue;
            }
            long cheat_total_time = pre_cheat_time + post_cheat_time + 2;
            List<Cheat> cheat_for_time_list = time_to_cheats.getOrDefault(cheat_total_time, new ArrayList<>());
            cheat_for_time_list.add(cheat);
            time_to_cheats.put(cheat_total_time, cheat_for_time_list);
        }


        List<Long> cheat_times = new ArrayList<>(time_to_cheats.keySet().stream().toList());
        Collections.sort(cheat_times, Collections.reverseOrder());

        int good_cheat_count = 0;
        for (long time : cheat_times) {
            if (time >= NO_CHEAT_TIME) {
                continue;
            }
            List<Cheat> cheats = time_to_cheats.get(time);
            if (NO_CHEAT_TIME - time <= PART1_TIME_LIMIT) {
                good_cheat_count += cheats.size();
            }
            out.printf("%d cheats saves %d time (total %d)\n", cheats.size(), NO_CHEAT_TIME - time, time);
        }


        out.printf("\nnumber of cheats that would save %d or more picoseconds: %d\n", PART1_TIME_LIMIT, good_cheat_count);


        long answer = -1;
        return String.valueOf(answer);
    }

    private static HashSet<Cheat> findPossibleCheats(HashSet<Vector2d> openSpots) {
        HashSet<Cheat> possible_cheats = new HashSet<>();
        for (Vector2d open_spot : openSpots) {
            List<Vector2d> adj_tiles = Directions.Compass.getNeighbors(open_spot);
            for (Vector2d adj : adj_tiles) {
                if (grid[adj.y][adj.x] == WALL) {
                    MAP_START = new Vector2d(adj.x, adj.y); // Wall space next to open space

                    for (Vector2d possible_end : Directions.Compass.getNeighbors(MAP_START)) {
                        // open_spot (.) -> start wall (#) -> end (.)
                        // Cheat open_spot -> end
                        MAP_END = possible_end;
                        //The three parts should be distinct, not all may be possible at this point,
                        // but check to be safe
                        if (MAP_END.equals(adj)) {
                            continue;
                        }
                        if (MAP_END.equals(MAP_START)) {
                            continue;
                        }

                        if (grid[MAP_END.y][MAP_END.x] != WALL) {
                            // adj Open -> start Wall -> end Open
                            Cheat n_cheat = new Cheat(open_spot, MAP_END);
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


    private static HashMap<Pair<Vector2d, Vector2d>, State> fastest_path_cache = new HashMap<>();
    private static State findFastestBetween(Vector2d start_pos, Vector2d end_pos) {
        Pair<Vector2d,Vector2d> input = new Pair<>(start_pos,end_pos);
        if (fastest_path_cache.containsKey(input)) {
            return fastest_path_cache.get(input);
        }
        State start = new State(start_pos, 0, new HashSet<>());
        start.path.add(start_pos);
        ArrayDeque<State> work_queue = new ArrayDeque<>();
        work_queue.add(start);
        while (!work_queue.isEmpty()) {
            State current = work_queue.removeFirst();
            if ((NO_CHEAT_TIME > 0) && (current.time > NO_CHEAT_TIME)) {
                State bad = new State(current.pos, -1, current.path);
                fastest_path_cache.put(input, bad );
                return bad;
            }
            if (current.pos.equals(end_pos)) {
                fastest_path_cache.put(input,current);
                return current;
            }

            List<Vector2d> next_to = Directions.Compass.getNeighbors(current.pos);
            for (Vector2d step : next_to) {
                char ch = grid[step.y][step.x];
                if (ch == '#') {
                    continue;
                }
                if (current.path.contains(step)) {
                    continue;
                }
                State new_state = new State(step, current.time + STEP_COST, new HashSet<>(current.path));
                new_state.path.add(step);
                work_queue.addLast(new_state);
            }
        }

        fastest_path_cache.put(input,new State(new Vector2d(-1,-1), -1, new HashSet<>()));
        return new State(new Vector2d(-1,-1), -1, new HashSet<>());
    }

    public static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }

    public record State(Vector2d pos, long time, HashSet<Vector2d> path) {
    }

    public record Cheat(Vector2d start, Vector2d end) {
        // open spots on either end of one wall cheat
    }
}