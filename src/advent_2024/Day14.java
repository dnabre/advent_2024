package advent_2024;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class Day14 extends AoCDay {

    public static final String PART1_ANSWER = "214109808";
    public static final int PART1_TICKS = 100;
    public static final String PART2_ANSWER = "7687";
    private static ArrayList<Robot> robot_initial;

    public boolean[] checkAnswers(String[] answers) {
        return new boolean[]{answers[0].equals(PART1_ANSWER), answers[1].equals(PART2_ANSWER)};
    }

    protected String getPart1() {
        Vector2d map_size = AdventOfCode2024.TESTING ? new Vector2d(11, 7) : new Vector2d(101, 103);
        ArrayList<Robot> new_positions = new ArrayList<>();
        for (Robot r : robot_initial) {
            Robot n_r = r.elapsed(PART1_TICKS, map_size);
            new_positions.add(n_r);
        }


        long quad_width = map_size.x / 2L;
        long quad_height = map_size.y / 2L;

        long[] quads = new long[4];
        for (Robot r : new_positions) {
            if ((r.pos.x < quad_width) && (r.pos.y < quad_height)) {
                quads[0] += 1;
            }
            if ((r.pos.x > quad_width) && (r.pos.y < quad_height)) {
                quads[1] += 1;
            }
            if ((r.pos.x < quad_width) && (r.pos.y > quad_height)) {
                quads[2] += 1;
            }
            if ((r.pos.x > quad_width) && (r.pos.y > quad_height)) {
                quads[3] += 1;
            }
        }

        long answer = quads[0] * quads[1] * quads[2] * quads[3];
        return String.valueOf(answer);
    }

    protected String getPart2() {

        Vector2d map_size = AdventOfCode2024.TESTING ? new Vector2d(11, 7) : new Vector2d(101, 103);

        int end_step = -1;
        boolean unique;
        for (int e = 0; e < 10000; e++) {
            ArrayList<Robot> new_pos = new ArrayList<>();
            for (Robot r : robot_initial) {
                Robot n_r = r.elapsed(e, map_size);
                new_pos.add(n_r);
            }
            HashSet<Vector2d> position_set = new HashSet<>();

            unique = true;
            for (Robot r : new_pos) {
                if (position_set.contains(r.pos)) {
                    unique = false;
                    break;
                } else {
                    position_set.add(r.pos);
                    end_step = e;
                }
            }
            if (unique) break;
        }


        long answer = end_step;
        return Long.toString(answer);
    }

    protected void parseInput(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(filename));
        robot_initial = new ArrayList<>();
        for (String s : lines) {
            String[] parts = s.split("\\s+");
            parts[0] = parts[0].substring(2);
            parts[1] = parts[1].substring(2);
            Vector2d p, v;
            String[] p_parts = parts[0].split(",");
            String[] v_parts = parts[1].split(",");
            p = new Vector2d(Integer.parseInt(p_parts[0]), Integer.parseInt(p_parts[1]));
            v = new Vector2d(Integer.parseInt(v_parts[0]), Integer.parseInt(v_parts[1]));
            robot_initial.add(new Robot(p, v));
        }
    }

    public Day14(int day) {
        super(day);
    }

    record Robot(Vector2d pos, Vector2d velocity) {
        public Robot elapsed(int time, Vector2d map_size) {
            int pos_x = (pos.x + velocity.x * time) % map_size.x;
            int pos_y = (pos.y + velocity.y * time) % map_size.y;
            if (pos_x < 0) {
                pos_x = pos_x + map_size.x;
            }
            if (pos_y < 0) {
                pos_y = pos_y + map_size.y;
            }

            return new Robot(new Vector2d(pos_x, pos_y), velocity);
        }

        @Override
        public String toString() {
            return String.format("Robot: p=%s, v=%s", pos, velocity);
        }
    }

}


