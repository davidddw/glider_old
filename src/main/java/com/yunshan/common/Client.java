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
        options.addOption(Option.builder("b").longOpt("build")
                .required(false).desc("auto create env from yaml config.")
                .hasArg(true).argName("build").build());
        
        options.addOption(Option.builder("d").longOpt("destroy")
                .required(false).desc("auto delete env from yaml config.")
                .hasArg(true).argName("destroy").build());

        options.addOption(Option.builder("h").longOpt("help")
                .required(false).desc("help for glider.")
                .hasArg(true).argName("help").build());
        
        args = new String[]{ "--build=d:/autotest.yml" };
        //args = new String[]{ "--destroy=d:/autotest.yml" };
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(options, args);

            // validate that block-size has been set
            HelpFormatter formatter = new HelpFormatter();
            if (line.hasOption("help")) {
                formatter.printHelp("glider", options);
            } else if(line.hasOption("build")) {
                String filename = line.getOptionValue("build");
                new EnvBuilder(filename).build();
            } else if(line.hasOption("destroy")) {
                String filename = line.getOptionValue("destroy");
                new EnvBuilder(filename).destroy();
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
