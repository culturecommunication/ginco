/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture et de la
 * Communication (2013) <p/> contact.gincoculture_at_gouv.fr <p/> This software
 * is a computer program whose purpose is to provide a thesaurus management
 * solution. <p/> This software is governed by the CeCILL license under French
 * law and abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". <p/> As a counterpart to the access to the source
 * code and rights to copy, modify and redistribute granted by the license,
 * users are provided only with a limited warranty and the software's author,
 * the holder of the economic rights, and the successive licensors have only
 * limited liability. <p/> In this respect, the user's attention is drawn to the
 * risks associated with loading, using, modifying and/or developing or
 * reproducing the software by the user in light of its specific status of free
 * software, that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systemsand/or data
 * to be ensured and, more generally, to use and operate it in the same
 * conditions as regards security. <p/> The fact that you are presently reading
 * this means that you have had knowledge of the CeCILL license and that you
 * accept its terms.
 */

Ext
		.define(
				'GincoApp.view.ImportWin',
				{
					extend : 'Ext.window.Window',

					autoShow : true,
					height : 250,
					width : 500,
					title : '<h2>Import</h2>',
					titleAlign : 'center',
					modal : true,

					// Labels
					xEmptyFileLabel : 'Sélectionner un fichier',
					xFileLabel : 'Fichier',
					xTitleLabel : '<h1>Import SKOS</h1>',
					xSaveBtn : 'Importer',
					xCancelLabel : 'Annuler',
					xWaitingLabel : 'Import en cours',
					xSucessLabelTitle : 'Succès',
					xSucessLabel : 'Le thésaurus a été importé avec succès : ',

					initComponent : function() {
						var me = this;

						Ext
								.applyIf(
										me,
										{
											items : [
													{
														xtype : 'label',
														html : me.xTitleLabel
													},
													{
														xtype : 'form',
														items : [ {
															xtype : 'filefield',
															itemId : 'importFormFile',
															emptyText : me.xEmptyFileLabel,
															fieldLabel : me.xFileLabel,
															name : 'import-file-path',
															allowBlank : false,
															buttonText : '',
															buttonConfig : {
																iconCls : 'upload-icon'
															}
														} ],

														buttons : [
																{
																	text : me.xSaveBtn,
																	handler : function() {
																		var form = this
																				.up(
																						'form')
																				.getForm();
																		if (form
																				.isValid()) {
																			form
																					.submit({
																						url : 'services/ui/importservice/import',
																						waitMsg : me.xWaitingLabel,
																						success : function(
																								fp,
																								o) {
																							console
																									.log(fp);
																							console
																									.log(o);
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
																						}
																					});
																		}
																	}
																},
																{
																	text : me.xCancelLabel,
																	handler : function() {
																		this
																				.up(
																						'form')
																				.getForm()
																				.reset();
																	}
																} ]
													} ]
										});

						me.callParent(arguments);
					}

				});