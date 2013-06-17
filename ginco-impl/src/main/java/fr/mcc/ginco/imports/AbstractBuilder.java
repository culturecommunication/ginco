/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture
 * et de la Communication (2013)
 * <p/>
 * contact.gincoculture_at_gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 * <p/>
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systemsand/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
package fr.mcc.ginco.imports;

import org.apache.commons.lang3.StringEscapeUtils;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * Abstract builder used to share useful methods
 *  
 */
public abstract class AbstractBuilder {
	
	public final String getSimpleStringInfo(Resource skosResource, Property prop) {
		return getSimpleStringInfo(skosResource, prop, null);
	}
	
	/**
	 * Returns the value of the given property for the given resource
	 * @param skosResource
	 * @param prop
	 * @param altProp if prop is not found, try altProp
	 * @return
	 */
	public final String getSimpleStringInfo(Resource skosResource, Property prop, Property altProp) {
		Statement stmt = skosResource.getProperty(prop);
		if (stmt!= null) {
			String toReturn = stmt.getString();
			if (toReturn != null) {
				return StringEscapeUtils.escapeHtml4(toReturn.trim());
			}
		} else {
			if (altProp != null)
			{
				return getSimpleStringInfo(skosResource, altProp);
			}
		}
		return null;
	}
	
	public final String getMultipleLineStringInfo(Resource skosResource,
			Property prop) {
		return getMultipleLineStringInfo(skosResource,prop,null);
	}
	
	
	/**
	 * Returns the concatenation of the given property values in the given resource, separated by line breaks
	 * @param skosResource
	 * @param prop
	 * @param altProp
	 * @return
	 */
	public final String getMultipleLineStringInfo(Resource skosResource,
			Property prop, Property altProp) {
		String lines = "";
		StmtIterator stmtIterator = skosResource.listProperties(prop);
		while (stmtIterator.hasNext()) {
			Statement stmt = stmtIterator.next();
			lines += stmt.getString();
			if (stmtIterator.hasNext()) {
				lines += "\n";
			}
		}
		if (lines.isEmpty() && altProp!=null){
			return getMultipleLineStringInfo(skosResource,altProp,null);
		}
		return lines;
	}
}
