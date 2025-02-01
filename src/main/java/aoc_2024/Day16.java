package src.main.java.aoc_2024;

import java.io.IOException;
import java.util.*;

import static java.lang.System.out;
import static src.main.java.aoc_2024.Directions.Compass;

public class Day16 extends AoCDay {
    public static final String PART1_ANSWER = "102460";
    public static final String PART2_ANSWER = "527";
    private static Vector2d MAP_END;
    private static Position MAP_START;
    static private final int STEP_PRICE = 1;
    static private final int TURN_PRICE1 = 1000;
    private static HashMap<Position, Integer> distance_from_start;
    private static char[][] grid;
    private static Vector2d max;

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {


        int cost = Integer.MAX_VALUE;
        for (Compass d : Compass.values()) {
            final int dist = distance_from_start.get(new Position(MAP_END, d));
            cost = Math.min(cost, dist);
        }
        long answer = cost;
        return String.valueOf(answer);
    }

    protected String getPart2() {


         ArrayList<Position> ends = new ArrayList<>(4);
        for (Compass d : Compass.values()) {
            ends.add(new Position(MAP_END, d));
        }

        int f_min = Integer.MAX_VALUE;
        for(Position p : ends) {
            f_min = Math.min(distance_from_start.get(p), f_min);
        }
        final int min = f_min;


        Position realEnd = distance_from_start.entrySet().stream()
                 .filter(e -> e.getValue().equals(min)).map(HashMap.Entry::getKey).toList().getFirst();

         HashSet<Vector2d> paths = new HashSet<>();
        findCoordinatesOnShortestPaths(realEnd, MAP_START, distance_from_start, paths);
        return String.valueOf(paths.size());
    }

    protected void parseInput(String filename) throws IOException {
        char[][] input_grid = AoCUtils.parseGrid(filename);
        max = new Vector2d(input_grid[0].length, input_grid.length);
        grid = new char[max.y][max.x];
        for (int y = 0; y < max.y; y++) {
            for (int x = 0; x < max.x; x++) {
                final Vector2d pos = new Vector2d(x, y);
                char ch = input_grid[y][x];
                if (ch == 'S') {
                    MAP_START = new Position(pos, Compass.EAST);
                    ch = '.';
                } else if (ch == 'E') {
                    MAP_END = pos;
                    ch = '.';
                }
                grid[y][x] = ch;
            }
        }
        distance_from_start = getAllDistancesStartingFrom(MAP_START);
        out.printf("size: %d\n",distance_from_start.size());
    }

    private static void findCoordinatesOnShortestPaths(Position current, Position start, HashMap<Position, Integer> distances, HashSet<Vector2d> coordinates) {
        Vector2d c = new Vector2d(current.pos.x, current.pos.y);
        coordinates.add(c);
        int distanceToStart = distances.get(current);
        if (!current.equals(start)) {
            Optional<Position> opNeighbour = current.getNeighbourReversed(grid);
            List<Position> nextPositions = new ArrayList<>();
            if (opNeighbour.isPresent() && distances.get(opNeighbour.get()) < distanceToStart) {
                nextPositions.add(opNeighbour.get());
            }

            List<Position> turns = current.getTurns();

            int distance1 = distances.get(turns.get(0));
            int distance2 = distances.get(turns.get(1));
            if (distance1 < distance2 && distance1 < distanceToStart) {
                nextPositions.add(turns.getFirst());
            } else if (distance2 < distanceToStart) {
                nextPositions.add(turns.get(1));
            }
            for (Position p : nextPositions) {
                findCoordinatesOnShortestPaths(p, start, distances, coordinates);
            }
        }
    }

    private static HashMap<Position, Integer> getAllDistancesStartingFrom(Position start) {

        PriorityQueue<Position> work_queue = new PriorityQueue<>();
        work_queue.offer(start);
        HashMap<Position, Integer> distances = new HashMap<>(65536);


        distances.put(start, 0);

        while (!work_queue.isEmpty()) {
            Position current = work_queue.poll();
            int dist = distances.get(current);

            Vector2d step_forward = current.pos.locationAfterStep(current.direction);
            if (grid[step_forward.y][step_forward.x] == '.') {
                Position in_front_of = new Position(step_forward, current.direction);
                int n_dist = dist + STEP_PRICE;
                if (n_dist < distances.getOrDefault(in_front_of, Integer.MAX_VALUE)) {
                    distances.put(in_front_of, n_dist);
                    work_queue.offer(in_front_of);
                }
            }
            Position left_turn = new Position(current.pos, current.direction.turnLeft());
            Position right_turn = new Position(current.pos, current.direction.turnRight());
            int turn_dist = dist + TURN_PRICE1;
                if (turn_dist < distances.getOrDefault(left_turn, Integer.MAX_VALUE)) {
                    distances.put(left_turn, turn_dist);
                    work_queue.offer(left_turn);
                }

                if (turn_dist < distances.getOrDefault(right_turn, Integer.MAX_VALUE)) {
                    distances.put(right_turn, turn_dist);
                    work_queue.offer(right_turn);
                }

        }


        return distances;
    }

    public Day16(int day) {
        super(day);
    }

    public record Position(Vector2d pos, Compass direction) implements Comparable<Position> {
        @Override
        public int compareTo(final Position other) {
            if (this.pos.y != other.pos.y) {
                return Integer.compare(this.pos.y, other.pos.y);
            } else if (this.pos.x != other.pos.x) {
                return Integer.compare(this.pos.x, other.pos.x);
            } else {
                return this.direction.compareTo(other.direction);
            }
        }


        public Optional<Position> getNeighbourReversed(final char[][] grid) {
            int x = this.pos.x;
            int y = this.pos.y;

            switch (this.direction) {
                case NORTH:
                    if (isReachablePosition(x, y + 1, grid)) {
                        return Optional.of(new Position(x, y + 1, Compass.NORTH));
                    }
                    break;
                case EAST:
                    if (isReachablePosition(x - 1, y, grid)) {
                        return Optional.of(new Position(x - 1, y, Compass.EAST));
                    }
                    break;
                case SOUTH:
                    if (isReachablePosition(x, y - 1, grid)) {
                        return Optional.of(new Position(x, y - 1, Compass.SOUTH));
                    }
                    break;
                default:
                    if (isReachablePosition(x + 1, y, grid)) {
                        return Optional.of(new Position(x + 1, y, Compass.WEST));
                    }
            }
            return Optional.empty();
        }

        public List<Position> getTurns() {
            final List<Position> turns = new ArrayList<>();
            int x = this.pos.x;
            int y = this.pos.y;


            switch (direction) {
                case NORTH:
                case SOUTH:
                    turns.add(new Position(x, y, Compass.EAST));
                    turns.add(new Position(x, y, Compass.WEST));
                    break;
                case EAST:
                case WEST:
                    turns.add(new Position(x, y, Compass.NORTH));
                    turns.add(new Position(x, y, Compass.SOUTH));
                    break;
            }
            return turns;
        }

        private boolean isReachablePosition(int x, int y, char[][] grid) {
            return ((x < max.x) && (y < max.y) && (grid[y][x] != '#'));

        }

        public Position(int x, int y, Compass compass) {
            this(new Vector2d(x, y), compass);
        }


    }


}