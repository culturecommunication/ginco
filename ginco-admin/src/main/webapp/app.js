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
// Splashscreen handling
var splashscreen;

Ext.onReady(function() {
    // Start the mask on the body and get a reference to the mask
    splashscreen = Ext.getBody().mask('GINCO Chargement...', 'splashscreen');
    // Add a new class to this mask as we want it to look different from the default.
    splashscreen.addCls('splashscreen');
    // Insert a new div before the loading icon where we can place our logo.
    Ext.DomHelper.insertFirst(Ext.query('.x-mask-msg')[0], {
        cls: 'x-splash-icon'
    });

});


Ext.Loader.setConfig({
    enabled: true
});

Ext.Loader.setLocale({
    enabled: true,
    language: 'fr',
    localizedByDefault: false,
    types: [ 'controller', 'view' ]
});

Ext.application({
    models: [
        'MainTreeModel',
        'ThesaurusModel',
        'ThesaurusTermModel',
        'ConceptModel',
        'SimpleConceptModel',
        'ConceptArrayModel'
    ],
    stores: [
        'MainTreeStore',
        'ThesaurusTypeStore',
        'ThesaurusFormatStore',
        'ThesaurusLanguageStore',
        'TermLanguageStore',
        'ThesaurusTermStore',
        'ConceptTermStore',
        'ConceptReducedStore',
        'ConceptNoteTypeStore',
        'TermNoteTypeStore',
        'NoteLanguageStore',
        'SimpleConceptStore',
        'TermRoleStore',
        'ConceptGroupTypeStore',
        'TermStatusStore',
        'ConceptStatusStore'
    ],
    views: [
        'GincoViewPort',
        'GlobalTabPanel',
        'LeftPanel',
        'TopToolbar',
        'AProposWin',
        'SelectConceptWin'
    ],
    autoCreateViewport: true,
    name: 'GincoApp',
    controllers: [
        'TopToolbarController',
        'MainTreeController',
        'ThesaurusFormController',
        'SandboxPanelController',
        'TermPanelController',
        'GlobalTabPanelController',
        'ConceptController',
        'NotePanelController',
        'ConceptArrayController',
        'ConceptGroupController',
        'ImportController'
    ],
    init: function(){
    },
    launch: function() {
    	 Ext.History.init();
        // Setup a task to fadeOut the splashscreen
        var task = new Ext.util.DelayedTask(function() {
            // Fade out the body mask
            splashscreen.fadeOut({
                duration: 1000,
                remove:true
            });
            // Fade out the icon and message
            splashscreen.next().fadeOut({
                duration: 1000,
                remove:true,
                listeners: {
                    afteranimate: function() {
                        // Set the body as unmasked after the animation
                        Ext.getBody().unmask();
                    }
                }
            });
        });
        // Run the fade 500 milliseconds after launch.
        task.delay(500);
    	//Ext.FocusManager.enable();
        var map = new Ext.util.KeyMap(Ext.getBody(), [{
            key: Ext.EventObject.ONE,
            ctrl: true,
            handler: function() {
                 this.getController("TopToolbarController").onNewThesaurusBtnClick();
            },
            scope: this,
            defaultEventAction: "stopEvent"
        },
        {
            key: Ext.EventObject.W, // W to close
            ctrl: true,
            fn: function(){
                var el = Ext.FocusManager.focusedCmp;
                if (el.xtype === 'tab' && el.closable) {
                    el.up().focus();
                    el.destroy();
                }
            },
            scope: this
        }]
        );
     }
});
