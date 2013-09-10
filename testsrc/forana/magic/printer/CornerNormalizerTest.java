package forana.magic.printer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import forana.magic.printer.image.CornerNormalizer;

public class CornerNormalizerTest {
	public static void main(String[] args) throws IOException {
		BufferedImage image = ImageIO.read(new File("roundingtest.jpg"));
		image = CornerNormalizer.normalize(image);
		ImageIO.write(image, "jpg", new File("roundingtest-result.jpg"));
	}
}
