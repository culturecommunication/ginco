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
package fr.mcc.ginco.tests.rest;

import java.util.List;

import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.exports.IExportService;
import fr.mcc.ginco.rest.services.ExportRestService;
import fr.mcc.ginco.services.IThesaurusService;

public class ExportRestServiceTest {  
    
    @Mock(name="exportService")
    private IExportService exportService;   

    @Mock(name="thesaurusService")
    private IThesaurusService thesaurusService;
	
	@InjectMocks
	private ExportRestService exportRestService = new ExportRestService();
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public final void testGetAlphabetical() {		
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("http://fake-thesaurusid");
		fakeThesaurus.setTitle("Thesaurus title");
		Mockito.when( thesaurusService.getThesaurusById(Mockito.anyString())).thenReturn(fakeThesaurus);
		
		Response alphabeticalResponse = exportRestService.getAlphabetical("http://fakethesaurusId");
		
		Assert.assertEquals(200, alphabeticalResponse.getStatus());		
		List<Object> contentDispositionheader = alphabeticalResponse.getHeaders().get("Content-Disposition"); 
		String fileSuffix = (String)contentDispositionheader.get(0);
		Assert.assertEquals(true, fileSuffix.endsWith(".txt\""));
	}	
	
	@Test
	public final void testGetHierarchical() {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("http://fake-thesaurusid");
		fakeThesaurus.setTitle("Thesaurus title");
		Mockito.when( thesaurusService.getThesaurusById(Mockito.anyString())).thenReturn(fakeThesaurus);
		
		Response hierarchicalResponse = exportRestService.getHierarchical("http://fakethesaurusId");
		
		Assert.assertEquals(200, hierarchicalResponse.getStatus());		
		List<Object> contentDispositionheader = hierarchicalResponse.getHeaders().get("Content-Disposition"); 
		String fileSuffix = (String)contentDispositionheader.get(0);
		Assert.assertEquals(true, fileSuffix.endsWith(".txt\""));
	}	
	
	@Test
	public final void testGetSKOS() {
		Thesaurus fakeThesaurus = new Thesaurus();
		fakeThesaurus.setIdentifier("http://fake-thesaurusid");
		fakeThesaurus.setTitle("Thesaurus title");
		Mockito.when( thesaurusService.getThesaurusById(Mockito.anyString())).thenReturn(fakeThesaurus);
		
		Response skosResponse = exportRestService.getSKOS("http://fakethesaurusId");
		
		Assert.assertEquals(200, skosResponse.getStatus());		
		List<Object> contentDispositionheader = skosResponse.getHeaders().get("Content-Disposition"); 
		String fileSuffix = (String)contentDispositionheader.get(0);
		Assert.assertEquals(true, fileSuffix.endsWith(".rdf\""));
	}
	
}
