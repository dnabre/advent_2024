package src.main.java.aoc_2024;

import java.io.IOException;
import java.util.*;

import static src.main.java.aoc_2024.Directions.Compass;

public class Day16 extends AoCDay {
    public static final String PART1_ANSWER = "102460";
    public static final String PART2_ANSWER = "527";
    private static Vector2d MAP_END;
    private static Vector2d MAP_START;

    static private final int STEP_PRICE = 1;
    static private final int TURN_PRICE1 = 1000;
    private static int bestScore = Integer.MAX_VALUE;
    private static char[][] grid;
    private static HashSet<Visit> onBestPath;


    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        HashMap<Vector2d, HashSet<Compass>> visited = new HashMap<>();
        PriorityQueue<State> work_queue = new PriorityQueue<>();
        onBestPath = new HashSet<>();

        work_queue.offer(new State(MAP_START, Compass.EAST, 0, null));
        while (!work_queue.isEmpty()) {
            State state = work_queue.poll();
            if (state.pos.equals(MAP_END)) {
                for (State st = state; st != null; st = st.prev) {
                    onBestPath.add(new Visit(st.pos, st.direction, st.score));
                }
                for (Compass direction : Compass.values()) {
                    onBestPath.add(new Visit(state.pos, direction, state.score));
                }
                bestScore = state.score;

                break;

            }
            Vector2d ahead = state.pos.locationAfterStep(state.direction);
            if (grid[ahead.y][ahead.x] != '#') {
                State next = new State(ahead, state.direction, state.score + STEP_PRICE, state);
                if (notAlreadyVisited(visited, next.pos, next.direction)) {
                    work_queue.offer(next);
                }
            }
            State left = new State(state.pos, state.direction.turnLeft(), state.score + TURN_PRICE1, state);
            if (notAlreadyVisited(visited, left.pos, left.direction)) {
                work_queue.offer(left);
            }
            State right = new State(state.pos, state.direction.turnRight(), state.score + TURN_PRICE1, state);
            if (notAlreadyVisited(visited, right.pos, right.direction)) {
                work_queue.offer(right);
            }
        }

        long answer = bestScore;
        return String.valueOf(answer);

    }

    protected String getPart2() {
        int seats = countSeats(onBestPath);

        int prev = 0;
        while (prev < seats) {

            PriorityQueue<State> queue = new PriorityQueue<>();
            HashMap<Vector2d, HashSet<Compass>> visited = new HashMap<>();
            queue.add(new State(MAP_START, Compass.EAST, 0, null));
            while (!queue.isEmpty()) {
                State state = queue.poll();
                if (state.score > bestScore) {
                    continue;
                }
                if (MAP_END.equals(state.pos)) {
                    continue;
                }
                Visit current_visit = new Visit(state.pos, state.direction, state.score);
                Vector2d ahead = state.pos.locationAfterStep(state.direction);
                if (grid[ahead.y][ahead.x] != '#') {

                    State next = new State(ahead, state.direction, state.score + STEP_PRICE, state);
                    Visit next_visit = new Visit(next.pos, next.direction, next.score);


                    if (!onBestPath.contains(current_visit) && onBestPath.contains(next_visit)) {
                        for (State p_state = state; p_state != null; p_state = p_state.prev) {
                            onBestPath.add(new Visit(p_state.pos, p_state.direction, p_state.score));
                        }
                    } else if (notAlreadyVisited(visited, next.pos, next.direction)) {
                        queue.add(next);
                    }

                }
                State left = new State(state.pos, state.direction.turnLeft(), state.score + TURN_PRICE1, state);
                Visit v = new Visit(left.pos, left.direction, left.score);
                if (!onBestPath.contains(current_visit) && onBestPath.contains(v)) {
                    for (State p_state = state; p_state != null; p_state = p_state.prev) {
                        onBestPath.add(new Visit(p_state.pos, p_state.direction, p_state.score));
                    }
                } else if (notAlreadyVisited(visited, left.pos, left.direction)) {
                    queue.add(left);
                }

                State right = new State(state.pos, state.direction.turnRight(), state.score + TURN_PRICE1, state);
                v = new Visit(right.pos, right.direction, right.score);
                if (!onBestPath.contains(current_visit) && onBestPath.contains(v)) {
                    for (State p_state = state; p_state != null; p_state = p_state.prev) {
                        onBestPath.add(new Visit(p_state.pos, p_state.direction, p_state.score));
                    }
                } else if (notAlreadyVisited(visited, right.pos, right.direction)) {
                    queue.add(right);
                }
            }

            prev = seats;
            seats = countSeats(onBestPath);

        }


        long answer = seats;
        return String.valueOf(answer);
    }

    protected void parseInput(String filename) throws IOException {
        char[][] input_grid = AoCUtils.parseGrid(filename);
        Vector2d max = new Vector2d(input_grid[0].length, input_grid.length);
        grid = new char[max.y][max.x];
        for (int y = 0; y < max.y; y++) {
            for (int x = 0; x < max.x; x++) {
                char ch = input_grid[y][x];
                if (ch == 'S') {
                    MAP_START = new Vector2d(x, y);
                    ch = '.';
                }
                if (ch == 'E') {
                    MAP_END = new Vector2d(x, y);
                    ch = '.';
                }
                grid[y][x] = ch;
            }
        }
    }

    private int countSeats(HashSet<Visit> onBestPath) {
        HashSet<Vector2d> seats = new HashSet<>();
        onBestPath.forEach(v -> seats.add(v.pos));
        return seats.size();
    }

    private static boolean notAlreadyVisited(HashMap<Vector2d, HashSet<Compass>> visited, Vector2d pos, Compass dir) {
        HashSet<Compass> dir_set;
        if (visited.containsKey(pos)) {
            dir_set = visited.get(pos);
        } else {
            dir_set = new HashSet<>(Compass.NUMBER_DIRECTIONS);
            visited.put(pos, dir_set);
        }
        boolean ret = dir_set.contains(dir);
        dir_set.add(dir);
        return !ret;
    }

    public Day16(int day) {
        super(day);
    }


    public record State(Vector2d pos, Compass direction, int score, State prev) implements Comparable<State> {
        @Override
        public int compareTo(State o) {
            return (this.score - o.score);
        }
    }

    public record Visit(Vector2d pos, Compass dir, int score) {
    }


}


