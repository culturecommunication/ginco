/*
 * File: app/view/ThesaurusPanel.js
 * Term Creation/Edition Form
 * 
 */
Ext.define('GincoApp.view.TermPanel', {
    extend: 'Ext.panel.Panel',

    termId: null,
    thesaurusData : '',

    alias: 'widget.termPanel',
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
    xLexicalValueLabel : 'Lexical value',
    xLanguagesLabel : 'Languages',
    xRelationLabel : 'Relation',
    xSourceLabel : 'Source',
    xTermPanelTitle : 'New Term',
   
    initComponent: function() {
        var me = this;

        
        Ext.applyIf(me, {
        	title: me.xTermPanelTitle,
            items: [
                {
                    xtype: 'form',
                    title: me.xTermPanelTitle,
                	flex: 1,
                	autoScroll: true,
                    pollForChanges : true,
                    trackResetOnLoad : true,
                    defaults: {
                        anchor: '70%',
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
                            allowBlank: false
                        },
                        {
                        	xtype: 'textareafield',
                        	name:'source',
                        	fieldLabel: me.xSourceLabel,
                        	grow: true
                        },
                        {
                        	xtype: 'hiddenfield',
                        	name:'prefered'
                        },
                        {
                        	xtype: 'hiddenfield',
                        	name:'status'
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
                        	store :  Ext.create('GincoApp.store.TermLanguageStore')
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }
});