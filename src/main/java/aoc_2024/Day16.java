package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;


import static java.lang.System.out;
import static src.main.java.aoc_2024.Directions.Compass;

public class Day16 {

    public static final String PART1_ANSWER = "102460";
    public static final String PART2_ANSWER = "527";
    private static Vector2d MAP_END;
    private static Position MAP_START;
    private static char[][] grid;
    private static Vector2d max;
    private static ArrayList<Position> positions;

    public static String getPart1() {
        distance_from_start = getAllDistancesStartingFrom(MAP_START);
        int cost = Integer.MAX_VALUE;
        for (Compass d : Compass.values()) {
            int dist = distance_from_start.get(new Position(MAP_END, d));
            cost = Math.min(cost, dist);
        }
        long answer = cost;
        return String.valueOf(answer);
    }

    public static String getPart2() {
        distance_from_start = getAllDistancesStartingFrom(MAP_START);


//        final List<Position> ends = getEnds(grid);
        ArrayList<Position> ends = new ArrayList<>(4);
        for(Compass d: Compass.values()) {
            ends.add(new Position(MAP_END, d));
        }


        final int min = ends.stream().mapToInt(e -> distance_from_start.get(e)).min().getAsInt();
        final Position realEnd = distance_from_start.entrySet().stream().filter(e -> e.getValue().equals(min)).map(Map.Entry::getKey)
                .findFirst().get();
        final Set<Vector2d> paths = new HashSet<>();
        findCoordinatesOnShortestPaths(realEnd, MAP_START, realEnd, distance_from_start,
                grid, paths);
        return String.valueOf(paths.size());








    }
    public static String getPart2b() {
        best_seats = new HashSet<>();

        int price = search();



        long answer = best_seats.size();
        return String.valueOf(answer);
    }

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  16");
        if (AdventOfCode2024.TESTING) {
            out.print("\t (testing)");
        }
        out.println();

        String[] answers = {"", ""};


        parseInput(inputString);
//        answers[0] = getPart1();
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

    static private final int PART1_TURN_PRICE = 1000;
    static private final int PART1_STEP_PRICE = 1;

    public record Position(Vector2d pos, Compass direction) implements Comparable<Position> {
        public Position(int x, int y, Compass compass) {
            this(new Vector2d(x,y), compass);
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
                        return Optional.of(new Position(x - 1, y,  Compass.EAST));
                    }
                    break;
                case SOUTH:
                    if (isReachablePosition(x, y - 1, grid)) {
                        return Optional.of(new Position(x, y - 1,  Compass.SOUTH));
                    }
                    break;
                default:
                    if (isReachablePosition(x + 1, y, grid)) {
                        return Optional.of(new Position(x + 1, y,  Compass.WEST));
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
            return (
                    (x < max.x) &&
                            (y < max.y) &&
                            (grid[y][x] != '#'));

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
                int n_dist = dist + PART1_STEP_PRICE;
                if (n_dist < distances.getOrDefault(in_front_of, Integer.MAX_VALUE)) {
                    distances.put(in_front_of, n_dist);
                    work_queue.offer(in_front_of);
                }
            }
            Position left_turn = new Position(current.pos, current.direction.turnLeft());
            Position right_turn = new Position(current.pos, current.direction.turnRight());

