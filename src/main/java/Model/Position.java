package candycrush.model;

import java.util.Objects;

/**
 * Value object: vị trí (hàng, cột) của một ô trên bàn cờ.
 * Bất biến (immutable) + có equals/hashCode nên dùng được trong HashSet.
 */
public final class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /** Hai ô kề nhau (trên/dưới/trái/phải) khi khoảng cách Manhattan = 1. */
    public boolean isAdjacentTo(Position other) {
        return Math.abs(row - other.row) + Math.abs(col - other.col) == 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position p = (Position) o;
        return row == p.row && col == p.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
