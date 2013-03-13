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
																				.up('form').getForm();
																		if (form.isValid()) {
																			form
																					.submit({
																						url : 'services/ui/importservice/import',
																						waitMsg : me.xWaitingLabel,
																						success : function(
																								fp,
																								o) {
																							console.log(fp);
																					 		console.log(o);
																							 Ext.Msg.show({
																						            title: me.xSucessLabelTitle,																						            
																						            msg: me.xSucessLabel + o.result.data.title,
																						            minWidth: 200,
																						            modal: true,
																						            icon: Ext.Msg.INFO,
																						            buttons: Ext.Msg.OK
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