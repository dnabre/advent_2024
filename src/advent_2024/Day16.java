package advent_2024;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

import static advent_2024.Directions.Compass;

public class Day16 extends AoCDay {
    public static final String PART1_ANSWER = "102460";
    public static final String PART2_ANSWER = "527";
    static private final int STEP_PRICE = 1;
    static private final int TURN_PRICE = 1000;
    private static Vector2d MAP_END;
    private static Vector2d MAP_START;
    private static char[][] grid;
    private static int lowestScore = Integer.MAX_VALUE;
    private static HashSet<Visit> mainPath;
    private static HashMap<Vector2d, HashSet<Compass>> visited;

    public Day16(int day) {
        super(day);
    }

    private static void addToMainPath(State p_state) {
        for (State p_s = p_state; p_s != null; p_s = p_s.prev) {
            mainPath.add(new Visit(p_s.pos, p_s.direction, p_s.score));
        }
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

    private static State tryNewState(Vector2d pos, Compass dir, int score_increment, State prev_state) {
        Visit current_visit = prev_state.getVisit();
        State new_state = new State(pos, dir, prev_state.score + score_increment, prev_state);
        Visit new_visit = new_state.getVisit();

        State return_value = null;
        if (!mainPath.contains(current_visit) && mainPath.contains(new_visit)) {
            addToMainPath(new_state);
        } else if (notAlreadyVisited(visited, pos, dir)) {
            return_value = new_state;
        }
        return return_value;
    }

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        HashMap<Vector2d, HashSet<Compass>> visited = new HashMap<>();
        PriorityQueue<State> work_queue = new PriorityQueue<>();
        mainPath = new HashSet<>();
        State state = new State(MAP_START, Compass.EAST, 0, null);
        while (state != null && !state.pos.equals(MAP_END)) {
            Vector2d ahead = state.pos.locationAfterStep(state.direction);
            if (grid[ahead.y][ahead.x] != '#') {
                State next = new State(ahead, state.direction, state.score + STEP_PRICE, state);
                if (notAlreadyVisited(visited, next.pos, next.direction)) {
                    work_queue.offer(next);
                }
            }
            State left = state.left();
            if (notAlreadyVisited(visited, left.pos, left.direction)) {
                work_queue.offer(left);
            }
            State right = state.right();
            if (notAlreadyVisited(visited, right.pos, right.direction)) {
                work_queue.offer(right);
            }
            state = work_queue.poll();
        }
        assert state != null;
        //Need to add these final visits for Part 2
        for (Compass direction : Compass.values()) {
            mainPath.add(state.getVisit(direction));
        }

        lowestScore = state.score;


        long answer = lowestScore;
        return String.valueOf(answer);

    }

    protected String getPart2() {
        int seats = 0;
        int prev;
        do {
            PriorityQueue<State> work_queue = new PriorityQueue<>();
            visited = new HashMap<>();
            work_queue.offer(new State(MAP_START, Compass.EAST, 0, null));
            while (!work_queue.isEmpty()) {
                State state = work_queue.poll();
                if (state.score > lowestScore) {
                    continue;
                }
                if (MAP_END.equals(state.pos)) {
                    continue;
                }

                state.getVisit();
                Vector2d ahead = state.pos.locationAfterStep(state.direction);
                if (grid[ahead.y][ahead.x] != '#') {
                    State new_state = tryNewState(ahead, state.direction, STEP_PRICE, state);
                    if (new_state != null) {
                        work_queue.offer(new_state);
                    }
                }
                State left_state = tryNewState(state.pos, state.direction.turnLeft(), TURN_PRICE, state);
                if (left_state != null) {
                    work_queue.offer(left_state);
                }
                State right_state = tryNewState(state.pos, state.direction.turnRight(), TURN_PRICE, state);
                if (right_state != null) {
                    work_queue.offer(right_state);
                }
            }
            prev = seats;
            HashSet<Vector2d> seat_set = new HashSet<>(mainPath.size());
            for (Visit v : mainPath) {
                seat_set.add(v.pos);
            }
            seats = seat_set.size();

        } while (prev < seats);

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

    public record State(Vector2d pos, Compass direction, int score, State prev) implements Comparable<State> {
        @Override
        public int compareTo(State o) {
            return (this.score - o.score);
        }

        public Visit getVisit() {
            return new Visit(this.pos, this.direction, this.score);
        }

        public Visit getVisit(Compass dir) {
            return new Visit(this.pos, dir, this.score);
        }

        public State left() {
            return new State(pos, direction.turnLeft(), score + TURN_PRICE, this);
        }

        public State right() {
            return new State(pos, direction.turnRight(), score + TURN_PRICE, this);
        }
    }

    public record Visit(Vector2d pos, Compass dir, int score) {
    }


}


