package advent_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Day12 extends AoCDay {
    public static final String PART1_ANSWER = "1464678";
    public static final String PART2_ANSWER = "877492";
    private static char[][] farm_grid;
    private static Vector2d max;
    private static ArrayList<Plot> plot_list;

    public Day12(int day) {
        super(day);
    }

    private static int calcPerimeter(Plot plot, char[][] grid) {
        int p = 0;
        for (Vector2d v : plot.tiles) {
            final Vector2d[] edges = Directions.Compass.getNeighbors(v);
            for (int e = 0; e < 4; e++) {
                final Vector2d g = edges[e];
                if ((g.x < 0) || (g.x >= max.x) || (g.y < 0) || (g.y >= max.y)) {
                    p++;
                } else {
                    char c = grid[g.y][g.x];
                    if (plot.plant != c) {
                        p++;
                    }
                }
            }
        }
        return p;
    }

    private static int calcSides(Plot region) {

        int edges = 0;
        for (Vector2d v : region.tiles) {
            Vector2d north = new Vector2d(v.x, v.y - 1);
            Vector2d north_west = new Vector2d(v.x - 1, v.y - 1);
            Vector2d north_east = new Vector2d(v.x + 1, v.y - 1);
            Vector2d east = new Vector2d(v.x + 1, v.y);
            Vector2d west = new Vector2d(v.x - 1, v.y);
            Vector2d south = new Vector2d(v.x, v.y + 1);
            Vector2d south_west = new Vector2d(v.x - 1, v.y + 1);

            if (!(region.in(north) || region.in(west) && !region.in(north_west))) {
                edges++;
            }
            if (!(region.in(south) || region.in(west) && !region.in(south_west))) {
                edges++;
            }
            if (!(region.in(west) || (region.in(north)) && (!region.in(north_west)))) {
                edges++;
            }
            if (!region.in(east) && (!region.in(north) || region.in(north_east))) {
                edges++;
            }
        }
        return edges;
    }

    private static ArrayList<Plot> getPlots(char[][] grid) {
        HashSet<Vector2d> assigned = new HashSet<>();
        ArrayList<Plot> plot_list = new ArrayList<>();
        for (int y = 0; y < max.y; y++) {
            for (int x = 0; x < max.x; x++) {
                char ch = grid[y][x];
                Vector2d here = new Vector2d(x, y);
                if (!assigned.contains(here)) {
                    HashSet<Vector2d> tiles = new HashSet<>();
                    ArrayDeque<Vector2d> work_queue = new ArrayDeque<>();
                    work_queue.add(here);
                    while (!work_queue.isEmpty()) {
                        Vector2d current = work_queue.pop();
                        char tile_char = grid[current.y][current.x];
                        if (assigned.contains(current) || (tile_char != ch)) {
                            continue;
                        }
                        tiles.add(current);
                        assigned.add(current);

                        List<Vector2d> neighbors = Directions.Compass.getNeighborsClamped(current, 0, max.x - 1);
                        work_queue.addAll(neighbors);
                    }
                    Plot p = new Plot(ch, tiles);
                    plot_list.add(p);

                }
            }
        }
        return plot_list;
    }

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        int total = 0;
        for (Plot p : plot_list) {
            total += calcPerimeter(p, farm_grid) * p.area();
        }
        long answer = total;
        return String.valueOf(answer);
    }

    protected String getPart2() {
        int total = 0;
        for (Plot p : plot_list) {
            total += calcSides(p) * p.area();
        }
        long answer = total;
        return String.valueOf(answer);
    }

    protected void parseInput(String filename) throws IOException {
        String[] lines = Files.readAllLines(Path.of(filename)).toArray(new String[0]);
        int width = lines[0].length();
        int height = lines.length;
        max = new Vector2d(width, height);
        farm_grid = new char[height][width];
        for (int y = 0; y < max.y; y++) {
            char[] line_array = lines[y].toCharArray();
            if (max.x >= 0) System.arraycopy(line_array, 0, farm_grid[y], 0, max.x);
        }
        plot_list = getPlots(farm_grid);
    }

    record Plot(char plant, HashSet<Vector2d> tiles) {

        public int area() {
            return this.tiles.size();
        }

        public boolean in(Vector2d v) {
            return tiles.contains(v);
        }

        @Override
        public String toString() {
            return String.format("[Plot - plant: %c, tiles: %d ]", this.plant, tiles.size());
        }


    }


}
