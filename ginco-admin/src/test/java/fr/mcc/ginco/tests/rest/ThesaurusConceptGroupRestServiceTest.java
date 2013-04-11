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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.mcc.ginco.beans.ThesaurusConceptGroup;
import fr.mcc.ginco.beans.ThesaurusConceptGroupLabel;
import fr.mcc.ginco.beans.ThesaurusConceptGroupType;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.ExtJsonFormLoadData;
import fr.mcc.ginco.extjs.view.pojo.ThesaurusConceptGroupView;
import fr.mcc.ginco.extjs.view.utils.TermViewConverter;
import fr.mcc.ginco.extjs.view.utils.ThesaurusConceptGroupLabelViewConverter;
import fr.mcc.ginco.extjs.view.utils.ThesaurusConceptGroupViewConverter;
import fr.mcc.ginco.rest.services.ThesaurusConceptGroupRestService;
import fr.mcc.ginco.services.IThesaurusConceptGroupService;
import fr.mcc.ginco.services.IThesaurusConceptGroupTypeService;
import fr.mcc.ginco.tests.LoggerTestUtil;

public class ThesaurusConceptGroupRestServiceTest {
	

	@Mock(name="thesaurusConceptGroupTypeService")
	private IThesaurusConceptGroupTypeService thesaurusConceptGroupTypeService;
	
	@Mock(name="thesaurusConceptGroupService")
	private IThesaurusConceptGroupService thesaurusConceptGroupService;
	
	@Mock(name="thesaurusConceptGroupViewConverter")
    private ThesaurusConceptGroupViewConverter thesaurusConceptGroupViewConverter;
	
