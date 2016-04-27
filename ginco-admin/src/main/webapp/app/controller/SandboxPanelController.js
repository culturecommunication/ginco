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

Ext.define('GincoApp.controller.SandboxPanelController',
		{
			extend : 'Ext.app.Controller',

			models : [ 'ThesaurusModel' ],

			onGridRender : function(theGrid) {
				var thePanel = theGrid.up('sandboxPanel');
				var thesPanel = theGrid.up('thesaurusTabPanel');
				var theStore = theGrid.getStore();
				theStore.getProxy().setExtraParam('idThesaurus',
						thesPanel.thesaurusData.id);
				theStore.load();
				thePanel.setTitle(thePanel.title);
				thePanel.addNodePath(thesPanel.thesaurusData.id);
				thePanel.addNodePath("SANDBOX_"+thesPanel.thesaurusData.id);
			},

			onNodeDblClick : function(theGrid, record, item, index, e, eOpts) {
				var thesPanel = theGrid.up('thesaurusTabPanel');
				this.openThesaurusTermTab(record,thesPanel.thesaurusData);
			},
			openThesaurusTermTab : function(aRecord, aThesaurusData) {
				var topTabs = Ext.ComponentQuery.query('thesaurusTabs')[0];
				topTabs.fireEvent('opentermtab',topTabs,aThesaurusData.id, aRecord.data.identifier);				
			},
			refreshSandBoxList : function(thesaurusData)
			{	
				var sandBoxTabs = Ext.ComponentQuery.query('thesaurusTabs sandboxPanel');
				Ext.Array.each(sandBoxTabs, function(sandBox, index, array) {
					if (sandBox.up('thesaurusTabPanel').thesaurusData.id ==  thesaurusData.id) {
						var sandBoxGrid= sandBox.down("gridpanel");
						sandBoxGrid.getStore().load();
					}
				});	
				/*No need to refresh sand box list here*/
				//this.application.fireEvent('refreshtree', thesaurusData);
			},

			init : function() {
				this.application.on({
					'termdeleted' : this.refreshSandBoxList,
					'termupdated' : this.refreshSandBoxList,
                    'conceptupdated' : this.refreshSandBoxList,
                    'conceptdeleted' : this.refreshSandBoxList,
			        scope: this
				});
				this.control({
					'sandboxPanel gridpanel' : {
						render : this.onGridRender,
						itemdblclick : this.onNodeDblClick
					}
				});

			}
		});