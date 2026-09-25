
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
}
