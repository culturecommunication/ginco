package fr.mcc.ginco.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Hello world!
 *
 */
public class App 
{
	
	private static Logger log = LoggerFactory.getLogger(App.class);
	private static ApplicationContext ctx;
	
	private static Options getOptions() {
		Option skosExport = OptionBuilder.withArgName("thesaurusid> <outputFile").hasArgs(2).withValueSeparator(' ').withDescription("Export a thesaurus in skos format").create("e");
		Option skosImport = OptionBuilder.withArgName("inputFile").hasArgs().withDescription("Import a thesaurus in skos format").create("i");
    	Options options = new Options();
    	
    	OptionGroup group = new OptionGroup();
    	// Ajout des options exclusives
    	group.addOption(skosExport) ;
    	group.addOption(skosImport);
    	group.setRequired(true);
    	options.addOptionGroup(group);
    	return options;
	}
	
	private static void exportSkos(String thesaurusId, String outputFile)
	{
		log.info("Exporting "+thesaurusId+" to "+outputFile);
		CliExporter exporter = (CliExporter) ctx.getBean("cliExporter");
		exporter.export(thesaurusId, outputFile);
	}
	
	private static void importSkos(String inputFile) {
		log.info("Importing "+inputFile);
		CliImporter exporter = (CliImporter) ctx.getBean("cliImporter");
		exporter.importSkos(inputFile);
		
	}
	
    public static void main( String[] args )
    {
    	

    	CommandLineParser parser = new GnuParser();
    	Options options =  getOptions();
    	try {
			CommandLine cmd = parser.parse(options , args);
			 ctx = new ClassPathXmlApplicationContext( new String[] {
		    	        "classpath:applicationContext-cli.xml","classpath*:spring/applicationContext-*.xml"});
			if (cmd.hasOption("e"))
			{
				exportSkos(cmd.getOptionValues("e")[0],cmd.getOptionValues("e")[1]);
			} else if (cmd.hasOption("i"))
			{
				importSkos(cmd.getOptionValues("i")[0]);
			}
			
			
		} catch (ParseException exp) {
			// TODO Auto-generated catch block
			System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp( "ginco-cli", options );

		}
    }


}
