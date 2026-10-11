package Main;

import Controller.GameManager;
import candycrush.model.Position; // Thêm import lớp Position từ Model
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        GameManager game = new GameManager();
        game.startGame();

        // ---------------------------------------------------------
        // PHẦN BỔ SUNG: Vòng lặp chờ người chơi nhập lệnh từ Console
        // ---------------------------------------------------------
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- HƯỚNG DẪN CHƠI TRÊN CONSOLE ---");
        System.out.println("Nhập tọa độ 2 viên kẹo cần đổi chỗ theo cú pháp cách nhau bằng khoảng trắng: hàng1 cột1 hàng2 cột2");
        System.out.println("Ví dụ: 0 1 0 2 (đổi viên kẹo ở hàng 0 cột 1 với hàng 0 cột 2)");
        System.out.println("Nhập -1 để thoát game.");

        while (true) {
            System.out.print("\nNhập nước đi của bạn: ");
            
            // Đọc tọa độ đầu tiên
            int r1 = scanner.nextInt();
            
            // Điều kiện thoát: Nếu nhập -1 thì dừng chương trình
            if (r1 == -1) {
                System.out.println("Đã thoát game.");
                break;
            }

            // Đọc 3 tọa độ còn lại
            int c1 = scanner.nextInt();
            int r2 = scanner.nextInt();
            int c2 = scanner.nextInt();

            // Đóng gói tọa độ thành đối tượng Position để đưa cho Controller
            Position pos1 = new Position(r1, c1);
            Position pos2 = new Position(r2, c2);

            // Gửi nước đi cho GameManager xử lý
            game.processPlayerMove(pos1, pos2);
        }

        scanner.close();
    }
}