            int turn_dist = dist + PART1_TURN_PRICE;
            if( turn_dist < distances.getOrDefault(left_turn, Integer.MAX_VALUE)) {
                distances.put(left_turn, turn_dist);
                work_queue.offer(left_turn);
            }
            if( turn_dist < distances.getOrDefault(right_turn, Integer.MAX_VALUE)) {
                distances.put(right_turn, turn_dist);
                work_queue.offer(right_turn);
            }
        }
        return distances;
    }


    private static void parseInput(String filename) throws IOException {
        char[][] input_grid = AoCUtils.parseGrid(filename);
        max = new Vector2d(input_grid[0].length, input_grid.length);
        grid = new char[max.y][max.x];
        positions = new ArrayList<>();
        for (int y = 0; y < max.y; y++) {
            for (int x = 0; x < max.x; x++) {
                Vector2d pos = new Vector2d(x, y);
                for (Compass d : Compass.values()) {
                    positions.add(new Position(pos, d));
                }
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
    private static HashMap<Position, Integer> distance_from_start;


    private record State(Position pos, int price, HashSet<Vector2d> visited) {
        static Comparator<State> PRICE_COMPARATOR = Comparator.comparing(State::price);
    }
    private record State2(Position pos, int price) {
        static Comparator<State2> PRICE_COMPARATOR = Comparator.comparing(State2::price);
    }
    private static final HashMap<Position, Integer> reindeerPrice = new HashMap<>();
    private static int search() {
        PriorityQueue<State> work_queue = new PriorityQueue<>(State.PRICE_COMPARATOR);
        State start= new State(MAP_START, 0, new HashSet<>(List.of(MAP_START.pos)));
        work_queue.offer(start);
        reindeerPrice.put(MAP_START, 0);
        int minPrice = Integer.MAX_VALUE;
        boolean bestPathsFound = false;

        while (!work_queue.isEmpty() && !bestPathsFound) {
            State current = work_queue.poll();
            for (State next : nextStates(current)) {
                if((next) != null) {
                    if (next.price <= reindeerPrice.getOrDefault(next.pos, Integer.MAX_VALUE)) {
                        work_queue.offer(next);
                        reindeerPrice.put(next.pos, next.price);
                        if(next.pos.pos.equals(MAP_END)) {
                            out.printf("found end\n\t%s\n", next);
                            bestPathsFound = minPrice < next.price;
                            out.printf("\tbestPathsFound: %b, minPrice, %d, next.price %d\n",
                                    bestPathsFound, minPrice, next.price);
                            minPrice = Math.min(minPrice, next.price);
                            out.printf("\tminPrice: %d, next.visited size: %d\n", minPrice, next.visited.size());
                            int pre_expand_size = best_seats.size();
                            best_seats.addAll(next.visited);
                            out.printf("\texpanding best_seats from %d to %d (by %d)\n", pre_expand_size, best_seats.size(), next.visited.size());
                        }
                    }
                }
            }
        }

        return minPrice;
    }

    private static int search2() {
        PriorityQueue<State2> work_queue = new PriorityQueue<State2>(State2.PRICE_COMPARATOR);
        State2 start= new State2(MAP_START, 0);
        work_queue.offer(start);
        reindeerPrice.put(MAP_START, 0);
        int minPrice = Integer.MAX_VALUE;


        while (!work_queue.isEmpty()) {
            State2 current = work_queue.poll();
            State2 next;
            Position current_p = current.pos;
            Vector2d step_forward = current_p.pos.locationAfterStep(current_p.direction);
            if (grid[step_forward.y][step_forward.x] == '.') {
                Position in_front_of = new Position(step_forward, current_p.direction);
                next =  new State2(in_front_of, current.price + PART1_STEP_PRICE);

                if (next.price <= reindeerPrice.getOrDefault(next.pos, Integer.MAX_VALUE)) {
                    work_queue.offer(next);
                    reindeerPrice.put(next.pos, next.price);
                    if(next.pos.pos.equals(MAP_END)) {
                        minPrice = Math.min(minPrice, next.price);
                    }
                }


            }
            Position left_turn = new Position(current_p.pos, current_p.direction.turnLeft());
            Position right_turn = new Position(current_p.pos, current_p.direction.turnRight());
            next = new State2(left_turn, current.price+PART1_TURN_PRICE);
            if (next.price <= reindeerPrice.getOrDefault(next.pos, Integer.MAX_VALUE)) {
                work_queue.offer(next);
                reindeerPrice.put(next.pos, next.price);
                if(next.pos.pos.equals(MAP_END)) {
                    minPrice = Math.min(minPrice, next.price);
                }
            }
            next =  new State2(right_turn, current.price+PART1_TURN_PRICE);
            if (next.price <= reindeerPrice.getOrDefault(next.pos, Integer.MAX_VALUE)) {
                work_queue.offer(next);
                reindeerPrice.put(next.pos, next.price);
                if(next.pos.pos.equals(MAP_END)) {
                    minPrice = Math.min(minPrice, next.price);
                }
            }

        }

        return minPrice;
    }

    private static  HashSet<Vector2d> best_seats;
    private static State[] nextStates(State c_state) {
        State[] nexts = new State[3];
        Position current = c_state.pos;
        Vector2d step_forward = current.pos.locationAfterStep(current.direction);
        if (grid[step_forward.y][step_forward.x] == '.') {
            Position in_front_of = new Position(step_forward, current.direction);
            nexts[0] = new State(in_front_of, c_state.price + PART1_STEP_PRICE, new HashSet<>(c_state.visited));
            nexts[0].visited.add(in_front_of.pos);
        } else {
            nexts[0] = null;
        }

        Position left_turn = new Position(current.pos, current.direction.turnLeft());
        Position right_turn = new Position(current.pos, current.direction.turnRight());
        nexts[1] = new State(left_turn, c_state.price+PART1_TURN_PRICE, c_state.visited);
        nexts[2] = new State(right_turn, c_state.price+PART1_TURN_PRICE, c_state.visited);
        return nexts;
    }
    private static State2[] nextStates2(State2 c_state) {
        State2[] nexts = new State2[3];
        Position current_p = c_state.pos;
        Vector2d step_forward = current_p.pos.locationAfterStep(current_p.direction);
        if (grid[step_forward.y][step_forward.x] == '.') {
            Position in_front_of = new Position(step_forward, current_p.direction);
            nexts[0] = new State2(in_front_of, c_state.price + PART1_STEP_PRICE);

        } else {
            nexts[0] = null;
        }

        Position left_turn = new Position(current_p.pos, current_p.direction.turnLeft());
        Position right_turn = new Position(current_p.pos, current_p.direction.turnRight());
        nexts[1] = new State2(left_turn, c_state.price+PART1_TURN_PRICE);
        nexts[2] = new State2(right_turn, c_state.price+PART1_TURN_PRICE);
        return nexts;
    }


    private static void findCoordinatesOnShortestPaths(final Position current, final Position start, final Position end,
                                                final Map<Position, Integer> distances, final char[][] grid, final Set<Vector2d> coordinates) {
        final Vector2d c = new Vector2d(current.pos.x, current.pos.y);
        coordinates.add(c);
        final int distanceToStart = distances.get(current);
        if (current.equals(start)) {
            return;
        } else {
            final Optional<Position> opNeighbour = current.getNeighbourReversed(grid);
            final List<Position> nextPositions = new ArrayList<>();
            if (opNeighbour.isPresent() && distances.get(opNeighbour.get()) < distanceToStart) {
                nextPositions.add(opNeighbour.get());
            }

            final List<Position> turns = current.getTurns();

            final int distance1 = distances.get(turns.get(0));
            final int distance2 = distances.get(turns.get(1));
            if (distance1 < distance2 && distance1 < distanceToStart) {
                nextPositions.add(turns.get(0));
            } else if (distance2 < distanceToStart) {
                nextPositions.add(turns.get(1));
            }
            for (final Position p : nextPositions) {
                findCoordinatesOnShortestPaths(p, start, end, distances, grid, coordinates);
            }
        }
    }




}