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
package fr.mcc.ginco.exports;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import fr.mcc.ginco.beans.*;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.exports.result.bean.FormattedLine;
import fr.mcc.ginco.imports.SKOS;
import fr.mcc.ginco.services.*;
import fr.mcc.ginco.utils.DateUtil;
import fr.mcc.ginco.utils.LabelUtil;
import org.apache.cxf.helpers.IOUtils;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.skos.*;
import org.semanticweb.skosapibinding.SKOSFormatExt;
import org.semanticweb.skosapibinding.SKOSManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.net.URI;
import java.util.*;

@Service("exportService")
public class ExportServiceImpl implements IExportService {
    @Inject
    @Named("thesaurusArrayService")
    private IThesaurusArrayService thesaurusArrayService;

    @Inject
    @Named("thesaurusTermService")
    private IThesaurusTermService thesaurusTermService;

    @Inject
    @Named("nodeLabelService")
    private INodeLabelService nodeLabelService;

    @Inject
    @Named("thesaurusConceptService")
    private IThesaurusConceptService thesaurusConceptService;

    @Inject
    @Named("thesaurusTermRoleService")
    private IThesaurusTermRoleService thesaurusTermRoleService;

    @Inject
    @Named("noteService")
    private INoteService noteService;

    @Inject
    @Named("associativeRelationshipService")
    private IAssociativeRelationshipService associativeRelationshipService;

    @Value("${ginco.default.language}")
    private String defaultLang;

    @Override
    public List<FormattedLine> getHierarchicalText(Thesaurus thesaurus) throws BusinessException {
        List<ThesaurusConcept> listTT =
                thesaurusConceptService.getTopTermThesaurusConcepts(thesaurus.getIdentifier());

        List<ThesaurusArray> orphanArrays =
                thesaurusArrayService.getArraysWithoutParentConcept(thesaurus.getIdentifier());

        Set<ThesaurusConcept> exclude = new HashSet<ThesaurusConcept>();

        for(ThesaurusArray array : orphanArrays) {
            exclude.addAll(array.getConcepts());
        }

        List<FormattedLine> result = new ArrayList<FormattedLine>();

        for(ThesaurusConcept conceptTT : listTT) {
            if(!exclude.contains(conceptTT)) {
                result.addAll(getHierarchicalText(0, conceptTT));
            }
        }

        for(ThesaurusArray array : orphanArrays) {
            addThesaurusArray(result, array, -1);
        }

        return result;
    }
  
    @Override
    public List<FormattedLine> getAlphabeticalText(Thesaurus thesaurus) throws BusinessException {
        List<FormattedLine> result = new ArrayList<FormattedLine>();

        List<ThesaurusConcept> concepts =
                thesaurusConceptService.getConceptsByThesaurusId(null,thesaurus.getThesaurusId(),null, Boolean.TRUE);

        Collections.sort(concepts, new ThesaurusConceptComparator());

        for(ThesaurusConcept concept : concepts) {
            addConceptTitle(0, result, concept);
            addConceptInfo(1, result, concept);
        }

        return result;
    }

    private String getConceptTitle(ThesaurusConcept concept) {
        return thesaurusTermService.getPreferedTerms(
                thesaurusTermService.getTermsByConceptId(concept.getIdentifier()))
                .get(0).getLexicalValue();
    }

    private String getConceptTitleLanguage(ThesaurusConcept concept) {
        return thesaurusTermService.getPreferedTerms(
                thesaurusTermService.getTermsByConceptId(concept.getIdentifier()))
                .get(0).getLanguage().getPart1();
    }


    private void addConceptTitle(Integer base, List<FormattedLine> result, ThesaurusConcept concept) {
        result.add(new FormattedLine(base, getConceptTitleWithLanguage(concept)));
    }

    private String getConceptTitleWithLanguage(ThesaurusConcept concept) {
        return thesaurusConceptService.getConceptPreferredTerm(concept.getIdentifier()).getLexicalValue();
    }

