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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.NodeLabel;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusArray;
import fr.mcc.ginco.dao.IThesaurusArrayDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.helpers.ThesaurusArrayHelper;
import fr.mcc.ginco.services.INodeLabelService;
import fr.mcc.ginco.services.IThesaurusArrayService;
import fr.mcc.ginco.services.ThesaurusArrayServiceImpl;

public class ThesaurusArrayServiceTest {

    @Mock
    private IThesaurusArrayDAO thesaurusArrayDAO;

    @Mock(name = "nodeLabelService")
    private INodeLabelService nodeLabelService;
    
    @Mock(name = "thesaurusArrayHelper")
    private ThesaurusArrayHelper thesaurusArrayHelper;

    @InjectMocks
    private IThesaurusArrayService thesaurusArrayService = new ThesaurusArrayServiceImpl();

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

    @Test
    public final void testUpdateArray() throws BusinessException {
        ThesaurusArray mockArray = new ThesaurusArray();
        NodeLabel mockLabel = new NodeLabel();
        mockLabel.setIdentifier(1);
        mockLabel.setThesaurusArray(mockArray);

        when(thesaurusArrayDAO.getById(anyString())).thenReturn(mockArray);
        when(thesaurusArrayDAO.update(any(ThesaurusArray.class))).thenReturn(mockArray);
        when(nodeLabelService.updateOrCreate(any(NodeLabel.class))).thenReturn(any(NodeLabel.class));

        ThesaurusArray array = thesaurusArrayService.getThesaurusArrayById("1");
        array.setNotation("test notation");

        ThesaurusArray updated = thesaurusArrayService.updateThesaurusArray(mockArray, mockLabel, null);
        Assert.assertEquals(array.getNotation(),updated.getNotation());
    }

    @Test
    public final void testGetArray() {
        ThesaurusArray mockArray = new ThesaurusArray();
        mockArray.setIdentifier("1");

        when(thesaurusArrayDAO.getById(anyString())).thenReturn(mockArray);

        ThesaurusArray actualArray = thesaurusArrayService.getThesaurusArrayById("1");
        Assert.assertEquals(mockArray.getIdentifier(), actualArray.getIdentifier());
    }

    @Test
    public final void testGetAllThesaurusArrayByThesaurusId() {
        Thesaurus mockThesaurus = new Thesaurus();
        mockThesaurus.setIdentifier("th1");

        final ThesaurusArray mockArray2 = new ThesaurusArray();
        mockArray2.setIdentifier("2");
        mockArray2.setThesaurus(mockThesaurus);

        final ThesaurusArray mockArray1 = new ThesaurusArray();
        mockArray1.setIdentifier("1");
        mockArray1.setThesaurus(mockThesaurus);

        when(thesaurusArrayDAO.getThesaurusArrayListByThesaurusId(anyString(), anyString())).thenReturn(new ArrayList<ThesaurusArray>(){{
            add(mockArray1);
            add(mockArray2);
        }});

        List<ThesaurusArray> actual = thesaurusArrayService.getAllThesaurusArrayByThesaurusId(null, "th1");

        Assert.assertEquals(2, actual.size());
    }
    
    @Test
    public final void testDestroy() {
        ThesaurusArray mockArray = new ThesaurusArray();
        mockArray.setIdentifier("1");

        when(thesaurusArrayDAO.delete(any(ThesaurusArray.class))).thenReturn(mockArray);

        ThesaurusArray actualArray = thesaurusArrayService.destroyThesaurusArray(mockArray);
        Assert.assertEquals(mockArray.getIdentifier(), actualArray.getIdentifier());
    }

}