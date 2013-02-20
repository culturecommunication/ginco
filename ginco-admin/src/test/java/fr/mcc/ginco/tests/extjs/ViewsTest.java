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
package fr.mcc.ginco.tests.extjs;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import fr.mcc.ginco.IThesaurusConceptService;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.beans.ThesaurusType;
import fr.mcc.ginco.exceptions.BusinessException;
import fr.mcc.ginco.extjs.view.enums.ThesaurusListNodeType;
import fr.mcc.ginco.extjs.view.node.IThesaurusListNode;
import fr.mcc.ginco.extjs.view.node.ThesaurusListBasicNode;
import fr.mcc.ginco.extjs.view.utils.FolderGenerator;
import fr.mcc.ginco.extjs.view.utils.OrphansGenerator;
import fr.mcc.ginco.log.Log;

public class ViewsTest {

    private Thesaurus testThesaurus = new Thesaurus();
   
    @Mock(name="thesaurusConceptService")
	private IThesaurusConceptService thesaurusConceptService;	
	
    @Mock(name="orphansGenerator")
    private OrphansGenerator orphansGenerator;
    
	@InjectMocks
    private FolderGenerator folderGenerator = new FolderGenerator();

    @Before
    public final void setUp() {
        MockitoAnnotations.initMocks(this);
        testThesaurus.setTitle("testNode");
        testThesaurus.setIdentifier("555");
        ThesaurusType type = new ThesaurusType();
        type.setIdentifier(1);
        type.setLabel("testType");
        testThesaurus.setType(type);
        
    	ReflectionUtils.doWithFields(folderGenerator.getClass(),
				new ReflectionUtils.FieldCallback() {

					public void doWith(Field field)
							throws IllegalArgumentException,
							IllegalAccessException {
						ReflectionUtils.makeAccessible(field);

						if (field.getAnnotation(Log.class) != null) {
							Logger logger = LoggerFactory
									.getLogger(folderGenerator.getClass());
							field.set(folderGenerator, logger);
						}
					}
				});
        
    }

    @Test
    public final void testFolderCreation() throws BusinessException {
        IThesaurusListNode node = new ThesaurusListBasicNode();

        node.setExpanded(false);
        node.setTitle(testThesaurus.getTitle());
        node.setId(testThesaurus.getIdentifier());
        node.setChildren(folderGenerator.generateFolders(testThesaurus.getId()));

        String idOfFirstChild = node.getChildren().get(0).getId();
        String realId = idOfFirstChild.split("_")[1];

        Assert.assertEquals("Error creating folders! Id of folder does not correspond to one of parent!",
                realId, testThesaurus.getIdentifier());

        Assert.assertEquals("Service sent wrong reponse ! Type should be FOLDER for second level node !",
                node.getChildren().get(0).getType(), ThesaurusListNodeType.FOLDER);
    }

    @Test
    public final void testNodeCreation() {
        IThesaurusListNode node = new ThesaurusListBasicNode();

        node.setExpanded(false);
        node.setTitle(testThesaurus.getTitle());
        node.setId(testThesaurus.getIdentifier());
        node.setType(ThesaurusListNodeType.THESAURUS);

        String idOfNode = node.getId();
        String titleOfNode = node.getTitle();

        Assert.assertEquals("Error creating node! Id of node does not correspond to one of parent!",
                idOfNode, testThesaurus.getIdentifier());

        Assert.assertEquals("Error creating node! Title of node does not correspond to one of parent!",
                titleOfNode, testThesaurus.getTitle());

        Assert.assertEquals("Service sent wrong reponse ! Type should be THESAURUS for first level node !",
                node.getType(), ThesaurusListNodeType.THESAURUS);
    }
}