    /**
     * For alphabetic export.
     *
     * banquette
     * NA: Siège à plusieurs places, peu profond, garni, comportant éventuellement soit un dossier, soit des
     * accotoirs, soit les deux.
     * EP: banquette à accotoirs
     * banquette à dossier
     * TG: siège
     * banquette à accotoirs
     * EM: banquette
     * banquette à dossier
     * EM: banquette
     */
    private void addConceptInfo(Integer base, List<FormattedLine> result, ThesaurusConcept concept) {
        List<Note> notes = noteService.getConceptNotePaginatedList(concept.getIdentifier(), 0, 0);

        for(Note note : notes) {
            if("scopeNote".equals(note.getNoteType().getCode())) {
                result.add(new FormattedLine(base,"NA: "+note.getLexicalValue()));
            }
        }

        for(ThesaurusConcept parent : concept.getParentConcepts()) {
            result.add(new FormattedLine(base, "TG: "+getConceptTitleWithLanguage(parent)));
        }

        for(ThesaurusConcept ta : thesaurusConceptService.getThesaurusConceptList(associativeRelationshipService.getAssociatedConceptsId(concept))) {
            result.add(new FormattedLine(base, "TA: "+getConceptTitleWithLanguage(ta)));
        }


        for(ThesaurusTerm term : thesaurusTermService.getTermsByConceptId(concept.getIdentifier())) {
            if(!term.getPrefered()) {
                result.add(new FormattedLine(base, "EP: "+ LabelUtil.getLocalizedLabel(term.getLexicalValue(), term.getLanguage(), defaultLang)));
            }
        }

        for(ThesaurusConcept child : thesaurusConceptService.getChildrenByConceptId(concept.getIdentifier())) {
            result.add(new FormattedLine(base, "NT: "+getConceptTitleLanguage(child)));
        }

        for(ThesaurusTerm term : thesaurusTermService.getTermsByConceptId(concept.getIdentifier())) {
            if(!term.getPrefered()) {
                result.add(new FormattedLine(base-1, LabelUtil.getLocalizedLabel(term.getLexicalValue(), term.getLanguage(), defaultLang)));
                if(term.getRole() == null) {
                    result.add(new FormattedLine(base, thesaurusTermRoleService.getDefaultThesaurusTermRole().getCode()
                            + ": "
                            + LabelUtil.getLocalizedLabel(term.getLexicalValue(), term.getLanguage(), defaultLang)));
                } else {
                    result.add(new FormattedLine(base, term.getRole().getCode()
                            + ": "
                            + LabelUtil.getLocalizedLabel(term.getLexicalValue(), term.getLanguage(), defaultLang)));
                }
            }
        }
    }

