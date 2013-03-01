/*
 * File: app/view/ThesaurusPanel.js
 * Term Creation/Edition Form
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