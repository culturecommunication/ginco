import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import fr.mcc.ginco.beans.ThesaurusTerm;

public class AuditGenerateSQL {
	 
	public static void main (String[] args){
		//Class<?> classes = ThesaurusTerm.class;
	     Configuration configuration = new Configuration();
	     configuration.setProperty(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
	        //for (Class<?> entityClass : classes) {
	            configuration.addAnnotatedClass(ThesaurusTerm.class);
	       // }
	     configuration.buildMappings();
	     AuditConfiguration.getFor(configuration);
	 
	     SchemaExport schemaExport = new SchemaExport(configuration);
	     schemaExport.setDelimiter(";");
	     schemaExport.setOutputFile(String.format("%s_%s.%s ", "ddl_audit", "postgres", "sql"));
	     boolean consolePrint = true;
	     boolean exportInDatabase = false;
	     schemaExport.create(consolePrint, exportInDatabase);
	}
}