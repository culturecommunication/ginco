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

Ext.define('GincoApp.controller.ImportController', {
	extend:'Ext.app.Controller',

	localized : true,
	
	xWaitingLabel : 'Importing...',
	xSucessLabelTitle : 'Success',
	xSucessLabel : 'Element successfully imported : ',
	xSucessSandboxLabel : 'Elements successfully imported',
	xFailureLabelTitle: 'Error',
	
	importSaveClick : function(theButton){
		var me = this;
		var theForm = theButton.up('#importForm');
		var theWin = theForm.up('importWindow');
		var importUrl = null;
		if (theWin.importType == 'skos') {
			importUrl = 'services/ui/importservice/import';
		} else if (theWin.importType == 'gincoxml') {
			importUrl = 'services/ui/importservice/importGincoXml';
		} else if (theWin.importType == 'txt') {
			importUrl = 'services/ui/importservice/importSandBoxTerms?thesaurusId='
	            + encodeURIComponent(theForm.up('importWindow').thesaurusData.id);
		} else if (theWin.importType == 'gincoBranchXml') {
			importUrl = 'services/ui/importservice/importGincoBranchXml?thesaurusId='
	            + encodeURIComponent(theForm.up('importWindow').thesaurusData.id);
		}
		
		
		if (theForm.getForm()
				.isValid()) {
			theForm.getForm()
					.submit({
						url : importUrl,
						waitMsg : me.xWaitingLabel,
						success : function(
								fp,
								o) {			
							if(theWin.importType == 'txt'){
								Ext.Msg
								.show({
									title : me.xSucessLabelTitle,
									msg : me.xSucessSandboxLabel,
									minWidth : 200,
									modal : true,
									icon : Ext.Msg.INFO,
									buttons : Ext.Msg.OK
								});
							}
							else{
								Ext.Msg
								.show({
									title : me.xSucessLabelTitle,
									msg : me.xSucessLabel
											+ o.result.data.title,
									minWidth : 200,
									modal : true,
									icon : Ext.Msg.INFO,
									buttons : Ext.Msg.OK
								});
								me.application.fireEvent('thesaurusupdated');	
							}					
						},
						failure: function(form, action) {
							Ext.MessageBox.show({
								title : me.xFailureLabelTitle,
								msg : action.result.message,
								buttons : Ext.MessageBox.OK,
								scope : this
							});
	                    }
					});
		}	
	
	},
	
	importCancelClick: function(theButton){
		var theForm = theButton.up('#importForm');
		theForm.getForm().reset();
		var thewindow = theButton.up('importWindow');
		thewindow.close();
	},	
	
    init:function(){    	  	 
         this.control({
        	'importWindow #importSaveBtn' : {
        		click : this.importSaveClick
 			},
 			'importWindow #importCancelBtn' : {
 				click : this.importCancelClick
 			}            
         });
    }
});