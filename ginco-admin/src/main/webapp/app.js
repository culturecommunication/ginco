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
        'ThesaurusModel'
    ],
    stores: [
        'MainTreeStore',
        'ThesaurusTypeStore',
        'ThesaurusFormatStore',
        'ThesaurusLanguageStore'
    ],
    views: [
        'HadocViewPort',
        'GlobalTabPanel',
        'LeftPanel',
        'TopToolbar',
        'AProposWin',
        'ThesaurusPanel'
    ],
    autoCreateViewport: true,
    name: 'HadocApp',
    controllers: [
        'TopToolbarController',
        'MainTreeController',
        'ThesaurusFormController'
    ],
    launch: function() {
    	Ext.FocusManager.enable();
        var map = new Ext.util.KeyMap(Ext.getBody(), {
            key: Ext.EventObject.ONE,
            ctrl: true,
            handler: function() {
                 this.getController("TopToolbarController").onNewThesaurusBtnClick();
            },
            scope: this,
            defaultEventAction: "stopEvent"
        });
     }
});
