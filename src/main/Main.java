package main;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
	public static void main(String[] args) {
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
	}
}
