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
 */
public class App {

	private static Logger log = LoggerFactory.getLogger(App.class);
	private static ApplicationContext ctx;

	private static Options getOptions() {
		Option skosExport = OptionBuilder.withArgName("thesaurusid> <outputFile").hasArgs(2).withValueSeparator(' ').withDescription("Export a thesaurus in skos format").create("e");
		Option skosImport = OptionBuilder.withArgName("inputFile").hasArgs().withDescription("Import a thesaurus in skos format").create("i");
		Option revisionsExport = OptionBuilder.withArgName("thesaurusid> <outputFile> <[timestamp]").hasArgs(3).withValueSeparator(' ').withDescription("Export (Mistral) revisions of a thesaurus").create("r");
		Option reindex = OptionBuilder.hasArgs(0).withDescription("Reindexation").create("x");
		Options options = new Options();

		OptionGroup group = new OptionGroup();
		// Ajout des options exclusives
		group.addOption(skosExport);
		group.addOption(skosImport);
		group.addOption(revisionsExport);
		group.addOption(reindex);
		group.setRequired(true);
		options.addOptionGroup(group);
		return options;
	}

	private static void exportSkos(String thesaurusId, String outputFile) {
		log.info("Exporting " + thesaurusId + " to " + outputFile);
		CliExporter exporter = (CliExporter) ctx.getBean("cliExporter");
		exporter.exportSKOS(thesaurusId, outputFile);
	}

	private static void importSkos(String inputFile) {
		log.info("Importing " + inputFile);
		CliImporter exporter = (CliImporter) ctx.getBean("cliImporter");
		exporter.importSkos(inputFile);

	}

	private static void exportRevisions(String thesaurusId, String outputFile, long timestamp) {
		log.info("Exporting " + thesaurusId + " to " + outputFile);
		CliExporter exporter = (CliExporter) ctx.getBean("cliExporter");
		exporter.exportRevisions(thesaurusId, outputFile, timestamp);
	}

	private static void reindex() {
		log.info("Reindexing");
		CliIndexer indexer = (CliIndexer) ctx.getBean("cliIndexer");
		indexer.reindex();
	}

	public static void main(String[] args) {
		CommandLineParser parser = new GnuParser();
		Options options = getOptions();
		try {
			CommandLine cmd = parser.parse(options, args);
			ctx = new ClassPathXmlApplicationContext("classpath:applicationContext-cli.xml", "classpath*:spring/applicationContext-*.xml");
			if (cmd.hasOption("e")) {
				exportSkos(cmd.getOptionValues("e")[0], cmd.getOptionValues("e")[1]);
			} else if (cmd.hasOption("r")) {
				if (cmd.getOptionValues("r").length == 3) {
					try {
						exportRevisions(cmd.getOptionValues("r")[0], cmd.getOptionValues("r")[1], Long.parseLong(cmd.getOptionValues("r")[2]));
					} catch (NumberFormatException nex) {
						System.err.println("NumberFormatException: " + nex.getMessage());
						HelpFormatter formatter = new HelpFormatter();
						formatter.printHelp("ginco-cli", options);
					}
				} else {
					exportRevisions(cmd.getOptionValues("r")[0], cmd.getOptionValues("r")[1], 0);
				}

			} else if (cmd.hasOption("i")) {
				importSkos(cmd.getOptionValues("i")[0]);
			} else if (cmd.hasOption("x")) {
				reindex();
			}

		} catch (ParseException exp) {
			// TODO Auto-generated catch block
			System.err.println("Parsing failed.  Reason: " + exp.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("ginco-cli", options);
		}
	}
}
