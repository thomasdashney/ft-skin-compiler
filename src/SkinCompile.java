

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class SkinCompile {
	public static void main(String[] args) {		
		// process arguments
		if (args.length != 2) {
			System.err.println("Error: project dir and .zip destination must be specified as arguments");
			System.exit(1);
		}
		
		File dir = new File(args[0]);
		File destination = new File(args[1]);
		
		if (!dir.exists() || !dir.isDirectory()) {
			System.err.println("Error: project dir is not a valid directory");
			System.exit(1);
		}
		
		// process the html templates		
		File[] files = {new File(dir, "html/index.html"),
						new File(dir, "html/inside-page.html"),
						new File(dir, "html/widget-page.html")};
		
		List<HtmlTemplate> templates = new ArrayList<HtmlTemplate>();
		for (File file: files) {
			HtmlTemplate template = null;
			try {
				template = new HtmlTemplate(file);
			} catch (IOException e) { // io error.. ignore?
				System.out.println("Note: no " + file.getName() + " found");
			} catch (Exception e) { // actual bad message -- tell the user as an error
				System.err.println(e.getMessage());
			}
			if (template != null) { // template is good
				template.removePlaceholderContents();
				template.removeThemeCss();
				templates.add(template);
			}
		}

		// create .zip file
		
		OutputStream zipOutput = null;
		try {
			zipOutput = new FileOutputStream(destination);
		
			ArchiveOutputStream logicalZip = null;
			ArchiveStreamFactory aSF = new ArchiveStreamFactory();
			logicalZip = aSF.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutput);
		
			// html files
			for (HtmlTemplate template: templates) {
				addStringToArchive("html/" + template.getTemplateFilename(), template.toString(), logicalZip);
			}
			
			// other necessary files
			addDirectoryToArchive("css", new File(dir, "css"), logicalZip);
			addDirectoryToArchive("images", new File(dir, "images"), logicalZip);
			addDirectoryToArchive("themes", new File(dir, "themes"), logicalZip);
			addFileToArchive("skin.json", new File(dir, "skin.json"), logicalZip);
			
			// write zip
			logicalZip.finish();
			zipOutput.close();
		} 
		catch (Exception e) {
			System.err.println("Error writing zip file: " + e.getMessage());
			System.exit(1);		
		}
		
		System.out.println("-------------\n"
				+ "zip file successfully created at: " + destination.getPath());
	}
	
	// zip helper methods: 

	public static void addStringToArchive(String newPath, String string, ArchiveOutputStream stream) throws IOException {
		ZipArchiveEntry entry = new ZipArchiveEntry(newPath);
		stream.putArchiveEntry(entry);
		IOUtils.write(string, stream);
		stream.closeArchiveEntry();
	}
	
	public static void addFileToArchive(String newPath, File file, ArchiveOutputStream stream) throws IOException {
		ZipArchiveEntry entry = new ZipArchiveEntry(file.getName());
		stream.putArchiveEntry(entry);
		IOUtils.copy(new FileInputStream(file), stream);
		stream.closeArchiveEntry();
	}
	
	public static void addDirectoryToArchive(String newPath, File directory, ArchiveOutputStream stream) throws IOException {
		Iterator<File> cssIterator = FileUtils.iterateFiles(directory, null, true);
		while(cssIterator.hasNext()) {
			File file = cssIterator.next();
			if (!file.getName().equals(".DS_Store")) {
				int length = directory.getPath().length();
				String path = file.getPath().substring(length + 1);
				ZipArchiveEntry entry = new ZipArchiveEntry(newPath + "/" + path);
				stream.putArchiveEntry(entry);
				IOUtils.copy(new FileInputStream(file), stream);
				stream.closeArchiveEntry();
			}
		}
	}
}
