package candycrush.model;

import java.awt.Color;
import java.util.Random;

/**
 * Enum: các loại kẹo. Mỗi loại kẹo gắn với một màu để vẽ.
 * Dùng enum giúp code an toàn kiểu (type-safe) và không phải so sánh chuỗi/số.
 */
public enum CandyType {
    RED(new Color(231, 76, 60)),
    ORANGE(new Color(243, 156, 18)),
    YELLOW(new Color(241, 220, 40)),
    GREEN(new Color(46, 204, 113)),
    BLUE(new Color(52, 152, 219)),
    PURPLE(new Color(155, 89, 182));

    private static final CandyType[] VALUES = values();

    private final Color color;

    CandyType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    /** Trả về một loại kẹo ngẫu nhiên. */
    public static CandyType random(Random rnd) {
        return VALUES[rnd.nextInt(VALUES.length)];
    }
}
