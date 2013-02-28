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
    xDateLabel : 'Last Modification Date',

    /*Fields prompting values*/
    xNoteListGridTitle : 'Liste des notes',
    xLanguageLabel : 'Language',
    xAddNote: 'Add a note',
    
    initComponent: function() {
        var me = this;

        
        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'form',
                	flex: 1,
                	autoScroll: true,
                    pollForChanges : true,
                    trackResetOnLoad : true,
                    defaults: {
                        anchor: '100%',
                        afterLabelTextTpl: new Ext.XTemplate('<tpl if="allowBlank === false"><span style="color:red;">*</span></tpl>', { disableFormats: true })
                    },
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'top',
                            items: [
                                {
                                    xtype: 'button',
                                    text: 'Enregistrer',
                                    disabled: true,
                                    formBind: true,
                                    cls: 'save',
                                    iconCls : 'icon-save'
                                },
                                {
                                    xtype: 'button',
                                    text: 'Supprimer',
                                    disabled: true,
                                    itemId: 'delete',
                                    cls: 'delete',
                                    iconCls : 'icon-delete'
                                }
                            ]
                        }
                    ],
                    items: [{
							xtype : 'gridpanel',
							title : me.xNoteListGridTitle,
							store : me.noteTermStore,

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
										text : me.xIdentifierLabel
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