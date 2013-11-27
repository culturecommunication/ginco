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
package fr.mcc.ginco.imports.ginco.idgenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.CustomConceptAttribute;
import fr.mcc.ginco.beans.CustomTermAttribute;
import fr.mcc.ginco.exports.result.bean.JaxbList;

/**
 * This class generate new ids for concept and terms custom attributes
 * 
 */
@Component
public class GincoCustomAttributesIdGenerator {

	@Inject
	private GincoIdMapParser gincoIdMapParser;

	/**
	 * Updates the list of concept custom attributes with new concept ids
	 * 
	 * @param customConceptAttributes
	 *            : map of concepts custom attributes where the key is the concept identifier and the value a list of CustomConceptAttribute
	 * @param idMapping
	 *            : the map where we store the mapping between old and new ids
	 * @return Map<String, JaxbList<CustomConceptAttribute>>
	 *         : updated CustomConceptAttribute map
	 */
	public Map<String, JaxbList<CustomConceptAttribute>> getIdsForCustomConceptAttributes(
			Map<String, JaxbList<CustomConceptAttribute>> customConceptAttributes,
			Map<String, String> idMapping) {

		Map<String, JaxbList<CustomConceptAttribute>> updatedCustomAttributes = new HashMap<String, JaxbList<CustomConceptAttribute>>();

		Iterator<Map.Entry<String, JaxbList<CustomConceptAttribute>>> iterator = customConceptAttributes
				.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, JaxbList<CustomConceptAttribute>> entry = iterator
					.next();
			// New id for the key
			String newConceptId = gincoIdMapParser.getNewId(entry.getKey(), idMapping);

			JaxbList<CustomConceptAttribute> customAttrList = customConceptAttributes
					.get(entry.getKey());
			for (CustomConceptAttribute customAttr: customAttrList.getList()) {		
				customAttr.getEntity().setIdentifier(newConceptId);
			}			
			
			updatedCustomAttributes.put(newConceptId, customAttrList);
		}
		
		return updatedCustomAttributes;
	}
	
	/**
	 * Updates the list of term custom attributes with new term ids
	 * 
	 * @param customTermAttributes
	 *            : map of terms custom attributes where the key is the term identifier and the value a list of CustomTermAttribute
	 * @param idMapping
	 *            : the map where we store the mapping between old and new ids
	 * @return Map<String, JaxbList<CustomTermAttribute>>
	 *         : updated CustomTermAttribute map
	 */
	public Map<String, JaxbList<CustomTermAttribute>> getIdsForCustomTermAttributes(
			Map<String, JaxbList<CustomTermAttribute>> customTermAttributes,
			Map<String, String> idMapping) {

		Map<String, JaxbList<CustomTermAttribute>> updatedCustomAttributes = new HashMap<String, JaxbList<CustomTermAttribute>>();

		Iterator<Map.Entry<String, JaxbList<CustomTermAttribute>>> iterator = customTermAttributes
				.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<String, JaxbList<CustomTermAttribute>> entry = iterator
					.next();
			// New id for the key
			String newTermId = gincoIdMapParser.getNewId(entry.getKey(), idMapping);

			JaxbList<CustomTermAttribute> customAttrList = customTermAttributes
					.get(entry.getKey());
			for (CustomTermAttribute customAttr: customAttrList.getList()) {						
				customAttr.getEntity().setIdentifier(newTermId);
			}			
			
			updatedCustomAttributes.put(newTermId, customAttrList);
		}
		
		return updatedCustomAttributes;
	}
}
