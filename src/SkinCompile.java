

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SkinCompile {
	public static void main(String[] args) {
		/*
		Options options = new Options();
		options.addOption("t", false, "test flag 't'");
		
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(1);
		}
		if( cmd.hasOption("t") ) {
		    System.out.println("flag!");
		}
		else {
			System.out.println("no flag!");
		}
		*/
		
		// loop html/index.html
		//		html/inside-page.html
		//		html/widget-page.html
		
		// first, get all of the files
		
		System.out.println("program");
		
		File htmlDir = new File("html");
		File[] files = {new File(htmlDir, "index.html"),
						new File(htmlDir, "inside-page.html"),
						new File(htmlDir, "widget-page.html")};
		
		for (File file: files) {
			if (!file.exists()) // file does not exist
				continue;
			try {
				Document doc = Jsoup.parse(file, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
