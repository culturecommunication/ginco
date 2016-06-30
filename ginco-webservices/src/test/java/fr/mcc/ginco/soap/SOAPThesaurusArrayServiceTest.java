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

package fr.mcc.ginco.soap;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.data.ReducedThesaurusArray;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.IThesaurusArrayService;


/**
 * Unit tests class for the ThesaurusConceptService
 * 
 */
public class SOAPThesaurusArrayServiceTest {

	@Mock(name = "thesaurusConceptService")
	private IThesaurusArrayService thesaurusArrayService;
	
	@Mock(name = "nodeLabelService")
	private INodeLabelService nodeLabelService;
	
	@InjectMocks
	private SOAPThesaurusArrayServiceImpl soapThesaurusArrayService;
	

	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);	
	}
	
	@Test
	public final void testGetArrayByThesaurusId(){
		ThesaurusArray fakeArray = new ThesaurusArray();
		fakeArray.setIdentifier("ar-1");
		List<ThesaurusArray> fakeArrayList = new ArrayList<ThesaurusArray>();
		fakeArrayList.add(fakeArray);
		NodeLabel label = new NodeLabel();
		label.setLexicalValue("test");
		when(thesaurusArrayService.getAllThesaurusArrayByThesaurusId(anyString(), anyString())).thenReturn(fakeArrayList);
		when(nodeLabelService.getByThesaurusArrayAndLanguage(Matchers.anyString())).thenReturn(label);
		
		
		List<ReducedThesaurusArray> results = soapThesaurusArrayService.getThesaurusArraysByThesaurusId("fakeThesaurusId");
		Assert.assertEquals(1,results.size());
		Assert.assertEquals("ar-1",results.get(0).getIdentifier());
		Assert.assertEquals("test",results.get(0).getTitle());
	}
	
	

}
