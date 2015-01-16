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
package fr.mcc.ginco.tests.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.ThesaurusType;
import fr.mcc.ginco.dao.IGenericDAO.SortingTypes;
import fr.mcc.ginco.dao.IThesaurusTypeDAO;
import fr.mcc.ginco.services.ThesaurusTypeServiceImpl;

public class ThesaurusTypeServiceTest {

	@Mock
    private IThesaurusTypeDAO thesaurusTypeDAO;

	@InjectMocks
	private ThesaurusTypeServiceImpl thesaurusTypeService;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
    public final void testGetThesaurusTypeList() {
		List<ThesaurusType> allTypes = new ArrayList<ThesaurusType>();
		ThesaurusType type = new ThesaurusType();
		allTypes.add(type);
		Mockito.when(thesaurusTypeDAO.findAll("label", SortingTypes.asc)).thenReturn(allTypes);
        List<ThesaurusType> actualResponse = thesaurusTypeService.getThesaurusTypeList();
		Assert.assertEquals("Error fetching all ThesaurusType", 1, actualResponse.size());
    }

}
