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

Ext.define('GincoApp.view.GlobalThesaurusTabsPanel', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.thesaurusTabs',
    //xCloseAllTlt : 'Close all',
    localized : false,
    activeTab: 0,
    flex: 1,
    initAria : function() {
    	var me = this;
    	me.callParent();
    	var actionEl = this.getActionEl();
        actionEl.dom.setAttribute('aria-live', 'polite');
    },
    
    initComponent: function() {
        var me = this;
        
        me.addEvents('doubleclickpanel');
        
        Ext.applyIf(me, {
        	tabBar:{
        		cls : 'globaltabs',
                defaults: {
                    height: 25 //sets the default height of the actual tab
                },
                height: 27,  //sets the height of the tabBar component
                listeners: {
                    afterrender: function(cmp){
                     // this will set the height of the tabBar body to the height of the tabBar component
                        cmp.body.setHeight(cmp.getHeight()); 
                    },
        			'render': function(panel) {
        				panel.body.on('dblclick', function() {
        					me.fireEvent('doubleclickpanel',panel);
        		       });
        		    }
                }
        	}
        	/*tabBar:{
                items:[{ 
                    xtype: 'button',
                    itemId:'closeAllTabs',
                    hidden :true,
                    text : this.xCloseAllTlt
                }]
            }*/
        });

        me.callParent(arguments);
    }

});