package Controller;

import candycrush.model.Game;
import candycrush.model.GameStatus;
import candycrush.model.Position;
import java.util.Set;

public class GameManager {
    private Game gameModel;
    
    // Các cờ trạng thái khóa đầu vào
    private boolean isInputLocked = false;
    private boolean isPaused = false;

    public GameManager() {
        // Khởi tạo Model
        this.gameModel = new Game();
    }

    public void startGame() {
        System.out.println("Đang khởi động Candy Crush (Mô hình MVC mới)...");
        gameModel.newGame();
        
        System.out.println("Mục tiêu điểm: " + gameModel.getTarget() + " | Số lượt đi: " + gameModel.getMovesLeft());
        printConsoleBoard();
    }

    // Xử lý nước đi của người chơi (Được gọi từ InputHandler)
    public void processPlayerMove(Position pos1, Position pos2) {
        // Chặn thao tác nếu đang khóa hoặc game đã kết thúc/tạm dừng
        if (isInputLocked || isPaused || gameModel.getStatus() != GameStatus.PLAYING) {
            System.out.println("Không thể thao tác lúc này!");
            return;
        }

        isInputLocked = true;
        System.out.println("\n>>> Thử tráo đổi: " + pos1 + " và " + pos2 + " <<<");

        // Giao cho Model xử lý việc đổi chỗ và kiểm tra hợp lệ
        boolean success = gameModel.trySwap(pos1, pos2);

        if (success) {
            System.out.println("Nước đi HỢP LỆ! Số lượt còn lại: " + gameModel.getMovesLeft());
            // TODO (View): Gọi View chạy animation đổi chỗ 2 viên kẹo.
            // Sau khi animation xong, gọi tiếp hàm xử lý chuỗi nổ (combo):
            processMatches(1); 
        } else {
            System.out.println("Nước đi SAI. Hoàn tác!");
            // TODO (View): Gọi View chạy animation đổi chỗ rồi đổi ngược lại (hoàn tác).
            // Mở khóa để người chơi đi lại:
            isInputLocked = false; 
        }
    }

    // Hàm đệ quy xử lý nổ dây chuyền (Combo)
    private void processMatches(int combo) {
        Set<Position> matches = gameModel.findMatches();
        
        if (!matches.isEmpty()) {
            // 1. Cập nhật điểm và xóa kẹo
            gameModel.clearMatches(matches, combo);
            System.out.println("--- Nổ " + matches.size() + " viên kẹo! (Combo x" + combo + ") ---");
            System.out.println("Điểm hiện tại: " + gameModel.getScore());
            
            // TODO (View): Gọi View chạy animation kẹo nổ/biến mất tại các vị trí trong 'matches'.

            // 2. Kéo kẹo rơi xuống và sinh kẹo mới
            gameModel.dropAndRefill();
            System.out.println("Bảng sau khi rơi và bù kẹo mới:");
            printConsoleBoard();
            
            // TODO (View): Gọi View chạy animation kẹo rơi và xuất hiện kẹo mới.

            // 3. Đệ quy tiếp tục kiểm tra xem bảng mới có tạo ra chuỗi nổ tiếp không
            // Trong game thực tế, bạn nên dùng Timer/Delay của giao diện để gọi lại hàm này
            // thay vì gọi trực tiếp (nhằm tránh việc animation chạy quá nhanh).
            processMatches(combo + 1);
            
        } else {
            // Kết thúc chuỗi combo
            checkGameState();
        }
    }

    // Kiểm tra trạng thái thắng/thua sau khi kết thúc chuỗi nổ
    private void checkGameState() {
        boolean boardReset = gameModel.ensurePlayable();
        if (boardReset) {
            System.out.println("Bàn cờ bị tắc! Đã tự động xáo trộn lại kẹo.");
            // TODO (View): Gọi View cập nhật lại toàn bộ lưới kẹo
        }

        GameStatus status = gameModel.getStatus();
        if (status == GameStatus.WON) {
            System.out.println("CHÚC MỪNG! BẠN ĐÃ QUA MÀN!");
            // TODO (View): Hiển thị màn hình Thắng
        } else if (status == GameStatus.LOST) {
            System.out.println("HẾT LƯỢT! BẠN ĐÃ THUA!");
            // TODO (View): Hiển thị màn hình Thua
        } else {
            // Vẫn đang chơi thì mở khóa chuột để thao tác tiếp
            isInputLocked = false;
        }
    }

    public void togglePause() {
        isPaused = !isPaused;
        System.out.println(isPaused ? "Game Tạm Dừng!" : "Tiếp tục chơi!");
    }

    // In bảng ra console để test (Vì chưa có UI)
    private void printConsoleBoard() {
        for (int r = 0; r < Game.ROWS; r++) {
            for (int c = 0; c < Game.COLS; c++) {
                String name = gameModel.getCandy(r, c) != null ? gameModel.getCandy(r, c).name() : "NULL";
                System.out.printf("%-8s", name);
            }
            System.out.println();
        }
    }
}