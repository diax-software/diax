package me.diax.diax;

import me.diax.diax.util.style.Colors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

public class ImageIOTests {
    private static final Font WHITNEY;

    static {
        try {
            WHITNEY = Font.createFont(
                    Font.TRUETYPE_FONT,
                    ImageIOTests.class.getClassLoader().getResourceAsStream("whitney.ttf")
            ).deriveFont(Font.PLAIN, 32);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        Color header = Colors.INSTANCE.getBURPLE();
        Color bg = Colors.INSTANCE.getBLACKY();

        //        BufferedImage ctx = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        BufferedImage ctx = ImageIO.read(new File("tests.png"));

        //Creating
        Graphics2D img = ctx.createGraphics();
        img.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);

        drawBase(ctx, img, header, bg, true);
        drawProfileName(ctx, img, "AdrianTodt#0722");

        File outputfile = new File("out.png");
        ImageIO.write(ctx, "png", outputfile);
    }

    private static void drawBase(BufferedImage ctx, Graphics2D img, Color header, Color bg, boolean decorate) {
        //sized
        int h = ctx.getHeight();
        int w = ctx.getWidth();
        //limits
        int lW = w - 50;
        int lH = h - 50;

        img.setColor(header);
        img.fillPolygon(
                new int[]{100, 50, 50, lW, lW},
                new int[]{50, 100, 150, 150, 50},
                5
        );

        if (decorate) {
            int sW = lW - 50 + 8;
            int sH = 50 + 8;
            int step = 30 / 2;
            int size = 8 / 2;
            img.setColor(Colors.INSTANCE.getWHITE());
            for (int i1 = 0; i1 < 3; i1++) {
                for (int i2 = 0; i2 <= i1; i2++) {
                    int x = sW + (step * i1) + 1;
                    int y = sH + (step * i2) + 1;
                    img.fillPolygon(
                            new int[]{x, x + size, x},
                            new int[]{y, y + size, y + size},
                            3
                    );
                }
            }
        }

        //Thumbnail place
        img.setColor(Colors.INSTANCE.getWHITE());
        img.fillRect(60, 60, 80, 80);
        img.setColor(Colors.INSTANCE.getBLACKY());
        img.fillRect(65, 65, 70, 70);

        img.setColor(bg);
        img.fillRect(50, 150, lW - 50, lH - 150);
    }

    private static void drawProfileName(BufferedImage ctx, Graphics2D img, String text) {
        Font font = WHITNEY;

        img.setColor(Colors.INSTANCE.getWHITE());
        img.setFont(font);

        Rectangle2D rec = font.getStringBounds(text, img.getFontRenderContext());
        int h = (int) rec.getHeight();

        img.drawString(text, 150, 90);
    }
}
