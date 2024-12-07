package src.main.java.aoc_2024;


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

    public char fromGrid(char[][] grid) {
        char ch = grid[x][y];
        return ch;
    }

    public void sub(Vector2d delta) {
        this.x = this.x - delta.x;
        this.y = this.y - delta.y;
    }
}
