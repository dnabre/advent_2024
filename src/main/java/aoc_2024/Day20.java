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
                out.print(ch);
            }
            out.println();
        }
        max = new Vector2d(grid[0].length, grid.length);
        grid[MAP_START.y][MAP_START.x] = '.';
        grid[MAP_END.y][MAP_END.x] = '.';

    }

    record Cheat(Vector2d from, Vector2d to, int saved){}

    public static String getPart1() {
        out.println("\nAoCUtils.printGridT():\n");
        AoCUtils.printGridT(grid);
        out.printf("maxes: %s\n", max);



        HashMap<Vector2d, Integer> from_start = findDistanceFromStartToEverywhere(grid,MAP_START);
        HashMap<Vector2d, Integer> to_end = findDistanceFromStartToEverywhere(grid, MAP_END);


        out.printf("distance from start to end: %d\n", to_end.get(MAP_START));
        ArrayList<Vector2d> path = new ArrayList<>();
        Vector2d current = MAP_START;
        while(!current.equals(MAP_END)) {
            current = bestNextStep(current,to_end);
            path.add(current);
        }
        out.printf("path from start to end (%s -> %s) is %d long\n", MAP_START, MAP_END, path.size());
        ArrayList<Cheat> cheat_list =  getAllCheatsFromPAth(path, from_start, to_end);





        long answer = -1;
        return String.valueOf(answer);


    }

    private static ArrayList<Cheat> getAllCheatsFromPAth(ArrayList<Vector2d> path,
                                    HashMap<Vector2d, Integer> fromStart, HashMap<Vector2d, Integer> toEnd) {
        ArrayList<Cheat> cheats = new ArrayList<>();
        for(Vector2d p : path) {


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





    public static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }



}