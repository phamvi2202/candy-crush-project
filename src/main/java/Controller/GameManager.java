
package Controller;


public class GameManager {
    private LevelLoader levelLoader;
    private int[][] currentMap;

    public GameManager() {
        levelLoader = new LevelLoader();
    }

    public void startGame() {
        System.out.println("Đang khởi động Candy Crush...");
        currentMap = levelLoader.loadMap("map.txt", 8, 8);

        System.out.println("Đã nạp bản đồ ban đầu:");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(currentMap[i][j] + " ");
            }
            System.out.println();
        }

        MatchLogic logic = new MatchLogic();
        
        // KỊCH BẢN 1: Thử một nước đi SAI (Đổi 2 viên kẹo không tạo ra cụm nổ)
        // Ví dụ: Đổi viên ở hàng 0 cột 0 với hàng 0 cột 1
        logic.swapCandies(currentMap, 0, 0, 0, 1);
        
        // KỊCH BẢN 2: Thử một nước đi ĐÚNG 
        // LƯU Ý: Để kịch bản này chạy nổ, bạn phải mở file map.txt 
        // và sắp xếp sao cho đổi 1 viên là tạo thành 3 viên giống nhau.
        // Ví dụ: logic.swapCandies(currentMap, 2, 3, 2, 4);

        System.out.println("Tổng điểm hiện tại: " + logic.getScore());
    }
    // 1. Thêm cờ khóa trạng thái
    private boolean isInputLocked = false; 

    public void processPlayerMove(int r1, int c1, int r2, int c2) {
        // 2. Chặn thao tác: Nếu đang khóa (đang có hoạt ảnh nổ/rơi), từ chối click
        if (isInputLocked) {
            System.out.println("Đang xử lý kẹo, vui lòng đợi...");
            return;
        }

        // 3. Khóa chuột ngay khi người chơi vừa đổi kẹo
        isInputLocked = true; 

        MatchLogic logic = new MatchLogic();
        boolean success = logic.swapCandies(currentMap, r1, c1, r2, c2);
        
        if (success) {
            System.out.println("Nước đi hợp lệ. Điểm: " + logic.getScore());
            // LƯU Ý GIAO TIẾP VỚI VIEW:
            // Tạm thời trên Console ta mở khóa ngay. Nhưng sau này ráp giao diện, 
            // View vẽ xong hiệu ứng rơi kẹo mới được gọi hàm mở khóa (isInputLocked = false).
            isInputLocked = false; 
        } else {
            System.out.println("Nước đi sai. Hoàn tác!");
            // Nước đi sai thì hoàn tác ngay lập tức nên mở khóa luôn để chơi tiếp
            isInputLocked = false; 
        }
    }
    
    // 4. Viết sẵn hàm Pause để sau này View ráp nút Menu vào gọi
    private boolean isPaused = false;
    
    public void togglePause() {
        isPaused = !isPaused;
        System.out.println(isPaused ? "Game Tạm Dừng!" : "Tiếp tục chơi!");
        isInputLocked = isPaused; // Khóa luôn bàn cờ khi đang pause
    }
}
