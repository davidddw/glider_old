package com.yunshan.common;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Client {
    
    public static void parseCommand(String[] args) {
        CommandLineParser parser = new DefaultParser();
        // create the Options
        Options options = new Options();
        options.addOption(Option.builder("a").longOpt("autocreate")
                .required(false).desc("auto create env from yaml config.")
                .hasArg(true).argName("autocreate").build());

        options.addOption(Option.builder("h").longOpt("help")
                .required(false).desc("help for glider.")
                .hasArg(true).argName("help").build());
        
        args = new String[]{ "--autocreate=d:/autotest.yml" };
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            // validate that block-size has been set
            HelpFormatter formatter = new HelpFormatter();
            if (line.hasOption("help")) {
                formatter.printHelp("glider", options);
            } else if(line.hasOption("autocreate")) {
                String filename = line.getOptionValue("autocreate");
                new EnvBuilder(filename).build();
            } else {
                formatter.printHelp("glider", options);
            }
        }
        catch(ParseException exp) {
            System.out.println("Unexpected exception:" + exp.getMessage());
        }
    }
        
    public static void main(String[] args) throws ParseException {
        parseCommand(args);
    }
        
}
