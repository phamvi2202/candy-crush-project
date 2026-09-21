
package Controller;


public class GameManager {
    private LevelLoader levelLoader;
    private int[][] currentMap;

    public GameManager() {
        levelLoader = new LevelLoader();
    }

    public void startGame() {
        System.out.println("Đang khởi động Candy Crush...");
        // Gọi hàm loadMap với bản đồ 8x8
        currentMap = levelLoader.loadMap("map.txt", 8, 8);

        // In mảng 2 chiều ra màn hình Console để test
        System.out.println("Đã nạp bản đồ thành công:");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(currentMap[i][j] + " ");
            }
            System.out.println();
        }
        // --- GỌI MATCHLOGIC ĐỂ CHẠY TEST ---
        System.out.println("\nBắt đầu kiểm tra nổ kẹo và hiệu ứng dây chuyền...");
        MatchLogic logic = new MatchLogic();
        
        // Vòng lặp Combo: Cứ có nổ -> rơi kẹo -> quét lại xem có nổ tiếp không
        boolean isMatching = true;
        while (isMatching) {
            isMatching = logic.checkAndScore(currentMap);
        }
        
        System.out.println("Đã xử lý xong toàn bộ chuỗi combo!");
        System.out.println("Tổng điểm bạn đạt được: " + logic.getScore());
    }
}
