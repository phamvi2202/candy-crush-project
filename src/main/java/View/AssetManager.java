package View;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class AssetManager {
    public static BufferedImage[] loadCandyImages() {
        BufferedImage[] images = new BufferedImage[6];
        String[] fileNames = {"red.png", "orange.png", "green.png", "blue.png", "pink.png", "purple.png"};
        
        for (int i = 0; i < 6; i++) {
            String path = "src/main/java/assets/" + fileNames[i];
            try {
                images[i] = ImageIO.read(new File(path));
            } catch (Exception e) {
                System.out.println("❌ LỖI đọc file ảnh -> " + path);
            }
        }
        return images;
    }
}