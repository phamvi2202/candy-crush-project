package candycrush.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Board: bàn cờ chứa lưới kẹo và TOÀN BỘ luật chơi cấp thấp
 * (tìm nhóm kẹo trùng, xoá, rơi xuống, bù kẹo mới...).
 * Lớp này không biết gì về giao diện (Swing) => dễ test, dễ tái sử dụng.
 */
public class Board {
    private final int rows;
    private final int cols;
    private final CandyType[][] grid;
    private final Random rnd = new Random();

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new CandyType[rows][cols];
        reset();
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean inBounds(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public CandyType get(int r, int c) {
        return grid[r][c];
    }

    /**
     * Tạo lại bàn cờ sao cho: (1) lúc đầu không có sẵn nhóm 3 kẹo trùng,
     * (2) luôn còn ít nhất một nước đi hợp lệ.
     */
    public final void reset() {
        do {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    CandyType t;
                    do {
                        t = CandyType.random(rnd);
                    } while (createsMatch(r, c, t));
                    grid[r][c] = t;
                }
            }
        } while (!hasPossibleMove());
    }

    /** Đặt kẹo t vào (r,c) có tạo ra 3 kẹo liên tiếp (chỉ xét bên trái và phía trên) không? */
    private boolean createsMatch(int r, int c, CandyType t) {
        boolean horizontal = c >= 2 && grid[r][c - 1] == t && grid[r][c - 2] == t;
        boolean vertical = r >= 2 && grid[r - 1][c] == t && grid[r - 2][c] == t;
        return horizontal || vertical;
    }

    /** Hoán đổi hai ô. */
    public void swap(Position a, Position b) {
        CandyType tmp = grid[a.getRow()][a.getCol()];
        grid[a.getRow()][a.getCol()] = grid[b.getRow()][b.getCol()];
        grid[b.getRow()][b.getCol()] = tmp;
    }

    /** Tìm tất cả ô nằm trong dãy >= 3 kẹo cùng loại (theo hàng hoặc cột). */
    public Set<Position> findMatches() {
        Set<Position> result = new HashSet<>();

        // Quét theo hàng
        for (int r = 0; r < rows; r++) {
            int start = 0;
            while (start < cols) {
                CandyType t = grid[r][start];
                int end = start;
                while (end + 1 < cols && t != null && grid[r][end + 1] == t) {
                    end++;
                }
                if (t != null && end - start + 1 >= 3) {
                    for (int c = start; c <= end; c++) {
                        result.add(new Position(r, c));
                    }
                }
                start = end + 1;
            }
        }

        // Quét theo cột
        for (int c = 0; c < cols; c++) {
            int start = 0;
            while (start < rows) {
                CandyType t = grid[start][c];
                int end = start;
                while (end + 1 < rows && t != null && grid[end + 1][c] == t) {
                    end++;
                }
                if (t != null && end - start + 1 >= 3) {
                    for (int r = start; r <= end; r++) {
                        result.add(new Position(r, c));
                    }
                }
                start = end + 1;
            }
        }
        return result;
    }

    /** Xoá các ô đã chọn (đặt về null). */
    public void remove(Set<Position> positions) {
        for (Position p : positions) {
            grid[p.getRow()][p.getCol()] = null;
        }
    }

    /** Trọng lực: kẹo rơi xuống lấp các ô trống trong từng cột. */
    public void applyGravity() {
        for (int c = 0; c < cols; c++) {
            int write = rows - 1;
            for (int r = rows - 1; r >= 0; r--) {
                if (grid[r][c] != null) {
                    grid[write][c] = grid[r][c];
                    if (write != r) {
                        grid[r][c] = null;
                    }
                    write--;
                }
            }
        }
    }

    /** Bù kẹo mới ngẫu nhiên vào các ô còn trống (phía trên cùng). */
    public void refill() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == null) {
                    grid[r][c] = CandyType.random(rnd);
                }
            }
        }
    }

    /** Còn nước đi nào tạo được match không? Thử mọi hoán đổi sang phải / xuống dưới. */
    public boolean hasPossibleMove() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Position p = new Position(r, c);
                if (c + 1 < cols && swapCreatesMatch(p, new Position(r, c + 1))) return true;
                if (r + 1 < rows && swapCreatesMatch(p, new Position(r + 1, c))) return true;
            }
        }
        return false;
    }

    /** Hoán đổi thử, kiểm tra có match không, rồi hoàn tác. */
    public boolean swapCreatesMatch(Position a, Position b) {
        swap(a, b);
        boolean ok = !findMatches().isEmpty();
        swap(a, b);
        return ok;
    }
}