    @Override
    public File getSKOSExport(Thesaurus thesaurus) throws BusinessException {

        final String baseURI = thesaurus.getIdentifier();

        SKOSManager man;
        SKOSDataset vocab;
        SKOSConceptScheme scheme;
        SKOSDataFactory factory;


        try {
            man = new SKOSManager();
            vocab = man.createSKOSDataset(URI.create(baseURI));
        } catch (SKOSCreationException e) {
            throw new BusinessException("Error creating dataset from URI.","error-in-skos-objects", e);
        }

        List<ThesaurusConcept> tt = thesaurusConceptService.getTopTermThesaurusConcepts(thesaurus.getIdentifier());

        factory = man.getSKOSDataFactory();

        scheme = factory.getSKOSConceptScheme(URI.create(baseURI));

        SKOSEntityAssertion schemaAssertion = factory.getSKOSEntityAssertion(scheme);

        List<SKOSChange> addList = new ArrayList<SKOSChange>();
        addList.add(new AddAssertion(vocab, schemaAssertion));

        SKOSAnnotation createdAnno = factory.getSKOSAnnotation(URI.create("http://purl.org/dct#created"), DateUtil.toString(thesaurus.getCreated()));
        SKOSAnnotationAssertion createdAssertion = factory.getSKOSAnnotationAssertion(scheme, createdAnno);
        addList.add(new AddAssertion(vocab, createdAssertion));

        SKOSAnnotation modifiedAnno = factory.getSKOSAnnotation(URI.create("http://purl.org/dct#modified"), DateUtil.toString(thesaurus.getDate()));
        SKOSAnnotationAssertion modifiedAssertion = factory.getSKOSAnnotationAssertion(scheme, modifiedAnno);
        addList.add(new AddAssertion(vocab, modifiedAssertion));

        addLine(thesaurus.getTitle(), DublinCoreVocabulary.TITLE, scheme, addList, scheme, factory, vocab);

        if(thesaurus.getCreator() != null) {
            addLine("_X_CREATOR_", DublinCoreVocabulary.CREATOR, scheme, addList, scheme, factory, vocab);
        }

        addLine(thesaurus.getRights(), DublinCoreVocabulary.RIGHTS, scheme, addList, scheme, factory, vocab);
        addLine(thesaurus.getDescription(), DublinCoreVocabulary.DESCRIPTION, scheme, addList, scheme, factory, vocab);
        addLine(thesaurus.getRelation(), DublinCoreVocabulary.RELATION, scheme, addList, scheme, factory, vocab);
        addLine(thesaurus.getSource(), DublinCoreVocabulary.SOURCE, scheme, addList, scheme, factory, vocab);
        addLine(thesaurus.getPublisher(), DublinCoreVocabulary.PUBLISHER, scheme, addList, scheme, factory, vocab);

        if(thesaurus.getType() != null) {
            addLine(thesaurus.getType().getLabel(), DublinCoreVocabulary.TYPE, scheme, addList, scheme, factory, vocab);
        }

        addLines(thesaurus.getContributor().split("\\r?\\n"), DublinCoreVocabulary.CONTRIBUTOR, scheme, addList, scheme, factory, vocab);
        addLines(thesaurus.getCoverage().split("\\r?\\n"), DublinCoreVocabulary.COVERAGE, scheme, addList, scheme, factory, vocab);
        addLines(thesaurus.getSubject().split("\\r?\\n"), DublinCoreVocabulary.SUBJECT, scheme, addList, scheme, factory, vocab);

        List<String> languages = new ArrayList<String>();
        for(Language lang : thesaurus.getLang()) {
            languages.add(lang.getId());
        }

        addLines(languages.toArray(), DublinCoreVocabulary.LANGUAGE, scheme, addList, scheme, factory, vocab);

        for(ThesaurusConcept conceptTT : tt) {
            exportSKOS(conceptTT, null, addList, scheme, factory, vocab);
        }

        try {

            String collections = addCollections(thesaurus);
            man.applyChanges(addList);

            File temp = File.createTempFile("skosExport"+DateUtil.nowDate().getTime(), ".rdf");
            temp.deleteOnExit();

            man.save(vocab, SKOSFormatExt.RDFXML, temp.toURI());

            FileInputStream fis = new FileInputStream(temp);

            String content = IOUtils.toString(fis);
            fis.close();

            String foaf = "xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"";
            String dc = "xmlns:dc=\"http://purl.org/dc/elements/1.1/\"";

            String dct_old = "xmlns:dct=\"http://purl.org/dct#\"";
            String dct = "xmlns:dct=\"http://purl.org/dc/terms/\"";

            content = content.replaceAll(dc, dc + "\n" + foaf)
                    .replaceAll(dct_old, dct)
                    .replaceAll("</rdf:RDF>", collections+"</rdf:RDF>");

            if(thesaurus.getCreator() != null) {

                String org = "\n\t\t<foaf:Organization>\n" +
                        "\t\t\t<foaf:name>NAME</foaf:name>\n" +
                        "\t\t\t<foaf:homepage>URL</foaf:homepage>\n" +
                        "\t\t</foaf:Organization>";

                org = org.replaceAll("NAME", thesaurus.getCreator().getName())
                        .replaceAll("URL", thesaurus.getCreator().getHomepage());

                String creator = "<dc:creator rdf:datatype=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#XMLLiteral\">";

                content = content.replaceAll(creator, "<dc:creator>").replaceAll("_X_CREATOR_", org);
            }

            BufferedOutputStream bos;
            FileOutputStream fos = new FileOutputStream(temp);

            bos = new BufferedOutputStream(fos);
            bos.write(content.getBytes());
            bos.flush();
            fos.close();

            return temp;

        } catch (SKOSChangeException e) {
            throw new BusinessException("Error saving data into dataset.","error-in-skos-objects", e);
        } catch (SKOSStorageException e) {
            throw new BusinessException("Error saving dataset to temp file.","error-in-skos-objects", e);
        } catch (IOException e) {
            throw new BusinessException("Error storing temporarty file for export SKOS",
                    "export-unable-to-write-temporary-file", e);
        }
    }

