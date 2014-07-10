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
package fr.mcc.ginco.tests.exports.skos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusArrayConcept;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exports.skos.SKOSArrayExporter;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.skos.namespaces.SKOS;
import fr.mcc.ginco.utils.DateUtil;

/**
 * This component is in charge of exporting collections to SKOS
 *
 */
public class SKOSArrayExporterTest {

	@Mock(name="thesaurusArrayService")
	private IThesaurusArrayService thesaurusArrayService;

	@Mock(name="nodeLabelService")
	private INodeLabelService nodeLabelService;

	@InjectMocks
	SKOSArrayExporter skosArrayExporter;

	@Before
	public void init() {
			MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testExportCollections() throws IOException {

		Language lang = new Language();
		lang.setPart1("fr");

		Thesaurus th = new Thesaurus();
		th.setIdentifier("http://th1");
		List<ThesaurusArray> arrays = new ArrayList<ThesaurusArray>();

		ThesaurusConcept c1 = new ThesaurusConcept();
		c1.setIdentifier("http://c1");
		ThesaurusConcept c2 = new ThesaurusConcept();
		c2.setIdentifier("http://c2");
		ThesaurusConcept c3 = new ThesaurusConcept();
		c3.setIdentifier("http://c3");
		ThesaurusConcept c4 = new ThesaurusConcept();
		c4.setIdentifier("http://c4");
		ThesaurusConcept c5 = new ThesaurusConcept();
		c5.setIdentifier("http://c5");


		ThesaurusArray th1 = new ThesaurusArray();
		th1.setIdentifier("http://th1");
		Set<ThesaurusArrayConcept> th1Concepts = new HashSet<ThesaurusArrayConcept>();
		ThesaurusArrayConcept.Id thac1id= new ThesaurusArrayConcept.Id();
		thac1id.setConceptId(c1.getIdentifier());
		thac1id.setThesaurusArrayId(th1.getIdentifier());
		ThesaurusArrayConcept thac1= new ThesaurusArrayConcept();
		thac1.setIdentifier(thac1id);
		th1Concepts.add(thac1);

		ThesaurusArrayConcept.Id thac2id= new ThesaurusArrayConcept.Id();
		thac2id.setConceptId(c2.getIdentifier());
		thac2id.setThesaurusArrayId(th1.getIdentifier());
		ThesaurusArrayConcept thac2= new ThesaurusArrayConcept();
		thac2.setIdentifier(thac2id);
		th1Concepts.add(thac2);

		ThesaurusArrayConcept.Id thac3id= new ThesaurusArrayConcept.Id();
		thac3id.setConceptId(c3.getIdentifier());
		thac3id.setThesaurusArrayId(th1.getIdentifier());
		ThesaurusArrayConcept thac3= new ThesaurusArrayConcept();
		thac3.setIdentifier(thac3id);
		th1Concepts.add(thac3);

		th1.setConcepts(th1Concepts);

		NodeLabel nodeLabel1 = new NodeLabel();
		nodeLabel1.setLexicalValue("node label 1");
		nodeLabel1.setLanguage(lang);
		nodeLabel1.setCreated(DateUtil.nowDate());
		nodeLabel1.setModified(DateUtil.nowDate());
		Mockito.when(nodeLabelService.getByThesaurusArray("http://th1")).thenReturn(nodeLabel1);

		arrays.add(th1);


		ThesaurusArray th2 = new ThesaurusArray();
		th2.setIdentifier("http://th2");
		Set<ThesaurusArrayConcept> th2Concepts = new HashSet<ThesaurusArrayConcept>();

		ThesaurusArrayConcept.Id thac4id= new ThesaurusArrayConcept.Id();
		thac4id.setConceptId(c4.getIdentifier());
		thac4id.setThesaurusArrayId(th1.getIdentifier());
		ThesaurusArrayConcept thac4= new ThesaurusArrayConcept();
		thac4.setIdentifier(thac4id);
		th2Concepts.add(thac4);

		ThesaurusArrayConcept.Id thac5id= new ThesaurusArrayConcept.Id();
		thac5id.setConceptId(c5.getIdentifier());
		thac5id.setThesaurusArrayId(th1.getIdentifier());
		ThesaurusArrayConcept thac5= new ThesaurusArrayConcept();
		thac5.setIdentifier(thac5id);
		th2Concepts.add(thac5);

		th2.setConcepts(th2Concepts);
		arrays.add(th2);


		NodeLabel nodeLabel2 = new NodeLabel();
		nodeLabel2.setLexicalValue("node label 2");
		nodeLabel2.setLanguage(lang);
		nodeLabel2.setCreated(DateUtil.nowDate());
		nodeLabel2.setModified(DateUtil.nowDate());
		Mockito.when(nodeLabelService.getByThesaurusArray("http://th2")).thenReturn(nodeLabel2);
        Mockito.when(thesaurusArrayService.getAllThesaurusArrayByThesaurusId(Mockito.anyString(), Mockito.anyString())).thenReturn(arrays);
		
        Model model = ModelFactory.createDefaultModel();
		skosArrayExporter.exportCollections(th, model);
		
		Model modelExpected = ModelFactory.createDefaultModel();
		Resource collec = modelExpected.createResource("http://th2");
		Resource c5Res = modelExpected.createResource("http://c5");

		Assert.assertTrue(model.containsResource(collec));	
		Assert.assertTrue(model.getResource("http://th2").hasProperty(RDF.type, SKOS.COLLECTION));

		Assert.assertTrue(model.contains(collec, SKOS.MEMBER, c5Res));
		
	}	
	
}
