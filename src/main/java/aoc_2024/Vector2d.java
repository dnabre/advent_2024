package src.main.java.aoc_2024;



import java.util.*;

public class Vector2d implements Comparable<Vector2d> {
    public Vector2d(int[] coords) {
        if(coords.length != 2) {
            throw new IllegalArgumentException(
                    String.format("Vector2d from arrays requires array of length 2, got: %d", coords.length));
        }
        this.x = coords[0];
        this.y = coords[1];

    }

    public Vector2d move(char ch) {
        Directions.Compass dir = Directions.Compass.fromChar(ch);
        Vector2d new_vector = this.plus(dir.coordDelta());
        return new_vector;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Vector2d vector2d)) return false;
        return x == vector2d.x && y == vector2d.y;
    }
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Vector2d coordFlip() {
        return new Vector2d(this.y,this.x);
    }
    public boolean isEqual(int x, int y) {
        return ((x== this.x) && (y == this.y));
    }
    public int x;
    public int y;
    public Vector2d(Vector2d other) {
        this.x = other.x;
        this.y = other.y;
    }
    public Vector2d(int nx, int ny) {
        x = nx;
        y = ny;
    }

    public void add(Vector2d delta) {
        add(delta.x, delta.y);
    }

    private void add(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public Vector2d plus(Vector2d delta) {
        return new Vector2d(this.x + delta.x, this.y + delta.y);
    }

    public Vector2d plus(int[] offsets) {
        return new Vector2d(this.x+offsets[0], this.y+offsets[1]);
    }
    public Vector2d locationAfterStep(Directions.Compass dir) {
        Vector2d new_loc = new Vector2d(this);
        return new_loc.plus(dir.coordDelta());
    }


    public boolean inside(int xMax, int yMax) {
        if ((x < 0) || (y<0)) {
            return false;
        }
        return (x <= xMax) && (y <= yMax);
    }

    @Override
    public String toString() {
         return String.format("(%d, %d)", this.x, this.y);
    }

    public boolean nextTo(Vector2d other) {
      if((other.x == this.x) && ((other.y+1 == this.y) || (other.y == this.y+1))) {
          return true;

      }
      if((other.y == this.y) && ((other.x+1 == this.x) || (other.x == this.x+1))){
            return true;

      }

    return false;
    }
    public int manhattan(Vector2d other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }
    public static List<Vector2d[]> getAllAdjacentPairs(List<Vector2d> ls) {
        List<Vector2d[]> result = new ArrayList<>();

        Iterator<Vector2d> iter = ls.iterator();
        if (iter.hasNext()) {
            Vector2d left = iter.next();
            while (iter.hasNext()) {
                final Vector2d right = iter.next();

                Vector2d[] pair = {left, right};
                result.add(pair);
                left = right;
            }
        }
        Vector2d[] wrap_around = {ls.getLast(), ls.getFirst()};
        result.add(wrap_around);
        return result;
    }

    @Override
    public int compareTo(Vector2d o) {
       if(this.y != o.y) {
           return Integer.compare(this.y,o.y);
       } else {
           return Integer.compare(this.x,o.x);
       }
    }
}
