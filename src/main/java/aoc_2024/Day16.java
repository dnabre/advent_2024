package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.System.out;
public class Day16 {

    public static final String PART1_ANSWER = "102460";
    public static final String PART2_ANSWER = "527";
    private static Vector2d MAP_END;
    private static Vector2d MAP_START;

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  16");
        if (AdventOfCode2024.TESTING) {
            out.print("\t (testing)");
        }
        out.println();

        String[] answers = {"", ""};
        String INPUT = Files.readString(Path.of(inputString));

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
    private static char[][] grid;
    private static Vector2d max;
    private static void parseInput(String filename) throws IOException {
        char[][] input_grid = AoCUtils.parseGrid(filename);

        max = new Vector2d(input_grid[0].length,input_grid.length);
        grid = new char[max.y][max.x];

        for (int y = 0; y < max.y; y++) {
            for (int x = 0; x < max.x; x++) {
                char ch = input_grid[y][x];
                out.print(ch);
                if(ch=='S') {
                    MAP_START = new Vector2d(x,y);
                    ch = '.';
                } else if (ch =='E') {
                    MAP_END = new Vector2d(x,y);
                    ch = '.';
                }
                grid[y][x] = ch;
            }
            out.println();
        }
    }

    static private final int PART1_TURN_PRICE = 1000;
    static private final int PART1_STEP_PRICE = 1;
    record State(Reindeer deer, int price, HashSet<Reindeer> path_set){
        static Comparator<State> PRICE_COMPARATOR = Comparator.comparing(State::price);
    }
    record Reindeer(Vector2d loc, Directions.Compass heading) {}
    public static String getPart1() {
        HashSet<State> seen = new HashSet<>();
        Reindeer initial = new Reindeer(MAP_START, Directions.Compass.EAST);
        State start = new State(initial, 0, new HashSet<>());
        //start.path_set.add(initial);
        PriorityQueue<State> work_queue = new PriorityQueue<>(State.PRICE_COMPARATOR);
        work_queue.offer(start);
        State current = null;
        while(!work_queue.isEmpty()) {
            current = work_queue.poll();
            out.printf("current: %s\n", current);

//            if(seen.contains(current)) {
//                continue;
//            } else {
//                seen.add(current);
//            }
            if(current.deer.loc.equals(MAP_END)) {
                break;
            }
            Vector2d step = current.deer.loc.locationAfterStep(current.deer.heading);


            current.path_set.add(current.deer);

            if (grid[step.y][step.x] !='#') {
                Reindeer n_deer = new Reindeer(step, current.deer.heading);
                if(!current.path_set.contains(n_deer)) {
                    State new_state = new State(n_deer,  current.price()+PART1_STEP_PRICE,
                            new HashSet<>(current.path_set));
                    new_state.path_set.add(n_deer);

                }
            }


            Vector2d left_and_step = current.deer.loc.locationAfterStep(current.deer.heading.turnLeft());
            if(grid[left_and_step.y][left_and_step.x] != '#') {
                Reindeer n_deer = new Reindeer(left_and_step, current.deer.heading.turnLeft());
                if(!current.path_set.contains(n_deer)) {
                    State new_state = new State(n_deer, current.price() + PART1_TURN_PRICE+PART1_STEP_PRICE,
                            new HashSet<>(current.path_set));
                    new_state.path_set.add(n_deer);
                    work_queue.offer(new_state);
                }
            }



            Vector2d right_and_step = current.deer.loc.locationAfterStep(current.deer.heading.turnRight());
            if(grid[right_and_step.y][right_and_step.x] != '#') {
                Reindeer n_deer = new Reindeer(left_and_step, current.deer.heading.turnRight());
                if(!current.path_set.contains(n_deer)) {
                    State new_state = new State(n_deer, current.price() + PART1_TURN_PRICE + PART1_STEP_PRICE,
                            new HashSet<>(current.path_set));
                    new_state.path_set.add(n_deer);
                    work_queue.offer(new_state);
                }
            }
        }





        out.println(current);


        long answer = current.price;
        return String.valueOf(answer);
    }


    public static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }
}