package src.main.java.aoc_2024;

public class Directions {
    public  enum Compass {
        NORTH,EAST,SOUTH,WEST;
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


    }

}