    private List<FormattedLine> getHierarchicalText(Integer base, ThesaurusConcept concept) throws BusinessException {
        List<FormattedLine> result = new ArrayList<FormattedLine>();

        Set<String> thesaurusArrayConcepts = new HashSet<String>();

        addConceptTitle(base, result, concept);

        List<ThesaurusArray> subOrdArrays = thesaurusArrayService.getSubOrdinatedArrays(concept.getIdentifier());

        for(ThesaurusArray subOrdArray : subOrdArrays) {
            for(ThesaurusConcept conceptInArray : subOrdArray.getConcepts()) {
                thesaurusArrayConcepts.add(conceptInArray.getIdentifier());
            }
        }

        List<ThesaurusConcept> children = new ArrayList<ThesaurusConcept>(thesaurusConceptService.getChildrenByConceptId(concept.getIdentifier()));
        Collections.sort(children, new ThesaurusConceptComparator());

        for(ThesaurusConcept child : children) {
            if (!thesaurusArrayConcepts.contains(child.getIdentifier())) {
                result.addAll(getHierarchicalText(base + 1, child));
            }
        }

        for(ThesaurusArray subOrdArray : subOrdArrays) {
            addThesaurusArray(result, subOrdArray, base);
        }

        return result;
    }

    private void addThesaurusArray(List<FormattedLine> result, ThesaurusArray subOrdArray, Integer base) throws BusinessException {
        result.add(new FormattedLine(base + 1,
                "<" + nodeLabelService.getByThesaurusArray(subOrdArray.getIdentifier()).getLexicalValue() + ">"));
        List<ThesaurusConcept> conceptsInArray =
                new ArrayList<ThesaurusConcept>(subOrdArray.getConcepts());
        Collections.sort(conceptsInArray, new ThesaurusConceptComparator());

        for(ThesaurusConcept conceptInArray : conceptsInArray) {
            result.addAll(getHierarchicalText(base + 1, conceptInArray));
        }
    }

    private String addCollections(Thesaurus thesaurus) {
        List<ThesaurusArray> arrays = thesaurusArrayService.getAllThesaurusArrayByThesaurusId(thesaurus.getIdentifier());

        if(arrays.size() != 0) {

        Model model = ModelFactory.createDefaultModel();

        for(ThesaurusArray array : arrays) {
            NodeLabel label = nodeLabelService.getByThesaurusArray(array.getIdentifier());

            Resource collectionRes = model.createResource(array.getIdentifier(), SKOS.COLLECTION);

            Resource inScheme = model.createResource(thesaurus.getIdentifier());
            model.add(collectionRes, SKOS.IN_SCHEME, inScheme);

            collectionRes.addProperty(SKOS.PREF_LABEL, label.getLexicalValue(), label.getLanguage().getPart1());

            for(ThesaurusConcept concept : array.getConcepts()) {
                Resource y = model.createResource(concept.getIdentifier());
                model.add(collectionRes, SKOS.MEMBER, y);
                model.add(collectionRes, SKOS.MEMBER, y);
            }
        }

        model.setNsPrefix("skos", SKOS.getURI());

        StringWriter sw = new StringWriter();
        model.write(sw, "RDF/XML-ABBREV");
        String result = sw.toString();
        int start = result.lastIndexOf("core#\">") + "core#\">".length() + 2;
        int end = result.lastIndexOf("</rdf:RDF>");
        return result.substring(start, end);
        }

        return "";
    }

    private void addLines(Object[] lines, DublinCoreVocabulary type, SKOSConceptScheme conceptScheme,  List<SKOSChange> addList, SKOSConceptScheme scheme,
                          SKOSDataFactory factory, SKOSDataset vocab) {
        for(Object line : lines) {
            addLine((String)line, type, conceptScheme, addList, scheme, factory, vocab);
        }
    }

    private void addLine(String line, DublinCoreVocabulary type, SKOSConceptScheme conceptScheme,  List<SKOSChange> addList, SKOSConceptScheme scheme,
                         SKOSDataFactory factory, SKOSDataset vocab) {
        SKOSAnnotation contributor = factory.getSKOSAnnotation(type.getURI(), line);
        SKOSAnnotationAssertion conAssertion = factory.getSKOSAnnotationAssertion(conceptScheme, contributor);
        addList.add(new AddAssertion(vocab, conAssertion));
    }

