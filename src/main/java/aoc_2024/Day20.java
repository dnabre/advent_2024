package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import static java.lang.System.out;

public class Day20 {

    public static final String PART1_ANSWER = "1441";
    public static final String PART2_ANSWER = "1021490";
    private static final char WALL = '#';
    private static char[][] grid;
    private static Vector2d MAP_START;
    private static Vector2d MAP_END;
    private static Vector2d max;




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

            }
        }
        max = new Vector2d(grid[0].length, grid.length);
        grid[MAP_START.y][MAP_START.x] = '.';
        grid[MAP_END.y][MAP_END.x] = '.';

    }

    record Cheat(Vector2d from, Vector2d to, int saved){}

    public static String getPart1() {
        HashMap<Vector2d, Integer> to_end = findDistanceFromStartToEverywhere(grid, MAP_END);

        HashSet<Vector2d> path = new HashSet<>();
        Vector2d current = MAP_START;
        do {
            path.add(current);
            current = bestNextStep(current,to_end);
        } while(!Objects.requireNonNull(current).equals(MAP_END));
        path.add(MAP_END);


        ArrayList<Cheat> cheat_list =  getAllCheatsFromPath(grid,path, to_end);

        HashMap<Integer,Integer> count_saved = new HashMap<>();
        for(Cheat c: cheat_list) {
            int current_count = count_saved.getOrDefault(c.saved,0);
            current_count++;
            count_saved.put(c.saved,current_count);
        }





        long answer = cheat_list.size();
        return String.valueOf(answer);


    }

    private static final int PART1_THRESHOLD =100;
    private static ArrayList<Cheat> getAllCheatsFromPath(char[][] grid, HashSet<Vector2d> path_set,
                                                          HashMap<Vector2d, Integer> toEnd) {

        ArrayList<Cheat> cheats = new ArrayList<>();
        for(Vector2d p : path_set) {
            int no_cheat_distance =  toEnd.get(p);
            for(Vector2d jump_through : Directions.Compass.getNeighbors(p)) {
                if(grid[jump_through.y][jump_through.x] == WALL) {
                    for(Vector2d landing: Directions.Compass.getNeighbors(jump_through)) {
                        if(path_set.contains(landing) && !landing.equals(p)) {
                            int cheat_distance = 2 + toEnd.get(landing);
                            int saved = no_cheat_distance - cheat_distance;
                            if(saved >= PART1_THRESHOLD ) {
                                Cheat new_cheat = new Cheat(p, landing, saved);
                                cheats.add(new_cheat);
                            }
                        }
                    }
                }
            }
        }
        return cheats;
    }

    private static Vector2d bestNextStep(Vector2d current, HashMap<Vector2d, Integer> toEnd) {
        int current_dist = toEnd.get(current);

        for(Vector2d n:Directions.Compass.getNeighbors(current)) {
            if(toEnd.getOrDefault(n, -1) == current_dist -1) {
                return n;
            }
        }
        return null;
    }


    static private HashMap<Vector2d, Integer> findDistanceFromStartToEverywhere(char[][] grid, Vector2d start) {
        PriorityQueue<Vector2d> queue = new PriorityQueue<>();
        queue.offer(start);
        HashMap<Vector2d, Integer> distances= new HashMap<>();
        distances.put(start,0);
        while(!queue.isEmpty()) {
            Vector2d current = queue.poll();
            int dist = distances.get(current);
            List<Vector2d> neighbors = Directions.Compass.getNeighborsClamped(current, 0, max.x -1);
            for(Vector2d v: neighbors) {
                if(grid[v.y][v.x] == '.') {
                    int new_dist = dist + 1;
                    if (new_dist < distances.getOrDefault(v, Integer.MAX_VALUE)) {
                        distances.put(v, new_dist);
                        queue.add(v);
                    }
                }
            }
        }
        return distances;
    }


    private static ArrayList<Vector2d> L1Offsets(int r) {
        ArrayList<Vector2d> offsets = new ArrayList<>();
        for(int offset=0; offset < r; offset++) {
            int inv_offset = r -offset;
            offsets.add(new Vector2d(offset,inv_offset));
            offsets.add(new Vector2d(offset,-inv_offset));
            offsets.add(new Vector2d(-offset,inv_offset));
            offsets.add(new Vector2d(-offset,-inv_offset));
        }
        return offsets;
    }


    public static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }



}