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
 * moify and/ or redistribute the software under the terms of the CeCILL
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

Ext.define('GincoApp.controller.UsersTabPanelController', {
	extend : 'Ext.app.Controller',	
	
	localized : true,
	
	xLoading : 'Loading',
	xSucessLabel : 'Success!',
	xSucessSavedMsg : 'Users saved successfully',
	xProblemLabel : 'Error !',

    onGridRender : function(theGrid) {
		
		var thesPanel = theGrid.up('thesaurusTabPanel');
		var theStore = theGrid.getStore();
		theStore.getProxy().setExtraParam('idThesaurus',
				thesPanel.thesaurusData.id);
		theStore.load();

	
	},
	
	onUpdateUserGrid : function( editor, e, eOpts){
		var theGrid = e.grid;
		theGrid.up('form').down('button[itemId=saveUsers]').setDisabled(false);
	},
	
	saveUsers : function(theButton,theCallback) {
		var me=this;
		var theGrid = theButton.up('usersTabPanel').down('gridpanel');
		var thePanel = theButton.up('usersTabPanel');

		thePanel.getEl().mask(me.xLoading);
		theGrid.getStore().sync({
			success : function(model, operation) {
				thePanel.getEl().unmask();
				Thesaurus.ext.utils.msg(me.xSucessLabel, me.xSucessSavedMsg);
				thePanel.down('button[itemId=saveUsers]').setDisabled(true);
				theGrid.getStore().load();
				if (theCallback && typeof theCallback == "function") {
					theCallback();
				}
			},
			failure : function(model, operation) {
				Thesaurus.ext.utils.msg(me.xProblemLabel, model.exceptions[0].error);
				thePanel.getEl().unmask();
			}
		});
	},

	init : function() {
		this.control({
			'usersTabPanel gridpanel' : {
				render : this.onGridRender
			},
		    'usersTabPanel #userGrid' : {
				validateedit : this.onUpdateUserGrid
			}			,
 			'usersTabPanel #saveUsers' : {
 				click : this.saveUsers
			},
		});
	}
});