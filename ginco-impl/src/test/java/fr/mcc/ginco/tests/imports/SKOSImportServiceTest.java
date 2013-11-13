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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.apache.cxf.helpers.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

import fr.mcc.ginco.beans.Alignment;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.dao.IThesaurusDAO;
import fr.mcc.ginco.dao.IThesaurusVersionHistoryDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.helpers.ThesaurusHelper;
import fr.mcc.ginco.imports.ConceptsBuilder;
import fr.mcc.ginco.imports.SKOSImportServiceImpl;
import fr.mcc.ginco.imports.SKOSImportUtils;
import fr.mcc.ginco.imports.ThesaurusArraysBuilder;
import fr.mcc.ginco.imports.ThesaurusBuilder;
import fr.mcc.ginco.tests.LoggerTestUtil;


public class SKOSImportServiceTest {
	@Mock(name = "thesaurusDAO")
	private IThesaurusDAO thesaurusDAO;

	@Mock(name = "thesaurusVersionHistoryDAO")
	private IThesaurusVersionHistoryDAO thesaurusVersionHistoryDAO;

	@Mock(name = "thesaurusHelper")
	private ThesaurusHelper thesaurusHelper;

	@Mock(name = "skosThesaurusBuilder")
	private ThesaurusBuilder thesaurusBuilder;

	@Mock(name = "skosConceptsBuilder")
	private ConceptsBuilder conceptsBuilder;

	@Mock(name = "skosArraysBuilder")
	private ThesaurusArraysBuilder arraysBuilder;
	
	@Mock(name = "skosImportUtils")
	private SKOSImportUtils skosImportUtils;
	

	@InjectMocks
    private SKOSImportServiceImpl skosImportService ;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		LoggerTestUtil.initLogger(skosImportService);
	}

    @Test
    public void testImportSKOSFile() throws BusinessException, IOException {
    	Thesaurus returnedThesaurus = new Thesaurus();
    	returnedThesaurus.setIdentifier("http://data.culture.fr/thesaurus/resource/ark:/67717/T69");
    	Mockito.when(thesaurusDAO.getById(Mockito.anyString())).thenReturn(null);
  		Mockito.when(thesaurusBuilder.buildThesaurus(Mockito.any(Resource.class),	Mockito.any(Model.class))).thenReturn(returnedThesaurus);


      String fileName = "concept_collections_temp.rdf";
      String tempDir = System.getProperty("java.io.tmpdir");
      InputStream is = ConceptBuilderTest.class
				.getResourceAsStream("/imports/concept_collections.rdf");
      String fileContent = IOUtils.toString(is);

      Map<Thesaurus, Set<Alignment>> res = skosImportService.importSKOSFile(fileContent, fileName, new File(tempDir));
      Assert.assertEquals("http://data.culture.fr/thesaurus/resource/ark:/67717/T69", res.keySet().iterator().next().getIdentifier());
    }

    @Test(expected=BusinessException.class)
    public void testImportSKOSFileExistingThesaurus() throws BusinessException, IOException {
    	Mockito.when(thesaurusDAO.getById(Mockito.anyString())).thenReturn(new Thesaurus());

      String fileName = "concept_collections_temp.rdf";
      String tempDir = System.getProperty("java.io.tmpdir");
      InputStream is = ConceptBuilderTest.class
				.getResourceAsStream("/imports/concept_collections.rdf");
      String fileContent = IOUtils.toString(is);
      skosImportService.importSKOSFile(fileContent, fileName, new File(tempDir));
    }
}
