package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import static src.main.java.aoc_2024.Directions.Compass;

public class Day16 extends AoCDay {

    public static final String PART1_ANSWER = "102460";
    public static final String PART2_ANSWER = "527";
    private static Vector2d MAP_END;
    private static Position MAP_START;
    private static HashMap<Position, Integer> distance_from_start;
    private static char[][] grid;
    private static Vector2d max;
    static private final int TURN_PRICE1 = 1000;
    static private final int STEP_PRICE = 1;

    public Day16(int day) {
        super(day);
    }

    public record Position(Vector2d pos, Compass direction) implements Comparable<Position> {
        public Position(int x, int y, Compass compass) {
            this(new Vector2d(x, y), compass);
        }

        @Override
        public int compareTo(final Position other) {
            if (this.pos.y != other.pos.y) {
                return Integer.compare(this.pos.y, other.pos.y);
            } else {
                return Integer.compare(this.pos.x, other.pos.x);
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


    }

    public static String[] runDayStatic(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  16");
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
        HashMap<Position, Integer> distances = new HashMap<>();
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

    protected String getPart1() {
        distance_from_start = getAllDistancesStartingFrom(MAP_START);
        int cost = Integer.MAX_VALUE;
        for (Compass d : Compass.values()) {
            final int dist = distance_from_start.get(new Position(MAP_END, d));
            cost = Math.min(cost, dist);
        }
        long answer = cost;
        return String.valueOf(answer);
    }

    protected String getPart2() {
        distance_from_start = getAllDistancesStartingFrom(MAP_START);

        final ArrayList<Position> ends = new ArrayList<>(4);
        for (Compass d : Compass.values()) {
            ends.add(new Position(MAP_END, d));
        }


        final int min = ends.stream().mapToInt(e -> distance_from_start.get(e)).min().getAsInt();
        final Position realEnd = distance_from_start.entrySet().stream().filter(e -> e.getValue().equals(min)).map(Map.Entry::getKey).toList().getFirst();

        final HashSet<Vector2d> paths = new HashSet<>();
        findCoordinatesOnShortestPaths(realEnd, MAP_START, distance_from_start, paths);
        return String.valueOf(paths.size());
    }


}