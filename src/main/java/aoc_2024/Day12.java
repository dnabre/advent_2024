package src.main.java.aoc_2024;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.lang.System.in;
import static java.lang.System.out;



public class Day12 {

    public static final String PART1_ANSWER = "1464678";
    public static final String PART2_ANSWER = "877492";
    // 876933  too low
    private static ArrayList<Plot> plot_list;
    private static int plot_id = 0;
    private static char[][] farm_grid;
    private static Vector2d max;
    long answer = -1;

    public static String[] runDay(PrintStream out, String inputString) throws IOException {
        out.println("Advent of Code 2024");
        out.print("\tDay  12");
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

    private static String getPart1() {
        out.printf("found %d plots\n", plot_list.size());
        int total = 0;
        for (Plot p : plot_list) {
            total += calcPerimeter(p, farm_grid) * p.area();
        }

        long answer = total;
        return String.valueOf(answer);
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
                    Plot p = new Plot(plot_id, ch, tiles);
                    plot_list.add(p);
                    plot_id++;
                }
            }
        }
        return plot_list;
    }



    private static String getPart2() {
        char[][] grid = farm_grid;
        int[][][] offsets = {{{-1, 0}, {0, -1}, {-1, -1}},    // upper left
                {{1, 0}, {0, -1}, {1, -1}},     // upper right
                {{-1, 0}, {0, 1}, {-1, 1}},     // lower left
                {{1, 0}, {0, 1}, {1, 1}},       // lower right
        };
        int[][][] inner_offets =
                {{{-1, 0}, {0, -1}},
                {{-1, 0}, {0, 1}},
                {{1, 0}, {0, -1}},
                {{1, 0}, {0, 1}},};

        int[][] inner_k_raw = new int[4][4];
        for (int i = 0; i < 4; i++) {
            inner_k_raw[i][0] = inner_offets[i][0][0];
            inner_k_raw[i][1] = inner_offets[i][0][1];
            inner_k_raw[i][2] = inner_offets[i][1][0];
            inner_k_raw[i][3] = inner_offets[i][1][1];
        }


        int total =0;

        for (Plot pl : plot_list) {
            if(total == 159710) {
                out.printf("plot: %s\n", pl);
                out.printf("\t tiles: %s\n", pl.tiles);

            }


            HashSet<Triple> corners = new HashSet<>();
            for (Vector2d p : pl.tiles) {
                for (int i = 0; i < offsets.length; i++) {
                    ArrayList<Vector2d> vals = new ArrayList<>();
                    for (var os : offsets[i]) {
                        Vector2d v = p.plus(os);
                        vals.add(v);
                    }
                    boolean v_good = true;
                    for (Vector2d v : vals) {
                        v_good = v_good && (!pl.tiles.contains(v));
                    }
                    if (v_good) {
                        corners.add(new Triple(p.x, p.y, i));
                    }
                }
            }
            HashSet<Triple> inner_corners = new HashSet<>();
            HashSet<Vector2d> boundary_tiles = getPerimeterTiles(pl,farm_grid);

            for (Vector2d p : boundary_tiles) {
                Vector2d[] b_array = boundary_tiles.toArray(new Vector2d[boundary_tiles.size()]);
                Arrays.sort(b_array);
                out.print("bounds: ");
                out.println(Arrays.toString(b_array));
                out.printf("bounds len: %d\n", b_array.length);



                for (int i = 0; i < inner_offets.length; i++) {
                    ArrayList<Vector2d> vals = new ArrayList<>();
                    for (int[] pair : inner_offets[i]) {
                        vals.add(p.plus(pair));
                    }
                    out.printf("vals: %s\n", vals );
                    if ((pl.tiles.contains(vals.get(0))) && (pl.tiles.contains(vals.get(1)))) {
                        int dx = inner_offets[i][0][0] + inner_offets[i][1][0];
                        int dy = inner_offets[i][0][1] + inner_offets[i][1][1];
//                        out.printf("dx: %d, dy %d \n", dx, dy);

                        if (pl.tiles.contains(new Vector2d(p.x + dx, p.y + dy))) {
                            inner_corners.add(new Triple(p.x + dx, p.y + dy, i));
                        } else {

                            Vector2d v1 = new Vector2d(vals.get(0));
                            out.printf("v1: %s\t", v1);
                            Vector2d v2 = new Vector2d(vals.get(1));
                            out.printf("v2: %s\n", v2);
                            Vector2d dv = new Vector2d(v1.x - v2.x, v1.y - v2.y);
                            out.printf("dv: %s\n", dv);
                            Vector2d[] d1 = {new Vector2d(-dv.x, 0), new Vector2d(0, dy)};
                            int[] d1a = {-dv.x, 0, 0, dy};
                            out.printf("d1: %s, %s \t %s\n", d1[0], d1[1], Arrays.toString(d1a));
                            Vector2d[] d2 = {new Vector2d(dv.x, 0), new Vector2d(0, -dy)};
                            int[] d2a = {dv.x, 0, 0, -dy};
                            out.printf("d2: %s, %s \t %s\n", d2[0], d2[1], Arrays.toString(d2a));
                            int p1 = -7, p2 = -77;
                            for(int ki=0; ki < 4; ki++) {
                                if(Arrays.equals(inner_k_raw[ki], d1a)) {
                                    out.printf("found equals (ki=%d) %s, %s\n", ki, Arrays.toString(inner_k_raw[ki]), Arrays.toString(d1a));
                                    p1 = ki;
                                }
                                if(Arrays.equals(inner_k_raw[ki], d2a)) {
                                    out.printf("found equals (ki=%d) %s, %s\n", ki, Arrays.toString(inner_k_raw[ki]), Arrays.toString(d2a));
                                    p2 = ki;
                                }
                            }

                            inner_corners.add(new Triple(v1.x, v1.y, p1));
                            out.printf("inner_corners insert1: %s\n", p1);
                            inner_corners.add(new Triple(v2.x, v2.y, p2));
                            out.printf("inner_corners insert2: %s\n", p2);

                            out.printf("inner_corners: %s\n", inner_corners);
                            System.exit(-1);
                        }


                    }

                }
            }
            if(total == 159710) {
                break;
            }
            int sides = corners.size() + inner_corners.size();
            total += sides * pl.tiles.size();
            out.printf("total : %d, Plot %s, sides: %d, cost: %d\n", total, pl, sides, sides*pl.tiles.size());


        }



        out.printf("total: %d\n", total);
        long answer = total;
        return String.valueOf(answer);
    }


    private static void parseInput(String filename) throws IOException {
        String[] lines = Files.readAllLines(Path.of(filename)).toArray(new String[0]);
        out.printf("read %d files from %s\n", lines.length, filename);
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

    private static HashSet<Vector2d> getPerimeterTiles(Plot plot, char[][] grid) {
        HashSet<Vector2d> outside = new HashSet<>();

        for (Vector2d v : plot.tiles) {
            final Vector2d[] edges = Directions.Compass.getNeighbors(v);
            for (int e = 0; e < 4; e++) {
                final Vector2d g = edges[e];
                if ((g.x < 0) || (g.x >= max.x) || (g.y < 0) || (g.y >= max.y)) {
                    outside.add(g);
                } else {
                    char c = grid[g.y][g.x];
                    if (plot.plant != c) {
                        outside.add(g);
                    }
                }
            }
        }
        return outside;
    }

    record Triple(int x, int y, int i) {
        @Override
        public String toString() {
            return String.format("(%d, %d, %d)", x, y, i);
        }
    }

    record Plot(int id, char plant, HashSet<Vector2d> tiles) {

        public int area() {
            return this.tiles.size();
        }

        @Override
        public String toString() {
            return String.format("[Plot %3d - plant: %c, tiles: %d ]", this.id, this.plant, tiles.size());
        }

        public String plotDetails() {
            return String.format("[Plot %3d] A region of %c plants with price %d ", this.id, this.plant, this.area());
        }
    }
}
