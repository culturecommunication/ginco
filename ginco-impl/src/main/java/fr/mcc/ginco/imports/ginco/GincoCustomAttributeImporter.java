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
package fr.mcc.ginco.imports.ginco;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.CustomConceptAttribute;
import fr.mcc.ginco.beans.CustomConceptAttributeType;
import fr.mcc.ginco.beans.CustomTermAttribute;
import fr.mcc.ginco.beans.CustomTermAttributeType;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusTerm;
import fr.mcc.ginco.dao.ICustomConceptAttributeDAO;
import fr.mcc.ginco.dao.ICustomConceptAttributeTypeDAO;
import fr.mcc.ginco.dao.ICustomTermAttributeDAO;
import fr.mcc.ginco.dao.ICustomTermAttributeTypeDAO;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import fr.mcc.ginco.services.ICustomConceptAttributeTypeService;
import fr.mcc.ginco.services.ICustomTermAttributeTypeService;

/**
 * This class gives methods to import ginco custom attribute and custom
 * attribute types (both for terms or concepts)
 *
 */
@Component("gincoCustomAttributeImporter")
public class GincoCustomAttributeImporter {

	@Inject
	private ICustomTermAttributeTypeDAO customTermAttributeTypeDAO;

	@Inject
	private ICustomConceptAttributeTypeDAO customConceptAttributeTypeDAO;

	@Inject
	private ICustomConceptAttributeDAO customConceptAttributeDAO;

	@Inject
	private ICustomTermAttributeDAO customTermAttributeDAO;

	@Inject
	private ICustomConceptAttributeTypeService customConceptAttributeTypeService;

	@Inject
	private ICustomTermAttributeTypeService customTermAttributeTypeService;

	/**
	 * This method stores all the ginco custom attribute types for the terms of
	 * the thesaurus included in the {@link GincoExportedThesaurus} object given
	 * in parameter
	 *
	 * @param customAttributeTypesToImport
	 * @return The list of the updated ginco custom attribute types
	 */
	public Map<String, CustomTermAttributeType> storeCustomTermAttributeTypes(
			List<CustomTermAttributeType> customAttributeTypesToImport,
			Thesaurus targetedThesaurus) {
		Map<String, CustomTermAttributeType> updatedTypes = new HashMap<String, CustomTermAttributeType>();
		for (CustomTermAttributeType customTermAttributeType : customAttributeTypesToImport) {
			customTermAttributeType.setThesaurus(targetedThesaurus);
			updatedTypes.put(customTermAttributeType.getCode(), customTermAttributeTypeDAO
					.update(customTermAttributeType));
		}
		return updatedTypes;
	}

	/**
	 * This method returns the list of the imported ginco custom attribute types for the terms in a branch.
	 * If a type doesn't exist in the target thesaurus, it will be ignored.
	 *
	 * @param customAttributeTypesToImport
	 * @param targetedThesaurus
	 * @return The list of the imported ginco custom attribute types
	 */
	public Map<String, CustomTermAttributeType> getBranchCustomTermAttributeTypes(
			List<CustomTermAttributeType> customAttributeTypesToImport,
			Thesaurus targetedThesaurus) {
		Map<String, CustomTermAttributeType> existingTypesToImport = new HashMap<String, CustomTermAttributeType>();
		for (CustomTermAttributeType customTermAttributeType : customAttributeTypesToImport) {
			CustomTermAttributeType existingTypeByCode = customTermAttributeTypeDAO.getAttributeByCode(
					targetedThesaurus, customTermAttributeType.getCode());
			if (existingTypeByCode != null && existingTypeByCode.getValue().equals(customTermAttributeType.getValue())) {
				existingTypesToImport.put(existingTypeByCode.getCode(), existingTypeByCode);
			}
		}
		return existingTypesToImport;
	}

	/**
	 * This method stores all the ginco custom attribute types for the concepts
	 * of the thesaurus included in the {@link GincoExportedThesaurus} object
	 * given in parameter
	 *
	 * @param customAttributeTypesToImport
	 * @return The list of the updated ginco custom attribute types
	 */
	public Map<String, CustomConceptAttributeType> storeCustomConceptAttributeTypes(
			List<CustomConceptAttributeType> customAttributeTypesToImport,
			Thesaurus targetedThesaurus) {
		Map<String, CustomConceptAttributeType> updatedTypes = new HashMap<String, CustomConceptAttributeType>();
		for (CustomConceptAttributeType customConceptAttributeType : customAttributeTypesToImport) {
			customConceptAttributeType.setThesaurus(targetedThesaurus);
			updatedTypes.put(customConceptAttributeType.getCode(),
					customConceptAttributeTypeDAO
							.update(customConceptAttributeType));
		}
		return updatedTypes;
	}

	/**
	 * This method returns the list of the imported ginco custom attribute types for the concepts in a branch.
	 * If a type doesn't exist in the target thesaurus, it will be ignored.
	 *
	 * @param customAttributeTypesToImport
	 * @param targetedThesaurus
	 * @return The list of the imported ginco custom attribute types
	 */
	public Map<String, CustomConceptAttributeType> getBranchCustomConceptAttributeTypes(
			List<CustomConceptAttributeType> customAttributeTypesToImport,
			Thesaurus targetedThesaurus) {
		Map<String, CustomConceptAttributeType> existingTypesToImport = new HashMap<String, CustomConceptAttributeType>();
		for (CustomConceptAttributeType customConceptAttributeType : customAttributeTypesToImport) {
			CustomConceptAttributeType existingTypeByCode = customConceptAttributeTypeDAO.getAttributeByCode(
					targetedThesaurus, customConceptAttributeType.getCode());
			if (existingTypeByCode != null && existingTypeByCode.getValue().equals(customConceptAttributeType.getValue())) {
				existingTypesToImport.put(existingTypeByCode.getCode(), existingTypeByCode);
			}
		}
		return existingTypesToImport;
	}

	/**
	 * This method stores the ginco custom attribute for the given concept
	 *
	 * @param customAttributeToImport
	 */
	public void storeCustomConceptAttribute(
			List<CustomConceptAttribute> customAttributeToImport,
			ThesaurusConcept concept, Map<String, CustomConceptAttributeType> savedTypes) {

		for (CustomConceptAttribute customConceptAttribute : customAttributeToImport) {
			customConceptAttribute.setEntity(concept);
			String typeCode = customConceptAttribute.getType().getCode();
			if (savedTypes.get(typeCode) != null){
				customConceptAttribute.setType(savedTypes.get(typeCode));
				customConceptAttributeDAO.update(customConceptAttribute);
			}
		}
	}

	/**
	 * This method stores the ginco custom attribute for the given term
	 *
	 * @param customAttributeToImport
	 */
	public void storeCustomTermAttribute(
			List<CustomTermAttribute> customAttributeToImport,
			ThesaurusTerm term,  Map<String, CustomTermAttributeType> savedTypes) {
		for (CustomTermAttribute customTermAttribute : customAttributeToImport) {
			customTermAttribute.setEntity(term);
			String typeCode = customTermAttribute.getType().getCode();
			if (savedTypes.get(typeCode) != null){
				customTermAttribute.setType(savedTypes.get(typeCode));
				customTermAttributeDAO.update(customTermAttribute);
			}
		}
	}
}
