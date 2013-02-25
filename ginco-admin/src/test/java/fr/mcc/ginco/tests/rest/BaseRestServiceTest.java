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

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import fr.mcc.ginco.extjs.view.utils.FolderGenerator;
import fr.mcc.ginco.rest.services.BaseRestService;
import fr.mcc.ginco.services.IThesaurusService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml",
        "classpath:applicationContext-daos.xml",
        "classpath:applicationContext-rest.xml"
})
public class BaseRestServiceTest {

    @Mock(name = "thesaurusService")
    private IThesaurusService thesaurusService;

    @Mock(name = "folderGenerator")
    private FolderGenerator folderGenerator;

    @InjectMocks
    private BaseRestService baseRestService = new BaseRestService();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
	/**
	 * Test the getVocabularies method with empty values for non mandatory
	 * fields in Thesauruses objects
	 */
	@Test
	public final void testGetVocabularies()  throws BusinessException {
        //Generating mocked Thesauruses in a single list "allThesaurus"
		Thesaurus mockedThesaurus1 = new Thesaurus();
		Thesaurus mockedThesaurus2 = new Thesaurus();
		Thesaurus mockedThesaurus3 = new Thesaurus();
		List<Thesaurus> allThesaurus = new ArrayList<Thesaurus>();
		allThesaurus.add(mockedThesaurus1);
		allThesaurus.add(mockedThesaurus2);
		allThesaurus.add(mockedThesaurus3);
        initServicesForGetVocabulariesTest(allThesaurus);

		//Getting thesauruses from rest webservice method and testing
        List<IThesaurusListNode> listThesauruses = baseRestService.getTreeContent("");
		Assert.assertEquals(3, listThesauruses.size());
	}
	
	private IThesaurusService initServicesForGetVocabulariesTest (
			List<Thesaurus> mockedThesauruses)  throws BusinessException {
		for (int i = 0; i < mockedThesauruses.size(); i++) {
			Mockito.when(
                    thesaurusService.getThesaurusById(mockedThesauruses.get(i)
							.getIdentifier())).thenReturn(
					mockedThesauruses.get(i));
		}

		Mockito.when(thesaurusService.getThesaurusList()).thenReturn(
				mockedThesauruses);

        Mockito.when(folderGenerator.generateFolders(Mockito.anyString())).thenReturn(null);

		return thesaurusService;
	}
	
}