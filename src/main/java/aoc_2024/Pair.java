package src.main.java.aoc_2024;

/**
 * Immutable pair, null not allowed
 *
 */
public record Pair<L, R>(L left, R right) {
    public Pair {
        if ((left == null) || (right == null)) {
            throw new IllegalArgumentException("Both elements of a Pair must be non-null");
        }
    }

}
