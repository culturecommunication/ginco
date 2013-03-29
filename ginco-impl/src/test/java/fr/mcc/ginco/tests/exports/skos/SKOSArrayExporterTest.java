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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Language;
import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exports.skos.SKOSArrayExporter;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.IThesaurusArrayService;

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
		Set<ThesaurusConcept> th1Concepts = new HashSet<ThesaurusConcept>();
		th1Concepts.add(c1);
		th1Concepts.add(c2);
		th1Concepts.add(c3);
		th1.setConcepts(th1Concepts);		
		
		NodeLabel nodeLabel1 = new NodeLabel();
		nodeLabel1.setLexicalValue("node label 1");
		nodeLabel1.setLanguage(lang);
		Mockito.when(nodeLabelService.getByThesaurusArray("http://th1")).thenReturn(nodeLabel1);

		arrays.add(th1);
		
		
		ThesaurusArray th2 = new ThesaurusArray();
		th2.setIdentifier("http://th2");
		Set<ThesaurusConcept> th2Concepts = new HashSet<ThesaurusConcept>();
		th2Concepts.add(c4);
		th2Concepts.add(c5);
		th2.setConcepts(th2Concepts);		
		arrays.add(th2);

		
		NodeLabel nodeLabel2 = new NodeLabel();
		nodeLabel2.setLexicalValue("node label 2");
		nodeLabel2.setLanguage(lang);
		Mockito.when(nodeLabelService.getByThesaurusArray("http://th2")).thenReturn(nodeLabel2);
		
		Mockito.when(thesaurusArrayService.getAllThesaurusArrayByThesaurusId(Mockito.anyString())).thenReturn(arrays);
		
		String skosArrays  = skosArrayExporter.exportCollections(th);
		
		InputStream is = SKOSArrayExporterTest.class.getResourceAsStream("/exports/arrays_export.rdf");
		Assert.assertEquals(IOUtils.toString(is), skosArrays);	
	
	}
}
