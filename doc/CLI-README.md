Import / export command line tool

#Installation et compilation
 go to ginco directory and run maven command:
> cd ginco
> mvn clean package

which generate ginco-cli-0.0.1-SNAPSHOT.jar in ginco/ginco-cli/target directory.
check that ginco/ginco-cli/target/lib directory which contains necessary libraries, has been generated.

#Configuration
To override the default tool configuration: Add a file named ginco-cli.properties in the same
directory that the file ginco-cli-0.0.1-SNAPSHOT.jar

List of configuration directives ginco-cli.properties

* jdbc.url = Address of database (default value = jdbc:postgresql://hadoc.lxc:5432/gincodump?useUnicode=true&amp;characterEncoding=UTF-8&amp;useFastDateParsing=false, hadoc.lxc = server
name, 5432 = port, gincodump = database name)
* jdbc.username = Database database user name (Use the one defined in the settings of your database)
* jdbc.password = Database user password (Use the one defined in the settings of your database)
* solr.url = Address of the indexing server Solr

#Use
##Importing vocabularies in SKOS format
The command syntax: -i <inputFile> where:
<inputFile> is the name of an import file in SKOS format (required)

Ordering example:
> cd ginco / ginco-cli / targer
> java -jar ./ginco-cli-2.0.9-SNAPSHOT.jar -i "SKOS_Joconde_2014-02-05.rdf"

##Export vocabulary to SKOS format
The command syntax: -e <thesaurusid> <outputFile> where:
<thesaurusid> is the ARK identifier of the vocabulary (mandatory)
<outputFile> is the name of the export file in SKOS format (required)

Ordering example:
> cd ginco / ginco-cli / targer
> java -jar ./ginco-cli-2.0.9-SNAPSHOT.jar -e http://data.culture.fr/thesaurus/resource/ark:/67717/T506 "SKOS_Joconde_2014-02-05.rdf"

