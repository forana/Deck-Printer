package forana.magic.printer.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;

public class CornerNormalizer {
	public static BufferedImage normalize(BufferedImage image) {
		int top = getDifferentialBoundary(image, 0, 0, 0, 1, image.getWidth(), 1);
		int bottom = getDifferentialBoundary(image, 0, image.getHeight() - 1, 0, -1, image.getWidth(), 1);
		int left = getDifferentialBoundary(image, 0, 0, 1, 0, 1, image.getHeight());
		int right = getDifferentialBoundary(image, image.getWidth() - 1, 0, -1, 0, 1, image.getHeight());

		BufferedImage result = image;

		if (top != 0 || bottom != image.getHeight() - 1 || left != 0 || right != image.getWidth() - 1) {
			Color c = new Color(image.getRGB(0, 0), true);
			result = result.getSubimage(left, top, right - left + 1, bottom - top + 1);
			result = addBorder(result, c);
		}

		return result;
	}

	private static final double MAX_TOLERATED_COLOR_DIFF = 0.05;

	private static int getDifferentialBoundary(BufferedImage im, int sx, int sy, int dx, int dy, int sliceWidth, int sliceHeight) {
		int x = sx;
		int y = sy;
		int[] pixels = new int[sliceWidth * sliceHeight];
		while (x >= 0 && x < im.getWidth() && y >= 0 && y < im.getHeight()) {
			pixels = im.getRGB(x, y, sliceWidth, sliceHeight, pixels, 0, 1);
			double diff = diffPixelArray(pixels);
			if (diff > MAX_TOLERATED_COLOR_DIFF) {
				break;
			}

			x += dx;
			y += dy;
		}
		return x == 0 ? y : x;
	}

	private static double diffPixelArray(int[] pixels) {
		Color base = new Color(pixels[0], true);
		double total = 0;
		for (int i = 1; i < pixels.length; i++) {
			Color other = new Color(pixels[i], true);
			total += diffColors(base, other);
		}
		return total / (pixels.length - 1);
	}

	private static double diffColors(Color c1, Color c2) {
		double diff = Math.abs(c1.getRed() - c2.getRed());
		diff += Math.abs(c1.getGreen() - c2.getGreen());
		diff += Math.abs(c1.getBlue() - c2.getBlue());

		return diff / 3 / 255;
	}

	private static final double BORDER_FRACTION = 0.05;
	//private static final int AA_SCALE = 10;

	private static BufferedImage addBorder(BufferedImage image, Color c) {
		int borderSize = (int) Math.round(Math.min(image.getWidth(), image.getHeight()) * BORDER_FRACTION);
		int border2 = borderSize * 2;
		BufferedImage borderedImage = new BufferedImage(
				image.getWidth() + border2,
				image.getHeight() + border2,
				BufferedImage.TYPE_BYTE_INDEXED);

		Graphics2D g = (Graphics2D) (borderedImage.getGraphics());

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, borderedImage.getWidth(),
				borderedImage.getHeight());

		g.setColor(c);
		g.fill(new Arc2D.Float(0, 0, border2, border2, 90, 90, Arc2D.PIE));
		g.fill(new Arc2D.Float(image.getWidth(), 0, border2, border2, 0, 90, Arc2D.PIE));
		g.fill(new Arc2D.Float(0, image.getHeight(), border2, border2, 180, 90, Arc2D.PIE));
		g.fill(new Arc2D.Float(image.getWidth(), image.getHeight(), border2, border2, 270, 90, Arc2D.PIE));
		
		g.fillRect(borderSize, 0, borderedImage.getWidth() - border2, borderedImage.getHeight());
		g.fillRect(0, borderSize, borderedImage.getWidth(), borderedImage.getHeight() - border2);
		
		g.drawImage(image, null, borderSize, borderSize);

		return borderedImage;
	}
}
