package src.main.java.aoc_2024;

import src.main.java.aoc_2024.Day12.Plot;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static java.lang.System.out;
import static java.lang.System.setSecurityManager;


public class Day12 {

    public static final String PART1_ANSWER = "199986";
    public static final String PART2_ANSWER = "236804088748754";

    record Plot(int plot_id, char plant, HashSet<Vector2d> tiles) {
        public int area() {
            return this.tiles.size();
        }

        public int part1_cost() {
            return this.area() * this.perimeter();
        }

        public int perimeter() {
            return this.calcPerimeter();

//            if(plot_perimter.containsKey(this)) {
//                return plot_perimter.get(this);
//            } else {
//                int permeter = this.calcPerimeter();
//                plot_perimter.put(this,permeter);
//                return permeter;
//            }
        }

        private int calcPerimeter() {
            int p = 0;
            for (Vector2d v : this.tiles) {
                final Vector2d[] edges = Directions.Compass.getNeighbors(v);
                for (int e = 0; e < 4; e++) {
                    final Vector2d g = edges[e];
                    if ((g.x < 0) || (g.x >= max.x) || (g.y < 0) || (g.y >= max.y)) {
                        p++;
                    } else {
                        char c = farm_grid[g.y][g.x];
                        if (this.plant != c) {
                            p++;
                        }
                    }
                }
            }
            return p;
        }

        @Override
        public String toString() {
            return String.format("[PlotId: %3d] plant: %c, tiles: %d ", this.plot_id,
                    this.plant, tiles.size());
        }

        public String plotDetails() {
            return String.format("[PlotId: %3d]A region of %c plants with price %d * %d = %d.", this.plot_id,
                    this.plant, area(), perimeter(), part1_cost());
        }

