package src.main.java.aoc_2024;


import java.awt.geom.Point2D;
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

    public Vector2d[] antinodes(Vector2d other) {
        int l_dx = this.x - other.x;
        int l_dy = this.y - other.y;
        int r_dx = other.x - this.x;
        int r_dy = other.y - this.y;

        Vector2d[] result = new Vector2d[2];
        result[0] = new Vector2d(  this.x + l_dx, this.y +  l_dy);
        result[1] = new Vector2d( other.x - l_dx, other.y -l_dy);

        return result;
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
