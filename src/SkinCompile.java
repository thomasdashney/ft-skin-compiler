

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		
		try {
			ZipFile zip = new ZipFile(destination);
		
			// add html files
			for (HtmlTemplate template: templates) {
				zip.addString("html/" + template.getTemplateFilename(), template.toString());
			}
			
			// add other necessary files
			zip.addDirectory("css", new File(dir, "css"));
			zip.addDirectory("images", new File(dir, "images"));
			zip.addDirectory("themes", new File(dir, "themes"));
			zip.addFile("skin.json", new File(dir, "skin.json"));
			
			// write zip
			zip.close();
		} 
		catch (Exception e) {
			System.err.println("Error writing zip file: " + e.getMessage());
			System.exit(1);		
		}
		
		System.out.println("-------------\n"
				+ "zip file successfully created at: " + destination.getPath());
	}
	
	// zip helper methods: 

	
}
