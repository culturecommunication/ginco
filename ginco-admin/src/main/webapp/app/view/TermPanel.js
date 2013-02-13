/*
 * File: app/view/ThesaurusPanel.js
 * Term Creation/Edition Form
 * 
 */
Ext.define('GincoApp.view.TermPanel', {
    extend: 'Ext.panel.Panel',

    termId: '',
    thesaurusId : '',

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
                                }
                            ]
                        }
                    ],
                    items: [
                        {
                        	xtype: 'displayfield',
                        	name:'id',
                        	fieldLabel: me.xIdentifierLabel
                        },
                        {
                        	xtype: 'displayfield',
                        	name:'created',
                        	fieldLabel: me.xCreatedDateLabel
                        },
                        {
                            xtype: 'displayfield',
                            name:'date',
                            fieldLabel: me.xDateLabel
                        },
                        {
                            xtype: 'textfield',
                            name:'lexicalvalue',
                            fieldLabel: me.xLexicalValueLabel,
                            allowBlank: false
                        },
                        {
                        	xtype: 'textareafield',
                        	name:'source',
                        	fieldLabel: me.xSourceLabel,
                        	allowBlank: false,
                        	grow: true
                        },
                        {
                        	xtype: 'combobox',
                        	name:'languages',
                        	fieldLabel: me.xLanguagesLabel,
                        	editable : false,
                        	displayField : 'refname',
                        	valueField : 'id',
                        	forceSelection : true,
                        	store : 'ThesaurusLanguageStore'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }
});