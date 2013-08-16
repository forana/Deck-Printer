package forana.magic.printer;

import java.io.IOException;

import forana.magic.printer.image.ImageCache;

public class ImageCacheTest {
	public static void main(String[] args) throws IOException {
		StatusReceiver s = new StatusReceiver() {
			public void setCompleted(int dontCare) {}
			
			public void setTotal(int total) {
				System.out.println("Total: " + total);
			}
			
			public void setStatus(String status) {
				System.out.println(status);
			}
		};
		
		ImageCache.init();
		System.out.println(ImageCache.get(1, s));
		System.out.println(ImageCache.get(10, s));
		System.out.println(ImageCache.get(100, s));
		System.out.println(ImageCache.get(1000, s));
		System.out.println(ImageCache.get(10000, s));
	}
}
