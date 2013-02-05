/*
 * File: app/view/ThesaurusPanel.js
 * Thesaurus Creation Form
 * 
 */
Ext.define('HadocApp.view.ThesaurusPanel', {
    extend: 'Ext.panel.Panel',

    thesaurusId: '',

    alias: 'widget.thesaurusPanel',
    localized: true,
    autoScroll: true,
    closable: true,
    layout: 'anchor',
    
    /*Fields with auto generated values*/
    xIdentifierLabel : 'Identifier',
    xCreatedDateLabel : 'Creation date',
    xDateLabel : 'Last Modification Date',

    /*Fields prompting values*/
    xTitleLabel : 'Title',
    xServiceLabel : 'Service',
    xUrlLabel : 'URL',
    xContributorLabel : 'Contributor',
    xPublisherLabel : 'Publisher',
    xPublisherValue : 'Ministère chargé de la culture',
    xRightsLabel : 'Rights',
    xDescriptionLabel : 'Description',
    xCoverageLabel : 'Coverage',
    xSubjectLabel : 'Subject',
    xTypeLabel : 'Type',
    xFormatLabel : 'Format',
    xLanguagesLabel : 'Languages',
    xRelationLabel : 'Relation',
    xSourceLabel : 'Source',
    xThesaurusTitle : 'Thesaurus',
   
    initComponent: function() {
        var me = this;
        
        Ext.applyIf(me, {
        	title: me.xThesaurusTitle,
            items: [
                {
                    xtype: 'form',
                    title: 'Vocabulaire : scientifique (ID)',
                    defaults: {
                        anchor: '70%'
                    },
                    dockedItems: [
                        {
                            xtype: 'toolbar',
                            dock: 'top',
                            items: [
                                {
                                    xtype: 'button',
                                    text: 'Enregistrer'
                                },
                                {
                                    xtype: 'button',
                                    text: 'Créer version'
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
                            name:'date',
                            fieldLabel: me.xDateLabel
                        },
                        {
                            xtype: 'textfield',
                            name:'title',
                            fieldLabel: me.xTitleLabel,
                            allowBlank: false
                        },
                        {
                        	 xtype: 'container',
                        	 layout: 'column',
                        	 defaults: {
                                 margin:'0 50 5 0',
                                 layout: 'anchor'
                             },
                        	 items: [{
	                        	xtype: 'textfield',
	                        	name:'service',
	                        	fieldLabel: me.xServiceLabel
	                        },
	                        {
	                        	xtype: 'textfield',
	                        	name:'url',
	                        	fieldLabel: me.xUrlLabel,
	                        	vtype:'url'
	                        }]
                        },
                        {
                        	xtype: 'textareafield',
                        	name:'contributor',
                        	fieldLabel: me.xContributorLabel,
                        	grow: true
                        },
                        {
                        	xtype: 'textfield',
                        	name:'publisher',
                        	fieldLabel: me.xPublisherLabel,
                        	value: me.xPublisherValue
                        },
                        {
                        	xtype: 'textareafield',
                        	name:'rights',
                        	fieldLabel: me.xRightsLabel,
                        	allowBlank: false,
                        	grow: true
                        },
                        {
                            xtype: 'textareafield',
                            name: 'description',
                            fieldLabel: me.xDescriptionLabel,
                            grow: true
                        },
                        {
                        	xtype: 'textareafield',
                        	name:'coverage',
                        	fieldLabel: me.xCoverageLabel,
                        	grow: true
                        },
                        {
                        	xtype: 'textareafield',
                        	name:'subject',
                        	fieldLabel: me.xSubjectLabel,
                        	grow: true
                        },
                        {
                        	xtype : 'combobox',
                        	name : 'type',
                        	fieldLabel : me.xTypeLabel,
                        	editable : true,
                        	displayField : 'type',
                        	valueField : 'identifier',
                        	forceSelection : false,
                        	multiSelect : false
                        	//store : 'typesStore'
                        	//=> to implement a store with identifier + type name
                        },
                        {
                        	xtype: 'combobox',
                        	name:'format',
                        	fieldLabel: me.xFormatLabel,
                        	editable : false,
                        	displayField : 'format',
                        	valueField : 'identifier',
                        	forceSelection : false,
                        	multiSelect : false
                        	//store : 'formatsStore'
                        	//=> to implement a store with identifier + format name
                        },
                        {
                        	xtype: 'combobox',
                        	name:'languages',
                        	fieldLabel: me.xLanguagesLabel,
                        	editable : false,
                        	displayField : 'language',
                        	valueField : 'identifier',
                        	forceSelection : true,
                        	multiSelect : true
                        	//store : 'languagesStore'
                        	//=> to implement a store with identifier + language name
                        },
                        {
                            xtype: 'htmleditor',
                            name: 'relation',
                            fieldLabel: me.xRelationLabel,
                            enableAlignments: false,
                            enableColors: false,
                            enableFont: false,
                            enableFontSize: false,
                            enableFormat: false,
                            enableLists: false,
                            enableSourceEdit: false
                        },
                        {
                            xtype: 'htmleditor',
                            name: 'source',
                            fieldLabel: me.xSourceLabel,
                            enableAlignments: false,
                            enableColors: false,
                            enableFont: false,
                            enableFontSize: false,
                            enableFormat: false,
                            enableLists: false,
                            enableSourceEdit: false
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }
});