	@Mock(name="thesaurusConceptGroupLabelViewConverter")
    private ThesaurusConceptGroupLabelViewConverter thesaurusConceptGroupLabelViewConverter;
	
	
	@InjectMocks
	private ThesaurusConceptGroupRestService thesaurusConceptGroupRestService = new ThesaurusConceptGroupRestService();
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		LoggerTestUtil.initLogger(thesaurusConceptGroupRestService);
	}

	
	/**
	 * Test to get concept group types
	 */
	@Test
	public final void tsetGetAllConceptGroupTypes() {
		List<ThesaurusConceptGroupType> types = getFakeConceptGroupType();
		Mockito.when(thesaurusConceptGroupTypeService.getConceptGroupTypeList()).thenReturn(types);
		ExtJsonFormLoadData<List<ThesaurusConceptGroupType>> actualResponse = thesaurusConceptGroupRestService.getConceptGroupTypes();
		Assert.assertEquals(types.size(), actualResponse.getData().size());
		Assert.assertEquals(types.get(0).getLabel(), actualResponse.getData().get(0).getLabel());
	}
	
	/**
	 * Test to get a concept group by id
	 */
	@Test
	public final void testGetConceptGroupById() {
		ThesaurusConceptGroup myGroup = getFakeThesaurusConceptGroup("fake1");
		ThesaurusConceptGroupView myGroupView = getFakeThesaurusConceptGroupView("fake1"); 
		Mockito.when(thesaurusConceptGroupService.getConceptGroupById(Mockito.anyString())).thenReturn(myGroup);
		Mockito.when(thesaurusConceptGroupViewConverter.convert(Mockito.any(ThesaurusConceptGroup.class))).thenReturn(myGroupView);
		
		ThesaurusConceptGroupView actualResponse = thesaurusConceptGroupRestService.getConceptGroupById("fake1");
		Assert.assertEquals(myGroup.getIdentifier(), actualResponse.getIdentifier());
	}
	
	/**
	 * Test to get all concept groups by thesaurus id without excluded group
	 */
	@Test
	public final void testGetAllConceptGroupsByThesaurusIdWithoutExcludedGroup() {
		ThesaurusConceptGroup myGroup1 = getFakeThesaurusConceptGroup("fake1");
		ThesaurusConceptGroup myGroup2 = getFakeThesaurusConceptGroup("fake2");
		ThesaurusConceptGroupView myGroupView1 = getFakeThesaurusConceptGroupView("fake1");
		ThesaurusConceptGroupView myGroupView2 = getFakeThesaurusConceptGroupView("fake2");
		
		List<ThesaurusConceptGroup> allGroups = new ArrayList<ThesaurusConceptGroup>();
		allGroups.add(myGroup1);
		allGroups.add(myGroup2);
		
		List<ThesaurusConceptGroupView> allGroupViews = new ArrayList<ThesaurusConceptGroupView>();
		allGroupViews.add(myGroupView1);
		allGroupViews.add(myGroupView2);
		
		Mockito.when(thesaurusConceptGroupService.getAllThesaurusConceptGroupsByThesaurusId(Mockito.anyString(), Mockito.anyString())).thenReturn(allGroups);
		Mockito.when(thesaurusConceptGroupViewConverter.convert(allGroups)).thenReturn(allGroupViews);
		
		ExtJsonFormLoadData<List<ThesaurusConceptGroupView>> actualResponse = thesaurusConceptGroupRestService.getAllConceptGroupsByThesaurusId("", "fake1");
		ExtJsonFormLoadData<List<ThesaurusConceptGroupView>> expectedResponse = new ExtJsonFormLoadData<List<ThesaurusConceptGroupView>>(allGroupViews);
		expectedResponse.setTotal((long) allGroups.size());
		Assert.assertEquals(expectedResponse.getTotal(), actualResponse.getTotal());
		Assert.assertEquals(expectedResponse.getData(), actualResponse.getData());
	}
	
	/**
	 * Test to update a concept group
	 */
	@Test
	public final void testUpdateThesaurusConceptGroup() {
		ThesaurusConceptGroup myGroup = getFakeThesaurusConceptGroup("fake1");
		ThesaurusConceptGroupView myGroupView = getFakeThesaurusConceptGroupView("fake1");
		ThesaurusConceptGroupLabel myGroupLabel = getFakeThesaurusConceptGroupLabel(1);
		
		Mockito.when(thesaurusConceptGroupLabelViewConverter.convert(Mockito.any(ThesaurusConceptGroupView.class))).thenReturn(myGroupLabel);
		Mockito.when(thesaurusConceptGroupViewConverter.convert(Mockito.any(ThesaurusConceptGroupView.class))).thenReturn(myGroup);
		Mockito.when(thesaurusConceptGroupService.updateThesaurusConceptGroup(Mockito.any(ThesaurusConceptGroup.class), Mockito.any(ThesaurusConceptGroupLabel.class))).thenReturn(myGroup);
		Mockito.when(thesaurusConceptGroupViewConverter.convert(Mockito.any(ThesaurusConceptGroup.class))).thenReturn(myGroupView);
		
		ThesaurusConceptGroupView actualResponse = thesaurusConceptGroupRestService.updateThesaurusConceptGroup(myGroupView);
		Assert.assertEquals(myGroup.getIdentifier(), actualResponse.getIdentifier());
	}
	
	private List<ThesaurusConceptGroupType> getFakeConceptGroupType() {
		List<ThesaurusConceptGroupType> types = new ArrayList<ThesaurusConceptGroupType>();
		ThesaurusConceptGroupType type1 = new ThesaurusConceptGroupType();
		type1.setCode("D");
		type1.setLabel("DOMAIN");
		
		ThesaurusConceptGroupType type2 = new ThesaurusConceptGroupType();
		type2.setCode("T");
		type2.setLabel("THEMATIC");
		
		types.add(type1);
		types.add(type2);
		return types;
	}
	
	private ThesaurusConceptGroup getFakeThesaurusConceptGroup(String id) {
		ThesaurusConceptGroup group = new ThesaurusConceptGroup();
		group.setIdentifier(id);
		return group;
	}
	
	private ThesaurusConceptGroupView getFakeThesaurusConceptGroupView(String id) {
		ThesaurusConceptGroupView group = new ThesaurusConceptGroupView();
		group.setIdentifier(id);
		return group;
	}
	
	private ThesaurusConceptGroupLabel getFakeThesaurusConceptGroupLabel(Integer id) {
		ThesaurusConceptGroupLabel groupLabel = new ThesaurusConceptGroupLabel();
		groupLabel.setIdentifier(id);
		groupLabel.setLexicalValue("fakeLabel");
		return groupLabel;
	}	
}
