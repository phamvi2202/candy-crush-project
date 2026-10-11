package candycrush.model;

import java.util.Set;

/**
 * Game: luật chơi cấp cao (điểm, số lượt, màn chơi, thắng/thua).
 * Dùng Board để thao tác trên lưới kẹo. Đây là "Model" trong mô hình MVC.
 */
public class Game {
    public static final int ROWS = 8;
    public static final int COLS = 8;
    private static final int POINTS_PER_CANDY = 10;
    private static final int MOVES_PER_LEVEL = 20;

    private final Board board = new Board(ROWS, COLS);
    private int level;
    private int score;
    private int movesLeft;
    private int target;

    public Game() {
        newGame();
    }

    /** Bắt đầu lại từ màn 1. */
    public void newGame() {
        startLevel(1);
    }

    public void nextLevel() {
        startLevel(level + 1);
    }

    private void startLevel(int newLevel) {
        level = newLevel;
        score = 0;
        movesLeft = MOVES_PER_LEVEL;
        target = 600 + (level - 1) * 400;
        board.reset();
    }

    /**
     * Thử hoán đổi hai ô. Chỉ chấp nhận nếu hai ô kề nhau VÀ tạo ra match.
     * Nếu hợp lệ: giữ nguyên hoán đổi, trừ 1 lượt, trả về true.
     * Nếu không: hoàn tác, trả về false.
     */
    public boolean trySwap(Position a, Position b) {
        if (!a.isAdjacentTo(b)) {
            return false;
        }
        if (!board.swapCreatesMatch(a, b)) {
            return false;
        }
        board.swap(a, b);
        movesLeft--;
        return true;
    }

    public Set<Position> findMatches() {
        return board.findMatches();
    }

    /** Xoá các kẹo trùng và cộng điểm; combo càng cao điểm càng nhiều. */
    public void clearMatches(Set<Position> matches, int combo) {
        score += matches.size() * POINTS_PER_CANDY * combo;
        board.remove(matches);
    }

    public void dropAndRefill() {
        board.applyGravity();
        board.refill();
    }

    /** Nếu hết nước đi hợp lệ thì tạo lại bàn cờ. */
    public boolean ensurePlayable() {
        if (!board.hasPossibleMove()) {
            board.reset();
            return true;
        }
        return false;
    }

    public GameStatus getStatus() {
        if (score >= target) return GameStatus.WON;
        if (movesLeft <= 0) return GameStatus.LOST;
        return GameStatus.PLAYING;
    }

    public CandyType getCandy(int r, int c) {
        return board.get(r, c);
    }

    public boolean inBounds(int r, int c) {
        return board.inBounds(r, c);
    }

    public int getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    public int getTarget() {
        return target;
    }
}
