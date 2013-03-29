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
package fr.mcc.ginco.exports.skos;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.skos.AddAssertion;
import org.semanticweb.skos.SKOSAnnotation;
import org.semanticweb.skos.SKOSAnnotationAssertion;
import org.semanticweb.skos.SKOSChange;
import org.semanticweb.skos.SKOSConceptScheme;
import org.semanticweb.skos.SKOSDataFactory;
import org.semanticweb.skos.SKOSDataset;
import org.springframework.stereotype.Component;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.utils.DateUtil;

/**
 * This component is in charge of exporting a thesaurus data SKOS
 * 
 */
@Component("skosThesaurusExporter")
public class SKOSThesaurusExporter {

	/**
	 * Export the thesaurus data skos API
	 * 
	 * @param thesaurus
	 * @return
	 */
	public List<SKOSChange> exportThesaurusSKOS(Thesaurus thesaurus,
			SKOSDataFactory factory, SKOSDataset vocab, SKOSConceptScheme scheme) {
		List<SKOSChange> addList = new ArrayList<SKOSChange>();

		SKOSAnnotation createdAnno = factory.getSKOSAnnotation(
				URI.create("http://purl.org/dct#created"),
				DateUtil.toString(thesaurus.getCreated()));
		SKOSAnnotationAssertion createdAssertion = factory
				.getSKOSAnnotationAssertion(scheme, createdAnno);
		addList.add(new AddAssertion(vocab, createdAssertion));

		SKOSAnnotation modifiedAnno = factory.getSKOSAnnotation(
				URI.create("http://purl.org/dct#modified"),
				DateUtil.toString(thesaurus.getDate()));
		SKOSAnnotationAssertion modifiedAssertion = factory
				.getSKOSAnnotationAssertion(scheme, modifiedAnno);
		addList.add(new AddAssertion(vocab, modifiedAssertion));

		addLine(thesaurus.getTitle(), DublinCoreVocabulary.TITLE, scheme,
				addList, factory, vocab);

		if (thesaurus.getCreator() != null) {
			addLine("_X_CREATOR_", DublinCoreVocabulary.CREATOR, scheme,
					addList, factory, vocab);
		}

		addLine(thesaurus.getRights(), DublinCoreVocabulary.RIGHTS, scheme,
				addList, factory, vocab);
		addLine(thesaurus.getDescription(), DublinCoreVocabulary.DESCRIPTION,
				scheme, addList, factory, vocab);
		addLine(thesaurus.getRelation(), DublinCoreVocabulary.RELATION, scheme,
				addList, factory, vocab);
		addLine(thesaurus.getSource(), DublinCoreVocabulary.SOURCE, scheme,
				addList, factory, vocab);
		addLine(thesaurus.getPublisher(), DublinCoreVocabulary.PUBLISHER,
				scheme, addList, factory, vocab);

		if (thesaurus.getType() != null) {
			addLine(thesaurus.getType().getLabel(), DublinCoreVocabulary.TYPE,
					scheme, addList, factory, vocab);
		}

		addLines(thesaurus.getContributor().split("\\r?\\n"),
				DublinCoreVocabulary.CONTRIBUTOR, scheme, addList, factory,
				vocab);
		addLines(thesaurus.getCoverage().split("\\r?\\n"),
				DublinCoreVocabulary.COVERAGE, scheme, addList, factory, vocab);
		addLines(thesaurus.getSubject().split("\\r?\\n"),
				DublinCoreVocabulary.SUBJECT, scheme, addList, factory, vocab);

		List<String> languages = new ArrayList<String>();
		for (Language lang : thesaurus.getLang()) {
			languages.add(lang.getId());
		}

		addLines(languages.toArray(), DublinCoreVocabulary.LANGUAGE, scheme,
				addList, factory, vocab);

		return addList;

	}

	private void addLines(Object[] lines, DublinCoreVocabulary type,
			SKOSConceptScheme conceptScheme, List<SKOSChange> addList,
			SKOSDataFactory factory, SKOSDataset vocab) {
		for (Object line : lines) {
			addLine((String) line, type, conceptScheme, addList, factory, vocab);
		}
	}

	private void addLine(String line, DublinCoreVocabulary type,
			SKOSConceptScheme conceptScheme, List<SKOSChange> addList,
			SKOSDataFactory factory, SKOSDataset vocab) {
		SKOSAnnotation contributor = factory.getSKOSAnnotation(type.getURI(),
				line);
		SKOSAnnotationAssertion conAssertion = factory
				.getSKOSAnnotationAssertion(conceptScheme, contributor);
		addList.add(new AddAssertion(vocab, conAssertion));
	}
}
