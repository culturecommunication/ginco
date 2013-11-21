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

Ext.define('GincoApp.controller.CustomAttributeTypesController', {
    extend:'Ext.app.Controller',

    stores : [ 'CustomConceptAttributeTypeStore', 'CustomTermAttributeTypeStore' ],
    models : [ 'CustomAttributeTypeModel'],

    localized : true,

    xLoading : 'Loading',
    xSucessLabel : 'Success!',
    xSucessSavedMsg : 'Attributes types saved successfully',
    xProblemLabel : 'Error !',
    xProblemSaveMsg : 'Impossible to save attributes !',

    loadConceptData : function(theGrid, thesaurusId) {
        var conceptsGridStore = theGrid.getStore();
        conceptsGridStore.getProxy().extraParams = {
            thesaurusId: thesaurusId
        };
        conceptsGridStore.load();
    },

    loadTermData : function(theGrid, thesaurusId) {
        var termsGridStore = theGrid.getStore();
        termsGridStore.getProxy().extraParams = {
            thesaurusId: thesaurusId
        };
        termsGridStore.load();
    },

    onConceptGridRender : function(theGrid) {
        var me = this;
        var thesaurusData = theGrid.up('thesaurusTabPanel').thesaurusData;

        if (thesaurusData != null) {
            me.loadConceptData(theGrid, thesaurusData.id);
        }
    },

    onTermGridRender : function(theGrid) {
        var me = this;
        var thesaurusData = theGrid.up('thesaurusTabPanel').thesaurusData;

        if (thesaurusData != null) {
            me.loadTermData(theGrid, thesaurusData.id);
        }
    },

    saveConceptTypes : function(theButton, theCallback) {
        var me = this;
        var theGrid = theButton.up('#conceptAttributTypesGrid');
        var conceptsGridStore = theGrid.getStore();
        conceptsGridStore.sync({
        	success: function() {
        		Thesaurus.ext.utils.msg(
						me.xSucessLabel,
						me.xSucessSavedMsg);
        		if (theCallback && typeof theCallback == "function") {
					theCallback();
				}
        	},
			failure : function(batch) {
				Thesaurus.ext.utils.msg(me.xProblemLabel,
						me.xProblemSaveMsg + " " + batch.operations[0].request.scope.reader.jsonData["message"]);
			}
        });
    },

    saveTermTypes : function(theButton, theCallback) {
        var me = this;
        var theGrid = theButton.up('#termAttributTypesGrid');
        var termsGridStore = theGrid.getStore();
        termsGridStore.sync({
        	success: function() {
        		Thesaurus.ext.utils.msg(
						me.xSucessLabel,
						me.xSucessSavedMsg);
        		if (theCallback && typeof theCallback == "function") {
					theCallback();
				}
        	},
        	failure : function(batch) {
				Thesaurus.ext.utils.msg(me.xProblemLabel,
						me.xProblemSaveMsg+ " " + batch.operations[0].request.scope.reader.jsonData["message"]);
			}
        });
    },

    addTermType : function(theButton) {
        var theGrid = theButton.up('#termAttributTypesGrid');
        var termsGridStore = theGrid.getStore();
        var model = Ext.create('GincoApp.model.CustomAttributeTypeModel');
        var thesaurusData = theButton.up('thesaurusTabPanel').thesaurusData;
        model.set('thesaurusId',thesaurusData.id);
        model.set('value','Nouvel attribut');
        model.set('code','code');
        termsGridStore.add(model);
    },

    addConceptType : function(theButton) {
        var theGrid = theButton.up('#conceptAttributTypesGrid');
        var conceptsGridStore = theGrid.getStore();
        var model = Ext.create('GincoApp.model.CustomAttributeTypeModel');
        var thesaurusData = theButton.up('thesaurusTabPanel').thesaurusData;
        model.set('thesaurusId',thesaurusData.id);
        model.set('value','Nouvel attribut');
        model.set('code','code');
        conceptsGridStore.add(model);
    },

    onDeleteAttributeType : function(gridview, el, rowIndex, colIndex,
           e, rec, rowEl) {
        var theGrid = gridview.up('gridpanel');
        var theStore = theGrid.getStore();
        theStore.remove(rec);
    },

    init:function(){
        this.control({
            'customAttributeTypesPanel gridpanel #conceptActionColumn' : {
                click : this.onDeleteAttributeType
            },
            'customAttributeTypesPanel gridpanel #termActionColumn' : {
                click : this.onDeleteAttributeType
            },
            'customAttributeTypesPanel #conceptAttributTypesGrid' : {
                afterrender : this.onConceptGridRender
            },
            'customAttributeTypesPanel #termAttributTypesGrid' : {
                afterrender : this.onTermGridRender
            },
            'customAttributeTypesPanel #saveConceptAttributTypes' : {
                click : this.saveConceptTypes
            },
            'customAttributeTypesPanel #saveTermAttributTypes' : {
                click : this.saveTermTypes
            },
            'customAttributeTypesPanel #addTermAttributTypes' : {
                click : this.addTermType
            },
            'customAttributeTypesPanel #addConceptAttributTypes' : {
                click : this.addConceptType
            }
        });
    }
});
