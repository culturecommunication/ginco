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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.dao.IThesaurusConceptDAO;
import fr.mcc.ginco.dao.IThesaurusConceptGroupTypeDAO;
import fr.mcc.ginco.skos.namespaces.SKOS;

/**
 * builder in charge of building thesaurus concept groups
 */
@Service("skosConceptGroupBuilder")
public class ThesaurusConceptGroupBuilder extends AbstractBuilder {

	private static final String CONCEPT_GROUP_TYPE_DOMAIN = "D";
	
	@Inject
	private IThesaurusConceptDAO thesaurusConceptDAO;
	
	@Inject
	private IThesaurusConceptGroupTypeDAO thesaurusConceptGroupTypeDAO;

	public ThesaurusConceptGroupBuilder() {
		super();
	}

	/**
	 * Builds a ThesaurusArray from the given resource
	 *
	 * @param skosCollection
	 * @param model
	 * @param thesaurus
	 * @return
	 */
	public ThesaurusConceptGroup buildConceptGroup(Resource skosCollection, Model model,
	                                 Thesaurus thesaurus, Map<String, ThesaurusConceptGroup> builtConceptGroups) {

		ThesaurusConceptGroup group = new ThesaurusConceptGroup();
		group.setIdentifier(skosCollection.getURI());
		group.setThesaurus(thesaurus);
		
		// set the concept group type
		group.setConceptGroupType(thesaurusConceptGroupTypeDAO.getById(CONCEPT_GROUP_TYPE_DOMAIN));
		
		// TODO : set the notation

		StmtIterator stmtMembersItr = skosCollection
				.listProperties(SKOS.MEMBER);
		Set<ThesaurusConcept> membersConcepts = new HashSet<ThesaurusConcept>();
		while (stmtMembersItr.hasNext()) {
			Statement stmt = stmtMembersItr.next();
			Resource memberRes = stmt.getObject().asResource();
			String relatedURI = memberRes.getURI();
			if (thesaurusConceptDAO.getById(relatedURI) != null) {
				ThesaurusConcept memberConcept = thesaurusConceptDAO
						.getById(relatedURI);
				membersConcepts.add(memberConcept);
			}
		}

		group.setConcepts(membersConcepts);

		builtConceptGroups.put(skosCollection.getURI(), group);
		return group;
	}

	/**
	 * Builds children of ThesaurusArray from the given resource
	 *
	 * @param skosCollection
	 * @param thesaurus
	 * @return
	 */
	public List<ThesaurusConceptGroup> getChildrenGroups(Resource skosCollection,
	                                              Thesaurus thesaurus, Map<String, ThesaurusConceptGroup> builtConceptGroups) {

		StmtIterator stmtMembersItr = skosCollection
				.listProperties(SKOS.MEMBER);
		List<ThesaurusConceptGroup> membersGroup = new ArrayList<ThesaurusConceptGroup>();
		while (stmtMembersItr.hasNext()) {
			Statement stmt = stmtMembersItr.next();
			Resource memberRes = stmt.getObject().asResource();
			String childGroupURI = memberRes.getURI();
			if (builtConceptGroups.get(childGroupURI) != null) {
				ThesaurusConceptGroup memberGroup = builtConceptGroups.get(childGroupURI);
				memberGroup.setParent(builtConceptGroups.get(skosCollection.getURI()));
				membersGroup.add(memberGroup);
			}
		}
		return membersGroup;
	}

}
