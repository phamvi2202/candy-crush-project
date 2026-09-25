
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
    }
}
