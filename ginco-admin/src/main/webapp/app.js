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
        'ConceptGroupController'
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
