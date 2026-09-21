/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.util.Random;

public class MatchLogic {
    private int totalScore = 0;
    private final int BASE_SCORE = 10; // Điểm cơ bản cho 1 cụm nổ

    // Nhiệm vụ 1: Lấy điểm số hiện tại (để sau này View lấy ra in lên màn hình)
    public int getScore() {
        return totalScore;
    }

    // Nhiệm vụ 2: Quét ma trận, tìm kẹo thẳng hàng, tính điểm và xóa (biến thành 0)
    // Trả về true nếu CÓ nổ kẹo (để GameManager biết mà chạy combo tiếp), false nếu KHÔNG có gì nổ
    public boolean checkAndScore(int[][] map) {
        int rows = map.length;
        int cols = map[0].length;
        boolean[][] toDelete = new boolean[rows][cols]; 
        boolean hasMatch = false;
        
        // Quét ngang
        for (int r = 0; r < rows; r++) {
            int c = 0;
            while (c < cols - 2) {
                if (map[r][c] != 0) {
                    int matchLen = 1;
                    while (c + matchLen < cols && map[r][c] == map[r][c + matchLen]) {
                        matchLen++;
                    }
                    if (matchLen >= 3) {
                        for (int i = 0; i < matchLen; i++) toDelete[r][c + i] = true;
                        int multiplier = matchLen - 2;
                        totalScore += (BASE_SCORE * multiplier);
                    }
                    c += matchLen;
                } else {
                    c++;
                }
            }
        }

        // Quét dọc
        for (int c = 0; c < cols; c++) {
            int r = 0;
            while (r < rows - 2) {
                if (map[r][c] != 0) {
                    int matchLen = 1;
                    while (r + matchLen < rows && map[r][c] == map[r + matchLen][c]) {
                        matchLen++;
                    }
                    if (matchLen >= 3) {
                        for (int i = 0; i < matchLen; i++) toDelete[r + i][c] = true;
                        int multiplier = matchLen - 2;
                        totalScore += (BASE_SCORE * multiplier);
                    }
                    r += matchLen;
                } else {
                    r++;
                }
            }
        }

        // Đổi kẹo bị đánh dấu thành 0
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (toDelete[r][c]) {
                    map[r][c] = 0;
                    hasMatch = true;
                }
            }
        }

        // Nhiệm vụ 3: Tự động gọi trọng lực nếu có kẹo nổ
        if (hasMatch) {
            System.out.println("--- Bảng sau khi nổ --- (Điểm: " + totalScore + ")");
            printMap(map);
            applyGravity(map); 
        }
        
        return hasMatch;
    }

    // Nhiệm vụ 4: Kéo kẹo cũ xuống lấp chỗ trống và sinh random kẹo mới ở trên cùng
    private void applyGravity(int[][] map) {
        int rows = map.length;
        int cols = map[0].length;
        Random rand = new Random();

        for (int c = 0; c < cols; c++) {
            int emptyRow = rows - 1; 

            // Kéo kẹo xuống
            for (int r = rows - 1; r >= 0; r--) {
                if (map[r][c] != 0) {
                    map[emptyRow][c] = map[r][c];
                    if (emptyRow != r) {
                        map[r][c] = 0; 
                    }
                    emptyRow--; 
                }
            }

            // Sinh kẹo ngẫu nhiên (từ 1 đến 5)
            for (int r = emptyRow; r >= 0; r--) {
                map[r][c] = rand.nextInt(5) + 1;
            }
        }
        
        System.out.println("--- Bảng sau khi rơi & sinh mới ---");
        printMap(map);
    }

    // Hàm phụ in Console để test
    private void printMap(int[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
}
