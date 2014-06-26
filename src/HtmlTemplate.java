import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HtmlTemplate {
	private File originalFile;
	private Document doc;

	public HtmlTemplate(File file) throws IOException, Exception {
		if (!file.exists())
			throw new IOException("Html file '" + file.getName() + "' does not exist");
		String ext = FilenameUtils.getExtension(file.getName());
		if (!ext.equals("html"))
			throw new Exception("File '" + file.getName() + "' is not an html file");
		originalFile = file;
		
		// read the file into a doc
		this.doc = Jsoup.parse(file, "UTF-8");
	}
	
	public void removePlaceholderContents() {
		// find each placeholder element and remove the contents
		Elements placeholderTags = this.doc.getElementsByAttribute("cmsPlaceholder");
		for (Element placeholderTag: placeholderTags) {
			placeholderTag.empty();
		}
	}
	
	public void removeThemeCss() {
		Element themeLink = this.doc.getElementById("themeCssLink");
		if (themeLink != null)
			themeLink.remove();
	}
	
	public File getOriginalFile() {
		return this.originalFile;
	}
	
	public String getTemplateFilename() {
		return this.originalFile.getName();
	}
	
	@Override public String toString() {
		return this.doc.toString();
	}
}
