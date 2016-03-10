package com.yunshan.common;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.yunshan.cloudbuilder.op.EPCRequest;

public class Client {
    
    public static void parseCommand() {
        CommandLineParser parser = new DefaultParser();
        // create the Options
        Options options = new Options();
        options.addOption("a", "all", false, "do not hide entries starting with ." );
        options.addOption("A", "almost-all", false, "do not list implied . and .." );
        options.addOption("b", "escape", false, "print octal escapes for nongraphic characters" );
        options.addOption(Option.builder().longOpt("blocksize")
                .required(true).desc("use SIZE-byte blocks").hasArg().argName("SIZE").build());
        options.addOption( "B", "ignore-backups", false, "do not list implied entried "
                                                         + "ending with ~");
        options.addOption( "c", false, "with -lt: sort by, and show, ctime (time of last " 
                                       + "modification of file status information) with "
                                       + "-l:show ctime and sort by name otherwise: sort "
                                       + "by ctime" );
        options.addOption( "C", false, "list entries by columns" );

        String[] args1 = new String[]{ "--blocksize=10" };
        
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "ant", options );

        try {
            // parse the command line arguments
            CommandLine line = parser.parse( options, args1 );

            // validate that block-size has been set
            if( line.hasOption( "blocksize" ) ) {
                // print the value of block-size
                System.out.println( line.getOptionValue( "blocksize" ) );
            }
        }
        catch( ParseException exp ) {
            System.out.println( "Unexpected exception:" + exp.getMessage() );
        }
           //EPCRequest rc = new EPCRequest("10.33.37.28", "19c206ba-9d4e-44ce-8bae-0b8a5857a798", 2);
           //System.out.println(rc.getEPCs());
       }

    
    public static void main(String[] args) throws ParseException {
        parseCommand();
    }
        
}
