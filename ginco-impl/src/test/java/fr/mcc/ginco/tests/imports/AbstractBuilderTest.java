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
package fr.mcc.ginco.tests.imports;

import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.DCTerms;

import fr.mcc.ginco.imports.AbstractBuilder;
import fr.mcc.ginco.imports.SKOS;

public class AbstractBuilderTest {
	
	
	@Test
    public void testGetSimpleStringInfo()    
    {   
    	Model model = ModelFactory.createDefaultModel();
		InputStream is = ThesaurusBuilderTest.class
				.getResourceAsStream("/imports/abstract_builder_tests.rdf");
		model.read(is, null);

		Resource skosResource = model
				.getResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69");

    	AbstractBuilder abstractBuilder = Mockito.mock(AbstractBuilder.class, Mockito.CALLS_REAL_METHODS);
    	String simpleString = abstractBuilder.getSimpleStringInfo(skosResource, DC.type);
    	Assert.assertEquals("Thésaurus", simpleString);    	
    }
	
	@Test
    public void testGetSimpleStringInfoNull()    
    {   
    	Model model = ModelFactory.createDefaultModel();
		InputStream is = ThesaurusBuilderTest.class
				.getResourceAsStream("/imports/abstract_builder_tests.rdf");
		model.read(is, null);

		Resource skosResource = model
				.getResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69");

    	AbstractBuilder abstractBuilder = Mockito.mock(AbstractBuilder.class, Mockito.CALLS_REAL_METHODS);
    	String simpleString = abstractBuilder.getSimpleStringInfo(skosResource, SKOS.PREF_LABEL);
    	Assert.assertNull(simpleString);    	
    }

    
	@Test
    public void testGetMultipleLineStringInfo()    {
		Model model = ModelFactory.createDefaultModel();
		InputStream is = ThesaurusBuilderTest.class
				.getResourceAsStream("/imports/abstract_builder_tests.rdf");
		model.read(is, null);

		Resource skosResource = model
				.getResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69");

    	AbstractBuilder abstractBuilder = Mockito.mock(AbstractBuilder.class, Mockito.CALLS_REAL_METHODS);
    	String multipleLines = abstractBuilder.getMultipleLineStringInfo(skosResource, DC.contributor);
    	String[] allLines = multipleLines.split("\n");
    	Assert.assertEquals(3, allLines.length);
		
	}
	
	@Test
    public void testExtendedDC()    
    {   
    	Model model = ModelFactory.createDefaultModel();
		InputStream is = ThesaurusBuilderTest.class
				.getResourceAsStream("/imports/abstract_builder_tests_dct.rdf");
		model.read(is, null);

		Resource skosResource = model
				.getResource("http://data.culture.fr/thesaurus/resource/ark:/67717/T69");

    	AbstractBuilder abstractBuilder = Mockito.mock(AbstractBuilder.class, Mockito.CALLS_REAL_METHODS);
    	String simpleString = abstractBuilder.getSimpleStringInfo(skosResource, DC.language,DCTerms.language);
    	Assert.assertEquals("fr", simpleString);    	
    }
	
	
	
}
