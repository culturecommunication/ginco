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

Ext.define('GincoApp.controller.ThesaurusFormController', {
	extend : 'Ext.app.Controller',

	models : [ 'ThesaurusModel' ],
	stores : [ 'MainTreeStore' ],

    localized : true,

    xDeleteMsgLabel : 'Are you sure to delete this thesaurus?',
    xDeleteMsgTitle : 'Delete this thesaurus?',
    xSucessLabel : 'Success!',
    xSucessSavedMsg : 'Thesaurus saved successfully',
    xSucessRemovedMsg : 'Thesaurus removed successfully',
    xProblemLabel : 'Error !',
    xWarningLabel : 'Warning !',
    xProblemSaveMsg : 'Unable to save this thesaurus!',
    xProblemDeleteMsg : 'Unable to delete this thesaurus!',
    xProblemPublishMsg : "Error publishing Thesaurus!",
    xProblemArchiveMsg : "Error archiving Thesaurus!",
    xWarningChangedPoly : "Attention! You are going to change polyhierarchism of thesaurus, do it on your own risk!",
    xWarningChangedLanguages : "There may be terms in this language. Do it on your own risk!",
    xImportTermsTitle : "Import terms",
    xImportBranchTitle : "Import branch",

	loadPanel : function(theForm) {
		var me = this;
		var model = Ext.create('GincoApp.model.ThesaurusModel');
		var thesaurusData = theForm.up('thesaurusPanel').thesaurusData;
		if (thesaurusData != null) {
			model.set(thesaurusData);
			me.loadData(theForm, model);
		} else {
			var defaultModel = this.getThesaurusModelModel();
			defaultModel.load(null, {
				success : function(newModel) {
					model.set(newModel.data);
					theForm.loadRecord(model);
				}
			});
		}
	},
	loadData : function(aForm, aModel) {
		var thesaurusPanel = aForm.up('thesaurusPanel');
		thesaurusPanel.setTitle(aModel.data.title);
		thesaurusPanel.thesaurusData = aModel.data;
		aForm.loadRecord(aModel);
		thesaurusPanel.down('button[cls=newBtnMenu]').setDisabled(false);

        if(thesaurusPanel.thesaurusData.canBeDeleted) {
           thesaurusPanel.down('button[cls=delete]').setDisabled(false);
        } else {
           thesaurusPanel.down('button[cls=delete]').setDisabled(true);
        }

        thesaurusPanel.down('button[cls=exportsBtnMenu]').setDisabled(false);
        thesaurusPanel.down('#exportHierarchical').setDisabled(false);
        thesaurusPanel.down('#exportSKOS').setDisabled(false);
        thesaurusPanel.down('#exportAlphabetical').setDisabled(false);
        thesaurusPanel.down('#exportGinco').setDisabled(false);
        thesaurusPanel.down('#versionTab').setDisabled(false);
        thesaurusPanel.down('button[cls=journalBtnMenu]').setDisabled(false);
        thesaurusPanel.down('#editJournal').setDisabled(false);
        thesaurusPanel.down('#publishThesaurus').setDisabled(false);
        thesaurusPanel.down('#importSandbox').setDisabled(false);

        if(thesaurusPanel.thesaurusData.archived) {
            thesaurusPanel.down('bottomFormToolBar').setArchived();
            thesaurusPanel.down('#archiveThesaurus').setDisabled(true);
        } else {
            thesaurusPanel.down('#archiveThesaurus').setDisabled(false);
        }


	},
	onNewTermBtnClick : function(theButton, e, options) {
		var thePanel = theButton.up('thesaurusPanel');
		this.createPanel('GincoApp.view.TermPanel',thePanel.thesaurusData );
	},

	onNewConceptBtnClick : function(theButton, e, options) {
		var thePanel = theButton.up('thesaurusPanel');
		this.createPanel('GincoApp.view.ConceptPanel',thePanel.thesaurusData );
	},

	onNewConceptArrayBtnClick : function(theButton) {
		var thePanel = theButton.up('thesaurusPanel');
		this.createPanel('GincoApp.view.ConceptArrayPanel',thePanel.thesaurusData);
	},

	onNewConceptGroupBtnClick : function(theButton) {
		var thePanel = theButton.up('thesaurusPanel');
		this.createPanel('GincoApp.view.ConceptGroupPanel',thePanel.thesaurusData);
	},

	onNewComplexConceptBtnClick : function (theButton) {
		var thePanel = theButton.up('thesaurusPanel');
		this.createPanel('GincoApp.view.ComplexConceptPanel',thePanel.thesaurusData);
	},

	createPanel : function(aType, thesaurusData)
	{
		var aNewPanel = Ext.create(aType);
		aNewPanel.thesaurusData = thesaurusData;
		var topTabs = Ext.ComponentQuery.query('topTabs')[0];
		var tab = topTabs.add(aNewPanel);
		topTabs.setActiveTab(tab);
		tab.show();
		return aNewPanel;
	},

    getActivePanel : function() {
        var topTabs = Ext.ComponentQuery.query('topTabs')[0];
        return topTabs.getActiveTab();
    },

    deleteThesaurus : function(theButton){
        var me = this;
        var theForm = theButton.up('form');
        var globalTabs = theForm.up('topTabs');
        var thePanel = me.getActivePanel();

        var updatedModel = theForm.getForm().getRecord();
            Ext.MessageBox.show({
                title : me.xDeleteMsgTitle,
                msg : me.xDeleteMsgLabel,
                buttons : Ext.MessageBox.YESNOCANCEL,
                fn : function(buttonId) {
                    switch (buttonId) {
                        case 'no':
                            break;
                        case 'yes':
                            updatedModel.destroy({
                                success : function(record, operation) {
                                    Thesaurus.ext.utils.msg(me.xSucessLabel,
                                        me.xSucessRemovedMsg);
                                    me.application.fireEvent('thesaurusdeleted',thePanel.thesaurusData);
                                    globalTabs.items.each(function(item){
                                        if(item.thesaurusData.id == record.data.id) {
                                            item.close();
                                        }
                                    });
                                    globalTabs.remove(thePanel);
                                },
                                failure : function(record, operation) {
                                    Thesaurus.ext.utils.msg(me.xProblemLabel,
                                        operation.error);
                                }
                            });
                            break;
                    }
                },
                scope : this
            });

    },

    publishThesaurus : function(theButton) {
        var me = this;
        var theForm = theButton.up('form');
        var url = "services/ui/thesaurusservice/publishVocabulary?thesaurusId="
            + encodeURIComponent(theForm.up('thesaurusPanel').thesaurusData.id)
            + "&userId=" + encodeURIComponent(Thesaurus.ext.utils.userInfo.data.username);

        Ext.Ajax.request({
            url: url,
            method: 'GET',
            success: function() {
               Thesaurus.ext.utils.msg('Succès',
                   'Le thesaurus a été publié!');
                me.application.fireEvent('thesaurusupdated');
            },
            failure: function() {
                Thesaurus.ext.utils.msg(me.xProblemLabel, me.xProblemPublishMsg);
            }
        });
    },

    archiveThesaurus : function(theButton) {
        var me = this;
        var theForm = theButton.up('form');
        var url = "services/ui/thesaurusservice/archiveVocabulary?thesaurusId="
            + encodeURIComponent(theForm.up('thesaurusPanel').thesaurusData.id);

        Ext.Ajax.request({
            url: url,
            method: 'GET',
            success: function() {
                var record = theForm.getForm().getRecord();
                record.set("archived",true);
                me.loadData(theForm, record);
                Thesaurus.ext.utils.msg('Succès',
                    'Le thesaurus a été archivé!');
            },
            failure: function() {
                Thesaurus.ext.utils.msg(me.xProblemLabel, me.xProblemArchiveMsg);
            }
        });
    },

    importBranch : function(theButton) {
    	var me = this;
    	Ext.create('GincoApp.view.ImportWin', {importType: 'gincoBranchXml', thesaurusData:theButton.up('thesaurusPanel').thesaurusData, xTitleLabel: '<h1>' + me.xImportBranchTitle + '</h1>'});
	},

    exportHierarchical : function(theButton) {
        var theForm = theButton.up('form');
        var url = "services/ui/exportservice/getHierarchical?thesaurusId="
            + encodeURIComponent(theForm.up('thesaurusPanel').thesaurusData.id);
        window.open(url);
    },

    exportJournal : function(theButton) {
        var theForm = theButton.up('form');
        var url = "services/ui/journalservice/exportLogJournal?thesaurusId="
            + encodeURIComponent(theForm.up('thesaurusPanel').thesaurusData.id);
        window.open(url);
    },

    exportAlphabetical : function(theButton) {
        var theForm = theButton.up('form');
        var url = "services/ui/exportservice/getAlphabetical?thesaurusId="
            + encodeURIComponent(theForm.up('thesaurusPanel').thesaurusData.id);
        window.open(url);
    },

    exportGinco : function(theButton) {
        var theForm = theButton.up('form');
        var url = "services/ui/exportservice/getGincoThesaurusExport?thesaurusId="
            + encodeURIComponent(theForm.up('thesaurusPanel').thesaurusData.id);
        window.open(url);
    },

    exportSKOS : function(theButton) {
        var theForm = theButton.up('form');
        var url = "services/ui/exportservice/getSKOS?thesaurusId="
            + encodeURIComponent(theForm.up('thesaurusPanel').thesaurusData.id);
        window.open(url);
    },

	saveForm : function(theButton, theCallback) {
		var me = this;
		var theForm = theButton.up('form');
		if (theForm.getForm().isValid()) {
			theForm.getEl().mask("Chargement");
			theForm.getForm().updateRecord();
			var updatedModel = theForm.getForm().getRecord();
			updatedModel.save({
				success : function(record, operation) {
					me.loadData(theForm, record);
					theForm.getEl().unmask();
					Thesaurus.ext.utils.msg('Succès',
							'Le thesaurus a été enregistré!');
					me.application.fireEvent('thesaurusupdated');
					if (theCallback && typeof theCallback == "function") {
						theCallback();
					}
				},
				failure : function(record, operation) {
					Thesaurus.ext.utils.msg(me.xProblemLabel, me.xProblemSaveMsg+" "+operation.error);
					theForm.getEl().unmask();
				}
			});
		}
	},

    onPolyChange : function(theCheckBox, newValue, oldValue, eOpts) {
        if(newValue == false) {
            Ext.MessageBox.show({
            	title: this.xWarningLabel,
                msg: this.xWarningChangedPoly,
                buttons: Ext.MessageBox.OK,
                icon: Ext.MessageBox.WARNING
            });
        }
    },

    onLangChange : function(theCombo, records) {
    	var thePanel = theCombo.up("thesaurusPanel");
    	var oldLanguages = thePanel.thesaurusData.languages;
    	for (var i=0; i<oldLanguages.length; i++){
    		var found = false;
    		Ext.Array.forEach(records, function (item) {
    			if (item.get("id")==oldLanguages[i]){
    				found = true;
    			}
    		});
    		if (found == false){
    			Ext.MessageBox.show({
    	    		title: this.xWarningLabel,
    	    		msg: this.xWarningChangedLanguages,
    	    		buttons: Ext.MessageBox.OK,
    	            icon: Ext.MessageBox.WARNING
    			});
    		}
    	}
    },

    importSandboxClick : function(theButton) {
    	var me = this;
    	Ext.create('GincoApp.view.ImportWin', {importType : 'txt', thesaurusData : theButton.up('thesaurusPanel').thesaurusData, xTitleLabel: '<h1>'+me.xImportTermsTitle+'</h1>'});
    },

	init : function(application) {
		this.control({
			"thesaurusPanel form" : {
				afterrender : this.loadPanel
			},
			"thesaurusPanel #saveThesaurus" : {
				click : this.saveForm
			},
            "thesaurusPanel #deleteThesaurus" : {
                click : this.deleteThesaurus
            },
			"thesaurusPanel #newTermBtn" : {
				click : this.onNewTermBtnClick
			},
            "thesaurusPanel #exportHierarchical" : {
                click : this.exportHierarchical
            },
            "thesaurusPanel #exportAlphabetical" : {
                click : this.exportAlphabetical
            },
            "thesaurusPanel #exportGinco" : {
                click : this.exportGinco
            },
            "thesaurusPanel #exportSKOS" : {
                click : this.exportSKOS
            },
			"thesaurusPanel #newConceptBtn" : {
				click : this.onNewConceptBtnClick
			},
			"thesaurusPanel #newComplexConceptBtn" : {
				click : this.onNewComplexConceptBtnClick
			},
			"thesaurusPanel #newConceptArrayBtn" : {
				click : this.onNewConceptArrayBtnClick
			},
			"thesaurusPanel #newConceptGroupBtn" : {
				click : this.onNewConceptGroupBtnClick
			},
			"thesaurusPanel #editJournal" : {
                click : this.exportJournal
            },
            "thesaurusPanel #publishThesaurus" : {
                click : this.publishThesaurus
            },
            "thesaurusPanel #archiveThesaurus" : {
                click : this.archiveThesaurus
            },
            "thesaurusPanel #importBranch" : {
                click : this.importBranch
            },
            "checkbox[cls=poly]" : {
                change : this.onPolyChange
            },
            "thesaurusPanel #thesauruslang" : {
            	select : this.onLangChange
            },
            "thesaurusPanel #importSandbox" : {
                click : this.importSandboxClick
            },
		});
	}
});
