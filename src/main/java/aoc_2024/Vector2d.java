package src.main.java.aoc_2024;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Vector2d  {

    public boolean equals(Object o) {
        if (!(o instanceof Vector2d vector2d)) return false;
        return x == vector2d.x && y == vector2d.y;
    }
    public int hashCode() {
        return Objects.hash(x, y);
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
    public Vector2d locationAfterStep(Directions.Compass dir) {
        Vector2d new_loc = new Vector2d(this);
        new_loc.plus(dir.coordDelta());
        return new_loc;
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
}
