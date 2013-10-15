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

Ext.require([ 'GincoApp.store.CustomConceptAttributeTypeStore',
    'GincoApp.store.CustomTermAttributeTypeStore']);

Ext.define('GincoApp.view.CustomAttributeTypesPanel', {
    extend : 'Ext.panel.Panel',

    alias : 'widget.customAttributeTypesPanel',
    localized : true,
    closable : true,
    layout : {
        type : 'vbox',
        align : 'stretch'
    },
    /* Fields with auto generated values */
    xCode : 'Code',
    xValue : 'Value',
    xDelete : 'Remove',
    xAdd : 'Add',
    xSave : 'Save',
    xActions : 'Actions',
    xConceptsTitle : 'Concepts custom attributes',
    xTermsTitle : 'Terms custom attributes',
    xExportableConceptColumnLabel : 'Exportable',

    conceptAttrStore : null,
    termAttrStore : null,

   initComponent : function() {
        var me = this;

       var rowEditingConcept = Ext.create('Ext.grid.plugin.RowEditing', {
           clicksToMoveEditor: 1,
           autoCancel: false,
           pluginId: 'rowEditing'
       });

       var rowEditingTerm = Ext.create('Ext.grid.plugin.RowEditing', {
           clicksToMoveEditor: 1,
           autoCancel: false,
           pluginId: 'rowEditing'
       });

        me.conceptAttrStore = Ext.create('GincoApp.store.CustomConceptAttributeTypeStore');
        me.termAttrStore = Ext.create('GincoApp.store.CustomTermAttributeTypeStore');

        Ext
            .applyIf(
            me,
            {
                title : 'attrs',
                items : [ {
                    xtype : 'gridpanel',
                    plugins : [ rowEditingConcept ],
                    itemId : 'conceptAttributTypesGrid',
                    title : me.xConceptsTitle,
                    autoScroll:true,
                    flex:1,
                    store : me.conceptAttrStore,
                    tbar : [ {
                        xtype : 'button',
                        text : me.xSave,
                        requiredRoles : ['ADMIN'],
                        cls : 'save',
                        iconCls : 'icon-save',
                        itemId : 'saveConceptAttributTypes',
                        disabled : false
                    },{
                        xtype : 'button',
                        text : me.xAdd,
                        requiredRoles : ['ADMIN'],
                        cls : 'add',
                        iconCls : 'icon-add',
                        itemId : 'addConceptAttributTypes',
                        disabled : false
                    } ],
                    columns : [
                        {
                            dataIndex : 'code',
                            renderer: 'htmlEncode',
                            text : me.xCode,
                            editor: {
                                xtype: 'textfield',
                                allowBlank: false
                            }
                        },{
                            dataIndex : 'value',
                            renderer: 'htmlEncode',
                            flex: 1,
                            text : me.xValue,
                            editor: {
                                xtype: 'textfield',
                                allowBlank: false
                            }
                        },{
							xtype : 'checkcolumn',
							dataIndex : 'exportable',
							header : me.xExportableConceptColumnLabel,
							stopSelection : false
						},{
                            xtype : 'actioncolumn',
                            itemId : 'conceptActionColumn',
                            header : me.xActions,
                            items : [ {
                                icon : 'images/detach.png',
                                tooltip : me.xDelete,
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
                },{
                    xtype : 'gridpanel',
                    plugins : [ rowEditingTerm ],
                    itemId : 'termAttributTypesGrid',
                    title : me.xTermsTitle,
                    autoScroll:true,
                    flex:1,
                    store : me.termAttrStore,
                    tbar : [ {
                        xtype : 'button',
                        text : me.xSave,
                        requiredRoles : ['ADMIN'],
                        cls : 'save',
                        iconCls : 'icon-save',
                        itemId : 'saveTermAttributTypes',
                        disabled : false
                    },{
                            xtype : 'button',
                            text : me.xAdd,
                            requiredRoles : ['ADMIN'],
                            cls : 'add',
                            iconCls : 'icon-add',
                            itemId : 'addTermAttributTypes',
                            disabled : false
                    } ],
                    columns : [
                        {
                            dataIndex : 'code',
                            text : me.xCode,
                            renderer: 'htmlEncode',
                            editor: {
                                xtype: 'textfield',
                                allowBlank: false
                            }
                        },
                        {
                            dataIndex : 'value',
                            flex: 1,
                            renderer: 'htmlEncode',
                            text : me.xValue,
                            editor: {
                                xtype: 'textfield',
                                allowBlank: false
                            }
                        },
                        {
                            xtype : 'actioncolumn',
                            itemId : 'termActionColumn',
                            header : me.xActions,
                            items : [ {
                                icon : 'images/detach.png',
                                tooltip : me.xDelete,
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
                }]
            });

        me.callParent(arguments);
    }
});