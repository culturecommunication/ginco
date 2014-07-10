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

import fr.mcc.ginco.beans.CustomConceptAttribute;
import fr.mcc.ginco.beans.CustomConceptAttributeType;
import fr.mcc.ginco.beans.Note;
import fr.mcc.ginco.beans.SplitNonPreferredTerm;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.dao.INoteDAO;
import fr.mcc.ginco.dao.ISplitNonPreferredTermDAO;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.exports.result.bean.GincoExportedEntity;
import fr.mcc.ginco.exports.result.bean.GincoExportedThesaurus;
import fr.mcc.ginco.exports.result.bean.JaxbList;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This class gives methods to import concepts and concepts notes
 */
@Component("gincoConceptImporter")
public class GincoConceptImporter {

	@Inject
	private INoteDAO noteDAO;

	@Inject
	private IThesaurusConceptDAO thesaurusConceptDAO;

	@Inject
	private ISplitNonPreferredTermDAO splitNonPreferredTermDAO;

	@Inject
	private GincoCustomAttributeImporter gincoCustomAttributeImporter;

	/**
	 * This method stores all the concepts of the thesaurus included in the
	 * {@link GincoExportedThesaurus} object given in parameter
	 *
	 * @param exportedThesaurus
	 * @return The list of stored concepts
	 */
	public List<ThesaurusConcept> storeConcepts(
			GincoExportedEntity exportedThesaurus, Thesaurus targetedThesaurus, Map<String, CustomConceptAttributeType> savedTypes) {
		List<ThesaurusConcept> conceptsToImport = exportedThesaurus
				.getConcepts();
		List<ThesaurusConcept> updatedConcepts = new ArrayList<ThesaurusConcept>();
		if (conceptsToImport != null && !conceptsToImport.isEmpty()) {
			for (ThesaurusConcept concept : conceptsToImport) {
				ThesaurusConcept updatedConcept = storeConcept(
						concept, exportedThesaurus, targetedThesaurus, savedTypes);
				updatedConcepts.add(updatedConcept);
			}
		}
		thesaurusConceptDAO.flush();
		return updatedConcepts;
	}

	/**
	 * Store a single concept and its custom attributes
	 *
	 * @param concept
	 * @param exportedThesaurus
	 * @param targetedThesaurus
	 * @param savedTypes
	 * @return
	 */
	public ThesaurusConcept storeConcept(
			ThesaurusConcept concept, GincoExportedEntity exportedThesaurus, Thesaurus targetedThesaurus, Map<String, CustomConceptAttributeType> savedTypes) {
		List<ThesaurusConcept> updatedConcepts = new ArrayList<ThesaurusConcept>();
		concept.setThesaurus(targetedThesaurus);
		updatedConcepts.add(thesaurusConceptDAO.update(concept));
		if (exportedThesaurus.getConceptAttributes() != null && exportedThesaurus.getConceptAttributes().containsKey((concept.getIdentifier()))) {
			List<CustomConceptAttribute> conceptAttributes = exportedThesaurus.getConceptAttributes()
					.get(concept.getIdentifier()).getList();

			gincoCustomAttributeImporter.storeCustomConceptAttribute(
					conceptAttributes,
					concept, savedTypes);
		}

		thesaurusConceptDAO.flush();
		return concept;
	}


	/**
	 * This method stores all the complex concepts of the thesaurus included in
	 * the {@link GincoExportedThesaurus} object given in parameter
	 *
	 * @param complexConceptsToImport
	 * @param targetedThesaurus
	 */
	public List<SplitNonPreferredTerm> storeComplexConcepts(
			List<SplitNonPreferredTerm> complexConceptsToImport,
			Thesaurus targetedThesaurus) {
		List<SplitNonPreferredTerm> updatedComplexConcepts = new ArrayList<SplitNonPreferredTerm>();
		if (complexConceptsToImport != null
				&& !complexConceptsToImport.isEmpty()) {
			for (SplitNonPreferredTerm complexConcept : complexConceptsToImport) {
				complexConcept.setThesaurus(targetedThesaurus);
				updatedComplexConcepts.add(splitNonPreferredTermDAO
						.update(complexConcept));
			}
		}
		return updatedComplexConcepts;
	}

	/**
	 * This method stores all the concept notes of the thesaurus included in the
	 * {@link GincoExportedThesaurus} object given in parameter
	 *
	 * @param conceptNotesToImport
	 * @return The list of stored notes
	 */
	public List<Note> storeConceptNotes(
			Map<String, JaxbList<Note>> conceptNotesToImport) {
		List<Note> result = new ArrayList<Note>();
		if (conceptNotesToImport != null && !conceptNotesToImport.isEmpty()) {
			Iterator<Map.Entry<String, JaxbList<Note>>> entries = conceptNotesToImport
					.entrySet().iterator();
			String conceptId = null;
			List<Note> notes = null;
			while (entries.hasNext()) {
				Map.Entry<String, JaxbList<Note>> entry = entries.next();
				// Getting the id of the concept
				conceptId = entry.getKey();

				// Getting the ids of the notes
				if (entry.getValue() != null && !entry.getValue().isEmpty()) {
					notes = entry.getValue().getList();
				}

				for (Note note : notes) {
					note.setConcept(thesaurusConceptDAO.getById(conceptId));
					result.add(noteDAO.update(note));
				}
			}
		}
		return result;
	}
}
