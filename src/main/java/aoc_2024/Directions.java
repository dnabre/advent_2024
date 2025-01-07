package src.main.java.aoc_2024;

import java.util.LinkedList;
import java.util.List;

public class Directions {



    public  enum Compass {
        NORTH,EAST,SOUTH,WEST;
        public static Vector2d[] getNeighbors(Vector2d pos) {
            Vector2d[] results = new Vector2d[Compass.values().length];
            for(int i=0; i < Compass.values().length; i++) {
                Compass d = Compass.values()[i];
                Vector2d delta = pos.plus(d.coordDelta());
                results[i] = delta;
            }
            return results;
        }

        public static List<Vector2d> getNeighborsClamped(Vector2d pos, int min_idx, int max_idx) {
            List<Vector2d> results = new LinkedList<>();
            for(Compass d:Compass.values()) {
                Vector2d delta = pos.plus(d.coordDelta());
                if ((delta.x >= min_idx) && (delta.x <= max_idx) && (delta.y >= min_idx) &&(delta.y <= max_idx)) {
                    results.add(delta);
                }
            }
            return results;
        }
        public Vector2d coordDelta() {
            switch(this) {
                case NORTH -> {
                    return new Vector2d(0, -1);
                }
                case EAST -> {
                    return new Vector2d(1, 0);
                }
                case SOUTH -> {
                    return new Vector2d(0, +1);
                }
                case WEST -> {
                    return new Vector2d(-1, 0);
                }
            }
            throw new RuntimeException("This should be Unreachable");
        }
        public Compass turnRight() {
            switch(this) {
                case NORTH -> {
                    return EAST;
                }
                case EAST -> {
                    return SOUTH;
                }
                case SOUTH -> {
                    return WEST;
                }
                case WEST -> {
                    return NORTH;
                }
            }
            throw new RuntimeException("This should be Unreachable");
        }
        public Compass turnLeft() {
            switch(this) {
                case NORTH -> {
                    return WEST;
                }
                case EAST -> {
                    return NORTH;
                }
                case SOUTH -> {
                    return EAST;
                }
                case WEST -> {
                    return SOUTH;
                }
            }
            throw new RuntimeException("This should be Unreachable");
        }

        public Compass reverse() {
            switch(this) {
                case NORTH -> {
                    return SOUTH;
                }
                case EAST -> {
                    return WEST;
                }
                case SOUTH -> {
                    return NORTH;
                }
                case WEST -> {
                    return EAST;
                }
            }
            throw new RuntimeException("This should be Unreachable");
        }

        public static Compass fromChar(char ch) {
            switch (ch) {
                case '^' -> {
                    return NORTH;
                }
                case '>' -> {
                    return EAST;
                }
                case 'v' -> {
                    return SOUTH;
                }
                case '<' -> {
                    return WEST;
                }
            }
            return null;
        }
        public char toChar() {
            switch(this) {
                case NORTH -> {
                    return '^';
                }
                case EAST -> {
                    return '>';
                }
                case SOUTH -> {
                    return 'v';
                }
                case WEST -> {
                    return '<';
                }
            }
            throw new RuntimeException("This should be Unreachable");
        }


    }

}
