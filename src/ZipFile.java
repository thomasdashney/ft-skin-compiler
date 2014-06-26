import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;


public class ZipFile {
	public ArchiveOutputStream outputStream;
	public OutputStream zipOutput;
	
	public ZipFile(File destination) throws FileNotFoundException, ArchiveException {
		this.zipOutput = new FileOutputStream(destination);
		ArchiveStreamFactory aSF = new ArchiveStreamFactory();
		this.outputStream = aSF.createArchiveOutputStream(ArchiveStreamFactory.ZIP, zipOutput);
	}
	
	public void close() throws IOException {
		this.outputStream.finish();
		this.zipOutput.close();
	}
	
	public void addString(String newPath, String string) throws IOException {
		ZipArchiveEntry entry = new ZipArchiveEntry(newPath);
		this.outputStream.putArchiveEntry(entry);
		IOUtils.write(string, this.outputStream);
		this.outputStream.closeArchiveEntry();
	}
	
	public void addFile(String newPath, File file) throws IOException {
		ZipArchiveEntry entry = new ZipArchiveEntry(file.getName());
		this.outputStream.putArchiveEntry(entry);
		IOUtils.copy(new FileInputStream(file), this.outputStream);
		this.outputStream.closeArchiveEntry();
	}
	
	public void addDirectory(String newPath, File directory) throws IOException {
		Iterator<File> cssIterator = FileUtils.iterateFiles(directory, null, true);
		while(cssIterator.hasNext()) {
			File file = cssIterator.next();
			if (!file.getName().equals(".DS_Store")) {
				// get the full path FOLLOWING the directory specified
				int length = directory.getPath().length();
				String path = file.getPath().substring(length + 1);
				
				// create entry
				ZipArchiveEntry entry = new ZipArchiveEntry(newPath + "/" + path);
				this.outputStream.putArchiveEntry(entry);
				IOUtils.copy(new FileInputStream(file), this.outputStream);
				this.outputStream.closeArchiveEntry();
			}
		}
	}
}
