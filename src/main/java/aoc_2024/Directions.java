package src.main.java.aoc_2024;

public class Directions {
    public  enum Compass {
        NORTH,EAST,SOUTH,WEST;
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