        public boolean canMerge(Plot other) {
            out.printf(" checking if merable: %d (%c) ~ %d (%c)\n", this.plot_id, this.plant,other.plot_id, other.plant);
            if (this.plant == other.plant) {
                return false;
            }

            for (Vector2d me : this.tiles) {
                for (Vector2d you : other.tiles) {
                    if (me.nextTo(you)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public void absordPlot(Plot other) {
            if( this.plot_id > other.plot_id) {
                out.printf("Plot %d trying to absorb %d. Plots with higher ids must absorb lower\n",
                        this.plot_id, other.plot_id );
                return;
            }
            assert(this.plot_id != other.plot_id);
            out.printf("\n\tPlot %d it trying to absorb plot %d\n ", this.plot_id, other.plot_id);
            int old_tile = this.tiles.size();
            int old_other= other.tiles.size();
            Vector2d[] o_tiles = other.tiles.toArray(new Vector2d[other.tiles.size()]);
            for (int i = 0; i < o_tiles.length; i++) {
                Vector2d v = o_tiles[i];
                this.tiles.add(v);
                other.tiles.remove(v);
            }

            out.printf("\t\t %s has gone from %d to %d tiles. %d has gone from %d to %d tiles\n",
                    this, old_tile, this.tiles.size(),
                    other.plot_id, old_other, other.tiles.size());
            out.printf("\t\t removing %s from id_to_plot, new size: ", other);
            id_to_plot.remove(other);
            out.println(id_to_plot.keySet().size());
        }
    }

    private static char[][] farm_grid;
    private static HashMap<Plot, Integer> plot_perimter;

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

    private static int plot_id = 0;
    private static HashMap<Integer, Plot> id_to_plot = new HashMap<>();
    protected static String getPart1() {
        AoCUtils.printGrid(farm_grid);
        HashSet<Plot> plot_set = new HashSet<>();
        HashMap<Character, ArrayList<Plot>> type_to_plot = new HashMap<>();
        HashMap<Vector2d, Plot> pos_to_plot = new HashMap<>();

        int total = 0;

        for (int y = 0; y < max.y; y++) {
            for (int x = 0; x < max.x; x++) {

                char ch = farm_grid[y][x];
                Vector2d here = new Vector2d(x, y);
                Plot my_plot = null;
                out.printf("plant '%c' @ %s\n", ch, here);
                ArrayList<Plot> plot_list = type_to_plot.getOrDefault(ch, new ArrayList<>());

                if (plot_list.size() == 0) {
                    out.printf("\t there are no plots with my plant in it\n", plot_list.size());
                    my_plot = register_plot(ch, here, plot_set, type_to_plot, pos_to_plot);
                } else {
                    out.printf("\t there are %d plots with my plant in it\n", plot_list.size());
                    for (Plot p : plot_list) {
                        out.printf("\t\t checking %s, for adjaceny to: %s\n", p, here);
                        for (Vector2d v : p.tiles) {
                            out.printf("\t\t\t %s adj?: ", v);
                            if (here.nextTo(v)) {
                                out.println(" yes");

                                p.tiles.add(here);
                                my_plot = p;
                                out.printf("\t\tfound my plot: %s\n", p);
                                break;
                            } else {
                                out.println(" no");
                            }
                        }
                        if (my_plot != null) {
                            break;
                        }
                    }
                }
                if (my_plot == null) {
                    my_plot = register_plot(ch, here, plot_set, type_to_plot, pos_to_plot);
                }
                pos_to_plot.put(here, my_plot);
            }
        }

        out.println("\n\n");
        int num_plots = plot_set.size();
        int plot_count = num_plots;

        Plot[] plot_array = plot_set.toArray(new Plot[plot_set.size()]);

        boolean changed=true;
        do {
            out.printf("num_plots: %d, id_to_plots: %d\n", num_plots, id_to_plot.size());
            changed = false;
            top:
            for(int id=0; id < plot_id; id++) {
                if (id_to_plot.containsKey(id)) {

                    Plot left = id_to_plot.get(id);
                    for (int oid = id + 1; oid < plot_id; oid++) {
                        if (id_to_plot.containsKey(oid)) {
                            out.printf("Plot %d is looking to absorb\n", left.plot_id);


                            Plot m_plot = id_to_plot.get(oid);
                            out.printf("Looking for somewhere to merge [id: %d, %c] \n", m_plot.plot_id, m_plot.plant);
                            if (left.canMerge(m_plot)) {
                                out.printf("\tcan merge into [id: %d] (%s)\n", left.plot_id, left);
                                left.absordPlot(m_plot);
                                changed = true;
                                break;
                            }

                        }


                    }
                }
            }


        } while (changed);


        out.println("---------------------------------------------------");

        for(int i=0; i < plot_id; i++) {
            if(id_to_plot.keySet().contains(i)) {
                Plot p = id_to_plot.get(i);
                int c = (p.area() * p.perimeter());
                total += c;
                out.println(p.plotDetails());
            }
        }
        out.printf("total: %d\n ", total);
        long answer = total;
        return String.valueOf(answer);
    }


    protected static String getPart2() {
        long answer = -1;
        return String.valueOf(answer);
    }

    private static Vector2d max;

    protected static void parseInput(String filename) throws IOException {
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
        plot_perimter = new HashMap<>();


    }
    private static Plot register_plot(char ch, Vector2d here, HashSet<Plot> plotSet,
                                      HashMap<Character, ArrayList<Plot>> typeToPlot, HashMap<Vector2d, Plot>
                                              posToPlot) {
        out.printf("\t *** registering plot '%c'@%s with id %d \t ", ch, here, plot_id);
        HashSet<Vector2d> tiles = new HashSet<>();
        tiles.add(here);
        Plot new_plot = new Plot(plot_id, ch, tiles);

        plotSet.add(new_plot);

        ArrayList<Plot> char_plot_list = typeToPlot.getOrDefault(ch, new ArrayList<>());
        char_plot_list.add(new_plot);
        typeToPlot.put(ch, char_plot_list);

        posToPlot.put(here, new_plot);
        id_to_plot.put(plot_id, new_plot);

        plot_id++;
        out.println(new_plot);
        return new_plot;
    }


}
