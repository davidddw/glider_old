package com.yunshan.common;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Level;

import com.yunshan.utils.Util;

public class Client {

	public static void parseCommand(String[] args) {
		CommandLineParser parser = new DefaultParser();
		// create the Options
		Options options = new Options();
		options.addOption(Option.builder("b").longOpt("build").required(false)
				.desc("auto create env from yaml config.")
				.hasArg(true).argName("build").build());
		options.addOption(Option.builder("s").longOpt("solution").required(false)
				.desc("auto create solution from yaml config.")
				.hasArg(true).argName("solution").build());
		options.addOption(Option.builder("d").longOpt("destroy").required(false)
				.desc("auto delete env from yaml config.").hasArg(true).argName("destroy").build());
		options.addOption(Option.builder("l").longOpt("log").required(false)
				.desc("auto delete env from yaml config.")
				.hasArg(false).argName("log").build());
		options.addOption(Option.builder("h").longOpt("help").required(false)
				.desc("help for glider.").hasArg(false)
				.argName("help").build());

		args = new String[]{ "--build=d:/autotest.yml", "--log" };
		//args = new String[]{ "--destroy=d:/autotest.yml", "--log" };
		//args = new String[]{ "--solution=d:/solution_simple.yml", "--log" };
		//args = new String[]{ "--solution=d:/solution_internet.yml", "--log" };
		try {
			// parse the command line arguments
			CommandLine line = parser.parse(options, args);
			// validate that block-size has been set
			HelpFormatter formatter = new HelpFormatter();
			if (line.hasOption("help")) {
				formatter.printHelp("glider", options);
				System.exit(0);
			}
			if (line.hasOption("log")) {
			    Util.setLevel(Level.ALL);
			}
			if (line.hasOption("build")) {
				String filename = line.getOptionValue("build");
				new EnvBuilder(filename).build();
			}
			if (line.hasOption("solution")) {
				String filename = line.getOptionValue("solution");
				new SolutionBuilder(filename).build();
			}
			if (line.hasOption("destroy")) {
				String filename = line.getOptionValue("destroy");
				new EnvBuilder(filename).destroy();
			}
		} catch (ParseException exp) {
			System.out.println("Unexpected exception:" + exp.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws ParseException {
		parseCommand(args);
	}

}
