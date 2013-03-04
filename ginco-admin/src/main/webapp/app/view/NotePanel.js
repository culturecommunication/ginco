/*
 * File: app/view/NotePanel.js
 * Note Creation/Edition
 * 
 */
Ext.define('GincoApp.view.NotePanel', {
    extend: 'Ext.panel.Panel',

    termId: null,
    thesaurusData : '',

    alias: 'widget.notePanel',
    localized: true,
    closable: true,
    layout: {
    	type: 'vbox',
    	align: 'stretch'
    },
    /*Fields with auto generated values*/
    xIdentifierLabel : 'Identifier',
    xCreatedDateLabel : 'Creation date',
    xModifiedDateLabel : 'Modification date',
    xLexicalValueLabel : 'Lexical Value',
    xTypeLabel : 'Type',

    /*Fields prompting values*/
    xNoteConceptListGridTitle : 'Liste des notes de concept',
    xLanguageLabel : 'Language',
    xAddNote: 'Add a note',
    xDetach: 'Delete a note',
    
    initComponent: function() {
        var me = this;
        me.noteConceptStore = Ext.create('GincoApp.store.ThesaurusNoteStore');

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'panel',
                	flex: 1,
                	autoScroll: true,
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'top',
                            items: [
                                {
                                    xtype: 'button',
                                    text: 'Enregistrer',
                                    cls: 'save',
                                    iconCls : 'icon-save',
                                    itemId : 'saveNote'
                                }
                            ]
                        }
                    ],
                    items: [{
							xtype : 'gridpanel',
							title : me.xNoteConceptListGridTitle,
							store : me.noteConceptStore,

							dockedItems : [ {
								xtype : 'toolbar',
								dock : 'top',
								items : [ {
									xtype : 'button',
									itemId : 'newNoteBtn',
									text : me.xAddNote
								} ]
							} ],

							columns : [
									{
										dataIndex : 'identifier',
										text : me.xIdentifierLabel,
										hidden: true
									},
									{
										dataIndex : 'lexicalValue',
										text : me.xLexicalValueLabel,
										flex : 1
									},
									{
										dataIndex : 'language',
										text : me.xLanguageLabel
									},
									{
										dataIndex : 'created',
										text : me.xCreatedDateLabel
									},
									{
										dataIndex : 'type',
										text : me.xTypeLabel
									},
									{
										dataIndex : 'created',
										text : me.xCreatedDateLabel,
										//hidden: true
									},
									{
										dataIndex : 'modified',
										text : me.xModifiedDateLabel,
										//hidden: true
									},
									{
										xtype : 'actioncolumn',
										itemId : 'noteDelete',
										items : [ {
											icon : 'images/detach.png',
											tooltip : me.xDetach,
											handler : function(
													view,
													rowIndex,
													colIndex,
													item,
													e,
													record,
													row) {
											}
										} ]
									}
                            ]
						}
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }
});