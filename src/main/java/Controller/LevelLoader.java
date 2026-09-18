
package Controller;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LevelLoader {
    // Hàm đọc file và trả về mảng 2 chiều (bảng kẹo)
    public int[][] loadMap(String filePath, int rows, int cols) {
        int[][] map = new int[rows][cols];
        try {
            Scanner scanner = new Scanner(new File(filePath));
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (scanner.hasNextInt()) {
                        map[i][j] = scanner.nextInt();
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Lỗi: Không tìm thấy file " + filePath + ". Hãy kiểm tra lại vị trí lưu file!");
        }
        return map;
    }

}