    private void exportSKOS(ThesaurusConcept concept, SKOSConcept parent, List<SKOSChange> addList, SKOSConceptScheme scheme,
                            SKOSDataFactory factory, SKOSDataset vocab) throws BusinessException {
        String prefLabel = getConceptTitle(concept);

        String prefLabelLang = getConceptTitleLanguage(concept);

        SKOSConcept conceptSKOS = factory.getSKOSConcept(URI.create(concept.getIdentifier()));
        SKOSEntityAssertion conceptAssertion = factory.getSKOSEntityAssertion(conceptSKOS);

        SKOSObjectRelationAssertion inScheme = factory.getSKOSObjectRelationAssertion(conceptSKOS, factory.getSKOSInSchemeProperty(), scheme);

        SKOSDataRelationAssertion prefLabelInsertion = factory.getSKOSDataRelationAssertion(conceptSKOS,
                factory.getSKOSDataProperty(factory.getSKOSPrefLabelProperty().getURI()),
                prefLabel,
                prefLabelLang);

        for(ThesaurusConcept related : thesaurusConceptService.getThesaurusConceptList(associativeRelationshipService.getAssociatedConceptsId(concept))) {
            SKOSConcept relConcept = factory.getSKOSConcept(URI.create(related.getIdentifier()));

            SKOSObjectRelationAssertion relAssertion =
                    factory.getSKOSObjectRelationAssertion(conceptSKOS, factory.getSKOSRelatedProperty(), relConcept);

            addList.add(new AddAssertion(vocab, relAssertion));
        }

        for(ThesaurusTerm altLabel : thesaurusTermService.getTermsByConceptId(concept.getIdentifier())) {
            if(altLabel.getLexicalValue().equals(prefLabel)) continue;

            SKOSDataRelationAssertion altLabelInsertion = factory.getSKOSDataRelationAssertion(conceptSKOS,
                    factory.getSKOSDataProperty(factory.getSKOSAltLabelProperty().getURI()),
                    altLabel.getLexicalValue(),
                    altLabel.getLanguage().getPart1());

            addList.add(new AddAssertion(vocab, altLabelInsertion));
        }

        addList.add(new AddAssertion(vocab, conceptAssertion));
        addList.add(new AddAssertion(vocab, inScheme));
        addList.add(new AddAssertion(vocab, prefLabelInsertion));

        List<Note> notes = noteService.getConceptNotePaginatedList(concept.getIdentifier(), 0, 0);
        for(Note note : notes) {
            if("historyNote".equals(note.getNoteType().getCode())) {
                SKOSDataRelationAssertion noteAssertion = factory.getSKOSDataRelationAssertion(conceptSKOS,
                        factory.getSKOSDataProperty(factory.getSKOSHistoryNoteDataProperty().getURI()),
                        note.getLexicalValue(),
                        note.getLanguage().getPart1());
                addList.add(new AddAssertion(vocab, noteAssertion));
            } else if("scopeNote".equals(note.getNoteType().getCode())) {
                SKOSDataRelationAssertion noteAssertion = factory.getSKOSDataRelationAssertion(conceptSKOS,
                        factory.getSKOSDataProperty(factory.getSKOSScopeNoteDataProperty().getURI()),
                        note.getLexicalValue(),
                        note.getLanguage().getPart1());
                addList.add(new AddAssertion(vocab, noteAssertion));
            }
        }

        if(parent != null) {
            SKOSObjectRelationAssertion childConnection = factory.getSKOSObjectRelationAssertion(conceptSKOS, factory.getSKOSBroaderProperty(), parent);
            SKOSObjectRelationAssertion parentConnection = factory.getSKOSObjectRelationAssertion(parent, factory.getSKOSNarrowerProperty(), conceptSKOS);
            addList.add(new AddAssertion(vocab, childConnection));
            addList.add(new AddAssertion(vocab, parentConnection));
        } else {
            SKOSObjectRelationAssertion topConcept = factory.getSKOSObjectRelationAssertion(scheme, factory.getSKOSHasTopConceptProperty(), conceptSKOS);
            addList.add(new AddAssertion(vocab, topConcept));
        }

        if(thesaurusConceptService.hasChildren(concept.getIdentifier())) {
            for(ThesaurusConcept child : thesaurusConceptService.getChildrenByConceptId(concept.getIdentifier())) {
                exportSKOS(child,  conceptSKOS, addList, scheme, factory, vocab);
            }
        }
    }

    /**
     * Comparator to use with two concepts - compares based on its lexicalValue.
     */
    class ThesaurusConceptComparator implements Comparator<ThesaurusConcept> {
        @Override
        public int compare(ThesaurusConcept o1, ThesaurusConcept o2) {
            try {
                String l1 = thesaurusTermService.getPreferedTerms(
                        thesaurusTermService.getTermsByConceptId(o1.getIdentifier()))
                        .get(0).getLexicalValue();
                String l2 = thesaurusTermService.getPreferedTerms(
                        thesaurusTermService.getTermsByConceptId(o2.getIdentifier()))
                        .get(0).getLexicalValue();
                return l1.compareToIgnoreCase(l2);
            } catch (BusinessException e) {
                return 0;
            }
        }
    }
}
