/*
 * File: app/view/ThesaurusPanel.js
 * Term Creation/Edition Form
 * 
 */
Ext.Loader.setPath('Ext.ux', 'extjs/ux');
Ext.require([ 'Ext.ux.CheckColumn', 'GincoApp.view.NoteTermPanel' ]);

Ext.define('GincoApp.view.TermPanel', {
    extend: 'Ext.panel.Panel',

    termId: null,
    thesaurusData : '',

    alias: 'widget.termPanel',
    localized: true,
    closable: true,
    
    /*Fields with auto generated values*/
    xIdentifierLabel : 'Identifier',
    xCreatedDateLabel : 'Creation date',
    xDateLabel : 'Last Modification Date',

    /*Fields prompting values*/
    xLexicalValueLabel : 'Lexical value',
    xLanguagesLabel : 'Languages',
    xRelationLabel : 'Relation',
    xSourceLabel : 'Source',
    xTermPanelTitle : 'New Term',
    xNotesTab : 'Notes of this term',
    xStatusLabel : 'Status',
   
    initComponent: function() {
        var me = this;
        
        Ext.applyIf(me, {
        	title: me.xTermPanelTitle,
			items : [ {
				xtype : 'tabpanel',
				items : [ {
					
                    xtype: 'form',
                    itemId: 'termForm',
                    title: me.xTermPanelTitle,
                	flex: 1,
                	autoScroll: true,
                    pollForChanges : true,
                    trackResetOnLoad : true,
                    defaults: {
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
                                    iconCls : 'icon-save',
                                    itemId : 'saveTerm'
                                },
                                {
                                    xtype: 'button',
                                    text: 'Supprimer',
                                    disabled: true,
                                    itemId: 'delete',
                                    cls: 'delete',
                                    iconCls : 'icon-delete'
                                },
                                {
                                    xtype: 'button',
                                    text: 'Créer un concept depuis ce terme',
                                    disabled: true,
                                    itemId: 'createconcept',
                                    iconCls : 'icon-add'
                                }
                            ]
                        }
                    ],
                    items: [
                        {
                        	xtype: 'displayfield',
                        	name:'identifier',
                        	fieldLabel: me.xIdentifierLabel
                        },
                        {
                        	xtype: 'displayfield',
                        	name:'created',
                        	fieldLabel: me.xCreatedDateLabel
                        },
                        {
                            xtype: 'displayfield',
                            name:'modified',
                            fieldLabel: me.xDateLabel
                        },
                        {
                            xtype: 'textfield',
                            name:'lexicalValue',
                            fieldLabel: me.xLexicalValueLabel,
                            allowBlank: false,
                        	anchor: '70%'
                        },
                        {
                        	xtype : 'htmleditor',
                        	name:'source',
                        	fieldLabel: me.xSourceLabel,
                        	enableAlignments : false,
							enableColors : false,
							enableFont : false,
							enableFontSize : false,
							enableFormat : false,
							enableLists : false,
							enableSourceEdit : false,
                        	anchor: '70%'
                        },
                        {
                        	xtype: 'hiddenfield',
                        	name:'prefered'
                        },
                        {
                        	xtype: 'hiddenfield',
                        	name:'role'
                        },
                        {
                        	xtype: 'hiddenfield',
                        	name:'conceptId'
                        },
                        {
                        	xtype: 'hiddenfield',
                        	name:'thesaurusId'
                        },
                        {
                        	xtype: 'combobox',
                        	name:'language',
                        	itemId: 'languageCombo',
                        	fieldLabel: me.xLanguagesLabel,
                        	editable : false,
                        	displayField : 'refname',
                        	valueField : 'id',
                        	forceSelection : true,
                        	store :  Ext.create('GincoApp.store.TermLanguageStore'),
                        	anchor: '70%',
                        	margin : '5 0 5 0'
                        },
                        {
                        	xtype: 'combobox',
                        	name:'status',
                        	itemId: 'statusCombo',
                        	fieldLabel: me.xStatusLabel,
                        	editable : false,
                        	multiSelect : false,
                        	readOnly : true,
                        	displayField : 'statusLabel',
                        	valueField : 'status',
                        	forceSelection : true,
                        	store :  Ext.create('GincoApp.store.TermStatusStore'),
                        	anchor: '70%',
                        	margin : '5 0 5 0'
                        }
                    ]
                },{
			        title: me.xNotesTab,
			        xtype: 'noteTermPanel',
			        closable : false,
			        disabled : true
			        }]
				}]
        });

        me.callParent(arguments);
    }
});