package forana.magic.printer.image;

import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import forana.magic.gatherer.Gatherer;
import forana.magic.printer.StatusReceiver;

public class ImageCache {
	private static final String IMAGE_DIR = "images";
	private static final String[] ALLOWED_EXTENSIONS = {".jpg", ".png"}; // order of probability
	
	private static Map<Long, File> fileMap = null;
	
	public static void init() throws IOException {
		fileMap = new HashMap<>();
		
		File dir = new File(IMAGE_DIR);
		if (!dir.exists() && !dir.mkdir()) {
			throw new IOException("Tried to create directory '" + IMAGE_DIR + "' but failed - check that you can permissions for this folder");
		}
		for (File file : dir.listFiles()) {
			String lname = file.getName().toLowerCase();
			for (String extension : ALLOWED_EXTENSIONS) {
				if (lname.endsWith(extension)) {
					long id = Long.parseLong(file.getName().substring(0, lname.length() - extension.length()));
					fileMap.put(id, file);
				}
			}
		}
	}
	
	public static Image get(long id, StatusReceiver receiver) throws IOException {
		receiver.setStatus("Getting image for ID " + id);
		
		File file = fileMap.get(id);
		
		if (file == null) {
			file = save(id, receiver);
		}
		
		receiver.setStatus("Loading " + file.getName());
		return ImageIO.read(file);
	}
	
	private static File save(long id, StatusReceiver receiver) throws IOException {
		String url = (new Gatherer()).getCardImageURL(id);
		receiver.setStatus("Downloading image from " + url);
		
		URLConnection conn = (new URL(url)).openConnection();
		
		String filename = null;
		if (conn.getContentType().toLowerCase().equals("image/png")) {
			filename = id + ".png";
		} else if (conn.getContentType().toLowerCase().equals("image/jpg") || conn.getContentType().toLowerCase().equals("image/jpeg")) {
			filename = id + ".jpg";
		}
		File file = new File(IMAGE_DIR + File.separator + filename);
		
		receiver.setCompleted(0);
		receiver.setTotal(conn.getContentLength());
		InputStream in = conn.getInputStream();
		OutputStream out = new FileOutputStream(file);
		
		for (int i=0; i < conn.getContentLength(); i++) {
			out.write(in.read());
			receiver.setCompleted(i+1);
		}
		
		in.close();
		out.close();
		
		fileMap.put(id, file);
		
		return file;
	}
}
